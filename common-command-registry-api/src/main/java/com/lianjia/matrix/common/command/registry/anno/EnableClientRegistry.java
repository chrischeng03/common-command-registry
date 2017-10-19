package com.lianjia.matrix.common.command.registry.anno;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 程天亮
 * @Created
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RegistryImportor.class)
public @interface EnableClientRegistry {
}
