package com.bidder.auctioncatalogueservice.repository;

import org.springframework.data.jpa.domain.Specification;

import com.bidder.auctioncatalogueservice.model.Auction;
import com.bidder.auctioncatalogueservice.model.Item;

import jakarta.persistence.criteria.*;

public class AuctionSpecification {

    public static Specification<Auction> findByCriteria(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Join<Auction, Item> itemJoin = root.join("item");

            Predicate itemNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("name")), "%" + keyword.toLowerCase() + "%");
            Predicate itemDescriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("description")), "%" + keyword.toLowerCase() + "%");
            Predicate itemCategoryPredicate = criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("category")), "%" + keyword.toLowerCase() + "%");

            return criteriaBuilder.or(itemNamePredicate, itemDescriptionPredicate, itemCategoryPredicate);
        };
    }
}
