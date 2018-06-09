# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers                                              # 是否混淆第三方jar
-dontpreverify
-dontwarn com.alibaba.fastjson.**
-dontwarn android.support.**

-verbose
-ignorewarnings
-keep class android.** {*;}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}



#-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
#  public static final android.os.Parcelable$Creator *;
#}

# For using GSON @Expose annotation
-keepattributes *Annotation*
#-keep class com.dtr.zxing.**{*;}
-keep class com.diligroup.mode.**{*;}
-keep class com.diligroup.bean.**{*;}
-keep class de.greenrobot.event.**{*;}
-dontwarn de.greenrobot.event.**

-keepattributes Signture
-keepclassmembers class ** {

    public void onEvent(**);

}
#pullToRefresh的保留
-dontwarn com.handmark.pulltorefresh.library.**
-keep class com.handmark.pulltorefresh.library.** { *;}
-dontwarn com.handmark.pulltorefresh.library.extras.**
-keep class com.handmark.pulltorefresh.library.extras.** { *;}
-dontwarn com.handmark.pulltorefresh.library.internal.**
-keep class com.handmark.pulltorefresh.library.internal.** { *;}
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,Synthetic,EnclosingMethod
#如果有引用v4包可以添加下面这行
-keep class android.support.v4.**{*;}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#-keep class android.graphics.BitmapFactory$*
#-keep class android.graphics.BitmapFactory$* {*;}
-keep class android.content.SharedPreferences$*
-keep class android.content.SharedPreferences$* {*;}
#支付宝第三方登录混淆配置
#-libraryjars libs/alipaySDK-20160812.jar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
#讯飞语音
-keep class com.iflytek.**{*;}
#百度地图
-keep class com.baidu.** {*;}
-keep class pvi.com.** {*;}
-dontwarn com.baidu.**
#-dontwarn vi.com.**
#-dontwarn pvi.com.**
# butterknife 混淆配置
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# google gson配置
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

-keepattributes Signature
-keepattributes *Annotation*
-keep public class com.project.mocha_patient.login.SignResponseData { private *; }


