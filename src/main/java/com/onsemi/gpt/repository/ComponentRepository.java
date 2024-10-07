package com.onsemi.gpt.repository;
import com.onsemi.gpt.models.BaseComponent;
import com.onsemi.gpt.models.entitites.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ComponentRepository {

    private final EntityManager em;

    public <T extends BaseComponent> List<T> executeQueryForComponent(String sql, Class<? extends BaseComponent> entityClass) {
        @SuppressWarnings("unchecked")
        List<T> resultList = em.createNativeQuery(sql, entityClass).getResultList();
        return resultList;
    }

}

