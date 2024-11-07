# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



# 泛型
-keepattributes Signature
# 注解
-keepattributes *Annotation*
# 不优化
#-dontoptimize
# 代码循环优化次数，0-7，默认为5
-optimizationpasses 5
#包名不使用大小写混合 aA Aa
-dontusemixedcaseclassnames
# 混淆后生产映射文件 map 类名->转化后类名的映射
# 存放在app\build\outputs\mapping\release中
-verbose
#不混淆第三方引用的库
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# 混淆时所采用的优化规则
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 关闭log
-assumenosideeffects class android.util.Log {
      public static boolean isLoggable(java.lang.String, int);
      public static int v(...);
      public static int i(...);
      public static int w(...);
      public static int d(...);
      public static int e(...);
  }

  # 混淆后生产映射文件 map 类名->转化后类名的映射
  # 存放在app\build\outputs\mapping\release中
  -verbose


# R文件下面的资源
-keep class **.R$* {*;}

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
#androidx包使用混淆
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

-keepclassmembers enum * {}

-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

-keep class com.example.imagealgorithmdemo.**{*;}

-dontwarn rx.**


