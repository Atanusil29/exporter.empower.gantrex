package com.gantrex.app.exporter;

import com.gantrex.exceptions.TaskException;

public interface Exporter {

	Boolean isTimedOut();

	void refresh() throws TaskException;

	void exportDocument(String docID, String exportPath) throws TaskException;

	void exportDocument(String docID, String exportPath, Boolean preserveDoc) throws TaskException;

}
