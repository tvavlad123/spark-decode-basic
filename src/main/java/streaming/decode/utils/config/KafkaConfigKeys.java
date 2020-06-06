package streaming.decode.utils.config;

/**
 * Kafka Configuration enum class.
 *
 * @author Vlad Toader
 * @since 06.06.2020
 */
public enum KafkaConfigKeys {
    KAFKA_STARTING_OFFSETS("spark.kafka.starting.offsets"),
    KAFKA_FAIL_ON_DATA_LOSS("spark.kafka.fail.data.loss"),
    KAFKA_TOPICS_DECODE_INPUT("spark.kafka.topics.decode.input"),
    KAFKA_TOPICS_DECODE_OUTPUT("spark.kafka.topics.decode.output"),
    KAFKA_BOOTSTRAP_SERVERS("spark.kafka.bootstrap.servers"),
    KAFKA_SASL_MECHANISM("spark.kafka.sasl.mechanism"),
    KAFKA_SECURITY_PROTOCOL("spark.kafka.security.protocol"),

    KAFKA_CONSUMER_SAS_ENDPOINT("spark.kafka.sas.decode.input.endpoint"),
    KAFKA_CONSUMER_SAS_KEY_NAME("spark.kafka.sas.decode.input.key.name"),
    KAFKA_CONSUMER_SAS_KEY("spark.kafka.sas.decode.input.key"),

    KAFKA_PRODUCER_SAS_ENDPOINT("spark.kafka.sas.decode.output.endpoint"),
    KAFKA_PRODUCER_SAS_KEY_NAME("spark.kafka.sas.decode.output.key.name"),
    KAFKA_PRODUCER_SAS_KEY("spark.kafka.sas.decode.output.key"),

    SPARK_CHECKPOINT_LOCATION("spark.checkpoint.location");

    private final String property;

    KafkaConfigKeys(final String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
