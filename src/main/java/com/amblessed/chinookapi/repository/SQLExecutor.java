package com.amblessed.chinookapi.repository;



/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 02-Sep-25
 */


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SQLExecutor {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> runQuery(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}
