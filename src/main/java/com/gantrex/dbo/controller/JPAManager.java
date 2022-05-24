package com.gantrex.dbo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gantrex.app.Application;

public class JPAManager {
	private final static Logger logger = LoggerFactory.getLogger(JPAManager.class);
	private static EntityManagerFactory entityManagerFactory;
	private static Map<String, String> addedOrOverridenProperties = new HashMap<String, String>();

	static {
		try {
			addedOrOverridenProperties.put("javax.persistence.jdbc.user", Application.getDBUser());
			addedOrOverridenProperties.put("javax.persistence.jdbc.password", Application.getDBPassword());
			addedOrOverridenProperties.put("javax.persistence.jdbc.url", Application.getDBUrl());
			addedOrOverridenProperties.put("hibernate.default_schema", Application.getDBSchema());
			addedOrOverridenProperties.put("javax.persistence.jdbc.driver", Application.getDBDriver());
			addedOrOverridenProperties.put("hibernate.dialect", Application.getDBDialect());

			entityManagerFactory = Persistence.createEntityManagerFactory("hsql", addedOrOverridenProperties);

		} catch (Exception e) {
			logger.error("Persistence creation failed, cause: {} message: {}", e.getCause(), e.getMessage());
			/* throw e; */
			System.exit(-1);
		}
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public static EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}

	public static void Close() {
		entityManagerFactory.close();
	}

}
