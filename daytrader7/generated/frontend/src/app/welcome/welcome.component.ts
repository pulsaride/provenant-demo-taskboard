import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';
import { TradeappApi } from '../api/tradeapp.api';
import { TradeappLoginRequest } from '../api/models';

@Component({
  selector: 'app-welcome',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="login()" data-legacy-body>
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
          <div id="loginContainer">
            <!-- TODO(ui_text): html_element_not_carried:font -->
            <span>{{ displayValues.results }}</span>
            <!-- TODO(ui_text): conditional_rendering:expression_not_modelled -->
            <h2>Log in to DayTrader</h2>
            <br>
            Username
            <br>
            <!-- TODO(ui_text): label_not_extracted -->
            <input id="userID" type="text" formControlName="userID" class="rounded" />
            <!-- TODO(widget-attributes): attributes bound to userID not reproduced: child:f:validator, size -->
            <!-- TODO(ui_text): core_tag_not_modelled:validator -->
            <br>
            <br>
            Password
            <br>
            <!-- TODO(widget): h:inputSecret bound to password not emitted: widget_attributes_unsupported (redisplay) -->
            <br>
            <br>
            <button type="submit" [disabled]="submitting()">Log in</button>
            <br>
            <br>
            Don&#39;t have an account?
            <br>
            <!-- TODO(ui_text): command_text_not_carried -->
          </div>
        </div>
      </div>
      <div id="footer"></div>
    </form>
  `,
})
export class WelcomeComponent implements OnInit {
  protected readonly submitting = signal(false);

  // TODO(display): values come from the legacy bean properties; not extracted yet
  protected readonly displayValues: { results: string } = { results: '' };
  protected readonly form = this.fb.nonNullable.group({
    userID: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(10)]],
  });


  protected tradeapp: { results: string | null } = { results: null };

  constructor(
    private readonly fb: FormBuilder,
    private readonly tradeappApi: TradeappApi,
  ) {}
  // TODO(nav): outcome "register.faces" unresolved: target_view_not_captured (source: daytrader-ee7-web/src/main/webapp/welcome.xhtml@3135)

  ngOnInit(): void {
    this.load();
  }

  /** Loads what the page reads (the JSF page-load, again after a command). */
  private load(): void {
    this.tradeappApi.getResults().subscribe({
      next: (value) => { this.tradeapp.results = value;  },
      error: (error: unknown) => { console.error('tradeapp.results', error); },
    });
  }

  protected login(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    // TODO(call): record_control_not_proven: tradeapp.login
    // business logic not yet extracted for login — see eir::uir's module docs
    console.warn("login: business logic not yet extracted — see eir::uir's module docs");
    // TODO(nav): outcome "Ready to Trade" -> /tradehome; which outcome applies depends on business logic not yet extracted (source: daytrader-ee7-web/src/main/webapp/welcome.xhtml)
    // TODO(nav): outcome "welcome" -> /welcome; which outcome applies depends on business logic not yet extracted (source: daytrader-ee7-web/src/main/webapp/welcome.xhtml)
    this.submitting.set(false);
  }
}
