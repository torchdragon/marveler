package com.torchdragon.marveler.MarvelAPI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by bryan_000 on 5/17/2015.
 */
public class MarvelAPIAdapter {

    protected static String mPrivateKey = "33de372afa1e66165040bc9825677e3a76e675b8";
    protected static String mPublicKey  = "6f1271d383437e4bf9306420b8be9b10";
    protected static String mSessionStarted = "";
    protected String mApiHash = "";
    protected RestAdapter mRestAdapter;
    protected MarvelAPIService mAPIService;
    protected Map<String, String> mParamBase;
    protected final int mLimit = 20;
    protected int mOffset = 5;

    public MarvelAPIAdapter() {
        this("http://gateway.marvel.com:80");
    }

    public MarvelAPIAdapter(String endPoint) {
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .build();

        mAPIService = mRestAdapter.create(MarvelAPIService.class);

        long sessionStart = new Date().getTime();
        mSessionStarted = String.valueOf(sessionStart);
        mApiHash = MarvelAPIAdapter.GenerateAPIHash(sessionStart);
        mParamBase = new HashMap<>();

        mParamBase.put("apikey", mPublicKey);
        mParamBase.put("hash", mApiHash);
        mParamBase.put("ts", mSessionStarted);
    }

    public static String GenerateAPIHash(long timestamp) {

        String beforeBytes = String.valueOf(timestamp) + mPrivateKey + mPublicKey;

        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte digested[] = digester.digest(beforeBytes.getBytes());

            StringBuffer MD5Hash = new StringBuffer();
            for(int i = 0; i < digested.length; i++) {
                String h = Integer.toHexString(0xFF & digested[i]);

                while(h.length() < 2) {
                    h = "0" + h;
                }

                MD5Hash.append(h);
            }

            return MD5Hash.toString();

        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        }
    }

    public void listCharacters(Callback<MarvelCharacterDataWrapper> callback) {
        Map<String, String> params = new HashMap<>(mParamBase);
        params.put("limit", String.valueOf(mLimit));
        params.put("offset", String.valueOf(mOffset * mLimit));
        mAPIService.listCharacters(params, callback);
    }
}
