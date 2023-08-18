package cn.wj.android.cashbook.feature.records.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BackdropScaffold
import androidx.compose.material3.BackdropScaffoldState
import androidx.compose.material3.BackdropValue
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBackdropScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.wj.android.cashbook.core.common.ext.completeZero
import cn.wj.android.cashbook.core.common.ext.decimalFormat
import cn.wj.android.cashbook.core.common.ext.toDoubleOrZero
import cn.wj.android.cashbook.core.common.ext.withCNY
import cn.wj.android.cashbook.core.design.component.CashbookFloatingActionButton
import cn.wj.android.cashbook.core.design.component.CashbookGradientBackground
import cn.wj.android.cashbook.core.design.component.CashbookModalBottomSheet
import cn.wj.android.cashbook.core.design.component.CashbookScaffold
import cn.wj.android.cashbook.core.design.component.Empty
import cn.wj.android.cashbook.core.design.component.Footer
import cn.wj.android.cashbook.core.design.component.Loading
import cn.wj.android.cashbook.core.design.component.TransparentListItem
import cn.wj.android.cashbook.core.design.component.painterDrawableResource
import cn.wj.android.cashbook.core.design.icon.CashbookIcons
import cn.wj.android.cashbook.core.design.theme.LocalExtendedColors
import cn.wj.android.cashbook.core.design.theme.PreviewTheme
import cn.wj.android.cashbook.core.model.entity.RecordDayEntity
import cn.wj.android.cashbook.core.model.entity.RecordViewsEntity
import cn.wj.android.cashbook.core.model.enums.RecordTypeCategoryEnum
import cn.wj.android.cashbook.core.ui.DevicePreviews
import cn.wj.android.cashbook.core.ui.DialogState
import cn.wj.android.cashbook.core.ui.R
import cn.wj.android.cashbook.feature.records.dialog.SelectDateDialog
import cn.wj.android.cashbook.feature.records.viewmodel.LauncherContentUiState
import cn.wj.android.cashbook.feature.records.viewmodel.LauncherContentViewModel
import java.time.YearMonth

/**
 * 首页内容
 *
 * @param recordDetailSheetContent 记录详情 sheet，参数：(记录数据，隐藏sheet回调) -> [Unit]
 * @param onRequestOpenDrawer 打开抽屉菜单
 * @param onRequestNaviToEditRecord 导航到编辑记录
 * @param onRequestNaviToSearch 导航到搜索
 * @param onRequestNaviToCalendar 导航到日历
 * @param onRequestNaviToMyAsset 导航到我的资产
 * @param onShowSnackbar 显示 [androidx.compose.material3.Snackbar]，参数：(显示文本，action文本) -> [SnackbarResult]
 */
@Composable
internal fun LauncherContentRoute(
    modifier: Modifier = Modifier,
    recordDetailSheetContent: @Composable (RecordViewsEntity?, () -> Unit) -> Unit = { _, _ -> },
    onRequestOpenDrawer: () -> Unit = {},
    onRequestNaviToEditRecord: (Long) -> Unit = {},
    onRequestNaviToSearch: () -> Unit = {},
    onRequestNaviToCalendar: () -> Unit = {},
    onRequestNaviToMyAsset: () -> Unit = {},
    onShowSnackbar: suspend (String, String?) -> SnackbarResult = { _, _ -> SnackbarResult.Dismissed },
    viewModel: LauncherContentViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val date by viewModel.dateData.collectAsStateWithLifecycle()

    LauncherContentScreen(
        shouldDisplayDeleteFailedBookmark = viewModel.shouldDisplayDeleteFailedBookmark,
        onRequestDismissBookmark = viewModel::dismissBookmark,
        recordDetailSheetContent = { record ->
            recordDetailSheetContent(record, viewModel::dismissSheet)
        },
        viewRecord = viewModel.viewRecord,
        date = date,
        onMenuClick = onRequestOpenDrawer,
        onDateClick = viewModel::displayDateSelectDialog,
        onDateSelected = viewModel::refreshSelectedDate,
        onSearchClick = onRequestNaviToSearch,
        onCalendarClick = onRequestNaviToCalendar,
        onMyAssetClick = onRequestNaviToMyAsset,
        onAddClick = { onRequestNaviToEditRecord.invoke(-1L) },
        uiState = uiState,
        onRecordItemClick = viewModel::displayRecordDetailsSheet,
        dialogState = viewModel.dialogState,
        onRequestDismissDialog = viewModel::dismissDialog,
        onRequestDismissSheet = viewModel::dismissSheet,
        onShowSnackbar = onShowSnackbar,
        modifier = modifier,
    )
}

