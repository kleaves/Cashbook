package cn.wj.android.cashbook.feature.records.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetLayout
import androidx.compose.material3.ModalBottomSheetState
import androidx.compose.material3.ModalBottomSheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.wj.android.cashbook.core.common.ext.toDoubleOrZero
import cn.wj.android.cashbook.core.common.ext.withCNY
import cn.wj.android.cashbook.core.design.component.CashbookGradientBackground
import cn.wj.android.cashbook.core.design.component.CashbookScaffold
import cn.wj.android.cashbook.core.design.component.CommonDivider
import cn.wj.android.cashbook.core.design.component.Empty
import cn.wj.android.cashbook.core.design.component.TransparentListItem
import cn.wj.android.cashbook.core.design.component.painterDrawableResource
import cn.wj.android.cashbook.core.design.theme.LocalExtendedColors
import cn.wj.android.cashbook.core.model.entity.RecordViewsEntity
import cn.wj.android.cashbook.core.model.enums.RecordTypeCategoryEnum
import cn.wj.android.cashbook.core.ui.BackPressHandler
import cn.wj.android.cashbook.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordListSheetScaffold(
    sheetViewData: RecordViewsEntity?,
    onRecordItemEditClick: (Long) -> Unit,
    onRecordItemDeleteClick: (Long) -> Unit,
    onSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { value ->
            if (value == ModalBottomSheetValue.Hidden) {
                onSheetDismiss()
            }
            true
        }),
    content: @Composable (PaddingValues) -> Unit,
) {

    if (sheetState.isVisible) {
        // sheet 显示时，返回隐藏 sheet
        BackPressHandler {
            onSheetDismiss()
        }
    }

    // 显示数据不为空时，显示详情 sheet
    LaunchedEffect(sheetViewData) {
        if (null != sheetViewData) {
            // 显示详情弹窗
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            RecordDetailsSheet(
                recordEntity = sheetViewData,
                onRecordItemEditClick = onRecordItemEditClick,
                onRecordItemDeleteClick = onRecordItemDeleteClick,
            )
        },
        content = {
            CashbookScaffold(
                topBar = topBar,
                floatingActionButton = floatingActionButton,
                content = content,
            )
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDetailsSheet(
    recordEntity: RecordViewsEntity?,
    onRecordItemEditClick: (Long) -> Unit,
    onRecordItemDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    CashbookGradientBackground {
        Box(
            modifier = modifier.fillMaxWidth(),
        ) {
            if (null == recordEntity) {
                // 无数据
                Empty(
                    hintText = stringResource(id = R.string.no_record_data),
                )
            } else {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.record_details),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f),
                        )
                        TextButton(
                            onClick = { onRecordItemEditClick(recordEntity.id) },
                        ) {
                            Text(
                                text = stringResource(id = R.string.edit),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        TextButton(
                            onClick = { onRecordItemDeleteClick(recordEntity.id) },
                        ) {
                            Text(
                                text = stringResource(id = R.string.delete),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                    CommonDivider()

                    // 金额
                    TransparentListItem(
                        headlineContent = { Text(text = stringResource(id = R.string.amount)) },
                        trailingContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (recordEntity.typeCategory == RecordTypeCategoryEnum.EXPENDITURE && recordEntity.reimbursable) {
                                    // 支出类型，并且可报销
                                    val text = if (recordEntity.relatedRecord.isEmpty()) {
                                        // 未报销
                                        stringResource(id = R.string.reimbursable)
                                    } else {
                                        // 已报销
                                        stringResource(id = R.string.reimbursed)
                                    }
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(end = 8.dp),
                                    )
                                }
                                Text(
                                    text = recordEntity.amount.withCNY(),
                                    color = when (recordEntity.typeCategory) {
                                        RecordTypeCategoryEnum.EXPENDITURE -> LocalExtendedColors.current.expenditure
                                        RecordTypeCategoryEnum.INCOME -> LocalExtendedColors.current.income
                                        RecordTypeCategoryEnum.TRANSFER -> LocalExtendedColors.current.transfer
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        },
                    )

                    if (recordEntity.charges.toDoubleOrZero() > 0.0) {
                        // 手续费
                        TransparentListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.charges)) },
                            trailingContent = {
                                Text(
                                    text = "-${recordEntity.charges}".withCNY(),
                                    color = LocalExtendedColors.current.expenditure,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                        )
                    }

                    if (recordEntity.typeCategory != RecordTypeCategoryEnum.INCOME && recordEntity.concessions.toDoubleOrZero() > 0.0) {
                        // 优惠
                        TransparentListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.concessions)) },
                            trailingContent = {
                                Text(
                                    text = "+${recordEntity.concessions.withCNY()}",
                                    color = LocalExtendedColors.current.income,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                        )
                    }

                    // 类型
                    TransparentListItem(
                        headlineContent = { Text(text = stringResource(id = R.string.type)) },
                        trailingContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterDrawableResource(idStr = recordEntity.typeIconResName),
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 8.dp),
                                )
                                Text(
                                    text = recordEntity.typeName,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        },
                    )

                    // TODO 关联的记录

                    recordEntity.assetName?.let { assetName ->
                        // 资产
                        TransparentListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.asset)) },
                            trailingContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = recordEntity.assetIconResId!!),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.padding(end = 8.dp),
                                    )
                                    Text(
                                        text = assetName,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                    // 关联资产
                                    recordEntity.relatedAssetName?.let { relatedName ->
                                        Text(
                                            text = "->",
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                        )
                                        Icon(
                                            painter = painterResource(id = recordEntity.relatedAssetIconResId!!),
                                            contentDescription = null,
                                            tint = Color.Unspecified,
                                            modifier = Modifier.padding(end = 8.dp),
                                        )
                                        Text(
                                            text = relatedName,
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    }
                                }
                            },
                        )
                    }

                    if (recordEntity.relatedTags.isNotEmpty()) {
                        // 标签
                        TransparentListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.tags)) },
                            trailingContent = {
                                val tagsText = with(StringBuilder()) {
                                    recordEntity.relatedTags.forEach { tag ->
                                        if (!isBlank()) {
                                            append(",")
                                        }
                                        append(tag.name)
                                    }
                                    var result = toString()
                                    if (result.length > 12) {
                                        result = result.substring(0, 12) + "…"
                                    }
                                    result
                                }
                                Text(
                                    text = tagsText,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(horizontal = 4.dp),
                                )
                            },
                        )
                    }

                    if (recordEntity.remark.isNotBlank()) {
                        // 备注
                        TransparentListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.remark)) },
                            trailingContent = {
                                Text(
                                    text = recordEntity.remark,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                        )
                    }

                    // 时间
                    TransparentListItem(
                        headlineContent = { Text(text = stringResource(id = R.string.time)) },
                        trailingContent = {
                            Text(
                                text = recordEntity.recordTime,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                    )
                }
            }
        }
    }
}