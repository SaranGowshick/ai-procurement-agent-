
package com.ai.procurement.config;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConnectionConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(MongoConnectionConfig.class);

    @Bean
    public CommandLineRunner verifyMongoConnection(
            MongoTemplate mongoTemplate) {

        return args -> {
            try {
                mongoTemplate.getDb()
                        .runCommand(new Document("ping", 1));

                logger.info(
                        "MongoDB connection established successfully."
                );

            } catch (Exception exception) {
                logger.error(
                        "MongoDB connection failed. "
                                + "Ensure MongoDB is running at localhost:27017.",
                        exception
                );

                throw new IllegalStateException(
                        "Procurement service startup aborted: "
                                + "MongoDB is unavailable.",
                        exception
                );
            }
        };
    }
}
