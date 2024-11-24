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

# Menjaga kelas utama, misalnya Activity, Service, atau BroadcastReceiver
-keep class alv.splash.browser.** { *; }

# Keep native libraries untuk GeckoView
-keep class org.mozilla.geckoview.** { *; }

# Jangan hilangkan class atau metode terkait JNI
-keepclassmembers class * {
    native <methods>;
}


# Menghapus debugging information agar ukuran APK lebih kecil

#-dontobfuscate
-dontpreverify
-dontoptimize

# Menghapus log untuk mengurangi ukuran APK
-assumenosideeffects class android.util.Log { *; }

# Mengoptimalkan kode menggunakan metode default
-optimizationpasses 5