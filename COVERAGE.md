# What Provenant migrates: coverage and roadmap

Provenant turns a Java EE application (JSF, EJB, CDI, JPA) into a **Spring Boot 3 backend and an Angular frontend**, and **keeps your existing database**: table names, schema and data stay as they are.

This page lists, per framework and pattern, what Provenant carries today and what comes next. Is something you use **missing, planned or not supported**? [Open a framework request](../../issues/new?template=framework-request.yml). Requests decide the order of the roadmap.

*Last updated: 2026-11-11.*

## How to read this page

| Mark | Meaning |
|---|---|
| ✅ **Proven** | Emitted, and the generated application has passed Provenant's release checks on a public application. Those checks cover build, tests, behaviour compared with the legacy, and byte-identical output on every run. |
| ☑️ **Supported** | Emitted and covered by automated tests, but not yet proven end to end on a public application. |
| 🔄 **In progress** | Being built for v1; partly available. |
| 📅 **Planned for v1** | Specified for the first release, not started. |
| ⏭️ **Later (v2+)** | Deliberately left out of the first release. |
| ❌ **Not supported yet** | No plan yet. [Tell us you need it](../../issues/new?template=framework-request.yml). |

**What happens to code Provenant cannot carry.** Provenant never guesses. If it cannot prove that a piece of code keeps its behaviour after rewriting, it does one of two things:
- it keeps the original code unchanged behind an adapter;
- it marks the place with a visible `TODO(kind: reason)` and counts it in the report.

So an unsupported item makes the migration less complete. It never makes it silently wrong.

## Output

- [x] ✅ Spring Boot **3.5** project (Maven), Java 21
- [ ] ⏭️ Spring Boot **4.1** on **Java 25** (Spring Framework 7, Hibernate 7, Jackson 3, Spring Security 7): **v1.1**, the first update after v1
- [x] ✅ Angular **22** frontend (standalone components), built into the same jar
- [x] ✅ Byte-identical output on every run of the same input
- [x] ✅ Layered (`web` / `service` / `repository` / `domain`) or **hexagonal** project layout
- [x] ☑️ Provenance for every generated unit: where it came from, and the evidence level behind it (Proven, Empirical, Retained, Isolated)
- [x] ☑️ Unprovable code isolated behind an adapter and counted, never rewritten by guess
- [x] ☑️ Dockerfile, actuator health endpoint and a build script in the generated project

## Application packaging and servers

- [x] ✅ Maven WAR project
- [x] ✅ `web.xml` security constraints, login configuration and roles
- [x] ☑️ `web.xml` data source, resolved into the Spring data-source URL
- [x] ☑️ Spring XML application contexts (`applicationContext.xml`) → `@Configuration`
- [x] ☑️ GlassFish, Payara, JBoss/WildFly, WebLogic, WebSphere and Liberty descriptors, read for their security settings
- [ ] 📅 `<env-entry>` → Spring properties
- [ ] ❌ EAR and multi-module `application.xml`
- [ ] ❌ Custom servlet listeners

## Business tier (EJB)

- [x] ✅ `@Stateless` session beans → `@Service` with transactions
- [x] ✅ `@EJB`, `@Resource`, `@PersistenceContext` injection
- [x] ☑️ `@Singleton` beans → Spring components
- [x] ☑️ `@Singleton @Startup` initialisers → run at startup in a transaction
- [x] ☑️ `@TransactionAttribute` → `@Transactional` with the same propagation
- [x] ☑️ JNDI lookups (kept as they are)
- [x] ☑️ `@Stateful` session beans → conversation-scoped beans
- [x] ☑️ Message-driven beans and JMS → `@JmsListener` on an embedded Artemis broker
- [ ] ⏭️ EJB timers and `@Schedule`, Quartz: **v2**
- [ ] ⏭️ Batch: Spring Batch 2/3 XML jobs and JSR-352: **v2**
- [ ] ⏭️ EJB 2 CMP entity beans
- [ ] ❌ EJB 2 session beans (home interfaces), `@Remote` views, bean-managed transactions
- [ ] ❌ Singleton concurrency control (`@Lock`, `@AccessTimeout`)
- [ ] ⏭️ Interceptors (`@Interceptors`, `@AroundInvoke`) and aspects (Spring AOP `@Aspect`, `<aop:config>`, AspectJ): **v2**, early

## CDI

- [x] ✅ `@ApplicationScoped`, `@SessionScoped`, `@RequestScoped`, `@Dependent`, `@Model`
- [x] ✅ `@ViewScoped` beans that hold no state across requests
- [x] ☑️ Producer methods → `@Bean`
- [x] ☑️ `@ConversationScoped` and `Conversation` → a generated conversation runtime shared by the backend and the Angular client
- [x] ☑️ `@ViewScoped` beans that hold state
- [x] ☑️ Faces Flows (`@FlowScoped`)
- [x] ☑️ Asynchronous CDI events (`fireAsync`): not delivered, counted in the report
- [ ] ❌ Synchronous CDI events (`@Observes`) and custom qualifiers

## Persistence

