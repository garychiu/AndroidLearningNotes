# android开发笔记
[toc]

## 一、android 
### 1.隐藏软键盘
```
if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
{
    if (getCurrentFocus() != null)
        ( (InputMethodManager) getSystemService (
              Context.INPUT_METHOD_SERVICE) ).
        hideSoftInputFromWindow (getCurrentFocus().getWindowToken(),
                                 InputMethodManager.HIDE_NOT_ALWAYS);
}
```
### 2.使用%S为字符串赋值
在string.xml中添加：
```
<string name="phone_sdk_version">当前手机的系统版本为：%s</string>
```
在java 代码中为其赋值：
```
textView.setText (String.format (getResources().getString (R.string.phone_sdk_version), Build.VERSION.RELEASE) );

```
### 3.获取设备唯一ID
```
String clientId = ((TelephonyManager)getSystemService(
				Context.TELEPHONY_SERVICE)).getDeviceId();
```
### 4.再按一次退出
```
private boolean isOnKeyBacking = false;
private final Handler mainLoopHandler = new Handler (Looper.getMainLooper() );
@Override
public boolean onKeyDown (int keyCode, KeyEvent event)
{
    if (keyCode == KeyEvent.KEYCODE_BACK)
    {
        if (isOnKeyBacking)
        {
            mainLoopHandler.removeCallbacks (onBackTimeThread);
            isOnKeyBacking = false;
            finish();
        }
        else
        {
            isOnKeyBacking = true;
            Toast.makeText (this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mainLoopHandler.postDelayed (onBackTimeThread, 2000);
        }
        return true;
    }
    else
    {
        return super.onKeyDown (keyCode, event);
    }
}
private final Runnable onBackTimeThread = new Runnable()
{
    public void run()
    {
        isOnKeyBacking = false;
    }
};
```
### 5.json解析

 - 返回的json中含有转义字符'\'，可以用string的replace("\\","")去掉后再转换成对象；
 - 解析时可以为了方便操作，更改本地的实体类型，便于解析使用（如：服务器返回的City对象，对象中放的是cityid,可以将本地的city改为String类型）

### 6.线程安全
使用handler避免内存泄露:

 - 用static声明handler，静态类不会引用外部类；
 - 如果Handler中必须用到Activity，那就用WeakReference去引用
 - 在Activity结束或暂停的事件中，removeMessages或者removeCallbacksAndMessages将消息队列中的消息移除（避免满足上面两条后，当Activity关闭了，但是Handler还未处理到，造成内存泄露）

### 7.广播等解绑
注册的广播需要添加unregisterReceiver()方法，及时释放，避免内存泄露
 
### 8.as drawable 图片详解
在as中，图片依然是放在drawable下，而mipmap是存放应用不同分辨率的 icon图标的。手机在加载显示图片是会根据不同的分辨率开始加载的。
加载过程：
如当前手机屏幕分辨率为 xxhdpi,加载如下：
xxhdpi -> xxxhdpi -> nodpi(密度无关) -> xhdpi -> hdpi -> mdpi -> ldpi
首先是在当前文件夹，如果没有，就向更高分辨率文件夹下找，如果有则加载缩放显示，如果没有更高分辨率，则到与密度无关的文件夹下匹配，如果没有，则再依次到低分辨文件夹下查找，如果有就加载并放大显示。
实际开发图片一般放在 xxxhdpi文件夹下，如果在低分辨我呢减价下，加载图片会消耗更多的内存，容易出现oom。
### 9.禁止Edittext弹出软件盘，光标依然正常显示。
```
 public void disableShowSoftInput(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEtInputNo.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(mEtInputNo, false);
            } catch (Exception e) {
                // TODO: handle exception
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
```
### 10.在listview中进行模糊查询可以使利用java中的String.contains()方法。
```
//查询str1中是否有str2
str1.conatins(str2)
```
### 11.拨打电话
```
Intent intent = new Intent(
Intent.ACTION_DIAL, Uri.parse("tel:"+tel);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
```

### 12. 获取手机应用的名称及icon
获取手机中已经安装的应用:
```
List<PackageInfo> packageInfoList = mPackageManager.getInstalledPackages(0);
```
获取应用名：
```
public String getApplicationName(
    String packageName,PackageManager packageManager) {    
    String applicationName=null;    
    try {        
        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
    } catch (PackageManager.NameNotFoundException e) {
    }    
    return applicationName;
}
```
与`getApplicationLabel`类似的，有`getApplicationIcon`方法来获取应用的名称，然而在部分的国内手机上无显示。可以利用`ApplicationInfo`类来获取更多的app信息：
```
public Drawable loadIcon(PackageManager pm) {    
    return pm.loadItemIcon(this, getApplicationInfo());
}
```
此外，ApplicationInfo类中还有如下属性：
```
public long firstInstallTime;//第一次安装时间
public long lastUpdateTime;//最后更新时间
public String sourceDir;//apk安装路径
```
获取apk大小（通过安装路径sourceDir，获取文件大小）：
```
File apkFile = new File(packageInfo.applicationInfo.sourceDir);
int size = apkFile.length() / 1024 / 1024;//获取文件大小，并转换成M
```