/**
 * 首页内容
 *
 * @param shouldDisplayDeleteFailedBookmark 删除失败提示
 * @param onRequestDismissBookmark 隐藏提示
 * @param viewRecord 需要显示的记录数据
 * @param recordDetailSheetContent 记录详情 sheet，参数：(记录数据) -> [Unit]
 * @param date 当前选择的年月信息
 * @param onMenuClick 菜单点击回调
 * @param onDateClick 日期点击回调
 * @param onDateSelected 日期选择回调
 * @param onSearchClick 搜索点击回调
 * @param onCalendarClick 日历点击回调
 * @param onMyAssetClick 我的资产点击回调
 * @param onAddClick 添加点击回调
 * @param uiState 界面 UI 数据
 * @param onRecordItemClick 记录列表 item 点击回调
 * @param dialogState 弹窗状态
 * @param onRequestDismissDialog 隐藏弹窗
 * @param onRequestDismissSheet 隐藏 sheet
 * @param onShowSnackbar 显示 [androidx.compose.material3.Snackbar]，参数：(显示文本，action文本) -> [SnackbarResult]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LauncherContentScreen(
    shouldDisplayDeleteFailedBookmark: Int,
    onRequestDismissBookmark: () -> Unit,
    viewRecord: RecordViewsEntity?,
    recordDetailSheetContent: @Composable (RecordViewsEntity?) -> Unit,
    date: YearMonth,
    onMenuClick: () -> Unit,
    onDateClick: () -> Unit,
    onDateSelected: (YearMonth) -> Unit,
    onSearchClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onMyAssetClick: () -> Unit,
    onAddClick: () -> Unit,
    uiState: LauncherContentUiState,
    onRecordItemClick: (RecordViewsEntity) -> Unit,
    dialogState: DialogState,
    onRequestDismissDialog: () -> Unit,
    onRequestDismissSheet: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> SnackbarResult,
    modifier: Modifier = Modifier,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed),
) {
    // 提示文本
    val deleteFailedFormatText = stringResource(id = R.string.delete_failed_format)
    // 显示提示
    LaunchedEffect(shouldDisplayDeleteFailedBookmark) {
        if (shouldDisplayDeleteFailedBookmark > 0) {
            val result = onShowSnackbar.invoke(
                deleteFailedFormatText.format(shouldDisplayDeleteFailedBookmark), null
            )
            if (SnackbarResult.Dismissed == result) {
                onRequestDismissBookmark.invoke()
            }
        }
    }

    CashbookScaffold(
        modifier = modifier,
        topBar = {
            LauncherTopBar(
                dateStr = "${date.year}-${date.monthValue.completeZero()}",
                onMenuClick = onMenuClick,
                onDateClick = onDateClick,
                onSearchClick = onSearchClick,
                onCalendarClick = onCalendarClick,
                onMyAssetClick = onMyAssetClick,
            )
        },
        floatingActionButton = {
            CashbookFloatingActionButton(onClick = onAddClick) {
                Icon(imageVector = CashbookIcons.Add, contentDescription = null)
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (null != viewRecord) {
                // 显示记录详情底部抽屉
                CashbookModalBottomSheet(
                    onDismissRequest = onRequestDismissSheet,
                    sheetState = rememberModalBottomSheetState(
                        confirmValueChange = {
                            if (it == SheetValue.Hidden) {
                                onRequestDismissSheet()
                            }
                            true
                        },
                    ),
                    content = {
                        recordDetailSheetContent(viewRecord)
                    },
                )
            }

            when (uiState) {
                LauncherContentUiState.Loading -> {
                    Loading(modifier = Modifier.align(Alignment.Center))
                }

                is LauncherContentUiState.Success -> {
                    BackdropScaffold(
                        scaffoldState = scaffoldState,
                        appBar = { /* 使用上层 topBar 处理 */ },
                        peekHeight = paddingValues.calculateTopPadding(),
                        backLayerBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        backLayerContent = {
                            // 背景布局
                            BackLayerContent(
                                paddingValues = paddingValues,
                                monthIncome = uiState.monthIncome,
                                monthExpand = uiState.monthExpand,
                                monthBalance = uiState.monthBalance,
                            )
                        },
                        frontLayerScrimColor = Color.Unspecified,
                        frontLayerContent = {
                            FrontLayerContent(
                                dialogState = dialogState,
                                onDateClick = onDateClick,
                                onDateSelected = onDateSelected,
                                onRequestDismissDialog = onRequestDismissDialog,
                                recordMap = uiState.recordMap,
                                onRecordItemClick = onRecordItemClick,
                            )
                        },
                    )
                }
            }
        }
    }
}

