import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {

    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {

    namespace = "io.bashpsk.zerodownload.core.network"

    compileSdk {

        version = release(37)
    }

    defaultConfig {

        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {

        compose = true
    }
}

kotlin {

    compilerOptions {

        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }
}

dependencies {

    //  DEFAULT             :
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //  KOTLINX             :
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.io.core)

    //  KTOR                :
    implementation(libs.bundles.ktor.client)

    //  HILT                :
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    //  EMPTY LIBS          :
    implementation(libs.bundles.bashpsk.emptylibs)

    //  YOUTUBE-DL          :
    implementation(libs.bundles.github.youtubedl.android)

    //  JACKSON             :
    implementation(libs.bundles.fasterxml.jackson.core)

    //  COMMON IO           :
    implementation(libs.commons.io)
    implementation(libs.apache.commons.compress)

    //  MODULE              :
    implementation(fileTree("libs") { include("*.aar") })
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}