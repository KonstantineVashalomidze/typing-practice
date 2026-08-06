package com.github.konstantinevashalomidze.domain.model;

import com.github.konstantinevashalomidze.domain.TypingSession;
import org.bson.types.ObjectId;

import java.util.List;

public record Metrics(
        ObjectId _id,
        double netWpm,
        double accuracy,
        int errorCount,
        List<TypingSession.Entry> keyTimestamps,
        String targetText
) {

  public Metrics(double netWpm, double accuracy, int errorCount, List<TypingSession.Entry> keyTimestamps, String targetText) {
      this(null, netWpm, accuracy, errorCount, keyTimestamps, targetText);
  }
}
