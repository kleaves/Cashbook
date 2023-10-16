plugins {
    alias(libs.plugins.cashbook.android.library)
    alias(libs.plugins.cashbook.android.library.compose)
    alias(libs.plugins.cashbook.android.library.jacoco)
}

android {
    namespace = "cn.wj.android.cashbook.core.design"
}

dependencies {

    implementation(projects.core.common)

    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.google.material)
}
