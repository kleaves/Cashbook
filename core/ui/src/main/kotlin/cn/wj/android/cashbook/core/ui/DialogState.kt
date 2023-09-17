package cn.wj.android.cashbook.core.ui

/**
 * 弹窗状态
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2023/6/19
 */
sealed interface DialogState {

    /** 弹窗隐藏 */
    data object Dismiss : DialogState

    /** 弹窗显示 */
    class Shown<T>(val data: T) : DialogState
}