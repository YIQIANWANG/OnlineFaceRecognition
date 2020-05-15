package com.chenframework.modules.base.service;

import com.chenframework.common.baiduface.Face;
import com.chenframework.common.model.PageParams;
import com.chenframework.common.persistence.QuerySpecification;
import com.chenframework.common.service.BaseService;
import com.chenframework.modules.base.entity.Student;
import com.chenframework.modules.base.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
@Transactional(readOnly = true)
public class StudentService extends BaseService<Student, StudentRepository> {

    public Page<Student> findAll(Student queryParams, PageParams pageParams) {
        UserSpecification spec = new UserSpecification(queryParams);
        return super.findAll(spec, pageParams);
    }

    private class UserSpecification extends QuerySpecification<Student> {
        private static final long serialVersionUID = 1L;

        private Student queryParams;

        public UserSpecification(Student queryParams) {
            this.queryParams = queryParams;
        }

        @Override
        public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            this.andLikeNotEmpty("no", queryParams.getNo());
            this.andLikeNotEmpty("name", queryParams.getName());
            return super.toPredicate(root, query, cb);
        }

    }

    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            Student s = super.findById(id);
            delete(s);
            Face.getInstance().faceDelete(Face.DEFAULT_GROUPID, s.getNo(), s.getFaceToken());
        }
    }
}
