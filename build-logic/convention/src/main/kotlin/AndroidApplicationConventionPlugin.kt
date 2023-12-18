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

import cn.wj.android.cashbook.buildlogic.ProjectSetting
import cn.wj.android.cashbook.buildlogic.configureBadgingTasks
import cn.wj.android.cashbook.buildlogic.configureGradleManagedDevices
import cn.wj.android.cashbook.buildlogic.configureKotlinAndroid
import cn.wj.android.cashbook.buildlogic.configurePrintApksTask
import cn.wj.android.cashbook.buildlogic.version
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

/**
 * Kotlin Android Application 插件
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2022/9/1
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(ProjectSetting.Plugin.PLUGIN_ANDROID_APPLICATION)
                apply(ProjectSetting.Plugin.PLUGIN_KOTLIN_ANDROID)
                apply(ProjectSetting.Plugin.PLUGIN_DEPENDENCY_GUARD)
                apply(ProjectSetting.Plugin.PLUGIN_CASHBOOK_LINT)
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(ProjectSetting.Config.javaVersion.version)
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                with(defaultConfig) {
                    targetSdk = ProjectSetting.Config.TARGET_SDK

                    // 应用版本号
                    versionCode = ProjectSetting.Config.versionCode
                    // 应用版本名
                    versionName = ProjectSetting.Config.versionName
                }
                configureGradleManagedDevices(this)
            }

            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
                configureBadgingTasks(extensions.getByType<BaseExtension>(), this)
            }
        }
    }
}
