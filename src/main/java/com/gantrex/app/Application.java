package com.gantrex.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConversionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gantrex.config.ConfigurationManager;
import com.gantrex.exceptions.ConfigNotInitializedException;

public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private static ConfigurationManager config;
	private static int threads = 8;

	private static String exportDirectory = null;
	private static String filterExtension = "";
	private static boolean isSecretLogEnabled = false;
	private static String dbUser = null;
	private static String dbPassword = null;
	private static String dbUrl = null;
	private static String dbSchema = "dbo";
	private static String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String dbDialect = "org.hibernate.dialect.SQLServer2012Dialect";
	private static String exportFileExtension = "mpw";
	private static Date lastExportDate = new Date(0L);

	public static void main(String[] args) {
		init();
		if (config != null) {
			ExportApplication.process();
		} else {
			log.debug("Config is null, System is exiting");
		}
	}

	public static void init() {
		log.info("Initializing Application");
		try {
			config = ConfigurationManager.getInstance();
		} catch (ConfigurationException e) {
			log.error("Problem initializing Configuration", e);
			System.exit(1);
		}

		try {
			dbUser = config.getDBUser();
		} catch (ConfigNotInitializedException e) {
			log.error("Problem initializing Configuration.", e);
			System.exit(1);
		} catch (ConversionException e) {
			log.error("Db user must be of string type.", e);
			System.exit(1);
		} catch (NoSuchElementException e) {
			log.error("Db user is missing in config file.", e);
			System.exit(1);
		}

		try {
			dbPassword = config.getDBPassword();
		} catch (ConfigNotInitializedException e) {
			log.error("Problem initializing Configuration.", e);
			System.exit(1);
		} catch (ConversionException e) {
			log.error("Db Password must be of string type.", e);
			System.exit(1);
		} catch (NoSuchElementException e) {
			log.error("Db password is missing in config file.", e);
			System.exit(1);
		}

		try {
			dbUrl = config.getDBUrl();
		} catch (ConfigNotInitializedException e) {
			log.error("Problem initializing Configuration.", e);
			System.exit(1);
		} catch (ConversionException e) {
			log.error("Db URL must be of string type.", e);
			System.exit(1);
		} catch (NoSuchElementException e) {
			log.error("Db URL is missing in config file.", e);
			System.exit(1);
		}

		try {
			dbSchema = config.getDBSchema();
			log.debug("Schema : {}", dbSchema);
		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
		} catch (ConversionException e) {
			log.warn("DB Schema must be of integer type. {}", e.getMessage());
		} catch (NoSuchElementException e) {
			log.warn("Db Schema has not been configured in config file. {}", e.getMessage());
		}

		try {
			dbDialect = config.getDBDialect();
			log.info("DB Dialect : {}", dbDialect);
		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
		} catch (ConversionException e) {
			log.warn("DB Dialect must be of integer type. {}", e.getMessage());
		} catch (NoSuchElementException e) {
			log.warn("DB Dialect has not been configured in config file. {}", e.getMessage());
		}

		try {
			dbDriver = config.getDBDriver();
			log.info("DB Driver : {}", dbDriver);
		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
		} catch (ConversionException e) {
			log.warn("DB Driver must be of integer type. {}", e.getMessage());
		} catch (NoSuchElementException e) {
			log.warn("DB Driver has not been configured in config file. {}", e.getMessage());
		}

		try {
			exportDirectory = config.getExportDirectory();
			log.info("Base Export Directory : {}", exportDirectory);

		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
			System.exit(1);
		} catch (ConversionException e) {
			log.warn("Export Folder must be of string type. {}", e.getMessage());
			System.exit(1);
		} catch (NoSuchElementException e) {
			log.warn("Export Folder has not been configured in config file. {}", e.getMessage());
			System.exit(1);
		}

		try {
			threads = config.getThreads();
			log.info("Threads : {}", threads);
		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
		} catch (ConversionException e) {
			log.warn("Threads must be of integer type. {}", e.getMessage());
		} catch (NoSuchElementException e) {
			log.warn("Threads has not been configured in config file. {}", e.getMessage());
		}

		try {
			filterExtension = config.getFileExtensionFilter();
		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
		} catch (ConversionException e) {
			log.warn("File Filter must be of string type. {}", e.getMessage());
		} catch (NoSuchElementException e) {
			log.warn("File Filter has not been configured in config file. {}", e.getMessage());
		}

		try {
			lastExportDate = new Date(config.getLastExportMillis());
		} catch (ConfigNotInitializedException e) {
			log.warn("Problem initializing Configuration. {}", e.getMessage());
		} catch (ConversionException e) {
			log.warn("Last Export Date must be of int type. {}", e.getMessage());
		} catch (NoSuchElementException e) {
			log.warn("Last Export Date has not been configured in config file. {}", e.getMessage());
		}

		log.info("FileExtension Filter : {}", filterExtension);
		if (StringUtils.isEmpty(filterExtension)) {
			log.warn("FileExtension Filter is not configured");
			filterExtension = "";
		}

		isSecretLogEnabled = config.getSecretLog();

		log.info("Application Initialized");
	}

	public static String getExportDirectory() {
		return exportDirectory;
	}

	public static int getThreads() {
		return threads;
	}

	public static String getFilterExtension() {
		return filterExtension;
	}

	public static String getDBUser() {
		return dbUser;
	}

	public static String getDBUrl() {
		return dbUrl;
	}

	public static String getDBDialect() {
		return dbDialect;
	}

	public static String getDBDriver() {
		return dbDriver;
	}

	public static String getDBSchema() {
		return dbSchema;
	}

	public static String getDBPassword() {
		return dbPassword;
	}

	public static boolean isSecretLogEnabled() {
		return isSecretLogEnabled;
	}

	public static Date getLastExportDate() {
		log.info("Last Export Date : {}", lastExportDate.toString());
		return lastExportDate;
	}

	public static String getExportFileExtension() {
		return exportFileExtension;
	}

	public static void saveLastExportDate() {
		saveLastExportDate(System.currentTimeMillis());
	}

	public static void saveLastExportDate(long millis) {
		try {
			config.setLastExportMillis(millis);
			lastExportDate = new Date(config.getLastExportMillis());
		} catch (ConfigNotInitializedException e) {
			log.error("Error While setting Last Export Date", e);
			System.exit(1);
		}
	}

	@SuppressWarnings("unused")
	private static String dateToString(Date date, String format) {
		if (date == null) {
			throw new NullPointerException("Date is null");
		}
		if (format == null) {
			format = "yyyyMMdd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}
