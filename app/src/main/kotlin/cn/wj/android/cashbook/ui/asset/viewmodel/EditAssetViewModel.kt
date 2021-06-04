package cn.wj.android.cashbook.ui.asset.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cn.wj.android.cashbook.R
import cn.wj.android.cashbook.base.ext.base.condition
import cn.wj.android.cashbook.base.ext.base.logger
import cn.wj.android.cashbook.base.ext.base.string
import cn.wj.android.cashbook.base.tools.dateFormat
import cn.wj.android.cashbook.base.ui.BaseViewModel
import cn.wj.android.cashbook.data.entity.AssetEntity
import cn.wj.android.cashbook.data.enums.AssetClassificationEnum
import cn.wj.android.cashbook.data.enums.ClassificationTypeEnum
import cn.wj.android.cashbook.data.model.UiNavigationModel
import cn.wj.android.cashbook.data.store.LocalDataStore
import cn.wj.android.cashbook.data.transform.toSnackbarModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * 编辑资产 ViewModel
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2021/6/2
 */
class EditAssetViewModel(private val local: LocalDataStore) : BaseViewModel() {

    /** 资产 id，新建为 -1L */
    var id = -1L

    /** 编辑资产创建时间 */
    var createTime = ""

    /** 标记资产余额 */
    private var oldBalance = ""

    /** 资产分类大类 */
    val classificationType: MutableLiveData<ClassificationTypeEnum> = MutableLiveData(ClassificationTypeEnum.CAPITAL_ACCOUNT)

    /** 资产分类 */
    val assetClassification: MutableLiveData<AssetClassificationEnum> = MutableLiveData(AssetClassificationEnum.CASH)

    /** 标记 - 是否是信用卡 */
    val creditCardType: LiveData<Boolean> = classificationType.map {
        it == ClassificationTypeEnum.CREDIT_CARD_ACCOUNT
    }

    /** 余额提示文本 */
    val balanceHint: LiveData<String> = creditCardType.map {
        if (it) {
            // 信用卡类型，提示欠款
            R.string.current_arrears
        } else {
            // 其他类型，提示余额
            R.string.asset_balance
        }.string
    }

    /** 资产名称 */
    val assetName: MutableLiveData<String> = MutableLiveData()

    /** 名称错误提示 */
    val nameError: ObservableField<String> = ObservableField()

    /** 信用卡总额度 */
    val totalAmount: MutableLiveData<String> = MutableLiveData()

    /** 余额、信用卡欠款 */
    val balance: MutableLiveData<String> = MutableLiveData()

    /** 账单日 */
    val billingDate: MutableLiveData<String> = MutableLiveData()

    /** 还款日 */
    val repaymentDate: MutableLiveData<String> = MutableLiveData()

    /** 标记 - 是否隐藏资产 */
    val invisibleAsset: MutableLiveData<Boolean> = MutableLiveData()

    /** 显示选择资产类型弹窗 */
    val showSelectAssetClassificationData: MutableLiveData<Int> = MutableLiveData()

    /** 标题文本 */
    val titleStr: ObservableField<String> = ObservableField(R.string.new_asset.string)

    /** 保持按钮是否允许点击 */
    val saveEnable: ObservableBoolean = ObservableBoolean(true)

    /** 返回按钮点击 */
    val onBackClick: () -> Unit = {
        // 退出当前界面
        uiNavigationData.value = UiNavigationModel.builder {
            close()
        }
    }

    /** 资产类型点击 */
    val onAssetClassificationClick: () -> Unit = {
        showSelectAssetClassificationData.value = 0
    }

    /** 账单日点击 */
    val onBillingDateClick: () -> Unit = {
        // TODO
        snackbarData.value = "账单日".toSnackbarModel()
    }

    /** 还款日点击 */
    val onRepaymentDateClick: () -> Unit = {
        // TODO
        snackbarData.value = "还款日".toSnackbarModel()
    }

    /** 保存点击 */
    val onSaveClick: () -> Unit = {
        saveAsset()
    }

    /** 保存资产 */
    private fun saveAsset() {
        val type = classificationType.value ?: return
        val classification = assetClassification.value ?: return
        val name = assetName.value
        if (name.isNullOrBlank()) {
            // 资产名称不能为空
            nameError.set(R.string.asset_name_cannot_be_empty.string)
            return
        }
        val totalAmount = totalAmount.value.orEmpty()
        val balance = balance.value.orEmpty()
        val billingDate = billingDate.value.orEmpty()
        val repaymentDate = repaymentDate.value.orEmpty()
        val invisible = invisibleAsset.value.condition
        val currentTime = System.currentTimeMillis().dateFormat()
        viewModelScope.launch {
            try {
                saveEnable.set(false)
                if (id >= 0) {
                    // 编辑
                    local.updateAsset(
                        AssetEntity(
                            id = id,
                            name = name,
                            totalAmount = totalAmount,
                            billingDate = billingDate,
                            repaymentDate = repaymentDate,
                            type = type,
                            classification = classification,
                            invisible = invisible,
                            createTime = createTime,
                            modifyTime = currentTime,
                            balance = balance
                        )
                    )
                    if (balance == oldBalance) {
                        // 余额没有修改
                        callOnSaveSuccess()
                    } else {
                        // 保存余额
                        saveBalance(id, balance)
                    }
                } else {
                    // 新建
                    val id = local.insertAsset(
                        AssetEntity(
                            id = -1,
                            name = name,
                            totalAmount = totalAmount,
                            billingDate = billingDate,
                            repaymentDate = repaymentDate,
                            type = type,
                            classification = classification,
                            invisible = invisible,
                            createTime = currentTime,
                            modifyTime = currentTime,
                            balance = balance
                        )
                    )
                    // 保存资产余额
                    saveBalance(id, balance)
                }
            } catch (throwable: Throwable) {
                saveEnable.set(true)
                logger().e(throwable, "saveAsset")
            }
        }
    }

    /** TODO 保存资产余额 */
    private fun saveBalance(id: Long, balance: String) {
        callOnSaveSuccess()
    }

    /** 保存成功 */
    private fun callOnSaveSuccess() {
        // 保存成功，提示并关闭界面
        snackbarData.value = R.string.save_success.string.toSnackbarModel(onCallback = object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                uiNavigationData.value = UiNavigationModel.builder {
                    close()
                }
            }
        })
    }

    /** TODO 刷新资产对应余额 */
    fun refreshBalance() {
        if (id < 0) {
            return
        }
        oldBalance = "1000.00"
        balance.value = oldBalance
    }
}