package com.github.mawen12.agentx.core.report;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.report.Reporter;
import com.github.mawen12.agentx.core.utils.JsonUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.Objects;
import java.util.Properties;

import static com.github.mawen12.agentx.api.config.Constants.*;

public class DefaultReporter implements Reporter {
    private static final Logger LOGGER = Agent.getLogger(DefaultReporter.class);

    private boolean enabled;
    private String topic;
    private KafkaProducer<byte[], byte[]> producer;

    @Override
    public void init(Config config) {
        Boolean reporterEnabled = config.getBoolean(CONFIG_REPORTER_ENABLED);
        enabled = Objects.equals(reporterEnabled, true);

        String bootstrapServers = config.getString(CONFIG_REPORTER_BOOTSTRAP_SERVERS);
        topic = config.getString(CONFIG_REPORTER_TOPIC);
        Integer maxBytes = config.getInt(CONFIG_REPORTER_MAX_BYTES);

        if (!enabled) {
            LOGGER.warn("reporter is disabled");
            return;
        }

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "agentx");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.COMPRESSION_GZIP_LEVEL_CONFIG, "1");

        producer = new KafkaProducer<>(props);
    }

    public void report(AppLogData log) {
        if (!enabled) {
            return;
        }

        try {
            producer.send(new ProducerRecord<>(topic, JsonUtils.encode(log)));
        } catch (Throwable e) {
            LOGGER.warn("reporter log failed", e);
        }

    }
}
