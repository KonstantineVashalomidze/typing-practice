package com.github.konstantinevashalomidze.domain.service;

import org.bson.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


public class RandomWordApiProvider implements TextProvider {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final int wordCount;
    private final Queue<String> cache = new LinkedList<>();
    private final int diff, length;


    public RandomWordApiProvider(int wordCount, int diff, int length) {
        this.wordCount = wordCount;
        this.diff = diff;
        this.length = length;

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (cache.size() <= 2) {
                    cache.offer(fetch());
                    cache.offer(fetch());
                    cache.offer(fetch());
                    cache.offer(fetch());
                    cache.offer(fetch());
                    cache.offer(fetch());
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000);

    }

    private String fetch() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://random-word-api.herokuapp.com/word?number=%d&diff=%d&length=%d".formatted(wordCount, diff, length)))
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Document document = Document.parse("{ \"words\": " + httpResponse.body() + " }");
            List<String> list = document.getList("words", String.class);
            return String.join(" ", list);
        } catch (IOException | InterruptedException e) {
            return "";
        }
    }


    @Override
    public String getText() {
        if (!cache.isEmpty()) {
            return cache.poll();
        } else {
            return fetch();
        }
    }
}
