package com.chenframework.common.model;

import com.chenframework.common.utils.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 数据表格请求参数
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class PageParams extends SortParams {
    public static final int DEFAULT_SIZE = 30;

    @NonNull
    private Integer pageNumber;
    @NonNull
    private Integer pageSize;

    private String searchText; // 关键字
    private String fields; // table显示的属性字段多个字段之间用,号隔开

    /**
     * 创建带排序的分页信息对象
     */
    public PageRequest createPageRequest() {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_SIZE;
        }
        return PageRequest.of(pageNumber - 1, pageSize, createSort());
    }

    public boolean isSearch() {
        return StringUtil.isNotEmpty2(this.searchText);
    }

    public List<String> getSearchFileds() {
        return StringUtil.splitByComma(fields);
    }
}
