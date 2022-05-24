package com.gantrex.app.exporter.task;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gantrex.app.Application;
import com.gantrex.app.exporter.Exporter;
import com.gantrex.app.exporter.ExporterImpl;
import com.gantrex.dbo.model.Document;
import com.gantrex.exceptions.TaskException;

public class ExporterTask implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(ExporterTask.class);

	private final Exporter exportWorker = new ExporterImpl();
	private String exportFolder;

	private List<Document> documents;

	public ExporterTask() {
		// TODO Auto-generated constructor stub
	}

	public ExporterTask(List<Document> files, String exportFolder) {
		this.documents = files;
		this.exportFolder = exportFolder;
	}

	public ExporterTask(Document file, String exportFolder) {
		this.documents = new ArrayList<Document>();
		this.documents.add(file);
		this.exportFolder = exportFolder;
	}

	@Override
	public void run() {
		documents.forEach(document -> {
			String docID = document.getId();
			if (StringUtils.isEmpty(docID)) {
				throw new NullPointerException("DocID is Empty");
			}
			String fileName = document.getCustomid();
			if (StringUtils.isEmpty(fileName)) {
				throw new NullPointerException("filename is Empty");
			}
			String extension = Application.getExportFileExtension();
			String path = Paths.get(exportFolder, fileName + "." + extension).toString();
			log.debug("Export {} to {}", docID, path);
			try {
				if (exportWorker.isTimedOut()) {
					log.debug("Refreshing authToken");
					exportWorker.refresh();
				}
				exportWorker.exportDocument(docID, path);

			} catch (TaskException e) {
				log.error(e.getMessage(), e);
			} finally {
			}

		});
	}

}
