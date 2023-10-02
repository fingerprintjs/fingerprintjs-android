import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fingerprintjs.android.playground"
        minSdk = 21
        targetSdk = 33
        versionCode = Integer.parseInt(project.property("VERSION_CODE") as String)
        versionName = project.property("VERSION_NAME") as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_SIGN_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_SIGN_KEY_PASSWORD")
        }

        create("releaseDummySign") {
            storeFile = file("release_dummy.jks")
            storePassword = "password"
            keyAlias = "key0"
            keyPassword = "password"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        // build release app locally using a dummy signature
        create("releaseDummySign") {
            isMinifyEnabled = true
            proguardFiles (getDefaultProguardFile ("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("releaseDummySign")
            matchingFallbacks += listOf("release")
        }
        // same as previous, but also profileable
        // when changing the name of the following build type, don't forget to update src/{this_build_type} dir
        create("releaseDummySignProfileable") {
            initWith(buildTypes.getByName("releaseDummySign"))
        }
    }

    namespace = "com.fingerprintjs.android.playground"

    applicationVariants.all {
        val variant = this
        this.outputs.all {
            (this as? BaseVariantOutputImpl)?.outputFileName = "Playground-${variant.name}-${variant.versionName}.apk"
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(project(":fingerprint"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Constants.kotlinVersion}")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    val composeBom = platform("androidx.compose:compose-bom:2022.11.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.google.accompanist:accompanist-pager:0.27.0")

    val daggerVersion = "2.48"
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

    implementation("com.google.code.gson:gson:2.10")
}
