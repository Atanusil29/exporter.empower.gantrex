package com.gantrex.config;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.AbsoluteNameLocationStrategy;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileLocator;
import org.apache.commons.configuration2.io.FileLocatorUtils;
import org.apache.commons.configuration2.io.ProvidedURLLocationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gantrex.exceptions.ConfigFileNotFoundException;
import com.gantrex.exceptions.ConfigNotInitializedException;

/*
 * The Singleton Configuration class.This interface allows accessing properties from configuration file.
 * There is a generic getProperty()method, which returns the value of the queried property in its raw datatype. 
 * Other getter methods are for returning specific properties applicable for project. For most of the property getter methods an 
 * overloaded version exists that allows to specify a default value, which will be returned if the queried property 
 * cannot be found in the configuration. The behavior of the methods that do not take a default value in case of a 
 * missing property is not defined returns null 
 * 
 */

public class ConfigurationManager {

	private static ConfigurationManager instance;
	private Configuration config;
	private static String configFile = "config.properties";
	private static final String configFileEnvVariable = "propertiesFile";
	private static FileLocationStrategy strategy = new CombinedLocationStrategy(Arrays.asList(
			new ProvidedURLLocationStrategy(), new ClasspathLocationStrategy(), new AbsoluteNameLocationStrategy()));

