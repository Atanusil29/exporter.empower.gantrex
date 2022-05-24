package com.gantrex.app.exporter;

import java.util.Date;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.gantrex.config.ConfigurationManager;
import com.gantrex.exceptions.AuthenticationFailedException;
import com.gantrex.exceptions.ConfigNotInitializedException;
import com.gantrex.exceptions.DocumentManagementException;
import com.gantrex.exceptions.TaskException;
import com.gantrex.rest.impl.Authenticator;
import com.gantrex.rest.impl.DocumentManager;
import com.gantrex.utils.Utils;

public class ExporterImpl implements Exporter {

	private Date expirationDate;
	private String authToken;
	private final static Integer shift = 5;
	private DocumentManager docMan;

	@Override
	public Boolean isTimedOut() {
		return (null == expirationDate) || expirationDate.compareTo(Utils.dateWithShift(shift)) < 0;
	}

	@Override
	public void refresh() throws TaskException {
		init();

	}

	@Override
	public void exportDocument(String docID, String exportPath) throws TaskException {
		exportDocument(docID, exportPath, true);

	}

	@Override
	public void exportDocument(String docID, String exportPath, Boolean preserveDoc) throws TaskException {
		try {
			docMan.exportAndSaveDocument(docID, preserveDoc, exportPath);
		} catch (DocumentManagementException e) {
			throw new TaskException("Failed To Export Document " + docID, e);
		}
	}

	private void init() throws TaskException {
		Authenticator authenticator;

		try {
			String user = ConfigurationManager.getInstance().getEmpowerUser();
			String password = ConfigurationManager.getInstance().getEmpowerUserPassword();
			authenticator = new Authenticator(user, password);
			authenticator.init();
			if (authenticator.hasToken()) {
				expirationDate = authenticator.getExpDate();
				authToken = authenticator.getAuthToken();
				docMan = new DocumentManager();
				docMan.init(authToken);
			}
		} catch (AuthenticationFailedException e) {
			throw new TaskException(e.getMessage(), e);
		} catch (ConfigNotInitializedException e) {
			throw new TaskException(e.getMessage(), e);
		} catch (ConfigurationException e) {
			throw new TaskException(e.getMessage(), e);
		} catch (DocumentManagementException e) {
			throw new TaskException(e.getMessage(), e);
		}
	}

}
