package com.yofish.kitmodule.util;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableByte;
import android.databinding.ObservableChar;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.databinding.ObservableShort;

import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.base_component.viewmodel.ItemViewModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * DataBinding帮助类
 * <p>
 * Created by hch on 2018/12/14.
 */
public class DataBindingHelper {


    /**
     * ItemViewModel
     * 将bean对象转换为相应的ItemViewModel对象
     *
     * @param bean    bean对象
     * @param clazz   对应的VM的class
     * @param constructorParameter 构造参数，用于创建VM对象
     * @param <T>     转换的目标对象必须继承BaseViewModel
     * @return VM对象
     * @throws IllegalAccessException    异常
     * @throws InstantiationException    异常
     * @throws NoSuchMethodException     异常
     * @throws InvocationTargetException 异常
     */
    public static <T extends ItemViewModel, E extends BaseViewModel> T parseBean2VM(Object bean, Class<T> clazz, E constructorParameter)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T result = clazz.getConstructor(constructorParameter.getClass()).newInstance(constructorParameter);
        beginParse(bean, result);
        return result;
    }

    /**
     * 将bean对象转换为相应的ViewModel对象
     *
     * @param bean bean对象
     * @param vm   对应的VM的对象
     * @param <T>  转换的目标对象必须继承BaseViewModel
     */
    public static <T extends BaseViewModel> void parseBean2VM(Object bean, T vm) {
        try {
            beginParse(bean, vm);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始转换
     *
     * @param bean bean
     * @param vm   vm
     * @param <T>  BaseViewModel子类
     * @throws IllegalAccessException 异常
     */
    private static <T> void beginParse(Object bean, T vm) throws IllegalAccessException {
        if (bean == null) {
            return;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        Field[] vmFields = vm.getClass().getFields();
        for (Field field : fields) {
            Object value = invokeGetMethod(bean, field);
            if (value == null) {
                continue;
            }
            for (Field vmField : vmFields) {
                /** 找到属性名称一样的Field */
                if (field.getName().equals(vmField.getName())) {
                    /**
                     * 校验该属性类型是否是BaseObservable的子类，如：ObservableField，
                     * ObservableInt等
                     */
                    if (vmField.get(vm) instanceof BaseObservable) {
                        BaseObservable observable = (BaseObservable) vmField.get(vm);
                        try {
                            invokeObversableSetMethod(observable, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 反射获取bean中对应fieldName属性名的值
     *
     * @param bean  bean对象
     * @param field 属性对象
     * @return 属性值
     */
    private static Object invokeGetMethod(Object bean, Field field) {
        String fieldName = field.getName();
        String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String prefix = "get";
        /** boolean类型的属性get方法默认为isFieldName */
        if (field.getType().getName().equals(boolean.class.getName())) {
            prefix = "is";
            String methodNameForBoolean = fieldName.substring(fieldName.startsWith("is") ? 2 : 0);
            methodName = Character.toUpperCase(methodNameForBoolean.charAt(0)) + methodNameForBoolean.substring(1);
        }
        try {
            Method method = bean.getClass().getDeclaredMethod(prefix + methodName);
            return method.invoke(bean);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反射设置BaseObservable属性的的值，通过set(T val)方法设置
     *
     * @param observable vm中的继承BaseObservable的属性
     * @param value      要赋予的值
     */
    private static void invokeObversableSetMethod(BaseObservable observable, Object value)
            throws InvocationTargetException, IllegalAccessException {
        /**
         * ObservableField中定义的set方法为public void set(T value){} 改方法被编译后为 public
         * void set(java.lang.Object value){} 所以此处获取方法时参数为Object.class类型
         */
        Class[] args = getObversableSetMethodArgs(observable);
        Method method = null;
        try {
            method = observable.getClass().getMethod("set", args);
        } catch (NoSuchMethodException e) {
            return;
        }
        Object[] obj = new Object[1];
        obj[0] = value;
        method.invoke(observable, obj);
    }

    /**
     * 获取VM中set方法的参数类型，目前只支持ObservableField类型和八种基本数据类型
     *
     * @param observable observable
     * @return args
     */
    private static Class[] getObversableSetMethodArgs(BaseObservable observable) {
        Class[] args = new Class[1];
        if (observable instanceof ObservableField) {
            args[0] = Object.class;
        } else if (observable instanceof ObservableInt) {
            args[0] = int.class;
        } else if (observable instanceof ObservableFloat) {
            args[0] = float.class;
        } else if (observable instanceof ObservableDouble) {
            args[0] = double.class;
        } else if (observable instanceof ObservableShort) {
            args[0] = short.class;
        } else if (observable instanceof ObservableLong) {
            args[0] = long.class;
        } else if (observable instanceof ObservableBoolean) {
            args[0] = boolean.class;
        } else if (observable instanceof ObservableByte) {
            args[0] = byte.class;
        } else if (observable instanceof ObservableChar) {
            args[0] = char.class;
        } else {
            /** 其它类型暂不支持 */
        }
        return args;
    }

    /**
     * 将beans转化为对应的vmList
     * <p>
     * 关于使用：1、目前只支持装换为ObservableField类型和ObservableInt等八种基本数据类型
     * 2、要求bean中的属性数据类型和VM中对应属性的数据类型要一致 3、只能转换VM中的public字段
     * <p>
     * 关于性能：经测试，转换20个bean对象，每个bean对象有8个String属性，耗时为21毫秒，平均每个对象转换耗时为1毫秒
     *
     * @param beans   bean对象集合
     * @param clazz   目标VM的class
     * @param constructorParameter 构造参数，ItemViewModel的构造参数是ViewModel本身
     * @param <T>     转换的目标对象必须继承BaseViewModel
     * @return vmList
     */
    public static <T extends ItemViewModel, E extends BaseViewModel> List<T> parseBeanList2VMList(List<? extends Object> beans, Class<T> clazz,
                                                                         E constructorParameter) {
        List<T> list = new ArrayList<>();
        if (beans == null) {
            return list;
        }
        for (Object bean : beans) {
            try {
                T vm = parseBean2VM(bean, clazz, constructorParameter);
                list.add(vm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
