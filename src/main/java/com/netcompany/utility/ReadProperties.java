package com.netcompany.utility;

import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
    private static final String PROPERTIES_DIR = "/files/";
    private static final String TEST_PROPERTIES_FILE_PATH = PROPERTIES_DIR + "config.properties";
    private static final String USER_PROPERTIES_FILE_PATH = PROPERTIES_DIR + "resource.properties";
    private static final Properties props;

    static {
        props = new Properties();
        loadTestPropertiesFromFile();
        loadUserPropertiesFromFile();
    }

    private static void loadTestPropertiesFromFile() {
        loadPropertiesFromFile(TEST_PROPERTIES_FILE_PATH);
    }

    private static void loadUserPropertiesFromFile() {
        loadPropertiesFromFile(USER_PROPERTIES_FILE_PATH);
    }

    private static void loadPropertiesFromFile(String propertiesFilePath) {
        try {
            InputStream propsStream;
            propsStream = ReadProperties.class.getResourceAsStream(propertiesFilePath);
            props.load(propsStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Problem loading test properties file. Is " + (propertiesFilePath + " on classpath?"), e);
        }
    }

    public String initialRegistrationEndPoint() {
        return getProp("InitialRegistrationEndpoint");
    }

    public String switchRequestEndpoint() {
        return getProp("SwitchRequestEndpoint");
    }

    public String invitationToInterventionEndpoint() {
        return getProp("InvitationToInterventionEndpoint");
    }

    private static String getProp(String key) {
        String s = props.getProperty(key);
        return (s != null) ? s.trim() : null;
    }
}
