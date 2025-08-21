package com.fonbec.p6o.repository;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.fonbec.p6o.util.SpecificationUtils;

@NoRepositoryBean
public interface BaseRepository<T, K> 
        extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {

    @SuppressWarnings("unchecked")
    default Class<T> getEntityClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericInterfaces()[0];
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    default Page<T> listar(Map<String, Object> params, Pageable pageable) {
        return findAll(SpecificationUtils.createSpecification(getEntityClass(), params), pageable);
    }
}
