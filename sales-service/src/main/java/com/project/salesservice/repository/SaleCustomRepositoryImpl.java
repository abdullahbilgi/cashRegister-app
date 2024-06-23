package com.project.salesservice.repository;

import com.project.salesservice.dto.SaleFilterDTO;
import com.project.salesservice.entity.Sale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SaleCustomRepositoryImpl implements SaleCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Sale> findSalesByCriteria(SaleFilterDTO saleFilter) {

        StringBuilder queryBuilder = new StringBuilder("SELECT t FROM Sale t WHERE 1 = 1");
        Map<String, Object> parameters = new HashMap<>();

        if (saleFilter.getMinCount() != null) {
            queryBuilder.append(" AND t.count >= :minCount");
            parameters.put("minCount", saleFilter.getMinCount());
        }

        if (saleFilter.getMaxCount() != null) {
            queryBuilder.append(" AND t.count <= :maxCount");
            parameters.put("maxCount", saleFilter.getMaxCount());
        }

        if (saleFilter.getMinTotalPrice() != null) {
            queryBuilder.append(" AND t.totalPrice >= :minTotalPrice");
            parameters.put("minTotalPrice", saleFilter.getMinTotalPrice());
        }

        if (saleFilter.getMaxTotalPrice() != null) {
            queryBuilder.append(" AND t.totalPrice <= :maxTotalPrice");
            parameters.put("maxTotalPrice", saleFilter.getMaxTotalPrice());
        }

        if (saleFilter.getProductId() != null) {
            queryBuilder.append(" AND t.eventId = :productId");
            parameters.put("productId", saleFilter.getProductId());
        }

        if (saleFilter.getUserId() != null) {
            queryBuilder.append(" AND t.userId = :userId");
            parameters.put("userId", saleFilter.getUserId());
        }

        TypedQuery<Sale> query = entityManager.createQuery(queryBuilder.toString(), Sale.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        if (saleFilter.getPage() < 1) {
            saleFilter.setPage(1);
        }

        query.setFirstResult((saleFilter.getPage() - 1) * saleFilter.getSize());
        query.setMaxResults(saleFilter.getSize());

        return query.getResultList();



    }
}
