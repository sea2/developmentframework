-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizations !class/unboxing/enum

-allowaccessmodification
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep public class * {
    public protected *;
}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers class * {
    @javax.annotation.Resource *;
}

#remove log code
# public static int e(...);
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);

}

# # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

# # -------------------------------------------
# #  ######## EventBus混淆  ##########
# # -------------------------------------------
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    public void onEventMainThread*(**);
}
#-libraryjars /libs/jpush-android-2.1.0.jar
#-libraryjars /libs/bugly_crash_release__2.1.jar

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

-dontwarn android.os.**
-dontwarn com.android.internal.**

-keepattributes SourceFile,LineNumberTable

# # -------------------------------------------
# #  ######## 极光混淆  ##########
# # -------------------------------------------
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# # -------------------------------------------
# #  ######## Modle 包下文件不混淆  ##########
# # -------------------------------------------
-keep class com.xcm91.xiaocaimi.model.** {*;}
-keep class com.xcm91.xiaocaimi.others.model.** {*;}
-keep class com.google.** {*;}
-dontwarn com.google.**
-dontwarn com.xcm91.xiaocaimi.model.**
-dontwarn com.xcm91.xiaocaimi.others.model.**
-keep class com.xcm91.xiaocaimi.others.database.** {*;}
-keep class android.support.** {*;}
-keep class com.nostra13.universalimageloader.** {*;}
-keep class de.greenrobot.** {*;}

-keepattributes *Annotation*
-keepattributes Exceptions, Signature, InnerClasses
-keepattributes SourceFile,LineNumberTable

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }


#TONGDUN
-dontwarn android.os.**
-dontwarn com.android.internal.**
-keep class cn.tongdun.android.**{*;}


#udesk
-keep class udesk.** {*;}
-keep class cn.udesk.**{*; }
#七牛
-keep class com.qiniu.android.dns.** {*; }
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class com.qiniu.android.** {*;}
#smack
-keep class org.jxmpp.** {*;}
-keep class de.measite.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.xmlpull.** {*;}
#Android M 权限
-keep class rx.** {*;}
-keep class com.tbruyelle.rxpermissions.** {*;}

 #其它
-keep class com.tencent.bugly.** {*; }
-keep class com.nostra13.universalimageloader.** {*;}
-keep class de.hdodenhof.circleimageview.** {*;}
 #混淆和3.2.1的时候是不一样的


#talkingdata
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.** { *; }