	private static final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);

	private static final String empowerBaseURLKey = "EmpowerBaseURL";
	private static final String otdsBaseURLKey = "OtdsBaseURL";
	private static final String restLogLevel = "RestLogLevel";
	private static final String clientIDKey = "ClientID";
	private static final String clientSecretKey = "ClientSecret";
	private static final String grantTypeKey = "GrantType";
	private static final String empowerUserKey = "EmpowerUser";
	private static final String empowerUserPasswordKey = "EmpowerUserPassword";
	private static final String appIDKey = "ApplicationID";
	private static final String threadsKey = "Threads";
	private static final String watchDirectoryKey = "WatchDirectory";
	private static final String filesExtensionFilterKey = "FilesToWatch";
	private static final String archiveDirectoryKey = "ArchiveDirectory";
	private static final String errorDirectoryKey = "ErrorDirectory";
	private static final String runTypeKey = "RunType";
	private static final String secretLogKey = "SecretLogType";
	private static final String exportDirectoryKey = "ExportDirectory";

	private static final String dbUserKey = "DBUser";
	private static final String dbPassKey = "DBPassword";
	private static final String dbUrlKey = "DBUrl";
	private static final String dbSchemaKey = "DBSchema";
	private static final String dbDriverKey = "DBDriver";
	private static final String dbDialectKey = "DBDialect";
	private static final String lastExportKey = "lastExportKey";
	private static FileBasedConfigurationBuilder<PropertiesConfiguration> builder;

	/**
	 * Get the Instance of this class There should only ever be one instance of this
	 * class and other classes can use this static method to retrieve the instance.
	 * This class loads properties from "propertiesFile" environment variable, if
	 * not defined falls back to config.properties in classpath
	 *
	 * @return the stored Instance of this class
	 * @throws ConfigurationException
	 * 
	 */
	public static ConfigurationManager getInstance() throws ConfigurationException {
		// double checked threadsafe Singleton implementation

		if (instance == null) {
			synchronized (ConfigurationManager.class) {
				if (instance == null) {
					log.info("Configuration Instance is null, Initializing new one");
					instance = new ConfigurationManager();
				}
			}
		}

		return instance;

	}

	/**
	 * private constructor
	 *
	 * @return new instance of ConfigurationManager otherwise throws
	 *         ConfigFileNotFound exception if config file can't be located
	 * @throws ConfigurationException if configuration build fails
	 * 
	 */
	private ConfigurationManager() throws ConfigurationException {

		String propertiesFile = Optional.ofNullable(System.getenv(configFileEnvVariable)).orElseGet(() -> {
			log.warn("Environment Variable {} is not defined", configFileEnvVariable);
			return configFile;
		});
		log.info("Configuration File :  {}", propertiesFile);

		Optional<URL> url = Optional.ofNullable(locateConfigFile(propertiesFile));

		if (url.isPresent()) {
			log.debug("Configuration File :  {} exist", propertiesFile);
			builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
					.configure(new Parameters().fileBased().setURL(url.get()).setThrowExceptionOnMissing(true)
							.setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
							.setLocationStrategy(strategy));
			builder.setAutoSave(true);

			try {
				config = builder.getConfiguration();
			} catch (ConfigurationException e) {
				throw e;
			}
		} else {
			log.debug("Configuration File :  {} doesn't exist", propertiesFile);
			throw new ConfigFileNotFoundException("Config File doesn't exist");
		}

	}

	private URL locateConfigFile(String filePath) {
		log.info("Locating : {}", filePath);
		FileLocator fileLocator = FileLocatorUtils.fileLocator().fileName(filePath).locationStrategy(strategy).create();

		return FileLocatorUtils.locate(fileLocator);

	}

	public Object getProperty(String key) throws ConfigNotInitializedException {
		log.info("Get Property :  {} ", key);
		if (config != null) {
			return config.getProperty(key);
		} else {
			log.debug("Configuration Object is null while getting property {}", key);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getEmpowerBaseURL() throws ConfigNotInitializedException {
		log.info("Get Empower BASE_URL using key :  {} ", empowerBaseURLKey);
		if (config != null) {
			return config.getString(empowerBaseURLKey);
		} else {
			log.debug("Configuration Object is null while getting Empower BASE_URL using {}", empowerBaseURLKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getOTDSBaseURL() throws ConfigNotInitializedException {
		log.info("Get OTDS BASE_URL using key :  {} ", otdsBaseURLKey);
		if (config != null) {
			return config.getString(otdsBaseURLKey);
		} else {
			log.debug("Configuration Object is null while getting OTDS BASE_URL using {}", otdsBaseURLKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getClientID() throws ConfigNotInitializedException {
		log.info("Get Client ID using key :  {} ", clientIDKey);
		if (config != null) {
			return config.getString(clientIDKey);
		} else {
			log.debug("Configuration Object is null while getting Client ID using {}", clientIDKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getClientSecret() throws ConfigNotInitializedException {
		log.info("Get Client Secret using key :  {} ", clientSecretKey);
		if (config != null) {
			return config.getString(clientSecretKey);
		} else {
			log.debug("Configuration Object is null while getting Client Secret using {}", clientSecretKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getEmpowerUser() throws ConfigNotInitializedException {
		log.info("Get Empower User using key :  {} ", empowerUserKey);
		if (config != null) {
			return config.getString(empowerUserKey);
		} else {
			log.debug("Configuration Object is null while getting Empower User using {}", empowerUserKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getGrantType() throws ConfigNotInitializedException {
		log.info("Get Grant Type using key :  {} ", grantTypeKey);
		if (config != null) {
			return config.getString(grantTypeKey);
		} else {
			log.debug("Configuration Object is null while getting Grant Type using {}", grantTypeKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getEmpowerUserPassword() throws ConfigNotInitializedException {
		log.info("Get Empower User's Password using key :  {} ", empowerUserPasswordKey);
		if (config != null) {
			return config.getString(empowerUserPasswordKey);
		} else {
			log.debug("Configuration Object is null while getting Empower User's Password using {}",
					empowerUserPasswordKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getApplicationID() throws ConfigNotInitializedException {
		log.info("Get Application ID using key :  {} ", appIDKey);
		if (config != null) {
			return config.getString(appIDKey);
		} else {
			log.debug("Configuration Object is null while getting Application ID using {}", appIDKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public int getRunType() throws ConfigNotInitializedException {
		log.info("Get Run type using key :  {} ", runTypeKey);
		if (config != null) {
			return config.getInt(runTypeKey);
		} else {
			log.debug("Configuration Object is null while getting run type using {}", runTypeKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public int getThreads() throws ConfigNotInitializedException {
		log.info("Get Threads using key :  {} ", threadsKey);
		if (config != null) {
			return config.getInt(threadsKey);
		} else {
			log.debug("Configuration Object is null while getting Threads using {}", threadsKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getWatchDirectory() throws ConfigNotInitializedException {
		log.info("Get Watch Directory using key :  {} ", watchDirectoryKey);
		if (config != null) {
			return config.getString(watchDirectoryKey);
		} else {
			log.debug("Configuration Object is null while getting Watch Directory using {}", watchDirectoryKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getErrorDirectory() throws ConfigNotInitializedException {
		log.info("Get Error Directory using key :  {} ", errorDirectoryKey);
		if (config != null) {
			return config.getString(errorDirectoryKey);
		} else {
			log.debug("Configuration Object is null while getting Error Directory using {}", errorDirectoryKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getFileExtensionFilter() throws ConfigNotInitializedException {
		log.info("Get File Extension Filter using key :  {} ", filesExtensionFilterKey);
		if (config != null) {
			return config.getString(filesExtensionFilterKey);
		} else {
			log.debug("Configuration Object is null while getting File Extension Filter using {}",
					filesExtensionFilterKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public long getLastExportMillis() throws ConfigNotInitializedException {
		log.info("Get lastExportMillis Filter using key :  {} ", lastExportKey);
		if (config != null) {
			return config.getLong(lastExportKey);
		} else {
			log.debug("Configuration Object is null while getting lastExportMillis using {}", lastExportKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public void setLastExportMillis(long millis) throws ConfigNotInitializedException {
		log.info("Set lastExportMillis Filter using key :  {} ", lastExportKey);
		if (config != null) {
			config.setProperty(lastExportKey, millis);
			// builder.save();
		} else {
			log.debug("Configuration Object is null while setting lastExportMillis using {}", lastExportKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getArchiveDirectory() throws ConfigNotInitializedException {
		log.info("Get Archive Directory using key :  {} ", archiveDirectoryKey);
		if (config != null) {
			return config.getString(archiveDirectoryKey);
		} else {
			log.debug("Configuration Object is null while getting Archive Directory using {}", archiveDirectoryKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getExportDirectory() throws ConfigNotInitializedException {
		log.info("Get Export Directory using key :  {} ", exportDirectoryKey);
		if (config != null) {
			return config.getString(exportDirectoryKey);
		} else {
			log.debug("Configuration Object is null while getting Export Directory using {}", exportDirectoryKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getDBUser() throws ConfigNotInitializedException {
		log.info("Get DB User using key :  {} ", dbUserKey);
		if (config != null) {
			return config.getString(dbUserKey);
		} else {
			log.debug("Configuration Object is null while getting DB User using {}", dbUserKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getDBPassword() throws ConfigNotInitializedException {
		log.info("Get DB Password using key :  {} ", dbPassKey);
		if (config != null) {
			return config.getString(dbPassKey);
		} else {
			log.debug("Configuration Object is null while getting DB Password using {}", dbPassKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getDBUrl() throws ConfigNotInitializedException {
		log.info("Get DB URL using key :  {} ", dbUrlKey);
		if (config != null) {
			return config.getString(dbUrlKey);
		} else {
			log.debug("Configuration Object is null while getting DB URL using {}", dbUrlKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getDBDialect() throws ConfigNotInitializedException {
		log.info("Get DB Dialect using key :  {} ", dbDialectKey);
		if (config != null) {
			return config.getString(dbDialectKey);
		} else {
			log.debug("Configuration Object is null while getting DB Dialect using {}", dbDialectKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getDBSchema() throws ConfigNotInitializedException {
		log.info("Get DB Schema using key :  {} ", dbSchemaKey);
		if (config != null) {
			return config.getString(dbSchemaKey);
		} else {
			log.debug("Configuration Object is null while getting DB Schema using {}", dbSchemaKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	public String getDBDriver() throws ConfigNotInitializedException {
		log.info("Get DB Driver using key :  {} ", dbDriverKey);
		if (config != null) {
			return config.getString(dbDriverKey);
		} else {
			log.debug("Configuration Object is null while getting DB Driver using {}", dbDriverKey);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

	// undoumented
	public boolean getSecretLog() {
		if (config != null) {
			try {
				int secretLog = config.getInt(secretLogKey);
				return secretLog == 0 ? false : true;
			} catch (Exception e) {
				return false;
			}

		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return the log level for ohttp log interceptor 0 -> NONE, 1 -> BASIC, 2 ->
	 *         HEADERS, 3 -> BODY
	 * @throws ConfigNotInitializedException
	 */
	public int getInterceptorLogLevel() throws ConfigNotInitializedException {
		log.info("Get Interceptor Log Level using key :  {} ", restLogLevel);
		if (config != null) {
			return config.getInt(restLogLevel);
		} else {
			log.debug("Configuration Object is null while getting Interceptor Log Level using {}", restLogLevel);
			throw new ConfigNotInitializedException("Configuration Object is null");
		}
	}

}
