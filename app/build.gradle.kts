plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.aimyskin.ultherapy_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aimyskin.ultherapy_android"
        minSdk = 24
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isPseudoLocalesEnabled  = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug{
            isPseudoLocalesEnabled  = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation("androidx.activity:activity:1.8.0")
    val lifeCycleVersion = "2.7.0"
    val materialVersion = "1.4.0"
    val fragmentVersion = "1.7.0"
    val navVersion = "2.7.7"
    val roomVersion = "2.6.1"
    val pagingVersion = "3.3.0"

    implementation(project(":MiscModule"))

    //Base
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Dialog
    implementation("com.afollestad.material-dialogs:core:3.2.1")
    implementation("com.afollestad.material-dialogs:input:3.3.0")
    //Display
    implementation("com.github.JessYanCoding:AndroidAutoSize:v1.2.1")
    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    //LiveData
    implementation("com.kunminx.arch:unpeek-livedata:7.8.0")
    implementation("io.github.jeremyliao:live-event-bus-x:1.8.0")
    //Material
    implementation("com.google.android.material:material:$materialVersion")
    //Fragment
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    //Util
    implementation("com.blankj:utilcodex:1.31.1")
    implementation("com.chibatching.kotpref:kotpref:2.13.1")
    implementation("com.github.GrenderG:Toasty:1.5.2")
    implementation("cat.ereza:customactivityoncrash:2.4.0")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    ksp("com.github.bumptech.glide:compiler:4.11.0")
    //Room
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$roomVersion")
    implementation("androidx.paging:paging-runtime:$pagingVersion")
    // alternatively - without Android dependencies for tests
    testImplementation("androidx.paging:paging-common:$pagingVersion")
}