/**
 * 标题栏
 *
 * @param dateStr 日期文本
 * @param onMenuClick 菜单点击回调
 * @param onDateClick 日期点击回调
 * @param onSearchClick 搜索点击回调
 * @param onCalendarClick 日历点击回调
 * @param onMyAssetClick 资产点击回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LauncherTopBar(
    dateStr: String,
    onMenuClick: () -> Unit,
    onDateClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onMyAssetClick: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = {
            Text(
                text = dateStr,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clickable(onClick = onDateClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = CashbookIcons.Menu,
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = CashbookIcons.Search,
                    contentDescription = null,
                )
            }
            IconButton(onClick = onCalendarClick) {
                Icon(
                    imageVector = CashbookIcons.CalendarMonth,
                    contentDescription = null,
                )
            }
            IconButton(onClick = onMyAssetClick) {
                Icon(
                    imageVector = CashbookIcons.WebAsset,
                    contentDescription = null,
                )
            }
        },
    )
}

/**
 * 背景布局
 *
 * @param paddingValues 背景 padding 数据
 * @param monthIncome 月收入
 * @param monthExpand 月支出
 * @param monthBalance 月结余
 */
@Composable
private fun BackLayerContent(
    paddingValues: PaddingValues,
    monthIncome: String,
    monthExpand: String,
    monthBalance: String,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onTertiaryContainer) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Text(text = stringResource(id = R.string.month_income))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = monthIncome.withCNY())
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${stringResource(id = R.string.month_expend)} ${monthExpand.withCNY()}",
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${stringResource(id = R.string.month_balance)} ${monthBalance.withCNY()}",
                )
            }
        }
    }
}

/**
 * 内容布局
 *
 * @param dialogState 弹窗状态
 * @param onDateClick 日期点击回调
 * @param onDateSelected 日期选中回调
 * @param onRequestDismissDialog 隐藏弹窗
 * @param recordMap 记录列表数据
 * @param onRecordItemClick 记录列表 item 点击回调
 */
@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun FrontLayerContent(
    dialogState: DialogState,
    onDateClick: () -> Unit,
    onDateSelected: (YearMonth) -> Unit,
    onRequestDismissDialog: () -> Unit,
    recordMap: Map<RecordDayEntity, List<RecordViewsEntity>>,
    onRecordItemClick: (RecordViewsEntity) -> Unit
) {
    CashbookGradientBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            (dialogState as? DialogState.Shown<*>)?.data?.let { date ->
                if (date is YearMonth) {
                    // 显示选择日期弹窗
                    SelectDateDialog(
                        onDialogDismiss = onRequestDismissDialog,
                        date = date,
                        onDateSelected = onDateSelected,
                    )
                }
            }

            if (recordMap.isEmpty()) {
                Empty(
                    hintText = stringResource(id = R.string.launcher_no_data_hint),
                    button = {
                        FilledTonalButton(onClick = onDateClick) {
                            Text(text = stringResource(id = R.string.launcher_no_data_button))
                        }
                    },
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            } else {
                LazyColumn {
                    recordMap.keys.reversed().forEach { key ->
                        val recordList = recordMap[key] ?: emptyList()
                        stickyHeader {
                            Row(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = when (key.dayType) {
                                        0 -> stringResource(id = R.string.today)
                                        -1 -> stringResource(id = R.string.yesterday)
                                        -2 -> stringResource(id = R.string.before_yesterday)
                                        else -> "${key.day}${stringResource(id = R.string.day)}"
                                    }
                                )
                                Text(
                                    text = buildString {
                                        val totalIncome = key.dayIncome.toDoubleOrZero()
                                        val totalExpenditure = key.dayExpand.toDoubleOrZero()
                                        val hasIncome = totalIncome != 0.0
                                        if (hasIncome) {
                                            append(
                                                stringResource(id = R.string.income_with_colon) + totalIncome.decimalFormat()
                                                    .withCNY()
                                            )
                                        }
                                        if (totalExpenditure != 0.0) {
                                            if (hasIncome) {
                                                append(", ")
                                            }
                                            append(
                                                stringResource(id = R.string.expend_with_colon) + totalExpenditure.decimalFormat()
                                                    .withCNY()
                                            )
                                        }
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            Divider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = DividerDefaults.color.copy(0.3f),
                            )
                        }
                        items(recordList, key = { it.id }) {
                            RecordListItem(
                                recordViewsEntity = it,
                                onRecordItemClick = {
                                    onRecordItemClick(it)
                                },
                            )
                        }
                    }

                    item {
                        Footer(hintText = stringResource(id = R.string.footer_hint_default))
                    }
                }
            }
        }
    }
}

