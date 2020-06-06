package streaming.decode.utils.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaConfiguration {

    public static Map<String, String> buildKafkaProperties(Properties properties) {
        Map<String, String> consumerProperties = new HashMap<>();
        consumerProperties.put(KafkaConfigKeys.KAFKA_BOOTSTRAP_SERVERS.getProperty().replace("spark.", ""),
                properties.getProperty(KafkaConfigKeys.KAFKA_BOOTSTRAP_SERVERS.getProperty()));
        consumerProperties.put(KafkaConfigKeys.KAFKA_SASL_MECHANISM.getProperty().replace("spark.", ""),
                properties.getProperty(KafkaConfigKeys.KAFKA_SASL_MECHANISM.getProperty()));
        consumerProperties.put(KafkaConfigKeys.KAFKA_SECURITY_PROTOCOL.getProperty().replace("spark.", ""),
                properties.getProperty(KafkaConfigKeys.KAFKA_SECURITY_PROTOCOL.getProperty()));

        return consumerProperties;
    }

    public static String buildConsumerConnectionString(Properties properties) {
        return EventhubConfiguration.getSaslConnectionString(
                properties.getProperty(KafkaConfigKeys.KAFKA_CONSUMER_SAS_ENDPOINT.getProperty()),
                properties.getProperty(KafkaConfigKeys.KAFKA_CONSUMER_SAS_KEY_NAME.getProperty()),
                properties.getProperty(KafkaConfigKeys.KAFKA_CONSUMER_SAS_KEY.getProperty()));
    }

    public static String buildProducerConnectionString(Properties properties) {
        return EventhubConfiguration.getSaslConnectionString(
                properties.getProperty(KafkaConfigKeys.KAFKA_PRODUCER_SAS_ENDPOINT.getProperty()),
                properties.getProperty(KafkaConfigKeys.KAFKA_PRODUCER_SAS_KEY_NAME.getProperty()),
                properties.getProperty(KafkaConfigKeys.KAFKA_PRODUCER_SAS_KEY.getProperty()));
    }
}