- [x] ✅ JPA entities and named queries → JPA on Hibernate 6
- [x] ✅ **The legacy database is kept**: the generated application validates against your schema and never alters it
- [x] ☑️ Annotations on fields or on getters, both carried
- [x] ☑️ EclipseLink and Hibernate 5 applications, with their provider-specific behaviour taken into account
- [x] ☑️ `persistence.xml` and `orm.xml` settings
- [x] ☑️ DeltaSpike Data repositories → Spring Data JPA
- [x] ☑️ Shipped test data (`import.sql`, JPA load scripts) copied byte for byte
- [x] ☑️ Legacy databases: Apache Derby, H2 (1.4 and 2.x), Oracle
- [ ] 🔄 iBatis SQL maps → repositories with the original SQL
- [ ] 📅 Hibernate `hbm.xml` mappings
- [ ] 🔄 PostgreSQL schema generation
- [ ] ⏭️ PL/SQL stored-procedure code translation
- [ ] ⏭️ MyBatis 3 mappers: **v2**
- [ ] ❌ Hibernate 5 Criteria API
- [ ] ⏭️ MySQL, DB2 and SQL Server as the kept legacy database: **v2**
- [ ] ⏭️ Plain JDBC and Spring `JdbcTemplate` DAOs: **v2**

## Web and UI (JSF, PrimeFaces, Struts)

**JSF and Facelets**
- [x] ✅ Facelets pages → Angular components, with forms, view parameters, view actions and links
- [x] ✅ Required-field validation → Angular validators
- [x] ☑️ Text, password, hidden, checkbox and select inputs, `p:calendar` / `p:datePicker`
- [x] ☑️ `ui:repeat`, resource-bundle labels
- [ ] ❌ Custom converters, `f:convertNumber`

**PrimeFaces**
- [ ] 📅 `p:dataTable`, `h:dataTable`, `p:dataList`, `p:dataGrid` → Angular table with pagination
- [ ] 📅 `p:ajax` and `f:ajax`
- [ ] 📅 `p:dialog`, `p:confirmDialog` and the Dialog Framework
- [ ] 📅 `p:wizard`, `p:autoComplete`, menus, layout panels, `p:growl`
- [ ] ⏭️ Table sorting, filtering, selection, in-cell editing, row expansion, frozen columns
- [ ] ⏭️ `LazyDataModel`, `p:tree`, `p:schedule`, `p:fileUpload`, `p:chart`, `p:gmap`, `p:dataScroller`, `p:carousel`, `p:ring`, `p:overlayPanel`, `p:sidebar`

**Other web frameworks**
- [ ] 🔄 OmniFaces (the runtime is checked class by class from its bytecode)
- [x] ☑️ Struts 1 and Struts 2 actions and validation → Spring REST endpoints
- [ ] ⏭️ JSP pages and Struts tags → Angular
- [ ] ⏭️ Spring MVC with JSP views

## Web services

- [x] ☑️ JAX-WS clients (Apache CXF, kept)
- [x] ☑️ JAX-RS resources → JAX-RS on Jersey inside Spring Boot
- [ ] ❌ JAX-WS server endpoints (`@WebService`)

## Security

- [x] ✅ Form login with a custom identity store → Spring Security session login and an Angular login page
- [x] ☑️ `@RolesAllowed` and `@DeclareRoles` → endpoint roles
- [x] ☑️ Applications with **no login**: emitted with no login, as the legacy has none. That must first be proven from the code, configuration and libraries.
- [x] ☑️ Endpoints declared public stay public only when that is proven
- [x] ☑️ Spring Security 3/4 in the legacy → current Spring Security
- [ ] ❌ JAAS, container realms and BASIC authentication (refused, never weakened)
- [x] ☑️ Login token rows written to the database exactly as the legacy writes them
- [ ] ❌ Remember-me cookies and automatic re-login

## Third-party libraries

Libraries are **migrated** to a current release, or **kept** as jars pinned by checksum.

- [x] ☑️ Jackson 1 → Jackson 2
- [x] ☑️ Ehcache 2 → Ehcache 3 (JCache)
- [x] ☑️ JasperReports 6 → 7
- [x] ☑️ Kept: Dozer, jsoup, ZXing, Xerces, Lombok and others
- [ ] ❌ Apache Camel, Apache Shiro, OpenJPA, Coherence / Hazelcast / Infinispan: on demand
- [ ] ❌ Anything else: [request it](../../issues/new?template=framework-request.yml)

## Applications used as public proof

| Application | Stack | Status |
|---|---|---|
| [Taskboard](https://github.com/hantsy/javaee8-jsf-sample) (this repository) | JSF 2.3, CDI, EJB, JPA, Java EE Security, GlassFish, Derby | ✅ Passes the release checks |
| [Eclipse Cargo Tracker](https://github.com/eclipse-ee4j/cargotracker) | Jakarta EE, JSF, JAX-RS, JMS, EclipseLink, H2 | 🔄 In progress |
| clublist (a public JSF sample) | JSF, PrimeFaces, CDI conversation, Hibernate, H2 | 🔄 In progress |

Provenant is also exercised on non-public enterprise code. Those results are not published.

## Your application is not covered?

[Open a framework request](../../issues/new?template=framework-request.yml) with:
- the framework or library;
- its version;
- roughly how much of your application uses it.

You can also ask for an assessment of your application there. Do not post confidential code.
