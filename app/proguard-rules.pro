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

# Keep Kotlin metadata and reflection info
-keepclassmembers class kotlin.Metadata { *; }

# Keep Compose runtime and UI classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Firebase classes needed for reflection & serialization
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Room Database
-keep class androidx.room.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>(...);
}
-keep @androidx.room.* class * { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# ZXing
-keep class com.google.zxing.** { *; }

# Coil (image loading)
-keep class coil.** { *; }

# CameraX
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# Timber (logging) — you can strip logs in release to reduce size
-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
}

# Serialization (Kotlinx Serialization)
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# Ktor (network client)
-keep class io.ktor.** { *; }

# Avoid warnings from generated code
-dontwarn kotlinx.coroutines.**
-dontwarn kotlin.reflect.jvm.internal.impl.**

# Keep all activities, services, receivers and providers referenced in manifest
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
# Keep Application class
-keep class com.ovais.quickcode.core.app.** { *; }
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
# Ignore Ktor's debug detector class that references unavailable Java SE classes
-dontwarn io.ktor.util.debug.IntellijIdeaDebugDetector