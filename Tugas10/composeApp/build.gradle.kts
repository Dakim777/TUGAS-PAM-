import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // === KOIN DI ===
            implementation("io.insert-koin:koin-core:3.5.3")
            implementation("io.insert-koin:koin-compose:1.1.2")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

            // === UNIT TEST & FLOW TEST ===
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            implementation("app.cash.turbine:turbine:1.0.0")
            implementation("io.insert-koin:koin-test:3.5.3")
        }

        // === FIX MOCKK & DEPENDENCIES UNTUK ANDROID UNIT TEST ===
        val androidUnitTest by getting {
            dependencies {
                implementation("io.mockk:mockk:1.13.9")

                // Ditulis eksplisit di sini agar Android Studio tidak bug/merah saat file dipindah
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                implementation("app.cash.turbine:turbine:1.0.0")
                implementation("io.insert-koin:koin-test:3.5.3")
                implementation(libs.kotlin.test)
            }
        }

        // === COMPOSE UI TEST (ANDROID) ===
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")

                // FIX ERROR INPUTMANAGER UNTUK ANDROID 14+
                implementation("androidx.test:core-ktx:1.6.1")
                implementation("androidx.test.ext:junit:1.2.1")
                implementation("androidx.test:runner:1.6.1")
                implementation("androidx.test:rules:1.6.1")
                implementation("androidx.test.espresso:espresso-core:3.6.1")

                // UPDATE VERSI COMPOSE UI TEST
                implementation("androidx.compose.ui:ui-test-junit4:1.6.7")
            }
        }
    }
}

android {
    namespace = "com.angkringan.tugas10"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.angkringan.tugas10"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // === RUNNER UNTUK INSTRUMENTED TEST ===
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            // Tambahkan dua baris ini saja
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }


        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)

    // === UI TEST MANIFEST ===
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.7")
}