package com.jackie0.common.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ClassName:CustomCollectionUtils <br/>
 * Date:     2015年11月03日 17:09 <br/>
 *
 * @author jackie0
 * @since JDK 1.8
 */
public class CustomCollectionUtils {
    private CustomCollectionUtils() {
    }

    /**
     * 从一个Collection对象中抽取元素里的指定属性值，并放到List中，Collection元素必须有公用get方法才能抽取
     *
     * @param datas                要抽取的Collection数据
     * @param elementsPropertyName 要抽取的元素的属性名称
     * @param clazz                元素属性的class类型
     * @param <E>                  Collection元素泛型
     * @param <T>                  Collection元素属性泛型
     * @return 指定元素属性的值列表
     * @see #getElementsPropertiesValue(Collection, String, boolean, Class)
     */
    public static <E, T> List<T> getElementsPropertiesValue(Collection<E> datas, String elementsPropertyName, Class<T> clazz) {
        return getElementsPropertiesValue(datas, elementsPropertyName, false, clazz);
    }

    /**
     * 从一个Collection对象中抽取元素里的指定属性值，并放到List中，Collection元素必须有公用get方法才能抽取
     *
     * @param datas                要抽取的Collection数据
     * @param elementsPropertyName 要抽取的元素的属性名称
     * @param keepDistinct         是否去重,true需要去重，false不需要去重
     * @param clazz                元素属性的class类型
     * @param <E>                  Collection元素泛型
     * @param <T>                  Collection元素属性泛型
     * @return 指定元素属性的值列表
     */
    public static <E, T> List<T> getElementsPropertiesValue(Collection<E> datas, String elementsPropertyName, boolean keepDistinct, Class<T> clazz) {
        if (CollectionUtils.isEmpty(datas)) {
            return Collections.emptyList();
        }
        List<T> elementsPropertiesValue = new ArrayList<>();
        for (E data : datas) {
            T propVal = DataUtils.getField(data, elementsPropertyName, clazz);
            if (propVal != null) {
                if ((keepDistinct && !elementsPropertiesValue.contains(propVal)) || !keepDistinct) {
                    elementsPropertiesValue.add(propVal);
                }
            }
        }
        return elementsPropertiesValue;
    }


    /**
     * 检查集合中是否存在与指定对象指定属性值相同的元素，比较调用equals方法
     *
     * @param srcCollection        要检查的集合
     * @param checkObj             要检查的指定对象
     * @param elementsPropertyName 要检查的属性名称
     * @param clazz                要检查的属性类型
     * @param <E>                  Collection元素泛型
     * @param <T>                  Collection元素属性泛型
     * @return 具有相同元素返回true, 否则返回false
     */
    public static <E, T> boolean isExistSamePropElement(Collection<E> srcCollection, E checkObj, String elementsPropertyName, Class<T> clazz) {
        if (CollectionUtils.isEmpty(srcCollection) || checkObj == null) {
            return false;
        }
        for (E element : srcCollection) {
            T propVal = DataUtils.getField(element, elementsPropertyName, clazz);
            T propValSrc = DataUtils.getField(checkObj, elementsPropertyName, clazz);
            if (propVal != null && propVal.equals(propValSrc)) {
                return true;
            }
        }
        return false;
    }
}
