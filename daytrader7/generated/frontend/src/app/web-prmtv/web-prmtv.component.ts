import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-web-prmtv',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" data-legacy-body>
      <!-- TODO(ui_text): head_not_carried:title -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <div id="page-wrap">
        <div id="header">
          <!-- TODO(ui_text): image_not_carried:src_not_proven -->
          <div id="nav">
            <ul id="list-nav">
              <li><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></li>
              <li><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></li>
              <li><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></li>
              <li><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></li>
              <li><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></li>
            </ul>
          </div>
        </div>
        <div id="content">
          <div id="contentContainer">
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <tbody>
              <tr>
                <td>
                  <h3>Web Primitive Tests</h3>
                </td>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
              </tr>
            </tbody>
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <tbody>
              <tr><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><b><!-- TODO(ui_text): html_element_not_carried:font -->Primitive Test Suite </b></tr>
              <tr>
                <td>
                  <p>The DayTrader performance benchmark sample provides a suite of web primitives. These primitives singularly test key operations in the enterprise Java programming model. Links to each of the web primitive tests are provided below along with a description of each operation.</p>
                  <p> Note that some primitives below can have their main operations repeated. These operations are marked with a red <!-- TODO(ui_text): html_element_not_carried:font -->* . In order to adjust the repetition, change the primitive iteration value in the Trade configuration page. <!-- TODO(ui_text): html_element_not_carried:font --></p>
                </td>
              </tr>
            </tbody>
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <tbody>
              <tr><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><b><!-- TODO(ui_text): html_element_not_carried:font -->Web Container ping suite </b></tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingHtml is the most basic operation providing access to a simple &quot;Hello World&quot; page of static HTML. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->Invoke Garbage Collection on AppServer. Reports heap statistics after the GC has completed. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet tests fundamental dynamic HTML creation through server side servlet processing. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServletCDI tests various simple CDI invocations.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServletCDIBeanManagerViaJNDI tests getting the BeanManager with JNDI.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServletCDIBeanManagerViaCDICurrent tests getting the BeanManager with SPI.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServletWriter extends PingServlet by using a PrintWriter for formatted output vs. the output stream used by PingServlet. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2Include tests response inclusion. Servlet 1 includes the response of Servlet 2. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2Servlet tests request dispatching. Servlet 1, the controller, creates a new JavaBean object forwards the request with the JavaBean added to Servlet 2. Servlet 2 obtains access to the JavaBean through the Servlet request object and provides dynamic HTML output based on the JavaBean data. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingJSP tests a direct call to JavaServer Page providing server-side dynamic HTML through JSP scripting. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingJSPEL tests a direct call to JavaServer Page providing server-side dynamic HTML through JSP scripting and the usage of the new JSP 2.0 Expression Language. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2JSP tests a commonly used design pattern, where a request is issued to servlet providing server side control processing. The servlet creates a JavaBean object with dynamically set attributes and forwards the bean to the JSP through a RequestDispatcher The JSP obtains access to the JavaBean and provides formatted display with dynamic HTML output based on the JavaBean data. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2PDF tests a call to a servlet which displays the contents of a PDF Document (~1 MB in file size). </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2DB tests a call to a servlet which makes a JDBC connection to the database. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingJSF tests a call to a JSF Facelet which performs a lookup of quotes. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:attribute_not_listed -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingCDIJSF tests a MangedBean called from a jsf page.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font --> PingHTTPSession1 - <b>SessionID</b> tests fundamental HTTP session function by creating a unique session ID for each individual user. The ID is stored in the users session and is accessed and displayed on each user request.  </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font --> PingHTTPSession2 <b>session create/destroy </b> further extends the previous test by invalidating the HTTP Session on every 5th user access. This results in testing HTTPSession create and destroy  </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font --> PingHTTPSession3 <b>large session object </b> tests the servers ability to manage and persist large HTTPSession data objects. The servlet creates a large custom java object. The class contains multiple data fields and results in 2048 bytes of data. This large session object is retrieved and stored to the session on each user request.  </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingJDBCRead tests fundamental servlet to JDBC access to a database performing a single-row read using a prepared SQL statment. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingJDBCRead tests fundamental servlet to JDBC access to a database performing a single-row write using a prepared SQL statment. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2JNDI tests the fundamental J2EE operation of a servlet allocating a JNDI context and performing a JNDI lookup of a JDBC DataSource. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingUpgradeServlet tests a simple UpgradeHandler request. JMeter is needed for testing.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingWebSocketTextSync tests a simple synchronous WebSocket with text.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingWebSocketTextAsync tests a simple asynchronous WebSocket with text.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingWebSocketBinary tests a simple WebSocket with binary data.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingWebSocketJson tests a WebSocket with a JSON Decoder and Encoder.</td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingManagedThread tests a ManagedThreadFactory inside an asynchronous servlet. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingManagedExecutor tests the ManagedExecutorService inside an asynchronous servlet. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingJSONP tests generating and parsing JSON. </td>
              </tr>
              <tr><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><b><!-- TODO(ui_text): html_element_not_carried:font -->EJB 3 Container ping suite </b></tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2Session tests key function of a servlet call to a remote stateless Session EJB. The Session EJB performs a simple calculation and returns the result </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2SessionLocal tests key function of a servlet call to a local stateless Session EJB. The Session EJB performs a simple calculation and returns the result </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This tests the full servlet to Session EJB to Entity EJB path to retrieve a single row from the database. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This tests the full servlet to Session EJB to Entity EJB path to retrieve a single row from the database and displays the output in a JSP. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This test extends the previous EJB Entity test by calling a Session EJB which uses a finder method on the Entity that returns a collection of Entity objects. Each object is displayed by the servlet. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This test drives an Entity EJB to get another Entity EJB&#39;s data through an EJB 3.0 CMR One to One relationship. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This test drives an Entity EJB to get another Entity EJB&#39;s data through an EJB 3.0 CMR One to Many relationship. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This tests the full servlet to Session EJB to JDBC path to retrieve a single row from the database. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->This test extends the previous JDBC test by calling a Session EJB to JDBC path which returns multiple rows from the database. </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font --> PingServlet2MDBQueue drives messages to a Queue based Message Driven EJB (MDB). Each request to the servlet posts a message to the Queue. The MDB receives the message asynchronously and prints message delivery statistics on each 100th message. <!-- TODO(ui_text): html_element_not_carried:font --><b>Note:</b> Not intended for performance testing.  </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font --> PingServlet2MDBTopic drives messages to a Topic based Publish/Subscribe Message Driven EJB (MDB). Each request to the servlet posts a message to the Topic. The TradeStreamMDB receives the message asynchronously and prints message delivery statistics on each 100th message. Other subscribers to the Topic will also receive the messages. <!-- TODO(ui_text): html_element_not_carried:font --><b>Note:</b> Not intended for performance testing.  </td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <!-- TODO(ui_text): html_element_not_carried:font -->
                *
                <td><!-- TODO(ui_text): html_element_not_carried:font -->PingServlet2TwoPhase drives a Session EJB which invokes an Entity EJB with findByPrimaryKey (DB Access) followed by posting a message to an MDB through a JMS Queue (Message access). These operations are wrapped in a global 2-phase transaction and commit. </td>
              </tr>
            </tbody>
          </div>
        </div>
      </div>
      <div id="footer"></div>
    </form>
  `,
})
export class WebPrmtvComponent {
  protected readonly form = this.fb.nonNullable.group({
  });


  constructor(private readonly fb: FormBuilder) {}
}
