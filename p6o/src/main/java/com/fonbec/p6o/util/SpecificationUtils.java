package com.fonbec.p6o.util;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Expression;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilidad para crear especificaciones dinámicas para consultas JPA.
 *
 * @param <T> Tipo de la entidad sobre la que se aplicará la especificación.
 */

import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class SpecificationUtils {

    public static <T> Specification<T> createSpecification(Class<T> entityClass, Map<String, Object> params) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Map<String, Field> campos = Arrays.stream(entityClass.getDeclaredFields())
                    .collect(Collectors.toMap(Field::getName, f -> f));

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();

                if (valor != null && campos.containsKey(campo)) {
                    Field field = campos.get(campo);
                    Class<?> tipoCampo = field.getType();

                    try {
                        if (tipoCampo == String.class) {
                            Expression<String> campoExpresion = cb.lower(root.get(campo));
                            String valorNormalizado = normalizar(valor.toString());
                            predicates.add(cb.like(campoExpresion, "%" + valorNormalizado + "%"));

                        } else if (tipoCampo.isEnum()) {
                            Object enumValue = Enum.valueOf((Class<Enum>) tipoCampo, valor.toString());
                            predicates.add(cb.equal(root.get(campo), enumValue));

                        } else if (Number.class.isAssignableFrom(tipoCampo) || tipoCampo.isPrimitive()) {
                            predicates.add(cb.equal(root.get(campo), valor));

                        } else if (tipoCampo == Boolean.class || tipoCampo == boolean.class) {
                            predicates.add(cb.equal(root.get(campo), Boolean.valueOf(valor.toString())));

                        } else if (tipoCampo == LocalDate.class) {
                            LocalDate fecha = LocalDate.parse(valor.toString());
                            predicates.add(cb.equal(root.get(campo), fecha));

                        } else if (tipoCampo == Date.class) {
                            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(valor.toString());
                            predicates.add(cb.equal(root.get(campo), fecha));

                        } else {
                            predicates.add(cb.equal(root.get(campo), valor));
                        }
                    } catch (IllegalArgumentException | DateTimeParseException | ParseException e) {
                        log.error(campo, e);
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static String normalizar(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }
}
