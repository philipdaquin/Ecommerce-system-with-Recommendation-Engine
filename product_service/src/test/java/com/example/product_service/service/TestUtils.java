package com.example.product_service.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** Final Class means TestUtils cannot be extended further */
public final class TestUtils {

    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() { 
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public static byte[] createByteArray(int size, String data) { 
        var byteArray = new byte[size];
        for (var i = 0;  i < size; i +=1) { 
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }
    
    /**
     * 
     * @param <T> the type of objects to be searched 
     * @param entityManager the instance of the entityManager
     * @param clss the class type to be searched 
     * @return a list of all found objects 
     */
    public static <T> List<T> findAll(EntityManager entityManager, Class<T> clss) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clss);
        
        Root<T> root = query.from(clss);
        CriteriaQuery<T> selectAll = query.select(root);
        TypedQuery<T> allQuery = entityManager.createQuery(selectAll);
        return allQuery.getResultList(); 
    }
}
