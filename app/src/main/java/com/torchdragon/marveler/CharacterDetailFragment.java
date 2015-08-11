package com.torchdragon.marveler;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.torchdragon.marveler.MarvelAPI.MarvelCharacter;
import com.torchdragon.marveler.MarvelAPI.MarvelCharacterDataWrapper;
import com.torchdragon.marveler.MarvelAPI.MarvelThumbnail;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class CharacterDetailFragment extends Fragment {

    protected String mTag = "CharacterDetailFragment";
    protected ChannelSelectionActivity mContext;
    protected Spinner mCharacterSelectionSpinner;
    protected TextView mCharacterNameTextView;
    protected ImageView mCharacterPortraitImageView;
    protected Button mRefreshCharacterListButton;
    protected boolean mIsRefreshCharacterListButtonEnabled;
    public boolean getIsRefreshCharacterListButtonEnabled() { return mIsRefreshCharacterListButtonEnabled; }
    public void setIsRefreshCharacterListButtonEnabled(boolean enabled)
    {
        mIsRefreshCharacterListButtonEnabled = enabled;
        mRefreshCharacterListButton.setEnabled(mIsRefreshCharacterListButtonEnabled);
    }

    protected retrofit.Callback<MarvelCharacterDataWrapper> mMarvelCharacterListCallback;
    protected List<MarvelCharacter> mAllCharacters;
    public List<MarvelCharacter> getAllCharacters() { return mAllCharacters; }
    public void setAllCharacters(List<MarvelCharacter> characters) {
        mAllCharacters = characters;

        if(mCharacterSelectionSpinner != null) {
            loadSpinnerWithCharacters();
        }
    }

    protected AdapterView.OnItemSelectedListener mCharacterSpinnerSelectedListener;

    protected Picasso mPicassoInstance;

    protected MarvelCharacter mSelectedCharacter;
    public void setSelectedCharacter(MarvelCharacter character) {
        mSelectedCharacter = character;
        mCharacterNameTextView.setText(character.name);
    }

    private void loadSpinnerWithCharacters() {

        ArrayList<String> names = new ArrayList<>();
        for(MarvelCharacter character : mAllCharacters) {
            names.add(character.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, names);
        mCharacterSelectionSpinner.setOnItemSelectedListener(mCharacterSpinnerSelectedListener);
        mCharacterSelectionSpinner.setAdapter(adapter);
    }

    public CharacterDetailFragment() {
        createSpinnerListeners();
    }

    protected void createSpinnerListeners() {
        mCharacterSpinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedCharacter(mAllCharacters.get(i));
                mPicassoInstance.load(R.drawable.picasso_loading_ring)
                        .into(mCharacterPortraitImageView);

                if(mSelectedCharacter.thumbnail != null) {
                    mPicassoInstance
                            .load(mSelectedCharacter.thumbnail.path + "." + mSelectedCharacter.thumbnail.extension)
                            .placeholder(R.drawable.picasso_loading_ring)
                            .error(R.drawable.picasso_error_ring)
                            .into(mCharacterPortraitImageView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = (ChannelSelectionActivity)activity;
        mPicassoInstance = new Picasso.Builder(mContext)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e("Picasso:CharacterDetail", "Picasso Error in Character Detail: " + exception.getMessage());
                    }
                })
                .build();
    }

    @Override
    public void onDetach() {
        mContext = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_character_detail, container, false);

        mCharacterSelectionSpinner = (Spinner) v.findViewById(R.id.character_selection_spinner);
        mCharacterNameTextView = (TextView) v.findViewById(R.id.character_name_textview);
        mCharacterPortraitImageView = (ImageView) v.findViewById(R.id.character_portrait_imageview);
        mRefreshCharacterListButton = (Button) v.findViewById(R.id.character_spinner_refresh_button);
        mRefreshCharacterListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshCharacterList();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RefreshCharacterList();
    }

    public void RefreshCharacterList() {
        setIsRefreshCharacterListButtonEnabled(false);
        mContext.getMarvelAPIAdapter().listCharacters(getCharacterListCallback());
    }

    @Override
    public void onDestroyView() {
        mCharacterSelectionSpinner = null;
        super.onDestroyView();
    }

    public retrofit.Callback<MarvelCharacterDataWrapper> getCharacterListCallback() {
        return new retrofit.Callback<MarvelCharacterDataWrapper>() {
            @Override
            public void success(MarvelCharacterDataWrapper marvelCharacterDataWrapper, Response response) {
                setAllCharacters(marvelCharacterDataWrapper.data.results);
                setIsRefreshCharacterListButtonEnabled(true);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e(mTag, "Character List Failed to Load: " + error.getMessage());

                ArrayList<MarvelCharacter> blank = new ArrayList<>();
                MarvelCharacter noneMan = new MarvelCharacter();
                noneMan.id = 0;
                noneMan.name = "No Characters Available.";
                noneMan.description = "No Characters were able to be loaded.";
                noneMan.thumbnail = new MarvelThumbnail();
                noneMan.thumbnail.path = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available";
                noneMan.thumbnail.extension = "jpg";

                blank.add(noneMan);
                setAllCharacters(blank);
                setIsRefreshCharacterListButtonEnabled(true);
            }
        };
    }
}
