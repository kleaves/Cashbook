package cn.wj.android.cashbook.core.network.util

import androidx.annotation.WorkerThread
import cn.wj.android.cashbook.core.common.BACKUP_FILE_EXT
import cn.wj.android.cashbook.core.common.annotation.CashbookDispatchers
import cn.wj.android.cashbook.core.common.annotation.Dispatcher
import cn.wj.android.cashbook.core.common.ext.logger
import cn.wj.android.cashbook.core.model.model.BackupModel
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.intellij.lang.annotations.Language
import org.jsoup.Jsoup

/**
 * 使用 OkHttp 实现的 WebDAV 操作
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2023/7/24
 */
class OkHttpWebDAVHandler @Inject constructor(
    private val callFactory: Call.Factory,
    @Dispatcher(CashbookDispatchers.IO) private val ioCoroutineContext: CoroutineContext,
) {

    private var account = ""
    private var password = ""

    fun setCredentials(
        account: String,
        password: String,
    ) {
        this.account = account
        this.password = password
    }

    @WorkerThread
    fun exists(url: String): Boolean {
        val response = callFactory.newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(account, password))
                .method("HEAD", null)
                .build()
        ).execute()
        logger().i("exists(url = <$url>), response = <$response>")
        return response.isSuccessful
    }

    @WorkerThread
    fun createDirectory(url: String): Boolean {
        val response = callFactory.newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(account, password))
                .method("MKCOL", null)
                .build()
        ).execute()
        logger().i("createDirectory(url = <$url>), response = <$response>")
        return response.isSuccessful
    }

    @WorkerThread
    fun put(url: String, dataStream: InputStream, contentType: String): Boolean {
        val requestBody = RequestBody.create(MediaType.parse(contentType), dataStream.readBytes())
        val response = callFactory.newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(account, password))
                .put(requestBody)
                .build()
        ).execute()
        logger().i("put(url = <$url>, dataStream = <$dataStream>, contentType = <$contentType>), response = <$response>")
        return response.isSuccessful
    }

    @WorkerThread
    fun put(url: String, file: File, contentType: String): Boolean {
        val response = callFactory.newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(account, password))
                .put(RequestBody.create(MediaType.parse(contentType), file))
                .build()
        ).execute()
        logger().i("put(url = <$url>, file = <$file>, contentType = <$contentType>), response = <$response>")
        return response.isSuccessful
    }

    @WorkerThread
    suspend fun list(
        url: String,
        propsList: List<String> = emptyList()
    ): List<BackupModel> = withContext(ioCoroutineContext) {
        val requestPropText = if (propsList.isEmpty()) {
            DAV_PROP.replace("%s", "")
        } else {
            DAV_PROP.format(with(StringBuilder()) {
                propsList.forEach {
                    appendLine("<a:$it/>")
                }
                toString()
            })
        }
        val response = callFactory.newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(account, password))
                .addHeader("Depth", "1")
                .method(
                    "PROPFIND",
                    RequestBody.create(MediaType.parse("text/plain"), requestPropText)
                )
                .build()
        ).execute()
        logger().i("list(url = <$url>, propsList = <$propsList>), response = <${response.code()}>")
        val responseString = response.body()?.string() ?: return@withContext emptyList()
        val result = arrayListOf<BackupModel>()
        Jsoup.parse(responseString).getElementsByTag("d:response").forEach {
            val href = it.getElementsByTag("d:href")[0].text()
            if (!href.endsWith("/")) {
                val fileName = href.split("/").last()
                if (fileName.endsWith(BACKUP_FILE_EXT)) {
                    result.add(BackupModel(fileName, href))
                }
            }
        }
        logger().i("list(url, propsList), result = <$result>")
        result
    }

    @WorkerThread
    suspend fun get(url: String): InputStream? = withContext(ioCoroutineContext) {
        val response = callFactory.newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(account, password))
                .build()
        ).execute()
        logger().i("get(url = <$url>), response = <${response.code()}>")
        response.body()?.byteStream()
    }

    companion object {
        /** 指定文件属性 */
        @Language("xml")
        private const val DAV_PROP =
            """<?xml version="1.0"?>
            <a:propfind xmlns:a="DAV:">
                <a:prop>
                    <a:displayname/>
                    <a:resourcetype/>
                    <a:getcontentlength/>
                    <a:creationdate/>
                    <a:getlastmodified/>
                    %s
                </a:prop>
            </a:propfind>"""
    }
}