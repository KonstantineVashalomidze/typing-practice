package com.github.konstantinevashalomidze.db;

import com.github.konstantinevashalomidze.domain.model.Metrics;
import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MetricsRepository {
    private final MongoCollection<Metrics> collection;
    private final Logger logger = LoggerFactory.getLogger(MetricsRepository.class);

    public MetricsRepository() {
        collection = MongoConfig.getMongoClient()
                .getDatabase("typing-practice-metrics")
                .getCollection("metrics", Metrics.class);
    }

    public Metrics findById(String id) {
        return collection.find(eq("_id", new ObjectId(id))).first();
    }

    public void save(Metrics metrics) {
        collection.insertOne(metrics);
    }

    public List<Metrics> findAll() {
        return collection.find().into(new ArrayList<>());
    }

}
