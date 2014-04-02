package edu.tugraz.sw14.xp04.entities.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMFService {
    private static final EntityManagerFactory emfInstance =
        Persistence.createEntityManagerFactory("transactions-optional");

    private EMFService() {}

    public static EntityManagerFactory get() {
        return emfInstance;
    }
    
    public static EntityManager getEntityManager() {
    	return emfInstance.createEntityManager();
    }
}
