
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

-assumenosideeffects class android.util.Log {
      public static boolean isLoggable(java.lang.String, int);
      public static int v(...);
      public static int i(...);
      public static int w(...);
      public static int d(...);
      public static int e(...);
  }

-keep class com.aimyskin.asentinel.**{*;}
-keep class com.aimyskin.EncrypyPortUtils

-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

-keepclassmembers class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}