package ru.vlsu.ispi;

import javax.persistence.*;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.User;

import java.util.function.Function;

@Component
public class JPAService {
    private static JPAService instance;

    private EntityManagerFactory entityManagerFactory;

    private JPAService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("tm_project2");
    }

    public static synchronized JPAService getInstance() {
        return instance == null ? instance = new JPAService() : instance;
    }

    public void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            instance = null;
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public <T> T runInTransaction(Function<EntityManager, T> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        boolean success = false;
        try {
            T returnValue = function.apply(entityManager);
            success = true;
            return returnValue;

        } finally {
            if (success) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        }
    }
}
