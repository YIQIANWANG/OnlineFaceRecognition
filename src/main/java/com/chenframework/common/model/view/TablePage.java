package com.chenframework.common.model.view;

import com.chenframework.common.model.PageParams;
import com.chenframework.common.utils.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表格数据包装并返回前端
 */
@Getter
public class TablePage {

    @Setter
    private int code = AjaxJson.CODE_SUCCESS; // layui table 特性
    @Setter
    private String msg;// layui table 特性

    private long total;// 总记录数
    private TableList rows; // 列表数据

    private Map<String, Object> attributes = new HashMap<>();// 其他参数

    public TablePage(Page<?> page) {
        this(page.getContent(), page.getTotalElements());
    }

    public TablePage(List<?> list) {
        this.set(list, list.size());
    }

    public TablePage(List<?> list, long total) {
        this.set(list, total);
    }

    /**
     * 将数组集合转换为分页对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TablePage(List list, PageParams pageParams) {
        int size = list.size();
        List result = CollectionUtil.newArrayList();
        int index = (pageParams.getPageNumber() - 1) * pageParams.getPageSize();
        for (int i = index; i < pageParams.getPageSize() + index; i++) {
            if (i < size) {
                result.add(list.get(i));
            }
        }
        this.set(result, size);
    }

    private void set(List<?> list, long total) {
        this.rows = new TableList(list);
        this.total = total;
    }

    public TablePage addAttr(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }
}
