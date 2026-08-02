package com.github.konstantinevashalomidze.domain;

import org.bson.types.ObjectId;

public record Metrics(
        ObjectId _id,
        double netWpm,
        double accuracy,
        int errorCount,
        String targetText
) {

  public Metrics(double netWpm, double accuracy, int errorCount, String targetText) {
      this(null, netWpm, accuracy, errorCount, targetText);
  }
}
