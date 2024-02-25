package org.example.audit.config;

import org.example.audit.aspect.AuditAspect;
import org.example.audit.repository.UserActionRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EnableAudit.class)
public class CustomConfigAudit {

    @Bean
    public UserActionRepositoryImpl userActionRepository() {
        return new UserActionRepositoryImpl();
    }

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect(new UserActionRepositoryImpl());
    }

}
