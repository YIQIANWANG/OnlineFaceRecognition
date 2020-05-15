package com.chenframework.common.utils.clazz;

import com.chenframework.common.utils.CollectionUtil;
import com.chenframework.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Bean操作工具类
 */
@Slf4j
public final class BeanUtil {

    public final static String SERIALVERSIONUID = "serialVersionUID";

    public final static String PRE_IS = "is";
    public final static String PRE_GET = "get";
    public final static String PRE_SET = "set";

    private BeanUtil() {

    }

    /**
     * 判断实体的属性是否不持久化{@link javax.persistence.Transient}
     */
    public static boolean checkTransient(Class<?> clazz, String fieldName) {
        Field field = getDeclaredField(clazz, fieldName);
        if (field == null) {
            return true;
        }
        Transient an = field.getAnnotation(Transient.class);
        if (an != null) {
            return true;
        }

        Method getMethod = getMethod(clazz, fieldName);
        if (getMethod == null) {
            return true;
        }
        Transient ans = getMethod.getAnnotation(Transient.class);
        return ans != null;
    }

    /**
     * 判断类是否拥有某个属性
     */
    public static boolean checkDeclaredField(Class<?> clazz, String fieldName) {
        return getDeclaredField(clazz, fieldName) != null;
    }

    /**
     * 对象拷贝, 将一个对象里面非空的值拷贝到同一个类的另一个实例对象中
     * @param source        原始对象
     * @param target        目标对象
     * @param encludeFields 拷贝的过程中,不需要拷贝的属性
     */
    public static void copyBeanNotNull2SameBean(Object source, Object target, String... encludeFields) {
        if (!source.getClass().equals(target.getClass())) {
            throw new RuntimeException("The original object and the target object are not the same class");
        }
        List<String> encludeFieldsList = CollectionUtil.newArrayList();
        if (encludeFields != null) {
            encludeFieldsList.addAll(Arrays.asList(encludeFields));
        }
        List<BeanProperty> beanFields = getBeanPropertiesIncludeSuper(source, null);
        beanFields.forEach(bp -> {
            String fieldName = bp.getName();
            if (!encludeFieldsList.contains(fieldName)) {
                Class<?> fieldType = bp.getClazz();
                Object fieldValue = bp.getValue();
                if (fieldValue != null) {
                    if (fieldType.equals(List.class) || fieldType.equals(Set.class)) {
                        Collection<?> collection = (Collection<?>) fieldValue;
                        if (!collection.isEmpty()) {
                            setFieldValue(target, fieldName, fieldValue);
                        }
                    } else if (fieldType.equals(Map.class)) {
                        Map<?, ?> map = (Map<?, ?>) fieldValue;
                        if (!map.isEmpty()) {
                            setFieldValue(target, fieldName, fieldValue);
                        }
                    } else {
                        setFieldValue(target, fieldName, fieldValue);
                    }
                }
            }
        });

    }

    /**
     * 读取一个实体bean的所有属性(包含名称，类型，和属性值),只读当前实体的属性
     * @param bean 需要被解析的实体bean
     * @return {@link BeanProperty}集合
     */
    public static List<BeanProperty> getBeanProperties(Object bean) {
        List<BeanProperty> properties = CollectionUtil.newArrayList();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!isStaticField(field)) {
                String name = field.getName();
                BeanProperty bp = BeanProperty.builder().name(name).clazz(field.getType()).value(getFieldValueFromMethod(bean, name)).build();
                properties.add(bp);
            }
        }
        return properties;
    }

    /**
     * 读取一个实体bean的所有属性(包含父类)(包含名称，类型，和属性值)
     * @param bean     需要被解析的实体bean
     * @param includes 父类中包含的属性,本类中的属性将全部被解析,如果该参数为空,则获取所有父类的属性,否则只获取父类中该参数中设置的属性
     * @return {@link BeanProperty}集合
     */
    public static List<BeanProperty> getBeanPropertiesIncludeSuper(Object bean, List<String> includes) {
        List<BeanProperty> properties = getBeanProperties(bean);

        for (Class<?> superClass = bean.getClass().getSuperclass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (!isStaticField(field)) {
                    String name = field.getName();
                    if (includes == null || includes.contains(name)) {
                        Class<?> clazz = field.getType();
                        makeAccessible(field);
                        Object value;
                        try {
                            value = field.get(bean);
                        } catch (IllegalAccessException e) {
                            log.error("Failed to get field value:{} in {}", name, bean.getClass(), e);
                            throw new RuntimeException(e.getMessage(), e);
                        }
                        BeanProperty bp = BeanProperty.builder().name(name).clazz(clazz).value(value).build();
                        properties.add(bp);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * 获取私有属性，如果本类中没有，则向其父类中查找
     */
    public static Field getDeclaredField(Class<?> clazz, String fieldName) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (Exception e) {
                // log.error("Failed to get the property field[{}] of class[{}].", fieldName, clazz, e);
            }
        }
        return null;
    }

    /**
     * 获取对象私有属性，如果本类中没有，则向其父类中查找
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 获取所有私有属性，如果本类中没有，则向其父类中查找
     */
    public static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> fields = CollectionUtil.newArrayList();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] flist = superClass.getDeclaredFields();
            for (Field f : flist) {
                if (!isStaticField(f)) {
                    fields.add(f);
                }
            }
        }
        return fields;
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new RuntimeException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            log.error("Failed to get field value:{} in {}", fieldName, object.getClass(), e);
        }
        return result;
    }

    /**
     * 获取实体的get方法
     */
    public static Method getMethod(final Class<?> clazz, final String fieldName) {
        try {
            String methodName = PRE_GET + StringUtil.convertFirstChar2UpperCase(fieldName);
            Method method = clazz.getMethod(methodName);
            if (method == null) {
                methodName = PRE_IS + StringUtil.convertFirstChar2UpperCase(fieldName);
                method = clazz.getMethod(methodName);
                if (method == null && fieldName.startsWith(PRE_IS)) {
                    method = clazz.getMethod(fieldName);
                }
            }
            return method;
        } catch (Exception e) {
            // log.error("Failed to get method", e);
            // throw new RuntimeException(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取属性值,通过getter方法获取
     */
    public static Object getFieldValueFromMethod(final Object object, final String fieldName) {
        try {
            Method method = getMethod(object.getClass(), fieldName);
            if (method != null) {
                return method.invoke(object);
            }

        } catch (Exception e) {
            // log.error("Failed to get property value:{} in {}", fieldName, object.getClass(), e);
            // throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 判断属性是否为静态属性
     */
    public static boolean isStaticField(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 判断属性是否为静态方法
     */
    public static boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 强制转换fileld可访问.
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new RuntimeException("Could not find field [" + fieldName + "] on target [" + object.getClass() + "]");
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            log.error("Failed to set property value:{} in {}", fieldName, object.getClass(), e);
        }
    }
}
