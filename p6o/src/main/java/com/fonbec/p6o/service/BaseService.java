package com.fonbec.p6o.service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.fonbec.p6o.util.SpecificationUtils;

public abstract class BaseService<T, K> {

    private final JpaSpecificationExecutor<T> specificationExecutor;
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseService(JpaSpecificationExecutor<T> specificationExecutor) {
        this.specificationExecutor = specificationExecutor;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Page<T> listar(Map<String, Object> filtros, Pageable pageable) {
        Specification<T> spec = SpecificationUtils.createSpecification(entityClass, filtros);
        return specificationExecutor.findAll(spec, pageable);
    }
}
