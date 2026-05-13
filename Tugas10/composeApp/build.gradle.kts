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

            // === TAMBAHAN: KOIN DI ===
            implementation("io.insert-koin:koin-core:3.5.3")
            implementation("io.insert-koin:koin-compose:1.1.2")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

            // === TAMBAHAN: UNIT TEST & FLOW TEST ===
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            implementation("app.cash.turbine:turbine:1.0.0")
            implementation("io.mockk:mockk:1.13.9")
            implementation("io.insert-koin:koin-test:3.5.3")
        }

        // === TAMBAHAN: COMPOSE UI TEST (ANDROID) ===
        val androidInstrumentedTest by getting {
            dependencies {
                implementation("androidx.compose.ui:ui-test-junit4")
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
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
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

    // === TAMBAHAN: UI TEST MANIFEST (ANDROID) ===
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}