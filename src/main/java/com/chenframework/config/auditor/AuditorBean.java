package com.chenframework.config.auditor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 实现实体中 {@link CreatedBy} 和 {@link LastModifiedBy}赋值
 */
@Configuration
public class AuditorBean implements AuditorAware<String> {

    @Override
    public final Optional<String> getCurrentAuditor() {
        return Optional.of("");
    }

}
