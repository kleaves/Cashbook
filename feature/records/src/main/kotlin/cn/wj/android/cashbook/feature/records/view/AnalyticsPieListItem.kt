package cn.wj.android.cashbook.feature.records.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.wj.android.cashbook.core.common.ext.decimalFormat
import cn.wj.android.cashbook.core.common.ext.withCNY
import cn.wj.android.cashbook.core.design.component.painterDrawableResource
import cn.wj.android.cashbook.core.model.entity.AnalyticsRecordPieEntity
import cn.wj.android.cashbook.core.ui.component.TypeIcon

@Composable
internal fun AnalyticsPieListItem(
    item: AnalyticsRecordPieEntity,
    tintColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TypeIcon(
            painter = painterDrawableResource(idStr = item.typeIconResName), containerColor = tintColor,
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = item.typeName)
                Text(
                    text = (item.percent * 100f).decimalFormat("###,###,##0.0") + "%",
                    color = LocalContentColor.current.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f),
                )
                Text(
                    text = item.totalAmount.withCNY(),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            LinearProgressIndicator(
                progress = item.percent,
                color = tintColor,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}