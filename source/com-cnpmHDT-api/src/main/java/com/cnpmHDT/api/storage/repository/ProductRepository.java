package com.cnpmHDT.api.storage.repository;
import com.cnpmHDT.api.storage.model.Account;
import com.cnpmHDT.api.storage.model.Category;
import com.cnpmHDT.api.storage.model.Product;
import com.cnpmHDT.api.storage.model.TablePrefix;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByCategory(Category category);

    @Query(value = "SELECT TOP (3)" +
            " FROM " + TablePrefix.PREFIX_TABLE + "product a" +
            " ORDER BY a.saleoff DESC", nativeQuery = true)
    List<Product> findTopSaleProduct(Product var1);
}