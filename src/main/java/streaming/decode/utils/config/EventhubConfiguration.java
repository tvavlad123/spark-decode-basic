package streaming.decode.utils.config;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;

import java.net.URI;

public class EventhubConfiguration {

    private static final String KAFKA_JAAS_TEMPLATE = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";

    public static String getSaslConnectionString(String endpoint, String sasKeyName, String sasKey) {
        ConnectionStringBuilder builder = new ConnectionStringBuilder();
        builder.setEndpoint(URI.create(endpoint));
        builder.setSasKeyName(sasKeyName);
        builder.setSasKey(sasKey);

        return String.format(KAFKA_JAAS_TEMPLATE, "$ConnectionString", builder.toString());
    }
}
