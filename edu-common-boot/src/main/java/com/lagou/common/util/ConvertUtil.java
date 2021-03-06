package com.lagou.common.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ConvertUtil
 * @Description 封装拷贝属性的工具类：ConverUtil
 * @Author zhouqian
 * @Date 2022/4/3 19:33
 * @Version 1.0
 */

public class ConvertUtil {
    /**
     * 将原对象的属性值，拷贝到目标对象对应的属性中
     * @param source   原对象
     * @param target   目标对象
     * @param <S>
     * @param <T>
     * @return         属性拷贝完成的目标对象
     */
    public static <S,T> T convert(S source,T target){
        if(source == null || target == null){
            return null;
        }
        BeanCopier copier = BeanCopier.create(source.getClass(),target.getClass(),false);
        T result = target;
        copier.copy(source,result,null);
        return result;
    }

    public static <S,T> T convert(S source, Class<T> target) {
        try {
            return convert(source,target.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *拷贝列表
     */
    public static <S,T> List<T> convertList(List<S> source, Class<T> target){
        if(source == null){
            return null;
        }
        return source.stream().map(item -> {
            T result = null;
            try {
                result = target.newInstance();
                convert(item,result);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return result;
        }).collect(Collectors.toList());
    }
}
