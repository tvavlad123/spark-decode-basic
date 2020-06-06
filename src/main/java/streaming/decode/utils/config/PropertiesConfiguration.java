package streaming.decode.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Spark Config class for Kafka streams.
 *
 * @author Vlad Toader
 * @since 06.06.2020
 */
public class PropertiesConfiguration {

    /**
     * Gets the Kafka producer properties.
     *
     * @return the properties read from the producer configuration property file.
     */
    public static Properties getProperties() {
        Properties applicationProperties = PropertiesConfiguration.getProperties("application.properties");
        PropertiesConfiguration.replaceWithSystemProperties(applicationProperties);
        return applicationProperties;
    }

    private static Properties getProperties(String applicationProperties) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(applicationProperties)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    private static void replaceWithSystemProperties(Properties properties) {
        Properties systemProperties = System.getProperties();
        systemProperties.entrySet().stream().filter(p -> properties.containsKey(p.getKey()))
                .forEach(k -> properties.put(k.getKey(), k.getValue()));
    }
}
