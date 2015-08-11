package com.torchdragon.marveler.MarvelAPI;

import java.util.List;

/**
 * Created by bryan_000 on 5/17/2015.
 */
public class MarvelCharacterDataContainer {
    public int offset;
    public int limit;
    public int total;
    public int count;
    public List<MarvelCharacter> results;
}
