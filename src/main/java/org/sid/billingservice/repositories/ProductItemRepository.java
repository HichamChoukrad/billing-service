package org.sid.billingservice.repositories;

import org.sid.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
@RepositoryRestResource
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    public PagedModel<ProductItem> findByBillId(Long id);
}
