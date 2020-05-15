package com.chenframework.common.model.view;

import com.chenframework.common.persistence.entity.BaseEntity;
import com.chenframework.common.persistence.entity.IdEntity;
import com.chenframework.common.utils.CollectionUtil;
import com.chenframework.common.utils.StringUtil;
import com.chenframework.common.utils.clazz.BeanUtil;
import com.chenframework.common.utils.web.RequestHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 返回页面的数据列表
 */
@SuppressWarnings("rawtypes")
public class TableList extends ArrayList implements List {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public TableList(List datas) {
        String[] fields = RequestHelper.getRequest().getParameterValues("fields");
        List<String> fieldList = CollectionUtil.emptyArrayList();
        if (fields != null) {
            for (String f : fields) {
                fieldList.addAll(StringUtil.splitByComma(f));
            }
        }

        boolean isFilter = fieldList.size() > 0;
        if (isFilter) {
            List<String> other = CollectionUtil.newArrayList(IdEntity.ID, BaseEntity.CREATE_ID, BaseEntity.CREATE_TIME, BaseEntity.ENABLE,
                    BaseEntity.REMARK, BaseEntity.UPDATE_ID, BaseEntity.UPDATE_TIME);
            other.forEach(f -> {
                if (!fieldList.contains(f)) {
                    fieldList.add(f);
                }
            });
        }

        datas.forEach(data -> {
            if (isFilter) {
                this.add(doFilter(data, fieldList));
            } else {
                this.add(data);
            }
        });

    }

    private Map<String, Object> doFilter(Object data, List<String> fieldList) {
        Map<String, Object> map = CollectionUtil.newLinkedHashMap();
        fieldList.forEach(field -> {
            Object value;

            int dotIndex = field.indexOf(".");
            if (dotIndex >= 0) {
                String[] fieldLevel = field.split("\\.");
                Object objValue = data;
                for (String f : fieldLevel) {
                    objValue = BeanUtil.getFieldValueFromMethod(objValue, f);
                    if (objValue == null) {
                        break;
                    }
                }
                value = objValue;
            } else {
                value = BeanUtil.getFieldValueFromMethod(data, field);
            }

            map.put(field, value);
        });
        return map;
    }

}
