package com.torchdragon.marveler.MarvelAPI;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;

/**
 * Created by bryan_000 on 5/17/2015.
 */

public class MarvelAPIAdapterTestCase extends TestCase {

    @Captor
    private ArgumentCaptor<Callback<MarvelCharacterDataWrapper>> characterListCallBack;

    protected String MockEndPoint = "mockAPIEndpoint";

    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public void testGenerateAPIHash() {
        long timestamp = 0;
        String expectedHash = "6040b4e44116c1ef5498d7e83344b659";
        ;

        String actualHash = MarvelAPIAdapter.GenerateAPIHash(timestamp);

        assertTrue(String.format("\nActual Hash: %1$s does not match\nExpected Hash: %2$s", actualHash, expectedHash), actualHash.equals(expectedHash));
    }
}
