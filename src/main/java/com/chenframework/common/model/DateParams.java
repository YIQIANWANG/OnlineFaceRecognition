package com.chenframework.common.model;

import com.chenframework.common.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 含有日期查询的请求参数
 */
public class DateParams extends PageParams {

    @Getter
    @Setter
    private Date queryDateFrom;
    @Setter
    private Date queryDateTo;

    public Date getQueryDateTo() {
        if (queryDateTo != null) {
            String str = DateUtil.formatDatetime(queryDateTo);
            String str2 = str.substring(11, 19);
            if ("00:00:00".equals(str2)) {
                String str3 = str.substring(0, 10) + " 23:59:50";
                return DateUtil.parseDatetime(str3);
            } else {
                return queryDateTo;
            }
        }
        return null;
    }

    public String getQueryDateFromFormat() {
        if (queryDateFrom == null) {
            return null;
        }
        return DateUtil.formatDate(queryDateFrom);
    }

    public String getQueryDateToFormat() {
        if (queryDateTo == null) {
            return null;
        }
        return DateUtil.formatDate(queryDateTo);

    }
}
