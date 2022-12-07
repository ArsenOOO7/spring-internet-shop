package com.arsen.internet.shop.soap.app.repository.build;

import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.Sort;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;


/**
 * Repository to search products by criteria
 * @author Arsen Sydoryk
 */
@Repository
public class ProductSearchRepository {

    private final EntityManager manager;

    /**
     * Constructor
     * @param manager EntityManager
     */
    public ProductSearchRepository(EntityManager manager) {
        this.manager = manager;
    }


    /**
     * Search products
     * @param search DTO
     * @return Page
     */
    public Page<Product> findProductsByCriteria(ProductSearchDto search) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> productCriteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = productCriteriaQuery.from(Product.class);

        Sort sort = Sort.NOVELTY;
        if(!search.getSort().isEmpty()){
            sort = Sort.getInstance(search.getSort());
        }

        Function<Expression<?>, Order> sortBy = sort.getOrder().equals("ASC") ? criteriaBuilder::asc : criteriaBuilder::desc;
        productCriteriaQuery.orderBy(sortBy.apply(root.get(sort.getField())));

        Predicate[] predicatesArray = buildPredicates(criteriaBuilder, root, search).toArray(Predicate[]::new);
        productCriteriaQuery.where(predicatesArray);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Product.class)));
        countQuery.where(buildPredicates(criteriaBuilder, countQuery.from(Product.class), search).toArray(Predicate[]::new));

        long total = manager.createQuery(countQuery).getSingleResult();

        TypedQuery<Product> typedQuery = manager.createQuery(productCriteriaQuery)
                .setFirstResult((search.getPage() - 1) * ShopValues.MAX_ROWS)
                .setMaxResults(ShopValues.MAX_ROWS);

        return new PageImpl<>(typedQuery.getResultList(), PageRequest.of(search.getPage() - 1, ShopValues.MAX_ROWS), total);
    }


    /**
     * Build conditions to search
     * @param criteriaBuilder CriteriaBuilder
     * @param root Root
     * @param search DTO
     * @return List of Predicates
     */
    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<?> root, ProductSearchDto search){

        List<Predicate> predicates = new LinkedList<>();

        if (!search.getSearchProduct().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("shortName"), search.getSearchProduct() + "%"));
        }

        if (search.getMinPrice() > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), search.getMinPrice()));
        }

        if (search.getMaxPrice() > 0) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), search.getMaxPrice()));
        }

        if (search.getCategory() > 0) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), search.getCategory()));
        }

        if (search.getColor() != null && !search.getColor().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("color"), Color.valueOf(search.getColor())));
        }

        if (search.getMinSize() > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sizeValue"), search.getMinSize()));
        }

        if (search.getMaxSize() > 0) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sizeValue"), search.getMaxSize()));
        }

        return predicates;
    }
}
