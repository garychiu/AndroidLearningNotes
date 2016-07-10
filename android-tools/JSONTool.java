package com.seeknovel.bpmcsps.utils;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by zhp93 on 2016-05-04.
 */
@SuppressWarnings("ALL")
public class JSONTool{
    static ObjectMapper objectMapper;

    public static <T> T readValue(String content, Class<T> valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJSon(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JavaType getParamterType(ObjectMapper mapper, Class<?> valueCalss, Class<?> paramterClass){
        return mapper.getTypeFactory().constructParametricType(valueCalss, paramterClass);
    }
    /*
     * 自定义对象中包含另外的对象，针对泛型的情况
     * */
    public static <T> T readValueOObject(String content, Class<T> valueCalss,Class<?> paramterClass) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(content, getParamterType(objectMapper,valueCalss,paramterClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JavaType getCollectionType(ObjectMapper mapper,Class<?> collectionClass, Class<?>... elementClasses) {

        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
    /*
     * 解析的数据中包含自定义的List,Map
     *
     * */
    public static <T> T readValueListObject(String content, Class<T> collectionClass,Class<?>...elementClasses) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(content, getCollectionType(objectMapper,collectionClass,elementClasses));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