/**
 * 记录列表 item 布局
 *
 * @param recordViewsEntity 记录数据
 * @param onRecordItemClick 点击回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordListItem(
    recordViewsEntity: RecordViewsEntity,
    onRecordItemClick: () -> Unit,
) {
    TransparentListItem(
        modifier = Modifier.clickable {
            onRecordItemClick()
        },
        leadingContent = {
            Icon(
                painter = painterDrawableResource(idStr = recordViewsEntity.typeIconResName),
                contentDescription = null
            )
        },
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = recordViewsEntity.typeName)
                val tags = recordViewsEntity.relatedTags
                if (tags.isNotEmpty()) {
                    val tagsText = with(StringBuilder()) {
                        tags.forEach { tag ->
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
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 4.dp),
                    )
                }
            }
        },
        supportingContent = {
            Text(
                text = "${
                    recordViewsEntity.recordTime.split(" ").first()
                } ${recordViewsEntity.remark}"
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // TODO 关联记录
                Text(
                    text = buildAnnotatedString {
                        append(recordViewsEntity.amount.withCNY())
                    },
                    color = when (recordViewsEntity.typeCategory) {
                        RecordTypeCategoryEnum.EXPENDITURE -> LocalExtendedColors.current.expenditure
                        RecordTypeCategoryEnum.INCOME -> LocalExtendedColors.current.income
                        RecordTypeCategoryEnum.TRANSFER -> LocalExtendedColors.current.transfer
                    },
                    style = MaterialTheme.typography.labelLarge,
                )
                recordViewsEntity.assetName?.let { assetName ->
                    Text(text = buildAnnotatedString {
                        val hasCharges = recordViewsEntity.charges.toDoubleOrZero() > 0.0
                        val hasConcessions = recordViewsEntity.concessions.toDoubleOrZero() > 0.0
                        if (hasCharges || hasConcessions) {
                            // 有手续费、优惠信息
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append("(")
                            }
                            if (hasCharges) {
                                withStyle(style = SpanStyle(color = LocalExtendedColors.current.expenditure)) {
                                    append("-${recordViewsEntity.charges}".withCNY())
                                }
                            }
                            if (hasConcessions) {
                                if (hasCharges) {
                                    append(" ")
                                }
                                withStyle(style = SpanStyle(color = LocalExtendedColors.current.income)) {
                                    append("+${recordViewsEntity.concessions.withCNY()}")
                                }
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append(") ")
                            }
                        }
                        append(assetName)
                        if (recordViewsEntity.typeCategory == RecordTypeCategoryEnum.TRANSFER) {
                            append(" -> ${recordViewsEntity.relatedAssetName}")
                        }
                    })
                }
            }
        },
    )
}

@DevicePreviews
@Composable
internal fun LauncherContentScreenPreview() {
    PreviewTheme(
        defaultEmptyImagePainter = painterResource(id = R.drawable.vector_no_data_200),
    ) {
        LauncherContentRoute()
    }
}