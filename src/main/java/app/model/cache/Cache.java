package app.model.cache;

import app.model.dao.DaoFactory;
import utils.UtilityManager;
import utils.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Cache {
    private static final String CONFIG_FILE = "cfg/config.properties";
    static Cache instance = null;
    private final Logger logger;
    DaoFactory factory;
    private Properties properties;

    private Cache() {
        factory = UtilityManager.getDaoFactory();
        logger = UtilityManager.getCacheLogger();
        properties = new Properties();
        loadConfig();
    }

    public static Cache getInstance() {
        if (instance == null)
            instance = new Cache();
        instance.saveConfig();
        return instance;
    }

    public DaoFactory getFactory() {
        return factory;
    }

    public void setFactory(DaoFactory factory) {
        this.factory = factory;
    }

    public void loadConfig() {
        logger.trace(true, null);
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading config file: " + e.getMessage());
            // System.out.println("Creating new config file");
            initConfig();
            loadConfig();
        }
        logger.trace(false, null);
    }

    public void saveConfig() {
        logger.trace(true, null);
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Configuration");
        } catch (IOException e) {
            System.err.println("Error saving config file: " + e.getMessage());
        }
        logger.trace(false, null);

    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    private void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    private void removeProperty(String key) {
        properties.remove(key);
    }

    public void setMostRecentDirectory(File newDirectory, DirectoryKey key) {
        logger.trace(true, null);
        if (!newDirectory.exists())
            return;

        properties.setProperty(key.toString(), newDirectory.getAbsolutePath());
        saveConfig();
        logger.trace(false, null);
    }

    public String getLastViewedDirectory(DirectoryKey directoryKey) {
        if (!properties.containsKey(directoryKey.toString()))
            loadConfig();
        return properties.getProperty(directoryKey.toString());
    }

    public void resetConfig() {
        logger.trace(true, null);
        properties.clear();
        saveConfig();
        loadConfig();
        logger.trace(false, null);
    }

    private void initConfig() {
        try {
            File file = new File(CONFIG_FILE);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.createNewFile())
                throw new IOException();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error creating config file: " + ex.getMessage());
        }
    }

    public enum DirectoryKey {
        ADD_FILE("ADD_FILE_DIRECTORY"),
        SAVE_EVALUATION("SAVE_EVALUATION_DIRECTORY"),
        SAVE_CHUNKS("SAVE_CHUNKS_DIRECTORY"),
        FUNCTION_PHRASE("FUNCTION_PHRASE_FILE");

        private final String key;

        DirectoryKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

}
