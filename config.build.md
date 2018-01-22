
```
ext {
    compileSdkVersion = 26
    buildToolsVersion = "27.0.1"
    minSdkVersion = 15
    targetSdkVersion = 26
    versionCode = 1
    versionName = "1.0.0"
    supportVersion = '26.1.0'
}
```
---
```
static def releaseTime(format = "yyyyMMddHHmm") {
    def calendar = new GregorianCalendar()
    def timeZone = calendar.getTimeZone()
    new Date().format(format, timeZone)
}
```
---
```
Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())
signingConfigs {
    release {
            storeFile file(properties.getProperty("keystroe_storeFile"))
            storePassword properties.getProperty("keystroe_storePassword")
            keyAlias properties.getProperty("keystroe_keyAlias")
            keyPassword properties.getProperty("keystroe_keyPassword")
    }
}

```
---
```
signingConfigs {
    release {
        storeFile file("imtianx.jks")
        storePassword 'imtianx'
        keyAlias 'imtianx'
        keyPassword 'imtianx'
    }
}
flavorDimensions("test")
productFlavors {
    meizu {}
    huawei {}
    baidu {}
}

def changeApkName(baseApkName = "test") {
    android.applicationVariants.all { variant ->
        variant.outputs.all {
            if ("true" == IS_JENKINS) {
                outputFileName = "${baseApkName}_${releaseTime()}_${variant.buildType.name}_jenkins.apk"
            } else {
                outputFileName = "${baseApkName}_${variant.productFlavors[0].name}_${android.defaultConfig.versionName}_${releaseTime()}.apk"
            }
        }
    }
}
```
---
