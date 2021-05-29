package cn.wj.android.cashbook.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.wj.android.cashbook.R
import cn.wj.android.cashbook.base.ext.TabsAdapter
import cn.wj.android.cashbook.base.ext.base.string
import cn.wj.android.cashbook.ui.fragment.ConsumptionTypeFragment

/**
 * 编辑记录界面 ViewPager 适配器
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2021/5/28
 */
class EditRecordVpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity), TabsAdapter {

    override fun getPageTitle(position: Int): String? {
        if (position < 0 || position >= TABS_TITLE.size) {
            return null
        }
        return TABS_TITLE[position]
    }

    override fun getItemCount(): Int {
        return TABS_TITLE.size
    }

    override fun createFragment(position: Int): Fragment {
        return ConsumptionTypeFragment.newInstance(position)
    }

    companion object {
        /** 标签标题数组 */
        private val TABS_TITLE = arrayOf(
            R.string.spending.string,
            R.string.income.string,
            R.string.transfer.string
        )
    }
}