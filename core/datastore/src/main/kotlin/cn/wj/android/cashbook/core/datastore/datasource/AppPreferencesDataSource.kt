/*
 * Copyright 2021 The Cashbook Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.wj.android.cashbook.core.datastore.datasource

import androidx.datastore.core.DataStore
import cn.wj.android.cashbook.core.common.ext.logger
import cn.wj.android.cashbook.core.datastore.AppPreferences
import cn.wj.android.cashbook.core.datastore.copy
import cn.wj.android.cashbook.core.model.enums.AutoBackupModeEnum
import cn.wj.android.cashbook.core.model.enums.DarkModeEnum
import cn.wj.android.cashbook.core.model.enums.VerificationModeEnum
import cn.wj.android.cashbook.core.model.model.AppDataModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 应用配置数据源
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2023/2/21
 */
@Singleton
class AppPreferencesDataSource @Inject constructor(
    private val appPreferences: DataStore<AppPreferences>,
) {

    val appData = appPreferences.data
        .map {
            AppDataModel(
                currentBookId = it.currentBookId,
                defaultTypeId = it.defaultTypeId,
                lastAssetId = it.lastAssetId,
                refundTypeId = it.refundTypeId,
                reimburseTypeId = it.reimburseTypeId,
                useGithub = it.useGithub,
                autoCheckUpdate = it.autoCheckUpdate,
                ignoreUpdateVersion = it.ignoreUpdateVersion,
                mobileNetworkDownloadEnable = it.mobileNetworkDownloadEnable,
                needSecurityVerificationWhenLaunch = it.needSecurityVerificationWhenLaunch,
                enableFingerprintVerification = it.enableFingerprintVerification,
                passwordIv = it.passwordIv,
                fingerprintIv = it.fingerprintIv,
                passwordInfo = it.passwordInfo,
                fingerprintPasswordInfo = it.fingerprintPasswordInfo,
                darkMode = DarkModeEnum.ordinalOf(it.darkMode),
                dynamicColor = it.dynamicColor,
                verificationModel = VerificationModeEnum.ordinalOf(it.verificationMode),
                agreedProtocol = it.agreedProtocol,
                webDAVDomain = it.webDAVDomain,
                webDAVAccount = it.webDAVAccount,
                webDAVPassword = it.webDAVPassword,
                backupPath = it.backupPath,
                autoBackup = AutoBackupModeEnum.ordinalOf(it.autoBackup),
                lastBackupMs = it.lastBackupMs,
                creditCardPaymentTypeId = it.creditCardPaymentTypeId,
                keepLatestBackup = it.keepLatestBackup,
                canary = it.canary,
                topUpInTotal = it.topUpInTotal,
                logcatInRelease = it.logcatInRelease,
            )
        }

    suspend fun updateCurrentBookId(bookId: Long) {
        appPreferences.updateData { it.copy { this.currentBookId = bookId } }
    }

    suspend fun updateLastAssetId(lastAssetId: Long) {
        appPreferences.updateData { it.copy { this.lastAssetId = lastAssetId } }
    }

    suspend fun updateUseGithub(useGithub: Boolean) {
        appPreferences.updateData { it.copy { this.useGithub = useGithub } }
    }

    suspend fun updateAutoCheckUpdate(autoCheckUpdate: Boolean) {
        appPreferences.updateData { it.copy { this.autoCheckUpdate = autoCheckUpdate } }
    }

    suspend fun updateIgnoreUpdateVersion(ignoreUpdateVersion: String) {
        appPreferences.updateData { it.copy { this.ignoreUpdateVersion = ignoreUpdateVersion } }
    }

    suspend fun updateMobileNetworkDownloadEnable(mobileNetworkDownloadEnable: Boolean) {
        appPreferences.updateData {
            it.copy {
                this.mobileNetworkDownloadEnable = mobileNetworkDownloadEnable
            }
        }
    }

    suspend fun updateNeedSecurityVerificationWhenLaunch(needSecurityVerificationWhenLaunch: Boolean) {
        appPreferences.updateData {
            it.copy {
                this.needSecurityVerificationWhenLaunch = needSecurityVerificationWhenLaunch
            }
        }
    }

    suspend fun updateEnableFingerprintVerification(enableFingerprintVerification: Boolean) {
        appPreferences.updateData {
            it.copy {
                this.enableFingerprintVerification = enableFingerprintVerification
            }
        }
    }

    suspend fun updatePasswordIv(iv: String) {
        logger().i("updatePasswordIv(iv = <$iv>)")
        appPreferences.updateData { it.copy { this.passwordIv = iv } }
    }

    suspend fun updateFingerprintIv(iv: String) {
        logger().i("updateFingerprintIv(iv = <$iv>)")
        appPreferences.updateData { it.copy { this.fingerprintIv = iv } }
    }

    suspend fun updatePasswordInfo(passwordInfo: String) {
        logger().i("updatePasswordInfo(iv = <$passwordInfo>)")
        appPreferences.updateData { it.copy { this.passwordInfo = passwordInfo } }
    }

    suspend fun updateFingerprintPasswordInfo(fingerprintPasswordInfo: String) {
        logger().i("updateFingerprintPasswordInfo(iv = <$fingerprintPasswordInfo>)")
        appPreferences.updateData {
            it.copy {
                this.fingerprintPasswordInfo = fingerprintPasswordInfo
            }
        }
    }

    suspend fun updateDarkMode(darkModeEnum: DarkModeEnum) {
        appPreferences.updateData { it.copy { this.darkMode = darkModeEnum.ordinal } }
    }

    suspend fun updateDynamicColor(dynamicColor: Boolean) {
        appPreferences.updateData { it.copy { this.dynamicColor = dynamicColor } }
    }

    suspend fun updateVerificationMode(verificationMode: VerificationModeEnum) {
        appPreferences.updateData { it.copy { this.verificationMode = verificationMode.ordinal } }
    }

    suspend fun updateAgreedProtocol(agreedProtocol: Boolean) {
        appPreferences.updateData { it.copy { this.agreedProtocol = agreedProtocol } }
    }

    suspend fun updateWebDAV(domain: String, account: String, password: String) {
        appPreferences.updateData {
            it.copy {
                this.webDAVDomain = domain
                this.webDAVAccount = account
                this.webDAVPassword = password
            }
        }
    }

    suspend fun updateAutoBackupMode(autoBackupMode: AutoBackupModeEnum) {
        appPreferences.updateData { it.copy { this.autoBackup = autoBackupMode.ordinal } }
    }

    suspend fun updateBackupPath(path: String) {
        appPreferences.updateData { it.copy { this.backupPath = path } }
    }

    suspend fun updateBackupMs(ms: Long) {
        appPreferences.updateData { it.copy { this.lastBackupMs = ms } }
    }

    suspend fun updateRefundTypeId(id: Long) {
        appPreferences.updateData { it.copy { this.refundTypeId = id } }
    }

    suspend fun updateReimburseTypeId(id: Long) {
        appPreferences.updateData { it.copy { this.reimburseTypeId = id } }
    }

    suspend fun updateCreditCardPaymentTypeId(id: Long) {
        appPreferences.updateData { it.copy { this.creditCardPaymentTypeId = id } }
    }

    suspend fun updateKeepLatestBackup(keepLatestBackup: Boolean) {
        appPreferences.updateData { it.copy { this.keepLatestBackup = keepLatestBackup } }
    }

    suspend fun updateCanary(canary: Boolean) {
        appPreferences.updateData { it.copy { this.canary = canary } }
    }

    suspend fun needRelated(typeId: Long): Boolean {
        val appDataModel = appData.first()
        return typeId == appDataModel.reimburseTypeId || typeId == appDataModel.refundTypeId
    }

    suspend fun updateTopUpInTotal(topUpInTotal: Boolean) {
        appPreferences.updateData { it.copy { this.topUpInTotal = topUpInTotal } }
    }

    suspend fun updateLogcatInRelease(logcatInRelease: Boolean) {
        appPreferences.updateData { it.copy { this.logcatInRelease = logcatInRelease } }
    }
}