### 13. 获取app使用的流量
使用[TrafficStats类](https://developer.android.com/reference/android/net/TrafficStats.html)中的相关方法：
```
PackageManager manager = getPackageManager();
    try {
        ApplicationInfo info = manager.getApplicationInfo("com.seeknovel.bpmcsps",
                PackageManager.GET_META_DATA);
        //上传流量
        long tx = TrafficStats.getUidTxBytes(info.uid);
        //下载流量
        long rx = TrafficStats.getUidRxBytes(info.uid);
        Log.i("imtianx", "uid： " + info.uid + "\t上传：" + tx + "\t下载：" + rx);
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
```
注：该方法在6.0机器上没问题，但在4.4的上面无法获取上传流量。

### 14.手机号正则
```
/**
 *
 * 手机号号段(2017-09-01)
 * <p>
 *     移动号段：134 135 136 137 138 139 147 148 150 151 152 157 158 159 172 178 182 183 184 187 188 198
 *     联通号段：130 131 132 145 146 155 156 166 175 176 185 186
 *     电信号段：133 149 153 173 177 180 181 189 199
 *     虚拟运营商号段：170 171
 * </p>
 */
String regex = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(166)|(17([0-3]|[5-8]))|(18[0-9])|(19([8|9])))\\d{8}$";

```

### 15.透明度百分比对应16进制值

>100% — FF
95% — F2
90% — E6
85% — D9
80% — CC
75% — BF
70% — B3
65% — A6
60% — 99
55% — 8C
50% — 80
45% — 73
40% — 66
35% — 59
30% — 4D
25% — 40
20% — 33
15% — 26
10% — 1A
  5% — 0D
  0% — 00

### 16.善用textview的tools属性
对于textview设置文本，仅为预览用，运行后不显示，可以用下面属性：
```
tools:text="imtianx"
```

## 二、gradle & adb 

### 1.gradle 查看依赖
```
//查看model名为app的依赖：
gradle :app:dependencies --configuration compile	
//查看指定库retrofit2的依赖：
gradle :app:dependencyInsight --dependency retrofit2 --configuration compile
```

### 2.查看android 权限
```
//全部权限
adb shell pm list permissions
//危险权限
adb shell pm list permissions -d -g
```

### 3. as 设置依赖库最新版本
为了是项目使用的依赖库为最新的版本，可以使用下面的两种方式设置依赖：（以jackson为例）

```
//1.在版本号后 添加 +
compile 'com.fasterxml.jackson.core:jackson-databind:2.8.3+'
//2.用 latest.integration 代替版本号
compile 'com.fasterxml.jackson.core:jackson-databind:latest.integration'
```

### 4.查看sha1安全码
`win+R` 打开命令行，进入`.android`目录下：
```
cd .android
```
然后执行如下命令,密钥库口令为:`android`,即可显示密钥信息:
```
keytool -v -list -keystore debug.keystore
```
已签名app查看，解压出 `CERT.RS` 文件
```
keytool -printcert -file D:\testtool\META-INF\CERT.RSA
```

### 5.monkey 测试

```
monkey -p com.puyue.www.xinge --ignore-crashes --ignore-timeouts --ignore-native-crashes --pct-touch 30  -v -v --throttle 200 1000 

```

### 6.cpu架构信息
```
adb shell
cat /proc/cpuinfo

```


## 三、git

### 1、git 回退 

撤回git commit push 操作：

```
git rest --hard <版本号>
git push origin branch_name -- force

```
> `--hard` 不保留提交记录，使用 `force` 强制提交，不能pull




## 四、other

### 1.Geohash 算法(可用在基于LBS距离排序)
参考1：[ 如何实现按距离排序、范围查找](http://blog.csdn.net/ghsau/article/details/50591932)
参考2：[JAVA实现空间索引编码](http://blog.csdn.net/xiaojimanman/article/details/50358506)

**简记：**
纬度：[-90,90]，纬度：[-180,180]；
(1). 对给定的坐标，经纬度分别进行二分，如果坐标点在左区间为0，右区间1;
(2). 最后得到20位数字：
纬度：10111001001101000110		奇数
经度：11010010101010100101		偶数
两者合并，其中纬度在奇数位，京东在偶数位，编码合并如下：
&nbsp;1 0 1 1 1 0 0 1 0 0 1 1 0 1 0 0 0 1 1 0	
	1 1 0 1 0 0 1 0 1 0 1 0 1 0 1 0 0 1 0 1		    
合并结果：
 1110011101001001100011011001100000110110
(3). base32处理(每五位合并转换为16进制)：
	11100&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;28&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;w
	11101&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;29&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;x
	00100&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 4&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 4
	11000&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;24&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; s
	11011&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;27&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; v
	00110&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 6 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 6
	00001&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1
	10110&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;22&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; q

最后组合上面的8位16进制数值即为该坐标的geohash编码。


  [1]: http://blog.csdn.net/ghsau/article/details/50591932
