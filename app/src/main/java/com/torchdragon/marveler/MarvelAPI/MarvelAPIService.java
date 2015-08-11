package com.torchdragon.marveler.MarvelAPI;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;


/**
 * Created by bryan_000 on 5/17/2015.
 */
public interface MarvelAPIService {
    @GET("/v1/public/characters")
    void listCharacters(
            @QueryMap Map<String, String> params,
            Callback<MarvelCharacterDataWrapper> response);
}
