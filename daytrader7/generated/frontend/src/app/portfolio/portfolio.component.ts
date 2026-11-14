import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AccountdataApi } from '../api/accountdata.api';
import { PortfolioApi } from '../api/portfolio.api';
import { QuotedataApi } from '../api/quotedata.api';
import { TradeappApi } from '../api/tradeapp.api';
import { TradeappLogoutRequest } from '../api/models';

@Component({
  selector: 'app-portfolio',
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
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <b>Portfolio </b>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <b>Number of Holdings: </b>
                <span>{{ displayValues.numberHoldings }}</span>
              </tr>
              <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
              <td colspan="6">
                <table style="font-size: smaller">
                  <tbody>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
      <!-- TODO(widget): h:dataTable bound to portfolio.holdingDatas not emitted: table_attributes_unsupported (binding, column:headerText, columnClasses, headerClass, rowClasses, width) -->
                  </tbody>
                </table>
                <table>
                  <tbody>
                    <tr>
                      <td style="float: right;">
                        <table class="table">
                          <tr>
                            <th class="tableHeader">Total purchase basis</th>
                            <th class="tableHeader">Total market value</th>
                            <th class="tableHeader">Total gain/loss</th>
                          </tr>
                          <tr class="tableOddRow">
                            <td class="tableColumn">
                              &#36;
                              <span>{{ displayValues.totalBasis }}</span>
                            </td>
                            <td class="tableColumn">
                              &#36;
                              <span>{{ displayValues.totalValue }}</span>
                            </td>
                            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                            &#36;
                            <span>{{ displayValues.totalGain }}</span>
                            <span>{{ displayValues.totalGainPercentHTML }}</span>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </td>
              <tr>
                <td colspan="6"></td>
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
export class PortfolioComponent implements OnInit {
  protected readonly submitting = signal(false);

  // TODO(display): values come from the legacy bean properties; not extracted yet
  protected readonly displayValues: { currentTime: string; numberHoldings: number; totalBasis: number; totalValue: number; totalGain: number; totalGainPercentHTML: string } = { currentTime: '', numberHoldings: 0, totalBasis: 0, totalValue: 0, totalGain: 0, totalGainPercentHTML: '' };
  protected readonly form = this.fb.nonNullable.group({
    symbols: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(40)]],
  });


  protected accountdata: { currentTime: string | null } = { currentTime: null };

  protected portfolio: { numberHoldings: number | null; totalBasis: number | null; totalValue: number | null; totalGain: number | null; totalGainPercentHTML: string | null } = { numberHoldings: null, totalBasis: null, totalValue: null, totalGain: null, totalGainPercentHTML: null };

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly accountdataApi: AccountdataApi,
    private readonly portfolioApi: PortfolioApi,
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
    this.portfolioApi.getNumberHoldings().subscribe({
      next: (value) => { this.portfolio.numberHoldings = value;  },
      error: (error: unknown) => { console.error('portfolio.numberHoldings', error); },
    });
    this.portfolioApi.getTotalBasis().subscribe({
      next: (value) => { this.portfolio.totalBasis = value;  },
      error: (error: unknown) => { console.error('portfolio.totalBasis', error); },
    });
    this.portfolioApi.getTotalValue().subscribe({
      next: (value) => { this.portfolio.totalValue = value;  },
      error: (error: unknown) => { console.error('portfolio.totalValue', error); },
    });
    this.portfolioApi.getTotalGain().subscribe({
      next: (value) => { this.portfolio.totalGain = value;  },
      error: (error: unknown) => { console.error('portfolio.totalGain', error); },
    });
    this.portfolioApi.getTotalGainPercentHTML().subscribe({
      next: (value) => { this.portfolio.totalGainPercentHTML = value;  },
      error: (error: unknown) => { console.error('portfolio.totalGainPercentHTML', error); },
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
    // TODO(nav): every outcome of this action leads to /welcome; JSF navigates there only if the action body succeeds, which is not extracted yet, so no navigation is emitted (success path assumed, not proven; source: daytrader-ee7-web/src/main/webapp/portfolio.xhtml)
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
