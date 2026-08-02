package com.github.konstantinevashalomidze.domain;

import org.bson.types.ObjectId;

public record Metrics(
        ObjectId _id,
        double netWpm,
        double accuracy,
        int errorCount
) {

  public Metrics(double netWpm, double accuracy, int errorCount) {
      this(null, netWpm, accuracy, errorCount);
  }
}
