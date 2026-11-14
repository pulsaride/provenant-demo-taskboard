import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AccountdataApi } from '../api/accountdata.api';
import { QuotedataApi } from '../api/quotedata.api';
import { TradeappApi } from '../api/tradeapp.api';
import { TradeappLogoutRequest } from '../api/models';

@Component({
  selector: 'app-tradehome',
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
            <!-- TODO(ui_text): html_element_not_carried:font -->
            <span>{{ displayValues.results }}</span>
            <span>{{ displayValues.currentTime }}</span>
            <h2>
              Welcome&#160;
              <span>{{ displayValues.profileID }}</span>
            </h2>
            <!-- TODO(ui_text): conditional_rendering:expression_not_modelled -->
            <!-- TODO(widget): h:dataTable bound to accountdata.closedOrders not emitted: repeat_source_unproven -->
            <table style="width: 100%; margin: 0 auto;">
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <h3>User Statistics</h3>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <span>{{ displayValues.accountID }}</span>
                <br>
                <span>{{ displayValues.creationDate }}</span>
                <br>
                <span>{{ displayValues.loginCount }}</span>
                <br>
                <span>{{ displayValues.sessionCreationDate }}</span>
                <br>
              </tr>
              <tr>
                <td></td>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <td></td>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <h3>Account Summary</h3>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <br>
                sum of cash/holdings
                <br>
                opening balance:
                <br>
                <hr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                &#36;&#160;
                <span>{{ displayValues.balance }}</span>
                <br>
                <span>{{ displayValues.numberHoldings }}</span>
                <br>
                &#36;&#160;
                <span>{{ displayValues.holdingsTotal }}</span>
                <br>
                &#36;&#160;
                <span>{{ displayValues.sumOfCashHoldings }}</span>
                <br>
                &#36;&#160;
                <span>{{ displayValues.openBalance }}</span>
                <br>
                <hr>
              </tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                <td></td>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                &#36;
                <b>
                  <span>{{ displayValues.gainHTML }}</span>
                  &#160;
                  <span>{{ displayValues.gainPercentHTML }}</span>
                </b>
              </tr>
            </table>
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
export class TradehomeComponent implements OnInit {
  protected readonly submitting = signal(false);

  // TODO(display): values come from the legacy bean properties; not extracted yet
  protected readonly displayValues: { results: string; currentTime: string; profileID: string; accountID: number; creationDate: string; loginCount: number; sessionCreationDate: string; balance: number; numberHoldings: number; holdingsTotal: number; sumOfCashHoldings: number; openBalance: number; gainHTML: string; gainPercentHTML: string } = { results: '', currentTime: '', profileID: '', accountID: 0, creationDate: '', loginCount: 0, sessionCreationDate: '', balance: 0, numberHoldings: 0, holdingsTotal: 0, sumOfCashHoldings: 0, openBalance: 0, gainHTML: '', gainPercentHTML: '' };
  protected readonly form = this.fb.nonNullable.group({
    symbols: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(40)]],
  });


  protected accountdata: { currentTime: string | null; profileID: string | null; accountID: number | null; creationDate: string | null; loginCount: number | null; sessionCreationDate: string | null; balance: number | null; numberHoldings: number | null; holdingsTotal: number | null; sumOfCashHoldings: number | null; openBalance: number | null; gainHTML: string | null; gainPercentHTML: string | null } = { currentTime: null, profileID: null, accountID: null, creationDate: null, loginCount: null, sessionCreationDate: null, balance: null, numberHoldings: null, holdingsTotal: null, sumOfCashHoldings: null, openBalance: null, gainHTML: null, gainPercentHTML: null };

  protected tradeapp: { results: string | null } = { results: null };

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly accountdataApi: AccountdataApi,
    private readonly quotedataApi: QuotedataApi,
    private readonly tradeappApi: TradeappApi,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  /** Loads what the page reads (the JSF page-load, again after a command). */
  private load(): void {
    this.tradeappApi.getResults().subscribe({
      next: (value) => { this.tradeapp.results = value;  },
      error: (error: unknown) => { console.error('tradeapp.results', error); },
    });
    this.accountdataApi.getCurrentTime().subscribe({
      next: (value) => { this.accountdata.currentTime = value;  },
      error: (error: unknown) => { console.error('accountdata.currentTime', error); },
    });
    this.accountdataApi.getProfileID().subscribe({
      next: (value) => { this.accountdata.profileID = value;  },
      error: (error: unknown) => { console.error('accountdata.profileID', error); },
    });
    this.accountdataApi.getAccountID().subscribe({
      next: (value) => { this.accountdata.accountID = value;  },
      error: (error: unknown) => { console.error('accountdata.accountID', error); },
    });
    this.accountdataApi.getCreationDate().subscribe({
      next: (value) => { this.accountdata.creationDate = value;  },
      error: (error: unknown) => { console.error('accountdata.creationDate', error); },
    });
    this.accountdataApi.getLoginCount().subscribe({
      next: (value) => { this.accountdata.loginCount = value;  },
      error: (error: unknown) => { console.error('accountdata.loginCount', error); },
    });
    this.accountdataApi.getSessionCreationDate().subscribe({
      next: (value) => { this.accountdata.sessionCreationDate = value;  },
      error: (error: unknown) => { console.error('accountdata.sessionCreationDate', error); },
    });
    this.accountdataApi.getBalance().subscribe({
      next: (value) => { this.accountdata.balance = value;  },
      error: (error: unknown) => { console.error('accountdata.balance', error); },
    });
    this.accountdataApi.getNumberHoldings().subscribe({
      next: (value) => { this.accountdata.numberHoldings = value;  },
      error: (error: unknown) => { console.error('accountdata.numberHoldings', error); },
    });
    this.accountdataApi.getHoldingsTotal().subscribe({
      next: (value) => { this.accountdata.holdingsTotal = value;  },
      error: (error: unknown) => { console.error('accountdata.holdingsTotal', error); },
    });
    this.accountdataApi.getSumOfCashHoldings().subscribe({
      next: (value) => { this.accountdata.sumOfCashHoldings = value;  },
      error: (error: unknown) => { console.error('accountdata.sumOfCashHoldings', error); },
    });
    this.accountdataApi.getOpenBalance().subscribe({
      next: (value) => { this.accountdata.openBalance = value;  },
      error: (error: unknown) => { console.error('accountdata.openBalance', error); },
    });
    this.accountdataApi.getGainHTML().subscribe({
      next: (value) => { this.accountdata.gainHTML = value;  },
      error: (error: unknown) => { console.error('accountdata.gainHTML', error); },
    });
    this.accountdataApi.getGainPercentHTML().subscribe({
      next: (value) => { this.accountdata.gainPercentHTML = value;  },
      error: (error: unknown) => { console.error('accountdata.gainPercentHTML', error); },
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
    // TODO(nav): every outcome of this action leads to /welcome; JSF navigates there only if the action body succeeds, which is not extracted yet, so no navigation is emitted (success path assumed, not proven; source: daytrader-ee7-web/src/main/webapp/tradehome.xhtml)
    this.submitting.set(false);
  }

  protected getQuotesBySymbols(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    const body: string = this.form.getRawValue().symbols;
    this.quotedataApi.getQuotesBySymbols(body).subscribe({
      next: () => {
        void this.router.navigateByUrl('/quote');
        this.submitting.set(false);
      },
      error: (error: unknown) => {
        console.error('getQuotesBySymbols', error);
        this.submitting.set(false);
      },
    });
  }
}
