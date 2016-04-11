### as 上jni 使用
* 1.新建项目，在activity中添加相应的方法，如下示例：<br/>
        
        //加载库
        static {
            System.loadLibrary("JNIDemo");
        }
        //自己添加的需要调用的方法
        public native String getStringFromNative();
* 2.build -> build project
* 3.打开terminal，输入命令生成.h文件，命令格式如下：<br/>
        
        javah -d jni -classpath <SDK_android.jar>;<APP_classes> packageName.MainActivity

      注：如果编译出错，找不发v7，需要添加v4,v7包下的支持的android库。如下示例：<br/>
        
        javah -d jni -classpath D:\software\Android\as_sdk\platforms\android-23\android.jar;
        D:\software\Android\as_sdk\extras\android\support\v4\android-support-v4.jar;
        D:\software\Android\as_sdk\extras\android\support\v7\appcompat\libs\android-support-v7-appcompat.jar;
        ..\..\build\intermediates\classes\debug cn.imtianx.jnidemo.MainActivity
        
* 4.编辑c文件：
把.h文件中生成的方法拷贝到文件中，进行实现。完成后再<poject>gradle.properties文件中添加：<br/>
            
        android.useDeprecatedNdk = true
     然后再执行2；
* 5.检查local.properties中是否添加的有ndk的路径，如果没有则添加;然后在build.gradle<model>中的
defaultConfig下添加如下代码：
        
        ndk {
            moduleName "JNIDemo"
            ldLibs "log", "z", "m"
            abiFilters "armeabi", "armeabi-v7a", "x86"
        }
    执行：build->Rebuild Project,就会生成相应的so 文件（位置：modelName/app/build/intermediates/ndk/debug/lib）
