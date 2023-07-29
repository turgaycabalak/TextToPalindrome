package com.palindrome.TextToPalindrome.repository.specification;

import com.palindrome.TextToPalindrome.model.TextFile;
import com.palindrome.TextToPalindrome.repository.TextFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class CustomTextFileRepository {

    private final TextFileRepository textFileRepository;


    public List<TextFile> getQueryResult(List<Filter> filters) {
        if (filters.size() > 0) {
            return textFileRepository.findAll(getSpecificationFromFilters(filters));
        } else {
            return textFileRepository.findAll();
        }
    }

    private Specification<TextFile> getSpecificationFromFilters(List<Filter> filters) {
        Specification<TextFile> specification = where(null);

        for (Filter input : filters) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<TextFile> createSpecification(Filter input) {
        switch (input.getOperator()) {

            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case NOT_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                                (Number) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                                (Number) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()),
                                "%" + input.getValue() + "%");

            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                                .value(castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValues()));

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        } else if (String.class.isAssignableFrom(fieldType)) {
            return String.valueOf(value);
        } else if (Long.class.isAssignableFrom(fieldType)) {
            return Long.valueOf(value);
        } else if (Boolean.class.isAssignableFrom(fieldType)) {
            return Boolean.valueOf(value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}
