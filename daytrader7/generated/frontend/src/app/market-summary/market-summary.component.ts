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
  selector: 'app-market-summary',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <form [formGroup]="form" data-legacy-body>
      <!-- TODO(call): enter_default_command_not_proven: several commands, Enter runs none -->
      <!-- TODO(ui_text): head_not_carried:title -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <!-- TODO(ui_text): html_element_not_carried:script -->
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
            <table style="width: 100%; margin: auto;">
              <tbody>
                <tr>
                  <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  <h3>Market Summary</h3>
                  <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  <div id="date"></div>
                </tr>
              </tbody>
            </table>
            <table style="width: 100%; margin: auto">
              <tr>
                <td style="vertical-align: top">
                  <table class="table" style="width: 200px; float: right;">
                    <tbody>
                      <tr class="tableOddRow">
                        <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                        <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                        <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                        <div id="tsia"></div>
                      </tr>
                      <tr class="tableOddRow">
                        <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                        <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                        <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                        <div id="volume"></div>
                      </tr>
                    </tbody>
                  </table>
                </td>
                <td>
                  <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  <tbody>
                    <tr class="tableOddRow"><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <td class="tableColumn" style="text-align: center;"><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></td>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change1_stock"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change1_price"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change1_change" style="color: CC0000; display: inline"></div>
                    </td>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change2_stock"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change2_price"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change2_change" style="color: CC0000; display: inline"></div>
                    </td>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change3_stock"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change3_price"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change3_change" style="color: CC0000; display: inline"></div>
                    </td>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change4_stock"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change4_price"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change4_change" style="color: CC0000; display: inline"></div>
                    </td>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change5_stock"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change5_price"></div>
                    </td>
                    <td class="tableColumn" style="text-align: center;">
                      <div id="change5_change" style="color: CC0000; display: inline"></div>
                    </td>
                  </tbody>
                </td>
              </tr>
            </table>
            <table style="margin: auto;">
              <tr>
                <td>
                  <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  <tbody>
                    <tr class="tableOddRow"><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <td class="tableColumn"><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></td>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="gainer1_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer1_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer1_change" style="color: #009900; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="gainer2_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer2_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer2_change" style="color: #009900; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="gainer3_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer3_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer3_change" style="color: #009900; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="gainer4_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer4_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer4_change" style="color: #009900; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="gainer5_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer5_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="gainer5_change" style="color: #009900; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                  </tbody>
                </td>
                <td>
                  <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  <tbody>
                    <tr class="tableOddRow"><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <td class="tableColumn"><!-- TODO(ui_text): anchor_not_carried:href_not_in_context --></td>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="loser1_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser1_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser1_change" style="color: #CC0000; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="loser2_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser2_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser2_change" style="color: #CC0000; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="loser3_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser3_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser3_change" style="color: #CC0000; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="loser4_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser4_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser4_change" style="color: #CC0000; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                    <tr class="tableOddRow">
                      <td class="tableColumn">
                        <div id="loser5_stock"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser5_price"></div>
                      </td>
                      <td class="tableColumn">
                        <div id="loser5_change" style="color: #CC0000; display: inline;"></div>
                        <!-- TODO(ui_text): image_not_carried:src_not_proven -->
                      </td>
                    </tr>
                  </tbody>
                </td>
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
export class MarketSummaryComponent implements OnInit {
  protected readonly submitting = signal(false);

  // TODO(display): values come from the legacy bean properties; not extracted yet
  protected readonly displayValues: { currentTime: string } = { currentTime: '' };
  protected readonly form = this.fb.nonNullable.group({
    symbols: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(40)]],
  });


  protected accountdata: { currentTime: string | null } = { currentTime: null };

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
    // TODO(nav): every outcome of this action leads to /welcome; JSF navigates there only if the action body succeeds, which is not extracted yet, so no navigation is emitted (success path assumed, not proven; source: daytrader-ee7-web/src/main/webapp/marketSummary.xhtml)
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
