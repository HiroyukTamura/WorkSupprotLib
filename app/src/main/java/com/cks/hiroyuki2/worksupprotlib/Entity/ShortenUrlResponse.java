package com.cks.hiroyuki2.worksupprotlib.Entity;

/**
 * RetrofitでshortenUrlのレスポンスをGsonでパースする際に用いる
 */
public class ShortenUrlResponse {
    private String kind;
    private String id;
    private String longUrl;

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
