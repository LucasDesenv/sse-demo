package com.sse.demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCompressor;
import com.mongodb.reactivestreams.client.MongoClients;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.transaction.ReactiveTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;
    private String db;

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    @Nonnull
    protected String getDatabaseName() {
        return db;
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        var connection = new ConnectionString(mongoUri);
        db = connection.getDatabase();
        builder.compressorList(List.of(MongoCompressor.createZlibCompressor()));
        builder.applyConnectionString(connection);
        builder.uuidRepresentation(UuidRepresentation.STANDARD);
        builder.codecRegistry(MongoClients.getDefaultCodecRegistry());
    }

    @Bean
    @ConditionalOnProperty(prefix = "mongodb.transaction", name = "enabled", havingValue = "true")
    ReactiveTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

}
