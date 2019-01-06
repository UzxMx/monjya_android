# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xmx/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer
-dontwarn javax.security.**
-dontwarn java.awt.**
-dontwarn java.beans.**
-dontwarn android.webkit.WebResourceError
-dontwarn android.webkit.WebViewClient

-dontwarn org.apache.harmony.**
-dontwarn com.android.org.conscrypt.**
-dontwarn java.net.PlainSocketImpl
-dontwarn java.net.**

-dontwarn com.squareup.picasso.**
-dontwarn fr.dvilleneuve.android.**
-dontwarn com.viewpagerindicator.**
-dontnote com.j256.ormlite.**

-dontwarn com.genyus.pacpiechart.**

-keep class android.support.** {
    *;
}

-keep class com.tencent.** {
    *;
}

-keep class com.sina.** {
    *;
}

# jackson
-keepnames class com.fasterxml.jackson.** {
    *;
}

-keepnames interface com.fasterxml.jackson.** {
    *;
}

# 以下代码可以使得 proguard 保留 generic type, jackson 解析List的时候,需要 generic type, 否则
# 就会产生一个 LinkedHashMap
-keepattributes Signature



# whether we need this or not??
-keepclassmembers @com.fasterxml.jackson.annotation.JsonIgnoreProperties class com.monjya.android.** {
    public <init>();
    *** is*();
    *** get*();
    void set*(***);
}

# ormlite related
-keep class com.j256.ormlite.** {
    *;
}

-keepclassmembers class com.monjya.android.*DBHelper {
    public <init>(android.content.Context);
}

-keepclassmembers class com.monjya.android.** { # 由于 ormlite DatabaseField
    @com.j256.ormlite.field.DatabaseField <fields>;
}

# 在抛出异常时, 会添加 line number
-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable

# from JPush
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

# from UMeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}