# DayTrader 7: Java EE 7 to Spring Boot 4.1 on Java 25

[`provenant-daytrader7-demo.mp4`](provenant-daytrader7-demo.mp4) (3.5 min) shows Provenant modernizing a public Java EE 7 application: EJB, JMS, JSP and WebSocket, all kept as the sample ships them.

**The application:** [WASdev/sample.daytrader7](https://github.com/WASdev/sample.daytrader7) at commit `643d04c100f75898003b5c98cde6b5f5015c613a` (Apache-2.0, IBM). It is not part of this repository. The legacy side runs on Open Liberty 26.0.0.11 (JDK 17) with the embedded Derby database the sample ships.

## What the video shows

1. **Legacy.** The sample is cloned at that commit, its EAR (built beforehand with the sample's own Maven recipe, only its size is shown) runs on the Liberty server. A short browser walk: log in with a shipped demo account (`uid:2`), account page, portfolio, one quote, buy 10 shares, sell one holding, account page again.
2. **Modernize, from the command line.** `modernizer profile` (what the engine recognises), `modernizer emit` (Spring Boot 4.1, layered profile), a second emission that is byte-identical (`diff -r` prints nothing), the counted losses from the emission report, `mvn package`, and the start on Java 25.
3. **Generated.** The same browser walk on the Spring Boot application.
4. **Compare.** The same eight steps side by side, legacy on the left.
5. **Closing card, measured result.** DayTrader behaviour harness on a shared legacy database: 10 of 11 scenarios equal. DT1 differs on one sub-test (a CDI decorator ping page), which is refused and counted; the deviation is accepted by the owner.

Both applications ran on their own copy of the same checksummed legacy Derby snapshot (sha256 `dcb3043b72b18b7562d1aa4674e10c2bb24e008a489fcb12e724e7981c9341d0`).

## Numbers in the video, and where they come from

| Number | Source |
|---|---|
| 10 of 11 scenarios equal, DT1 differs on its decorator sub-result | Provenant's DayTrader behaviour harness, measured on a shared legacy database (internal record `dt-followups-2026-11-14`) |
| Emission counts (185 files; provenance 2015 records: 0 Proven, 1583 Isolated, 432 Retained, 0 Empirical; 985 not-carried markers, each a `TODO(kind: reason)` line in the output) | printed by `modernizer emit` in the video, measured on this run |

The guarantee is tiered, not absolute: what could not be proven is isolated or counted, never guessed.

## Things the walks do not hide

- The generated application sends `X-Frame-Options: DENY`, and the legacy UI is an HTML frameset. In the video the generated pages are therefore opened without the frame header, so the compare shows page content only. The behaviour harness does not exercise frames.
- The second walk's startup time printed in the video was measured on a busy machine.
- Orders and prices in both walks are produced by the application itself (the buy and the sell used the same demo account and gave the same order numbers and holding on both sides).

## Reproduce

```sh
git clone https://github.com/WASdev/sample.daytrader7 daytrader7
git -C daytrader7 checkout 643d04c100f75898003b5c98cde6b5f5015c613a
# the engine reads the legacy runtime from an attestation file
mkdir daytrader7/.provenant   # add legacy-runtime.json: open-liberty 26.0.0.11

modernizer profile daytrader7
modernizer emit daytrader7 dt-modern com.ibm.websphere.samples.daytrader --profile layered --target-line boot-4.1
modernizer emit daytrader7 dt-again  com.ibm.websphere.samples.daytrader --profile layered --target-line boot-4.1
diff -r dt-modern dt-again && echo identical
grep -A8 'not carried' emit.txt          # the counted losses
cd dt-modern && mvn -B -q package -DskipTests      # JDK 25
PROVENANT_LEGACY_DB_URL=jdbc:derby:<copy-of-snapshot> PROVENANT_LEGACY_DB_USER=db_username PROVENANT_LEGACY_DB_PASSWORD=db_password \
  java -jar target/daytrader7-0.1.0-SNAPSHOT.jar --server.port=19480 --spring.profiles.active=legacydb
```

Provenant itself is a separate product and is not part of this repository. The sample's database credentials are the sample's own public defaults.

`provenant-logo.svg` and `provenant-logo.png` are the corner mark used in the video.
