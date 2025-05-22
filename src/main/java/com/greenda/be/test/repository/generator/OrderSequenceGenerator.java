package com.greenda.be.test.repository.generator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class OrderSequenceGenerator {

    private static EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        OrderSequenceGenerator.entityManager = entityManager;
    }

    public static Long next() {
        return ((Number) entityManager
                .createNativeQuery("SELECT NEXTVAL('ORDER_SEQ')")
                .getSingleResult())
                .longValue();
    }
}
