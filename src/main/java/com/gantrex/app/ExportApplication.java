package com.gantrex.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gantrex.app.exporter.task.ExporterTask;
import com.gantrex.dbo.controller.DBManager;
import com.gantrex.dbo.controller.DBManagerImpl;
import com.gantrex.dbo.model.Document;
import com.gantrex.exceptions.DBManagerException;
import com.gantrex.utils.Utils;

public class ExportApplication {

	private static final Logger log = LoggerFactory.getLogger(ExportApplication.class);
	private static final int pollingInterval = 30000;

	private static DBManager dbManager = new DBManagerImpl();

	public static void main(String[] args) {
		Application.init();
		process();
	}

	public static void process() {
		log.info("Starting Export Application");
		int i = 1;
		while (true) {
			log.debug(" Export Iteration Number : {}", i++);
			long millis = System.currentTimeMillis();
			List<Document> docs = new ArrayList<Document>();
			try {
				docs = dbManager.getDocumentsToExport();
				if (docs != null) {
					log.info("Number of docs to Export : {}", docs.size());
				}

			} catch (DBManagerException e) {
				log.error(e.getMessage(), e);
				break;
			}
			if (docs != null) {
				List<List<Document>> sublists = Utils.toSublists(docs, Application.getThreads());
				ExecutorService executor = Executors.newFixedThreadPool(Application.getThreads());
				sublists.forEach(s -> {
					executor.submit(new ExporterTask(s, Application.getExportDirectory()));
				});

				executor.shutdown();

				try {
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.warn("Doc list is null");
			}
			Application.saveLastExportDate(millis);
			try {
				Thread.sleep(pollingInterval);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
