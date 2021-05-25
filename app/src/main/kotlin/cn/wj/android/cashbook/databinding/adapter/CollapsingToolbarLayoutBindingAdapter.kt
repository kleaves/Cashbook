@file:Suppress("unused")

package cn.wj.android.cashbook.databinding.adapter

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * CollapsingToolbarLayout DataBinding 适配器
 *
 * - 创建时间：2020/11/20
 *
 * @author 王杰
 */

/**
 * 为 [CollapsingToolbarLayout] 设置折叠时标题文本颜色 [color]，[Int]类型颜色值
 * > 可使用资源类型 android:bind_params="@{@color/app_white}"
 */
@BindingAdapter("android:bind_ctl_collapsedTitleTextColor")
fun CollapsingToolbarLayout.setCollapsedTitleTextColor(@ColorInt color: Int?) {
    if (null == color) {
        return
    }
    setCollapsedTitleTextColor(color)
}

/**
 * 为 [CollapsingToolbarLayout] 设置展开时标题文本颜色 [color]，[Int]类型颜色值
 * > 可使用资源类型 android:bind_params="@{@color/app_white}"
 */
@BindingAdapter("android:bind_ctl_expandedTitleTextColor")
fun CollapsingToolbarLayout.setExpandedTitleColor(@ColorInt color: Int?) {
    if (null == color) {
        return
    }
    setExpandedTitleColor(color)
}

/** 添加折叠进度监听 */
@BindingAdapter("android:bind_ctl_onOffsetChanged")
fun CollapsingToolbarLayout.addOnOffsetChangedListener(onChanged: ((Int, Int) -> Unit)?) {
    if (null == onChanged) {
        return
    }
    val parent = this.parent
    if (parent is AppBarLayout) {
        parent.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            onChanged.invoke(verticalOffset, parent.totalScrollRange)
        })
    }
}