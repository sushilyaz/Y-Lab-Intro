package org.example.audit.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyCustomConfigImporter implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        System.out.println("selectImports");
        return new String[]{
                "org.example.audit.config.CustomConfigAudit"
        };
    }
}
