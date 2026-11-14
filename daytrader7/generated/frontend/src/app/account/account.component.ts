import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AccountdataApi } from '../api/accountdata.api';
import { QuotedataApi } from '../api/quotedata.api';
import { TradeappApi } from '../api/tradeapp.api';
import { TradeappLogoutRequest, TradeappUpdateProfileRequest } from '../api/models';

@Component({
  selector: 'app-account',
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
            <!-- TODO(ui_text): conditional_rendering:expression_not_modelled -->
            <!-- TODO(widget): h:dataTable bound to accountdata.closedOrders not emitted: repeat_source_unproven -->
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <tbody>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <tbody>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <h3>Account Information</h3>
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.creationDate }}</span>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.lastLogin }}</span>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.accountID }}</span>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.loginCount }}</span>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.balance }}</span>
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.profileID }}</span>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.logoutCount }}</span>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <span>{{ displayValues.openBalance }}</span>
                  </tr>
                  <tr>
                    <td colspan="8"></td>
                  </tr>
                </tbody>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <tbody>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <h3>Total Orders: <!-- TODO(ui_text): text_expression_not_carried --></h3>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <button type="button" (click)="toggleShowAllRows()" [disabled]="submitting()">Show All Rows</button>
                  </tr>
                  <tr>
                    <td colspan="6">
                      <!-- TODO(widget): h:dataTable bound to accountdata.allOrders not emitted: repeat_source_unproven -->
                    </td>
                  </tr>
                  <tr>
                    <td colspan="6"></td>
                  </tr>
                </tbody>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <tbody>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <h3>Account Profile</h3>
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): label_not_extracted -->
                    <input id="userID" type="text" formControlName="userID" class="rounded" />
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): label_not_extracted -->
                    <input id="fullname" type="text" formControlName="fullname" class="rounded" readonly />
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(widget): h:inputSecret bound to password not emitted: widget_attributes_unsupported (redisplay) -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <td>
                      <!-- TODO(ui_text): label_not_extracted -->
                      <input id="address" type="text" formControlName="address" class="rounded" readonly />
                    </td>
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <br>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): label_not_extracted -->
                    <input id="cpassword" type="text" formControlName="cpassword" class="rounded" />
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): label_not_extracted -->
                    <input id="ccn" type="text" formControlName="ccn" class="rounded" />
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): anchor_not_carried:href_not_in_context -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): label_not_extracted -->
                    <input id="email" type="text" formControlName="email" class="rounded" />
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <button type="button" (click)="updateProfile()" [disabled]="submitting()">Update Profile</button>
                  </tr>
                  <tr>
                    <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                    <td colspan="5"></td>
                  </tr>
                </tbody>
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
export class AccountComponent implements OnInit {
  protected readonly submitting = signal(false);

  // TODO(display): values come from the legacy bean properties; not extracted yet
  protected readonly displayValues: { results: string; currentTime: string; creationDate: string; lastLogin: string; accountID: number; loginCount: number; balance: number; profileID: string; logoutCount: number; openBalance: number } = { results: '', currentTime: '', creationDate: '', lastLogin: '', accountID: 0, loginCount: 0, balance: 0, profileID: '', logoutCount: 0, openBalance: 0 };
  protected readonly form = this.fb.nonNullable.group({
    userID: ['', [Validators.required]],
    fullname: [''],
    address: [''],
    cpassword: ['', [Validators.required]],
    ccn: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(30)]],
    email: ['', [Validators.required]],
    symbols: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(40)]],
  });


  protected accountdata: { currentTime: string | null; creationDate: string | null; lastLogin: string | null; accountID: number | null; loginCount: number | null; balance: number | null; profileID: string | null; logoutCount: number | null; openBalance: number | null } = { currentTime: null, creationDate: null, lastLogin: null, accountID: null, loginCount: null, balance: null, profileID: null, logoutCount: null, openBalance: null };

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
    this.accountdataApi.getCreationDate().subscribe({
      next: (value) => { this.accountdata.creationDate = value;  },
      error: (error: unknown) => { console.error('accountdata.creationDate', error); },
    });
    this.accountdataApi.getLastLogin().subscribe({
      next: (value) => { this.accountdata.lastLogin = value;  },
      error: (error: unknown) => { console.error('accountdata.lastLogin', error); },
    });
    this.accountdataApi.getAccountID().subscribe({
      next: (value) => { this.accountdata.accountID = value;  },
      error: (error: unknown) => { console.error('accountdata.accountID', error); },
    });
    this.accountdataApi.getLoginCount().subscribe({
      next: (value) => { this.accountdata.loginCount = value;  },
      error: (error: unknown) => { console.error('accountdata.loginCount', error); },
    });
    this.accountdataApi.getBalance().subscribe({
      next: (value) => { this.accountdata.balance = value;  },
      error: (error: unknown) => { console.error('accountdata.balance', error); },
    });
    this.accountdataApi.getProfileID().subscribe({
      next: (value) => { this.accountdata.profileID = value;  },
      error: (error: unknown) => { console.error('accountdata.profileID', error); },
    });
    this.accountdataApi.getLogoutCount().subscribe({
      next: (value) => { this.accountdata.logoutCount = value;  },
      error: (error: unknown) => { console.error('accountdata.logoutCount', error); },
    });
    this.accountdataApi.getOpenBalance().subscribe({
      next: (value) => { this.accountdata.openBalance = value;  },
      error: (error: unknown) => { console.error('accountdata.openBalance', error); },
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
    // TODO(nav): every outcome of this action leads to /welcome; JSF navigates there only if the action body succeeds, which is not extracted yet, so no navigation is emitted (success path assumed, not proven; source: daytrader-ee7-web/src/main/webapp/account.xhtml)
    this.submitting.set(false);
  }

  protected toggleShowAllRows(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    this.accountdataApi.toggleShowAllRows().subscribe({
      next: () => {
        this.submitting.set(false);
      },
      error: (error: unknown) => {
        console.error('toggleShowAllRows', error);
        this.submitting.set(false);
      },
    });
  }

  protected updateProfile(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    // TODO(call): record_control_not_proven: tradeapp.updateProfile
    // business logic not yet extracted for updateProfile — see eir::uir's module docs
    console.warn("updateProfile: business logic not yet extracted — see eir::uir's module docs");
    // TODO(nav): every outcome of this action leads to /account; JSF navigates there only if the action body succeeds, which is not extracted yet, so no navigation is emitted (success path assumed, not proven; source: daytrader-ee7-web/src/main/webapp/account.xhtml)
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
