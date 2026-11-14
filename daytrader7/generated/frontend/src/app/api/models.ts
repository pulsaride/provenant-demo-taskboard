// Shapes the REST endpoints exchange, as proven by the lowering (eir::TirDto).

export interface QuotedataBuyRequest {
  quantity: number | null;
  symbols: string | null;
}

export interface TradeappLoginRequest {
  password: string | null;
  userID: string | null;
}

export interface TradeappLogoutRequest {
  address: string | null;
  ccn: string | null;
  cpassword: string | null;
  email: string | null;
  fullname: string | null;
  password: string | null;
  userID: string | null;
}

export interface TradeappUpdateProfileRequest {
  address: string | null;
  ccn: string | null;
  cpassword: string | null;
  email: string | null;
  fullname: string | null;
  password: string | null;
  userID: string | null;
}
