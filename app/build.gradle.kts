plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlin.ksp)
	alias(libs.plugins.androidx.room)
	alias(libs.plugins.hilt.android)
}

android {
	namespace = "io.github.bimoid"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "io.github.bimoid"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
	kotlin {
		jvmToolchain(21)
	}
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        // Unable to strip the following libraries, packaging them as they are:
        jniLibs.keepDebugSymbols.add("**/libandroidx.graphics.path.so")
    }
    room {
		schemaDirectory("$projectDir/schemas")
	}
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    ksp(libs.kotlin.metadata.jvm)
	implementation(libs.obimp4j.core)
	implementation(libs.obimp4j.client)
	implementation(libs.room.runtime)
	ksp(libs.room.compiler)
	implementation(libs.room.ktx)
	implementation(libs.hilt.android)
	ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
	testImplementation(libs.room.testing)
	testImplementation(libs.hilt.android.testing)
	kspTest(libs.hilt.compiler)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	androidTestImplementation(libs.hilt.android.testing)
	kspAndroidTest(libs.hilt.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

hilt {
	enableAggregatingTask = true
}