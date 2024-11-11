import java.util.Date
import java.text.SimpleDateFormat
import kotlin.io.print

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

fun getTime(): String {
    return SimpleDateFormat("yyyyMMddHHmmss").format(Date())
}

android {
    namespace = "com.aimyskin.ultherapy_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aimyskin.ultherapy_android"
        minSdk = 24
        targetSdk = 34
        versionCode = 102
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("packJKS") {
            keyAlias = "ultherapy_android"
            keyPassword = "2024110817"
            storeFile = file("${rootDir.absolutePath}/ultherapy_android.jks")
            storePassword = "2024110817"
        }
    }

    buildTypes {
        val mySignConfig = signingConfigs.getByName("packJKS")
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            applicationIdSuffix = ".release"
            signingConfig = mySignConfig
        }
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = mySignConfig

        }
    }

    android.buildTypes.forEach { buildType ->
        // release 或 debug
        val typeName = buildType.name
        //版本号
        val versionName = defaultConfig.versionName
        val date = getTime()
        android.productFlavors.map { it.name }.ifEmpty { listOf("") }
            .forEach { flavorName ->
                // 将获取到的名称首字母变为大写，比如：release变为Release
                val combineName = "${flavorName.capitalize()}${typeName.capitalize()}"
                // 为我们的任务命名：比如叫nodeParserRelease
                val taskName = "ultherapy_android$combineName"
                // 找到打包的任务，比如ultherapy_androidrelease就是assembleRelease任务
                val originName = "assemble$combineName"
                // 创建一个任务专门做我们的自定义打包任务
                project.task(taskName) {
                    // 为任务分组
                    group = "UltherapyAndroid apk"
                    // 执行我们的任务之前会先执行的任务，比如，打release包时会先执行assembleRelease任务
                    dependsOn(originName)
                    // 执行完任务后，我们将得到的APK 重命名并输出到根目录下的apks文件夹下
                    doLast {
                        copy {
                            from(File(project.buildDir, "outputs/apk/$typeName"))
                            into(File(rootDir, "apks"))
                            rename { "UltherapyAndroid_${typeName}_V${versionName}_$date.apk" }
                            include("*.apk")
                        }
                    }
                }
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
    implementation(project(mapOf("path" to ":LaserSerialModule")))
    implementation(project(mapOf("path" to ":ASentinel")))
    implementation(project(mapOf("path" to ":UdiskUpgradeModule")))
    implementation(project(mapOf("path" to ":ResourceModule")))
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
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("com.afollestad.material-dialogs:input:3.3.0")
    implementation("com.afollestad.material-dialogs:datetime:3.3.0")

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
    // 上拉刷新，下拉加载更多
    implementation("io.github.scwang90:refresh-layout-kernel:2.1.0")      //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:2.1.0")    //经典刷新头
    implementation("io.github.scwang90:refresh-footer-classics:2.1.0")     //经典加载
    //
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")
    implementation("com.github.Jay-Goo:RangeSeekBar:3.0.0")
}