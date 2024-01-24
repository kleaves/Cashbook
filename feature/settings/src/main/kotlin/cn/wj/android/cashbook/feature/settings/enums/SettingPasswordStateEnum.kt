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

package cn.wj.android.cashbook.feature.settings.enums

/**
 * 密码相关操作状态
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2023/6/14
 */
enum class SettingPasswordStateEnum {

    /** 成功 */
    SUCCESS,

    /** 加密失败 */
    PASSWORD_ENCODE_FAILED,

    /** 解密失败 */
    PASSWORD_DECODE_FAILED,

    /** 密码错误 */
    PASSWORD_WRONG,
}