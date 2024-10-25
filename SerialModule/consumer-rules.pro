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

-keep class com.aimyskin.serialmodule.**{*;}

-keep class com.squareup.okio.**{*;}
-keep class javolution.**{*;}
-keep class android.serialport.**{*;}
-keep class com.hoho.android.usbseria.**{*;}


-keepclassmembers class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
















