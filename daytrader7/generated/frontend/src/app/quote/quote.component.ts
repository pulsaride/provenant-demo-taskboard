import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AccountdataApi } from '../api/accountdata.api';
import { QuotedataApi } from '../api/quotedata.api';
import { TradeappApi } from '../api/tradeapp.api';
import { TradeappLogoutRequest } from '../api/models';

@Component({
  selector: 'app-quote',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <form [formGroup]="form" data-legacy-body>
      <!-- TODO(call): enter_default_command_not_proven: several commands, Enter runs none -->
      <!-- TODO(ui_text): head_not_carried:title -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <div id="page-wrap">
        <div id="header">
          <!-- TODO(ui_text): image_not_carried:src_not_proven -->
          <div id="nav2">
            <ul id="list-nav2">
              <li>
                <a [routerLink]="['/tradehome']">Home</a>
              </li>
              <li>
                <a [routerLink]="['/account']">Account</a>
              </li>
              <li>
                <a [routerLink]="['/market-summary']">Market</a>
              </li>
              <li>
                <a [routerLink]="['/portfolio']">Portfolio</a>
              </li>
              <li>
                <a [routerLink]="['/quote']">Quotes/Trade</a>
              </li>
              <li>
                <button type="button" (click)="logout()" [disabled]="submitting()">Logoff</button>
              </li>
            </ul>
          </div>
        </div>
        <div id="content">
          <div id="contentContainer">
            <span>{{ displayValues.currentTime }}</span>
            <!-- TODO(ui_text): conditional_rendering:expression_not_modelled -->
            <!-- TODO(widget): h:dataTable bound to accountdata.closedOrders not emitted: repeat_source_unproven -->
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <tbody>
              <tr>
                <td>
                  <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  <tbody>
                    <tr><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><b>Quotes</b></tr>
                    <tr>
                      <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                      <!-- TODO(widget): h:dataTable bound to quotedata.quotes not emitted: repeat_source_unproven -->
                    </tr>
                  </tbody>
                </td>
              </tr>
            </tbody>
            <table style="width: 100%;">
              <tbody>
                <tr>
                  <td colspan="2">
                    <hr>
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <tbody>
                      <tr>
                        <td></td>
                        <td style="vertical-align: bottom; text-align: center">
                          <button type="button" (click)="getQuotesBySymbols()" [disabled]="submitting()">quotes</button>
                          <!-- TODO(ui_text): label_not_extracted -->
                          <input id="symbols" type="text" formControlName="symbols" class="rounded" />
                        </td>
                      </tr>
                    </tbody>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div id="footer"></div>
    </form>
  `,
})
export class QuoteComponent implements OnInit {
  protected readonly submitting = signal(false);

  // TODO(display): values come from the legacy bean properties; not extracted yet
  protected readonly displayValues: { currentTime: string } = { currentTime: '' };
  protected readonly form = this.fb.nonNullable.group({
    symbols: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(40)]],
  });


  protected accountdata: { currentTime: string | null } = { currentTime: null };

  constructor(
    private readonly fb: FormBuilder,
    private readonly accountdataApi: AccountdataApi,
    private readonly quotedataApi: QuotedataApi,
    private readonly tradeappApi: TradeappApi,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  /** Loads what the page reads (the JSF page-load, again after a command). */
  private load(): void {
    this.accountdataApi.getCurrentTime().subscribe({
      next: (value) => { this.accountdata.currentTime = value;  },
      error: (error: unknown) => { console.error('accountdata.currentTime', error); },
    });
  }

  protected logout(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    // TODO(call): record_control_not_proven: tradeapp.logout
    // business logic not yet extracted for logout — see eir::uir's module docs
    console.warn("logout: business logic not yet extracted — see eir::uir's module docs");
    // TODO(nav): every outcome of this action leads to /welcome; JSF navigates there only if the action body succeeds, which is not extracted yet, so no navigation is emitted (success path assumed, not proven; source: daytrader-ee7-web/src/main/webapp/quote.xhtml)
    this.submitting.set(false);
  }

  protected getQuotesBySymbols(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    // TODO(nav): outcome "quotes" unresolved: target_view_not_captured (source: daytrader-ee7-web/src/main/webapp/quote.xhtml)
    const body: string = this.form.getRawValue().symbols;
    this.quotedataApi.getQuotesBySymbols(body).subscribe({
      next: () => {
        this.submitting.set(false);
      },
      error: (error: unknown) => {
        console.error('getQuotesBySymbols', error);
        this.submitting.set(false);
      },
    });
  }
}
