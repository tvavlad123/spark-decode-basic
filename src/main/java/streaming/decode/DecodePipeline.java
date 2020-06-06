package streaming.decode;

import com.daimler.ca.data.roadenv.spark.RoadEnvironmentStructType;
import com.daimler.ca.data.vvr.spark.VVRStructType;
import streaming.decode.utils.config.KafkaConfigKeys;
import streaming.decode.utils.config.KafkaConfiguration;
import streaming.decode.utils.config.PropertiesConfiguration;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.Map;
import java.util.Properties;

public class DecodePipeline {
    SparkSession spark;
    private final Properties properties;

    public static void main(String[] args) throws StreamingQueryException {
        DecodePipeline pipeline = new DecodePipeline();
        pipeline.run();
    }

    public DecodePipeline() {
        spark = SparkSession.builder()
                .appName("RcfsDecodePipeline")
                .getOrCreate();
        this.properties = PropertiesConfiguration.getProperties();

    }

    void run() throws StreamingQueryException {
        Dataset<Row> inputStream = readStream();
        Dataset<Row> filteredStream = decodePipeline(inputStream);
        Dataset<Row> outputStream = filteredStream.toJSON().toDF();
        writeStream(outputStream);
        spark.streams().awaitAnyTermination();

    }

    /**
     * Filters only the events of type X and selects events from category Y and model Z
     *
     * @param stream input stream for decoding
     * @return returns the decoded values
     */
    Dataset<Row> decodePipeline(Dataset<Row> stream) {
        return stream.filter("event.type == 'X'")
                .selectExpr("event.category as " + "Y", "event.model as " + "Z");
    }

    Dataset<Row> readStream() {
        Map<String, String> kafkaProperties = KafkaConfiguration.buildKafkaProperties(this.properties);
        return spark
                .readStream()
                .format("kafka")
                .options(kafkaProperties)
                .option("subscribe", properties.getProperty(KafkaConfigKeys.KAFKA_TOPICS_DECODE_INPUT.getProperty()))
                .option("kafka.sasl.jaas.config", KafkaConfiguration.buildConsumerConnectionString(this.properties))
                .option("startingOffsets", properties.getProperty(KafkaConfigKeys.KAFKA_STARTING_OFFSETS.getProperty()))
                .option("failOnDataLoss", properties.getProperty(KafkaConfigKeys.KAFKA_FAIL_ON_DATA_LOSS.getProperty()))
                .load()
                .selectExpr("CAST(value AS STRING) as message")
                .select(functions.from_json(functions.col("message"), VVRStructType.getStructType()).as("event"));
    }

    void writeStream(Dataset<Row> stream) {
        Map<String, String> kafkaProperties = KafkaConfiguration.buildKafkaProperties(this.properties);
        stream.writeStream()
                .format("kafka")
                .options(kafkaProperties)
                .option("topic", properties.getProperty(KafkaConfigKeys.KAFKA_TOPICS_DECODE_OUTPUT.getProperty()))
                .option("kafka.sasl.jaas.config", KafkaConfiguration.buildProducerConnectionString(this.properties))
                .option("checkpointLocation", properties.getProperty(KafkaConfigKeys.SPARK_CHECKPOINT_LOCATION.getProperty()))
                .start();
    }
}
