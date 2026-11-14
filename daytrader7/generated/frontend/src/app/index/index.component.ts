import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-index',
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
            <h2>Overview</h2>
            <br>
            <blockquote>The Daytrader performance benchmark sample provides a suite of workloads for characterizing performance of Java EE Application Servers. The workloads consist of an end to end web application and a full set of primitives. The applications are a collection of Java classes, Java Servlets, JavaServer Pages, and Enterprise Java Beans built to open Java EE APIs. Together these provide versatile and portable test cases designed to measure aspects of scalability and performance.</blockquote>
            <br>
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <!-- TODO(ui_text): image_not_carried:src_not_proven -->
            <br>
            <b>Daytrader J2EE Components </b>
            <br>
            <b>Model-View-Controller architecture</b>
            <blockquote>
              <br>
              <h3>Daytrader</h3>
              DayTrader is an end-to-end benchmark and performance sample application. It provides a real world Java EE workload.
              <br>
              <br>
              DayTrader&#39;s new design spans Java EE 7, including the new WebSockets specification. Other Java EE features include JSPs, Servlets, EJBs, JPA, JDBC, JSF, JMS, MDBs, and transactions (synchronous and asynchronous/2-phase commit).
              <br>
              <br>
              <h3>Primitives</h3>
              The
              <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
              provide a set of workloads to individually test various components of a Java EE application Server. The primitives leverage the Daytrader application infrastructure to test specific Java EE components such as the servlet engine, JSP support, EJB Entitiy, Session and Message Driven beans, HTTp Session support and more.
            </blockquote>
          </div>
        </div>
      </div>
      <div id="footer"></div>
    </form>
  `,
})
export class IndexComponent {
  protected readonly form = this.fb.nonNullable.group({
  });


  constructor(private readonly fb: FormBuilder) {}
}
