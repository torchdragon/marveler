package com.torchdragon.marveler;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.torchdragon.marveler.MarvelAPI.MarvelAPIAdapter;



public class ChannelSelectionActivity extends AppCompatActivity {
    private String mTag = "ChannelSelectionActivity";
    private String[] mAPIChannels;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    protected CharacterDetailFragment mCharacterDetailFragment;

    private MarvelAPIAdapter mMarvelAPIAdapter;
    public MarvelAPIAdapter getMarvelAPIAdapter() { return mMarvelAPIAdapter; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_selection);

        mAPIChannels = getResources().getStringArray(R.array.api_channels);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_container);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer_listview);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer_listviewitem, mAPIChannels));
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mMarvelAPIAdapter = new MarvelAPIAdapter();

        mCharacterDetailFragment = new CharacterDetailFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mCharacterDetailFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_channel_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
