package com.chenframework.common.utils.clazz;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

/**
 * 利用CGLIB动态生成Bean.
 */
public class CglibBean {

    public static final String CBLIB_PROP_PREFIX = "$cglib_prop_";

    private Object object; // 实体object
    private BeanMap beanMap;// 属性map

    public CglibBean() {
        this(new HashMap<String, Class<?>>());
    }

    /**
     * 构造方法
     * <p>
     * 指定属性名称和属性的类型创建一个动态bean
     * <p>
     * @param propertyMap 属性集合
     */
    public CglibBean(Map<String, Class<?>> propertyMap) {
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }

    /**
     * 构造一个具有属性,属性类型和属性值的动态bean
     * @param properties {@link BeanProperty}集合
     */
    public CglibBean(List<BeanProperty> properties) {
        Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
        for (BeanProperty bp : properties) {
            propertyMap.put(bp.getName(), bp.getClazz());
        }
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
        for (BeanProperty bp : properties) {
            this.setValue(bp.getName(), bp.getValue());
        }
    }

    /**
     * 由指定的属性集合,动态创建实体bean
     * @param propertyMap
     * @return
     */
    private Object generateBean(Map<String, Class<?>> propertyMap) {
        BeanGenerator beanGenerator = new BeanGenerator();
        Set<String> keySet = propertyMap.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String key = it.next();
            beanGenerator.addProperty(key, propertyMap.get(key));
        }
        return beanGenerator.create();
    }

    /**
     * 给指定的属性赋值
     * @param property 属性名称
     * @param value 属性值
     */
    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    /**
     * 获取某个属性的值
     * @param property 属性名称
     * @return 属性值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }

    /**
     * 获取创建的动态代理对象
     * @return 对象
     */
    public Object getObject() {
        return object;
    }

}
