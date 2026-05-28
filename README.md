# Single Agent ADD 3.0 Assignment

This project implements Option 2 of the assignment: a single Agent using sequential reasoning and self-reflection to apply ADD 3.0 to the Hotel Pricing System case study.

## Submission Files

For the assignment requirements, the key single-agent deliverables are located here:

- Source code: this repository.
- Complete LLM conversation log with timestamps: `outputs/origin(English)/conversation-log.jsonl`.
- Readable Markdown version of the conversation log: `outputs/origin(English)/conversation-log.md`.
- English report source: `outputs/origin(English)/report-draft.md`.
- Final PDF report: `report-draft.pdf`.
- Chinese reference versions: `outputs/chinese/`.

## Runtime

- Java 17+
- Maven 3.9+
- Spring Boot
- Spring AI Alibaba DashScope starter
- Model: `qwen3-32b`

## Configuration

Set the DashScope API key before running:

```bash
export AI_DASHSCOPE_API_KEY=your_api_key
```

The application also accepts the shorter alias:

```bash
export DASHSCOPE_API_KEY=your_api_key
```

Make sure the variable is exported in the same terminal session where you run Maven:

```bash
echo $AI_DASHSCOPE_API_KEY
mvn spring-boot:run
```

The model is configured in `src/main/resources/application.yml`:

```yaml
spring:
  ai:
    dashscope:
      read-timeout: 180000
      chat:
        options:
          model: qwen3-32b
          enable-thinking: false
```

The assignment uses explicit self-reflection calls, so `enable-thinking` is disabled to reduce request latency while preserving the required single-agent reflection workflow.

## Run

```bash
mvn spring-boot:run
```

Optional output directory:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--assignment.output-dir=outputs"
```

## Outputs

After a successful run, the application writes:

- `outputs/conversation-log.jsonl`
- `outputs/add-results.md`
- `outputs/report-draft.md`

In this submitted repository, the original English outputs have been archived under `outputs/origin(English)/`, and Chinese-readable reference files have been placed under `outputs/chinese/`.

## Tests

```bash
mvn test
```
