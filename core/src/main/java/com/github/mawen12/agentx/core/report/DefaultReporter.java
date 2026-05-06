package com.github.mawen12.agentx.core.report;

import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.report.Reporter;
import com.github.mawen12.agentx.core.utils.JsonUtils;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Encoding;
import zipkin2.reporter.kafka.KafkaSender;

import java.util.Objects;

import static com.github.mawen12.agentx.api.config.Constants.*;

public class DefaultReporter implements Reporter {
    private boolean enabled;
    private AsyncReporter<BytesWrapper> reporter;

    @Override
    public void init(Config config) {
        Boolean reporterEnabled = config.getBoolean(CONFIG_REPORTER_ENABLED);
        enabled = Objects.equals(reporterEnabled, true);

        String bootstrapServers = config.getString(CONFIG_REPORTER_BOOTSTRAP_SERVERS);
        String topic = config.getString(CONFIG_REPORTER_TOPIC);
        Integer maxBytes = config.getInt(CONFIG_REPORTER_MAX_BYTES);

        if (!enabled) {
            return;
        }

        KafkaSender sender = KafkaSender.newBuilder()
                .bootstrapServers(bootstrapServers)
                .topic(topic)
                .encoding(Encoding.JSON)
                .messageMaxBytes(maxBytes)
                .build();

        reporter = AsyncReporter.builder(sender).build(BytesWrapperEncoder.INSTANCE);
    }

    public void report(AppLogData log) {
        if (!enabled) {
            return;
        }

        reporter.report(new BytesWrapper(JsonUtils.encode(log)));
    }
}
