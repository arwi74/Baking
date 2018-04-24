package com.example.arek.baking.recipeDetails.recipeStep;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.databinding.FragmentRecipeStepBinding;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.model.Step;
import com.example.arek.baking.recipeDetails.RecipeDetailActivity;
import com.example.arek.baking.repository.RecipeRepository;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepActivityFragment extends Fragment implements RecipeStepContract.View{
    private long mRecipeId;
    private long mRecipeStepId;
    private FragmentRecipeStepBinding mBinding;
    @Inject
    RecipeRepository mRepository;
    private SimpleExoPlayer mExoPlayer;
    private static final String STATE_PLAYER_POSITION = "state_exo_player_position";
    private static final String STATE_PLAYER_WINDOW = "state_player_window";
    private static final String STATE_PLAYER_PLAY = "state_player_play";
    private long mPlayerPosition;
    private int mPlayerWindow;
    private boolean mIsPlayerPlay = true;
    private RecipeStepContract.Presenter mPresenter;

    public RecipeStepActivityFragment() {
    }

    public static RecipeStepActivityFragment newInstance(long recipeId, long recipeStepId) {
        RecipeStepActivityFragment fragment = new RecipeStepActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RecipeDetailActivity.EXTRA_RECIPE_ID, recipeId);
        bundle.putLong(RecipeStepActivity.EXTRA_RECIPE_STEP_ID, recipeStepId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
        if ( mPresenter != null ) {
            mPresenter.dropView();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if ( mExoPlayer != null ) {
            Timber.d("exo player save position: " + mExoPlayer.getCurrentPosition());
            outState.putLong(STATE_PLAYER_POSITION, mExoPlayer.getCurrentPosition());
            outState.putInt(STATE_PLAYER_WINDOW, mExoPlayer.getCurrentWindowIndex());
            outState.putBoolean(STATE_PLAYER_PLAY, mExoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_step, container, false);

        ((BakingApp)getActivity().getApplication()).getRepositoryComponent().inject(this);

        if ( savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong(STATE_PLAYER_POSITION);
            mPlayerWindow = savedInstanceState.getInt(STATE_PLAYER_WINDOW);
            mIsPlayerPlay = savedInstanceState.getBoolean(STATE_PLAYER_PLAY);
            Timber.d("exo player restore position: " +mPlayerPosition);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if ( bundle != null && hasArguments(bundle) ) {
            mRecipeId = bundle.getLong(RecipeDetailActivity.EXTRA_RECIPE_ID);
            mRecipeStepId = bundle.getLong(RecipeStepActivity.EXTRA_RECIPE_STEP_ID);
        }
        Timber.d("Recipe id: "+mRecipeId+" step: "+mRecipeStepId);
        mPresenter = new RecipeStepPresenter(mRepository);
        mPresenter.takeView(this);
        mPresenter.getRecipeStep(mRecipeId, mRecipeStepId);
    }

    private boolean hasArguments(Bundle bundle) {
        return bundle.containsKey(RecipeDetailActivity.EXTRA_RECIPE_ID) &&
                bundle.containsKey(RecipeStepActivity.EXTRA_RECIPE_STEP_ID);
    }

    @Override
    public void showRecipeStep(Step step) {
        mBinding.recipeStepDescription.setText(step.getDescription());
        mBinding.recipeStepShortDescription.setText(step.getShortDescription());
        String videoUrl = step.getVideoURL();
        if ( !videoUrl.isEmpty() ) {
            initializePlayer(videoUrl);
        } else {
            hideExoPlayerView();
        }
    }

    @Override
    public void showErrorMessage() {
        if ( getActivity() != null ) {
            Toast.makeText(
                    getActivity(),
                    R.string.recipe_step_fregment_error_message,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void hideExoPlayerView() {
        mBinding.recipeStepPlayer.setVisibility(View.GONE);
    }

    private void initializePlayer(String videoUrl) {
        if ( mExoPlayer != null || getActivity() == null) return;
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        mExoPlayer.addListener(new ExoPlayerListener());

        mBinding.recipeStepPlayer.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
        Uri mediaUri = Uri.parse(videoUrl);
        MediaSource mediaSource = new ExtractorMediaSource(
                mediaUri,
                new DefaultDataSourceFactory(getActivity(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(mIsPlayerPlay);
        if ( mPlayerPosition > 0 ) {
            mExoPlayer.seekTo(mPlayerWindow, mPlayerPosition);
            Timber.d("exo player seek to position: " + mPlayerPosition);
            mPlayerPosition = 0;
        }
    }

    private void releasePlayer() {
        if ( mExoPlayer == null ) return;
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }


    private class ExoPlayerListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }
    }

}

