import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.androidx.room)
}

android {

    namespace = "io.bashpsk.zerodownload"
    compileSdk { version = release(36) }

    defaultConfig {

        applicationId = "io.bashpsk.zerodownload"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {

            useSupportLibrary = true
        }
    }

    buildTypes {

        release {

            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {

        compose = true
    }

    splits {

        abi {

            isEnable = !project.hasProperty("noSplits")
            reset()
            include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
            isUniversalApk = true
        }
    }

    packaging {

        resources {

            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }

        jniLibs {

            useLegacyPackaging = true
        }
    }
}

kotlin {

    compilerOptions {

        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.apply {

            add("-Xcontext-parameters")
            add("-XXLanguage:+ExplicitBackingFields")
            add("-Xexplicit-backing-fields")
        }
    }
}

room {

    schemaDirectory("$projectDir/schemas")
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

    //  ADAPTIVE LAYOUT     :
    implementation(libs.bundles.androidx.compose.material3.adaptive)

    //  WINDOW              :
    implementation(libs.androidx.material3.window.size)

    //  KOTLINX             :
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.io.core)

    //  NAVIGATION          :
    implementation(libs.bundles.androidx.navigation3)

    //  MATERIAL ICONS      :
    implementation(libs.androidx.material.icons.extended)

    //  DATASTORE           :
    implementation(libs.androidx.datastore.preferences)

    //  ROOM                :
    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    //  KTOR                :
    implementation(libs.bundles.ktor.client)

    //  HILT                :
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    //  WORKER              :
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    //  SPLASH              :
    implementation(libs.androidx.core.splashscreen)

    //  STARTUP             :
    implementation(libs.androidx.startup.runtime)

    //  MEDIA3              :
    implementation(libs.bundles.androidx.media3)

    //  EMPTY LIBS          :
    implementation(libs.bundles.bashpsk.emptylibs)

    //  PERMISSION          :
    implementation(libs.google.accompanist.permissions)

    //  COIL3               :
    implementation(libs.bundles.coil3.kt)

    //  YOUTUBE-DL          :
    implementation(libs.bundles.github.youtubedl.android)

    //  MODULE              :
    implementation(fileTree("libs") { include("*.aar") })
    implementation(project(":utilities"))
    implementation(project(":ytdl-ext"))
}