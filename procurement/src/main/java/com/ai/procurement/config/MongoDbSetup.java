package com.ai.procurement.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(
        name = "app.mongodb.setup.enabled",
        havingValue = "true"
)
public class MongoDbSetup {

    @Bean
    public CommandLineRunner initializeMongoDb() {
        return args -> {
            String databaseName = "procurement_db";
            String username = "procurement_app";
            String password = System.getenv("PROCUREMENT_DB_PASSWORD");

            if (password == null || password.isBlank()) {
                throw new IllegalStateException(
                        "Set the PROCUREMENT_DB_PASSWORD environment variable."
                );
            }

            // Initial setup assumes MongoDB authentication is disabled.
            try (MongoClient client =
                         MongoClients.create("mongodb://localhost:27017")) {

                MongoDatabase database =
                        client.getDatabase(databaseName);

                // Ensure the database exists by creating a collection
                // only if it does not already exist.
                boolean collectionExists =
                        database.listCollectionNames()
                                .into(new java.util.ArrayList<>())
                                .contains("application_init");

                if (!collectionExists) {
                    database.createCollection("application_init");
                    System.out.println(
                            "Created database: " + databaseName
                    );
                } else {
                    System.out.println(
                            "Database already initialized: " + databaseName
                    );
                }

                // Check whether the application user already exists.
                Document usersResult = database.runCommand(
                        new Document("usersInfo", username)
                );

                List<?> users =
                        (List<?>) usersResult.get("users");

                if (users == null || users.isEmpty()) {
                    Document user = new Document("createUser", username)
                            .append("pwd", password)
                            .append("roles", List.of(
                                    new Document("role", "readWrite")
                                            .append("db", databaseName)
                            ));

                    database.runCommand(user);

                    System.out.println(
                            "Created MongoDB application user: " + username
                    );
                } else {
                    System.out.println(
                            "MongoDB application user already exists."
                    );
                }
            }
        };
    }
}