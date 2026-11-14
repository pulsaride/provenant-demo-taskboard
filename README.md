# Provenant demos: Java EE to Spring Boot

Provenant is a deterministic modernization compiler (no LLM in the pipeline). Its current target is **Spring Boot 4.1 on Java 25**.

- **Same output every run.** Emitting the same input twice gives byte-identical output.
- **Provenance everywhere.** Every generated unit records where it came from and the tier of evidence behind it: Proven, Empirical, Retained or Isolated.
- **Counted, never hidden.** Whatever the engine could not prove is not guessed. It is left as a visible `TODO(kind: reason)` marker and counted.

Each demo folder holds the unchanged legacy application, what Provenant emitted from it, and the demo video.

| Demo | What it is |
|---|---|
| [`taskboard/`](taskboard) | Java EE 8 / JSF Taskboard (hantsy/javaee8-jsf-sample) to Spring Boot 4.1 + Angular. The video was recorded on the earlier Spring Boot 3 emission. |
| [`daytrader7/`](daytrader7) | IBM DayTrader 7 (EJB, JMS, JSP, WebSocket) to Spring Boot 4.1 on Java 25, with a 3.5 min video. |

What Provenant migrates and what comes next: [`COVERAGE.md`](COVERAGE.md). Using something that is not covered? [Request it](../../issues/new?template=framework-request.yml).

## Reproduce

Both `generated/` folders were produced by Provenant `0.1.0+c8357d8803ba166d` from the `legacy/` folder next to them, nothing edited by hand. The exact commands are in each demo README. Build a generated app with JDK 25 and Maven (`mvn -q package -DskipTests`, plus `-Pfrontend` for the Taskboard).

## Licenses

- `taskboard/legacy/`: hantsy/javaee8-jsf-sample, © Hantsy Bai, GNU GPL v3 (its upstream licence).
- `daytrader7/legacy/`: WASdev/sample.daytrader7 at `643d04c`, Apache-2.0 (IBM); its `LICENSE` is kept (upstream ships no NOTICE file).
- The `generated/` folders are derived from those sources and carry the same terms. The repository [`LICENSE`](LICENSE) is the GPL v3 that covers the Taskboard part.

Provenant itself is a separate product and is not part of this repository.
