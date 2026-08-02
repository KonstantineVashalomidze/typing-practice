package com.github.konstantinevashalomidze.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MongoConfig {

    private static MongoClient mongoClient;

    private MongoConfig() {
    }

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            synchronized (MongoConfig.class) {
                if (mongoClient == null) {
                    mongoClient = MongoClients.create(
                            MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://user:pass@localhost:27017?directConnection=true"))
                                    .applyToSocketSettings(builder ->
                                            builder.connectTimeout(5L, SECONDS))
                                    .build());
                }
            }
        }
        return mongoClient;
    }

}
