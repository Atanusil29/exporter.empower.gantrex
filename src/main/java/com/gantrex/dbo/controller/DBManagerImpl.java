package com.gantrex.dbo.controller;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.gantrex.app.Application;
import com.gantrex.dbo.model.Document;
import com.gantrex.exceptions.DBManagerException;

public class DBManagerImpl implements DBManager {

	private final static String QUERYDOCUMENTS = "FROM Document r where r.deleted = 0";

	private static String exportDocumentsQuery = "FROM Document r where r.deleted = 0 and r.importdate != r.savedate "
			+ "and r.editdate is not null and r.editdate > :lastretrievalDate";

	@Override
	public List<Document> getDocuments() throws DBManagerException {
		EntityManager entityManager = JPAManager.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			Query query = entityManager.createQuery(QUERYDOCUMENTS, Document.class);
			@SuppressWarnings("unchecked")
			List<Document> aliasRecords = (List<Document>) query.getResultList();
			entityTransaction.commit();
			return aliasRecords;
		} catch (Exception e) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			throw new DBManagerException(e.getMessage(), e);
		} finally {
			entityManager.close();
		}
	}

	@Override
	public <T> List<T> get(Class<T> theClass, String field, String value) throws DBManagerException {
		EntityManager entityManager = JPAManager.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			Query query = entityManager
					.createQuery("FROM " + theClass.getSimpleName() + " WHERE " + field + " = " + ":value", theClass);
			query.setParameter("value", value);
			@SuppressWarnings("unchecked")
			List<T> records = (List<T>) query.getResultList();
			entityTransaction.commit();
			return records;
		} catch (Exception e) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			throw new DBManagerException(e.getMessage(), e);
		} finally {
			entityManager.close();
		}
	}

	@Override
	public java.util.List<Document> getDocumentsToExport() throws DBManagerException {
		EntityManager entityManager = JPAManager.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			Date lastExportDate = Application.getLastExportDate();
			entityTransaction.begin();
			Query query = entityManager.createQuery(exportDocumentsQuery, Document.class);
			query.setParameter("lastretrievalDate", lastExportDate, TemporalType.TIME);
			@SuppressWarnings("unchecked")
			List<Document> aliasRecords = (List<Document>) query.getResultList();
			entityTransaction.commit();
			return aliasRecords;
		} catch (Exception e) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			throw new DBManagerException(e.getMessage(), e);
		} finally {
			entityManager.close();
		}
	}

	@Override
	public <T> List<T> get(Class<T> theClass) throws DBManagerException {
		return null;

	}
}
