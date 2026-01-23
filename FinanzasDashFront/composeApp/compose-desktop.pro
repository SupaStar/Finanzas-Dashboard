# ===============================
# Ktor + ServiceLoader
# ===============================
-keep class io.ktor.serialization.** { *; }
-keep class io.ktor.client.plugins.** { *; }

-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

-adaptresourcefilenames META-INF/services/**
-adaptresourcefilecontents META-INF/services/**

# ===============================
# Kotlinx Serialization
# ===============================
-keep class kotlinx.serialization.** { *; }

# ===============================
# Ignore OPTIONAL dependencies
# ===============================
-dontwarn org.apache.hc.**
-dontwarn org.brotli.dec.**
-dontwarn org.conscrypt.**
-dontwarn android.util.Log
-dontwarn io.ktor.utils.io.jvm.javaio.**

# ===============================
# Logging (safe to ignore)
# ===============================
-dontwarn org.slf4j.**
