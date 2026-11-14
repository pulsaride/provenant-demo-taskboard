import { Routes } from '@angular/router';
import { AccountComponent } from './account/account.component';
import { IndexComponent } from './index/index.component';
import { MarketSummaryComponent } from './market-summary/market-summary.component';
import { OrderComponent } from './order/order.component';
import { PingCdijsfComponent } from './ping-cdijsf/ping-cdijsf.component';
import { PingJsfComponent } from './ping-jsf/ping-jsf.component';
import { PortfolioComponent } from './portfolio/portfolio.component';
import { QuoteComponent } from './quote/quote.component';
import { TradehomeComponent } from './tradehome/tradehome.component';
import { WebPrmtvComponent } from './web-prmtv/web-prmtv.component';
import { WelcomeComponent } from './welcome/welcome.component';

export const routes: Routes = [
  { path: 'account', component: AccountComponent },
  { path: 'index', component: IndexComponent },
  { path: 'market-summary', component: MarketSummaryComponent },
  { path: 'order', component: OrderComponent },
  { path: 'ping-cdijsf', component: PingCdijsfComponent },
  { path: 'ping-jsf', component: PingJsfComponent },
  { path: 'portfolio', component: PortfolioComponent },
  { path: 'quote', component: QuoteComponent },
  { path: 'tradehome', component: TradehomeComponent },
  { path: 'web-prmtv', component: WebPrmtvComponent },
  { path: 'welcome', component: WelcomeComponent },
];
