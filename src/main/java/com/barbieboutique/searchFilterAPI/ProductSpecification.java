package com.barbieboutique.searchFilterAPI;

import com.barbieboutique.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

//public class ProductSpecification implements Specification<Product> {
public class ProductSpecification {

    public static Specification<Product> titleContains(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

    public static Specification<Product> priceGreaterThanOrEq(BigDecimal min) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Product> priceLesserThanOrEq(BigDecimal max) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), max);
    }


//    ==================================================================================

//    private SearchCriteria searchCriteria;
//
//    public ProductSpecification(SearchCriteria searchCriteria) {
//        this.searchCriteria = searchCriteria;
//    }
//
//    @Override
//    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
//            return criteriaBuilder.greaterThanOrEqualTo(
//                    root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
//
//        } else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
//            return criteriaBuilder.lessThanOrEqualTo(
//                    root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
//
//        } else if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
//            if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
//                return criteriaBuilder.like(
//                        root.<String>get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
//            } else {
//                return criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
//            }
//        }
//        return null;
//    }

}
