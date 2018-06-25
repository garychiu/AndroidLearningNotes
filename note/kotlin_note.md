# kotlin 使用注意事项

## 1、gson 解析注意事项

**问题：** json 解析 默认参数无法实例化

**分析：** 通常使用 `data class` 作为 model, 但它没有无参构造函数，所以直接将 json 字符串转换给对应的实体对象时，默认参数无法实例化，不管任何类型，全部为 `null`。这里通过查看 gson 源码 追踪到 `UnsafeAllocator`类，他的 `create`方法，通过反射 `sun.misc.Unsafe` 类来创建的，使用他创建对象回越过构造方法。

**解决办法：** 可以使用 `moarg` 插件，生成无参构造，但对于默认参数仍然无法解决。可以重新创建对象，将 gson 解析后的结果copy过来；或者 定义对应实体的 `InstanceCreator`，告诉 gson 如何实例化这个对象。
