package cn.wj.android.cashbook.domain.usecase

import cn.wj.android.cashbook.core.common.annotation.CashbookDispatchers
import cn.wj.android.cashbook.core.common.annotation.Dispatcher
import cn.wj.android.cashbook.core.common.ext.completeZero
import cn.wj.android.cashbook.core.common.ext.yearMonth
import cn.wj.android.cashbook.core.data.repository.RecordRepository
import cn.wj.android.cashbook.core.model.model.RecordViewsModel
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

/**
 * 获取指定时间区间内的记录
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2023/10/23
 */
class GetRecordViewsBetweenDateUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val recordModelTransToViewsUseCase: RecordModelTransToViewsUseCase,
    @Dispatcher(CashbookDispatchers.IO) private val coroutineContext: CoroutineContext,
) {

    suspend operator fun invoke(
        fromDate: LocalDate,
        toDate: LocalDate?,
        yearSelected: Boolean
    ): List<RecordViewsModel> = withContext(coroutineContext) {
        val from: String
        val to: String
        when {
            yearSelected -> {
                from = "${fromDate.year}-01-01 00:00:00"
                to = "${fromDate.year}-12-31 23:59:59"
            }

            null == toDate -> {
                from = "${fromDate.year}-${fromDate.monthValue.completeZero()}-01 00:00:00"
                to =
                    "${fromDate.year}-${fromDate.monthValue.completeZero()}-${fromDate.yearMonth.atEndOfMonth().dayOfMonth} 23:59:59"
            }

            else -> {
                from =
                    "${fromDate.year}-${fromDate.monthValue.completeZero()}-${fromDate.dayOfMonth.completeZero()} 00:00:00"
                to =
                    "${toDate.year}-${toDate.monthValue.completeZero()}-${toDate.dayOfMonth.completeZero()} 23:59:59"
            }
        }
        recordRepository.queryRecordListBetweenDate(from, to)
            .map {
                recordModelTransToViewsUseCase(it)
            }
    }
}