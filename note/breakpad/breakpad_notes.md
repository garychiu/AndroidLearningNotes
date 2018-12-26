
# 利用 Google 的 [breakpad](https://github.com/google/breakpad) 分析 android native 崩溃异常

##  1.下载 [depot_tools](http://dev.chromium.org/developers/how-tos/install-depot-tools) 
 Google 资源，可设置代理，
 ```
 # proxy
 export http_proxy="http://127.0.0.1:1087"
 export https_proxy="http://127.0.0.1:1087"
 ```
 
 clone 代码，并设置环境变量：
 ```
  git clone --depth=1 https://chromium.googlesource.com/chromium/tools/depot_tools.git
 ```
 
 ## 2.编译 breakpad
 ```
 // 创建目录
 mkdir breakpad && cd breakpad
 // 检出 资源
 fetch breakpad
 cd src
 // 构建
 ./configure && make
 // 测试
 make check
 // 安装
 make install
 ```
 
在上面构建之后就会生成相关工具：`breakpad/src/src/processor/minidump_stackwalk`

## 3. 利用 `minidump_stackwalk` 解析 dump 文件
```
.minidump_stackwalk ***.dmp >crashLog.txt 
```
如下内容：

```
Operating system: Android
                  0.0.0 Linux 4.4.23+ #1 SMP PREEMPT Mon Nov 12 18:06:49 CST 2018 aarch64
CPU: arm64
     8 CPUs

GPU: UNKNOWN

Crash reason:  SIGSEGV /SEGV_MAPERR
Crash address: 0x0
Process uptime: not available

Thread 0 (crashed)
 0  libcrash-lib.so + 0x600 // ------------------------------------- 发生 crash 的地址 0x600
     x0 = 0x00000072316bb2a0    x1 = 0x0000007fc6458444
     x2 = 0x0000007fc64586a0    x3 = 0x000000723393fa58
     x4 = 0x0000007fc64582b0    x5 = 0x0000007233a8fa48
     x6 = 0x0000007233a8f9d0    x7 = 0x0000000000000000
     x8 = 0x0000000000000000    x9 = 0x0000000000000001
    x10 = 0x0000000000430000   x11 = 0x0000000000000000
    x12 = 0x0000007235e2ded0   x13 = 0x40a714d5338a1ecd
    x14 = 0x0000007236112000   x15 = 0xffffffffffffffff
    x16 = 0x00000072143f2fe8   x17 = 0x00000072143e25ec
    x18 = 0x0000000000000020   x19 = 0x00000072316a3a00
    x20 = 0x000000723393fab8   x21 = 0x00000072316a3a00
    x22 = 0x0000007fc64586fc   x23 = 0x0000007233a986e0
    x24 = 0x0000000000000004   x25 = 0x00000072316a3aa0
    x26 = 0x0000000000000000   x27 = 0x0000000000000000
    x28 = 0x0000007fc6458440    fp = 0x0000007fc6458410
     lr = 0x00000072143e2624    sp = 0x0000007fc64583f0
     pc = 0x00000072143e2600
    Found by: given as instruction pointer in context
 1  libcrash-lib.so + 0x620
     fp = 0x0000007fc6458440    lr = 0x00000072314df704
     sp = 0x0000007fc6458420    pc = 0x00000072143e2624
    Found by: previous frame's frame pointer
 2  libart.so + 0x512700
     fp = 0x168b0cb000000001    lr = 0x0000000000000000
     sp = 0x0000007fc6458450    pc = 0x00000072314df704

```

## 4. 利用 `NDK` 下的 `addr2line` 工具解析符号
arm64 平台对应的工具地址：
```
$NDK_HOME/toolchains/aarch64-linux-android-4.9/prebuilt/darwin-x86_64/bin/aarch64-linux-android-addr2line
```
 > 根据对一个的平台找对应的工具。
 
 根据上面的 dump 信息找到对应的 so 文件，这里是：sample/build/intermediates/transforms/mergeJniLibs/debug/0/lib/arm64-v8a/libcrash-lib.so
 
 然后使用下面的命令解析符号：
 
 ```
./aarch64-linux-android-addr2line -f -C -e libcrash-lib.so 0x600

// 输出如下：
Crash()
XXX/sample/src/main/cpp/crash.cpp:10
 ```
 
 > 注意上面的 `0x600` 是 dump 中crash处的地址。
 
 参考：[https://github.com/AndroidAdvanceWithGeektime/Chapter01](https://github.com/AndroidAdvanceWithGeektime/Chapter01)
 
 
