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
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
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
    testImplementation("io.mockk:mockk:1.12.7")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
}
