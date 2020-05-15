package com.chenframework.modules.base.service;

import com.chenframework.common.model.PageParams;
import com.chenframework.common.persistence.QuerySpecification;
import com.chenframework.common.service.BaseService;
import com.chenframework.modules.base.entity.SignDetail;
import com.chenframework.modules.base.repository.SignDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SignDetailService extends BaseService<SignDetail, SignDetailRepository> {

    public Page<SignDetail> findAll(String signId, String no, PageParams pageParams) {
        QuerySpecification<SignDetail> spec = new QuerySpecification<>();
        spec.andEq("signId", signId);
        spec.andEqNotEmpty("no", no);
        pageParams.setDefaultSort("no", true);
        return super.findAll(spec, pageParams);
    }

    public List<SignDetail> findAll(String signId) {
        QuerySpecification<SignDetail> spec = new QuerySpecification<>();
        spec.andEq("signId", signId);
        return super.findAll(spec);
    }

    public SignDetail find(String signId, String no) {
        QuerySpecification<SignDetail> spec = new QuerySpecification<>();
        spec.andEq("signId", signId);
        spec.andEq("no", no);
        List<SignDetail> list = super.findAll(spec);
        return list.size() > 0 ? list.get(0) : null;
    }
}
