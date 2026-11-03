# Provenant demo: Java EE 8 / JSF Taskboard → Spring Boot 3 + Angular

This repository holds both sides of a modernization so you can inspect them yourself:

| Folder | What it is |
|---|---|
| [`legacy/`](legacy) | The original application, **unchanged**: [hantsy/javaee8-jsf-sample](https://github.com/hantsy/javaee8-jsf-sample) at commit `f83d7c6`. It uses JSF 2.3, CDI 2, EJB, JPA 2.2 and Java EE Security, and runs on GlassFish 5.1. |
| [`generated/`](generated) | What **Provenant** emitted from it: a Spring Boot 3 backend, an Angular frontend and a Maven build. Nothing was edited by hand. |
| [`demo/`](demo) | The demo video and the SQL for the demo login. |

Provenant is a deterministic modernization compiler (no LLM in the pipeline):
- **Same output every run.** Emitting the same input twice gives byte-identical output.
- **Provenance everywhere.** Every generated unit records where it came from and the tier of evidence behind it: Proven, Empirical, Retained or Isolated.
- **Counted, never hidden.** Whatever the engine could not prove is not guessed. It is left as a visible `TODO(kind: reason)` marker and counted.

▶️ **Demo video** (2.5 min): [`demo/provenant-demo.mp4`](demo/provenant-demo.mp4)

## What to look at

- **The code itself:** `generated/src/main/java` (Spring Boot) and `generated/frontend/src/app` (Angular), next to `legacy/src/main/java` and `legacy/src/main/webapp/*.xhtml`.
- **`generated/modernization-contract.json`:** every emitted file with its provenance and tier, for traceability.
- **The `TODO(` markers:** every place the legacy behaviour was not carried, each one named and counted.
  ```sh
  grep -rho 'TODO([a-z_]*' generated/src generated/frontend/src | sort | uniq -c | sort -rn
  ```
- **`generated/.modernizer/`:** verification metadata.

## Run the legacy app (JDK 8, GlassFish 5.1)

```sh
cd legacy && mvn package -DskipTests
asadmin start-database && asadmin start-domain domain1
asadmin deploy --force=true --name javaee8-jsf-sample --contextroot javaee8-jsf-sample target/javaee8-jsf-sample.war
```

Then open <http://localhost:8080/javaee8-jsf-sample/>.

To log in, add the demo user to the legacy Derby database. Use Derby `ij` against `jdbc:derby://localhost:1527/sun-appserv-samples;user=APP;password=APP` and run [`demo/demo-user.sql`](demo/demo-user.sql). Redeploying resets that database, because the app drops and recreates its schema.

## Run the generated app (JDK 21, Maven 3.9, Node 20+)

```sh
cd generated
mvn -Pfrontend package -DskipTests          # builds the Angular app into the jar
java -jar target/taskboard-0.1.0-SNAPSHOT.jar --server.port=19080 \
  --spring.sql.init.mode=always \
  --spring.sql.init.data-locations=file:../demo/demo-user.sql \
  --spring.jpa.defer-datasource-initialization=true
```

Then open <http://localhost:19080/>. More options, such as running the frontend separately or using PostgreSQL, are in [`generated/README.md`](generated/README.md).

**Demo login on both apps:** `tbsec` / `tbsec-secret-1`. This is a demo-only account; the hash in `demo-user.sql` is generated in the format the legacy container uses.

## Known differences (shown, not hidden)

The generated app is meant to behave like the legacy one, legacy quirks included:
- **Remember-me cookie (scenario S5):** the legacy cookie gives a 500 after logout. That defect is reproduced, not fixed, so S5 is only partially equal.
- **`created_at` is never set:** with two or more tasks, sorting the board throws in **both** apps. This is a legacy defect, kept.
- **Stricter security (scenario S6):** the generated app requires a CSRF token, so it is stricter than the legacy one. This is a counted deviation.
- **Login checkbox:** the "Keep me signed in" checkbox is not rendered, recorded in the login template as `TODO(ui_text): remember_me_checkbox_not_rendered`.
- **Layout:** the page heading sits partly under the navbar. This is a styling gap in this release.

On this app, Provenant's behaviour harness reports scenarios S1–S4 equal, S5 partial and S6 stricter than the legacy.

## License and credit

The legacy application is © Hantsy Bai and licensed under the **GNU GPL v3**. The generated code is derived from it, so this whole repository is distributed under the same license (see [`LICENSE`](LICENSE)).

🙏 Many thanks to **Hantsy Bai** for publishing such a clean and complete Java EE 8 reference application.

Provenant itself, the compiler that produced `generated/`, is a separate product and is not part of this repository.

---

**To be continued:** larger real-world applications, the analysis console and upcoming releases. Stay tuned.
