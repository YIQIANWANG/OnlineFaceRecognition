package com.chenframework.modules.base.service;

import com.chenframework.common.persistence.QuerySpecification;
import com.chenframework.common.service.BaseService;
import com.chenframework.common.utils.SystemUtil;
import com.chenframework.modules.base.entity.Sign;
import com.chenframework.modules.base.entity.SignDetail;
import com.chenframework.modules.base.entity.Student;
import com.chenframework.modules.base.repository.SignRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SignService extends BaseService<Sign, SignRepository> {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SignDetailService signDetailService;

    public boolean check(String name) {
        QuerySpecification<Sign> spec = new QuerySpecification<>();
        spec.andEq("name", name);
        return baseRepository.count(spec) > 0;
    }

    @Transactional
    public Sign create(String name) {
        List<Student> students = this.studentService.findAll();

        Sign sign = new Sign();
        sign.setCreateTime(new Date());
        sign.setName(name);
        sign.setSignNum(0);
        sign.setTotalNum(students.size());

        sign = super.save(sign);

        for (Student student : students) {
            SignDetail detail = new SignDetail();
            BeanUtils.copyProperties(student, detail);
            detail.setIsSuccess(false);
            detail.setSignId(sign.getId());
            detail.setId(SystemUtil.getUUID());
            this.signDetailService.save(detail);
        }

        return sign;
    }

    @Transactional
    public void deleteSign(String signId) {
        super.delete(signId);
        List<SignDetail> details = this.signDetailService.findAll(signId);
        this.signDetailService.delete(details);

    }
}
