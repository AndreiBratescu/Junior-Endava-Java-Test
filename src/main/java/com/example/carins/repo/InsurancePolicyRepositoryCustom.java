package com.example.carins.repo;

import com.example.carins.model.InsurancePolicy;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InsurancePolicyRepositoryCustom {

    private final EntityManager entityManager;

    public InsurancePolicyRepositoryCustom(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<InsurancePolicy> findExpiredPolicies(LocalDate currentDate) {
        String query = "SELECT p FROM InsurancePolicy p WHERE p.endDate < :currentDate AND p.logged = false";
        TypedQuery<InsurancePolicy> typedQuery = entityManager.createQuery(query, InsurancePolicy.class);
        typedQuery.setParameter("currentDate", currentDate);
        return typedQuery.getResultList();
    }
}

