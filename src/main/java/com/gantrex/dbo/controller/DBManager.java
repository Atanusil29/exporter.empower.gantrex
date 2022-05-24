package com.gantrex.dbo.controller;

import java.util.List;

import com.gantrex.dbo.model.Document;
import com.gantrex.exceptions.DBManagerException;

public interface DBManager {

	List<Document> getDocuments() throws DBManagerException;

	List<Document> getDocumentsToExport() throws DBManagerException;

	<T> List<T> get(Class<T> theClass) throws DBManagerException;

	<T> List<T> get(Class<T> theClass, String field, String value) throws DBManagerException;
}
