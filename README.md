# typing-practice

A Java Swing desktop app for typing practice. It tracks net WPM, accuracy, and error count per session, and uses those numbers to generate new practice text with AI, so the material adapts to what you actually struggle with.

![Demo](demo.png)

## What it does

- Runs as a native desktop app, built on Java Swing with an MVP structure (presenter, view, domain)
- Stores every session's metrics in MongoDB, so progress persists across runs
- Generates fresh practice text through a GenAI integration, informed by your past sessions
- Tracks net words per minute, accuracy, and error count per session

## Stack

Java, Swing, MongoDB, Google GenAI

## Running it locally

```bash
docker compose up -d
```

That starts a local MongoDB instance. Copy `src/main/resources/application.properties.example` to `application.properties` and set your own `genai.api.key`, then run the app from your IDE or with Maven.

See `CONTRIBUTING.md` for more on the project layout.
