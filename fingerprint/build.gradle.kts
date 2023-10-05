import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

group = "com.github.fingerprintjs"

fun libraryOutputName(variantName: String): String {
    return "fingerprint-android-${project.property("VERSION_NAME")}-${variantName}.aar"
}

publishing {
    publications {
        create<MavenPublication>("fpRelease") {
            groupId = "com.github.fingerprintjs"
            artifactId = "fingerprint-android"
            version = project.property("VERSION_NAME") as String
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    compileSdk = 34

    defaultConfig {
        // This property does not affect the library itself, but affects test apk and lint.
        // As for now, I don't see any non-deprecated ways of accomplishing this task.
        // Discussions:
        // https://stackoverflow.com/questions/76084080/apply-targetsdk-in-android-instrumentation-test
        // https://issuetracker.google.com/issues/230625468 (looks like lint.targetSdk and testOptions.targetSdk will become available soon)
        targetSdk = 33

        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
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

    namespace = "com.fingerprintjs.android.fingerprint"

    libraryVariants.all {
        val variantName = this.name
        this.outputs.all {
            (this as? BaseVariantOutputImpl)?.apply {
                if (this.outputFileName.endsWith(".aar")) {
                    this.outputFileName = libraryOutputName(variantName)
                }
            }
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

androidComponents {
    onVariants {
        it.androidTest?.packaging?.resources?.excludes?.add("META-INF/*")
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
    if (!this.name.contains("Test")) {
        kotlinOptions.freeCompilerArgs += "-Xexplicit-api=warning"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Constants.kotlinVersion}")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.12.8")
    androidTestImplementation("io.mockk:mockk:1.12.8")
    androidTestImplementation ("io.mockk:mockk-android:1.12.8")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
}
