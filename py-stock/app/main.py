import json
import yfinance as yf
import redis
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI(title="Stock Info API")

redis_client = redis.Redis(
    host="redis",
    port=6379,
    decode_responses=True
)

CACHE_TTL = 1200  # 20 minutos

@app.get("/stock/{ticker}")
def get_stock_info(ticker: str):
    ticker = ticker.upper()
    cache_key = f"stock:{ticker}"
    print(f"Fetching data for ticker: {ticker}")

    # 1️⃣ Buscar en Redis
    cached = redis_client.get(cache_key)
    if cached:
        print(f"Cache hit for ticker: {ticker}")
        return {
            "ticker": ticker,
            "cached": True,
            "data": json.loads(cached)
        }

    # 2️⃣ Consultar yfinance
    try:
        print(f"Cache miss for ticker: {ticker}. Fetching from yfinance.")
        stock = yf.Ticker(ticker)
        print(f"yfinance response for {ticker}")
        info = stock.info

        if not info or "symbol" not in info:
            raise HTTPException(status_code=404, detail="Ticker no encontrado")

        data = {
            "symbol": info.get("symbol"),
            "name": info.get("shortName"),
            "price": info.get("regularMarketPrice"),
            "currency": info.get("currency"),
            "marketCap": info.get("marketCap"),
            "sector": info.get("sector"),
        }

        # 3️⃣ Guardar en Redis con TTL
        redis_client.setex(
            cache_key,
            CACHE_TTL,
            json.dumps(data)
        )
        print(f"Data cached for ticker: {ticker}")

        return {
            "ticker": ticker,
            "cached": False,
            "data": data
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


class RequestStocks(BaseModel):
    stocks: list[str]

class RequestHistorical(BaseModel):
    tickers: list[str]
    start: str
    end: str

@app.post("/stocks/")
def get_multiple_stocks(tickers: RequestStocks):
    results = []
    try:
        for ticker in tickers.stocks:
            try:
                result = get_stock_info(ticker)
                results.append(result)
            except HTTPException as e:
                results.append({
                    "ticker": ticker.upper(),
                    "error": e.detail
                })
        return results
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

import pandas as pd

@app.post("/historical/")
def get_historical(request: RequestHistorical):
    try:
        tickers_str = ",".join(sorted(request.tickers))
        cache_key = f"historical:{tickers_str}:{request.start}:{request.end}"
        
        cached = redis_client.get(cache_key)
        if cached:
            print(f"Cache hit for historical data: {cache_key}")
            return json.loads(cached)

        print(f"Fetching historical data for {request.tickers} from {request.start} to {request.end}")
        data = yf.download(request.tickers, start=request.start, end=request.end, auto_adjust=True)
        
        if data.empty:
            return {}

        # The 'Close' column contains the closing prices.
        # If auto_adjust=True, yf.download returns 'Close' which is adjusted.
        if "Close" not in data.columns:
            return {}
            
        close_prices = data['Close']
        close_prices.index = close_prices.index.strftime('%Y-%m-%d')
        
        # When downloading multiple tickers, close_prices is a DataFrame
        # When downloading a single ticker, close_prices is a Series
        if isinstance(close_prices, pd.Series):
            # It's a single ticker
            close_prices = close_prices.ffill().dropna()
            res = {request.tickers[0]: close_prices.to_dict()}
            redis_client.setex(cache_key, CACHE_TTL * 3, json.dumps(res)) # Cache for 1 hour
            return res
        else:
            # It's a DataFrame (multiple tickers)
            close_prices = close_prices.ffill().dropna(how='all')
            # dropna for each column might be tricky because to_dict returns everything.
            # to_dict will still return NaN if not handled, so we just convert it safely
            res = {}
            for col in close_prices.columns:
                s = close_prices[col].dropna()
                res[str(col[1] if isinstance(col, tuple) else col)] = s.to_dict()
            
            redis_client.setex(cache_key, CACHE_TTL * 3, json.dumps(res)) # Cache for 1 hour
            return res
            
    except Exception as e:
        print(f"Error fetching historical data: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/USD_MXN")
def get_dolar_value():
    try:
        print("Fetching USD to MXN exchange rate")
        cache_key = "USD_MXN_rate"
        cached = redis_client.get(cache_key)
        print(f"Cached USD to MXN rate: {cached}")
        if cached:
            print("Cache hit for USD to MXN rate")
            return {"USD_MXN": cached}

        change = yf.Ticker("USDMXN=X")
        last_price = change.info.get("open")
        print(f"Fetched USD to MXN rate: {last_price}")
        redis_client.setex(
            cache_key,
            CACHE_TTL,
            str(last_price)
        )
        print("Data cached for USD to MXN rate")
        return {"USD_MXN": float(last_price)}
    except Exception as e:
        print(f"Error fetching USD to MXN rate: {e}")
        raise HTTPException(status_code=500, detail=str(e))