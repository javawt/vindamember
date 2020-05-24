package com.songlanyun.jymall.common.JpaUtils;

import com.songlanyun.jymall.common.exception.RRException;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang on 2019/11/21.
 */
public class SpecificationUtil<T> {
    private final String ASC = "asc";
    private final String DESC = "desc";

    /**
     * 生成JPA单表复杂查询条件
     *
     * @param params //     * @param <T>
     *               //     * @return
     */
    public <T> Specification<T> getSpecification(Map<String, Object> params) {
        return (Specification<T>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                //排序字段
                if (ASC.equals(entry.getKey())) {
                    setAsc(entry, root, query, criteriaBuilder);
                    continue;
                }

                if (DESC.equals(entry.getKey())) {
                    setDesc(entry, root, query, criteriaBuilder);
                    continue;
                }

                //查询字段
                if (StringUtils.isNotBlank(String.valueOf(entry.getValue()))) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }

                //Jpa 复杂查询 root.get("实体类字段名")
//            predicates.add(criteriaBuilder.like(root.get("fileName"), "%a%"));
//            predicates.add(criteriaBuilder.equal(root.get("sourceName"), "eventlog_provider.dll"));
//            predicates.add(criteriaBuilder.equal(root.get("userId"), 1));
//            predicates.add(criteriaBuilder.like(root.get("filePath"), "%d%"));
//            predicates.add(criteriaBuilder.ge(root.get("size"), 20));
//            predicates.add(criteriaBuilder.like(root.get("fileType"), "%%"));
//            predicates.add(criteriaBuilder.greaterThan(root.get("crateTime"), new Date()));

                //排序
//            query.orderBy(criteriaBuilder.asc(root.get("id")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 分页
     *
     * @param params
     * @return
     */
    public static Pageable getPageable(Map<String, Object> params) {
        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");

            return PageRequest.of(page - 1, size);
        }
        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    //TODO 对于排序 key为排序方式,value为属性名集合,多个属性名用英文“,”逗号分隔

    /**
     * 排序
     *
     * @param entry
     * @param root
     * @param query
     * @param criteriaBuilder
     */
    public static void setAsc(Map.Entry<String, Object> entry, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        String[] columns = String.valueOf(entry.getValue()).split(",");
        Order[] orders = new Order[columns.length];
        for (int i = 0; i < columns.length; i++) {
            orders[i] = criteriaBuilder.asc(root.get(columns[i]));
        }
        query.orderBy(orders);
    }

    /**
     * 排序
     *
     * @param entry
     * @param root
     * @param query
     * @param criteriaBuilder
     */
    public static void setDesc(Map.Entry<String, Object> entry, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        String[] columns = String.valueOf(entry.getValue()).split(",");
        Order[] orders = new Order[columns.length];
        for (int i = 0; i < columns.length; i++) {
            orders[i] = criteriaBuilder.desc(root.get(columns[i]));
        }
        query.orderBy(orders);
    }

    private static boolean isNullOrEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String) return StringUtils.isBlank((String) obj);
        return String.valueOf(obj).length() == 0;
    }

    private static boolean isNumber(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Number) return true;
//        if (obj instanceof String) {
//            for (int i = 0; i < ((String) obj).length(); i++) {
//                if (!Character.isDigit(((String) obj).charAt(i))) {
//                    return false;
//                }
//            }
//            return true;
//        }
        return false;
    }

    /**
     * 时间区间查询
     *
     * @param lo 属性起始值
     * @param go 属性结束值
     */
    public static void between(List<Predicate> predicates, String attrName, Root root, CriteriaBuilder criteriaBuilder, Date lo, Date go) {
        if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
            predicates.add(criteriaBuilder.between(root.get(attrName), lo, go));
        }

        // if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
        // this.predicates.add(criteriaBuilder.lessThan(from.get(propertyName),
        // new DateTime(lo).toString()));
        // }
        // if (!isNullOrEmpty(go)) {
        // this.predicates.add(criteriaBuilder.greaterThan(from.get(propertyName),
        // new DateTime(go).toString()));
        // }

    }

    /**
     * 数字区间
     */
    public static void between(List<Predicate> predicates, String attrName, Root root, CriteriaBuilder criteriaBuilder, Number lo, Number go) {
        if (!(isNullOrEmpty(lo)))
            predicates.add(criteriaBuilder.ge(root.get(attrName), lo));

        if (!(isNullOrEmpty(go)))
            predicates.add(criteriaBuilder.le(root.get(attrName), go));
    }

    /**
     * 小于等于
     */
    public static void le(List<Predicate> predicates, Map.Entry<String, Object> entry, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        if (isNullOrEmpty(entry.getValue())) {
            return;
        }
        if (!isNumber(entry.getValue())) {
            return;
        }
        predicates.add(criteriaBuilder.le(root.get(entry.getKey()), (Number) entry.getValue()));
    }

    /**
     * 小于
     */
    public static void lt(List<Predicate> predicates, Map.Entry<String, Object> entry, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        if (isNullOrEmpty(entry.getValue())) {
            return;
        }
        if (!isNumber(entry.getValue())) {
            return;
        }
        predicates.add(criteriaBuilder.lt(root.get(entry.getKey()), (Number) entry.getValue()));
    }

    /**
     * 大于等于
     */
    public static void ge(List<Predicate> predicates, Map.Entry<String, Object> entry, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        if (isNullOrEmpty(entry.getValue())) {
            return;
        }
        if (!isNumber(entry.getValue())) {
            return;
        }
        predicates.add(criteriaBuilder.ge(root.get(entry.getKey()), (Number) entry.getValue()));
    }

    /**
     * 大于
     */
    public static void gt(List<Predicate> predicates, Map.Entry<String, Object> entry, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        if (isNullOrEmpty(entry.getValue())) {
            return;
        }
        if (!isNumber(entry.getValue())) {
            return;
        }
        predicates.add(criteriaBuilder.gt(root.get(entry.getKey()), (Number) entry.getValue()));
    }

}
