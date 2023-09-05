package cn.wj.android.cashbook.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.wj.android.cashbook.R
import cn.wj.android.cashbook.base.ext.base.string
import cn.wj.android.cashbook.base.ext.shaEncode
import cn.wj.android.cashbook.base.ui.BaseViewModel
import cn.wj.android.cashbook.data.event.LifecycleEvent
import cn.wj.android.cashbook.data.live.PasswordLiveData
import cn.wj.android.cashbook.data.model.UiNavigationModel
import cn.wj.android.cashbook.data.transform.toSnackbarModel

/**
 * 清除密码 ViewModel
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2022/10/11
 */
class VerifyPasswordViewModel : BaseViewModel() {

    /** 成功事件 */
    val verifySuccessEvent: LifecycleEvent<Int> = LifecycleEvent()

    /** 取消事件 */
    val cancelEvent: LifecycleEvent<Int> = LifecycleEvent()

    /** 提示文本 */
    val hintStr: MutableLiveData<String> = MutableLiveData()

    /** 旧密码 */
    val password: MutableLiveData<String> = MutableLiveData()

    /** 确认点击 */
    val onConfirmClick: () -> Unit = fun() {
        val pwd = password.value.orEmpty()
        if (pwd.shaEncode() != PasswordLiveData.value) {
            // 密码错误
            snackbarEvent.value = R.string.please_enter_right_password.string.toSnackbarModel()
        } else {
            // 验证成功，回调
            verifySuccessEvent.value = 0
            // 关闭弹窗
            uiNavigationEvent.value = UiNavigationModel.builder {
                close()
            }
        }
    }

    /** 取消点击 */
    val onCancelClick: () -> Unit = {
        cancelEvent.value = 0
        uiNavigationEvent.value = UiNavigationModel.builder {
            close()
        }
    }
}