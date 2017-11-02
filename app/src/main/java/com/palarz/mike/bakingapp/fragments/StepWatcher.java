package com.palarz.mike.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.data.Step;
import com.palarz.mike.bakingapp.utilities.StepAdapter;

/**
 * Created by mpala on 10/20/2017.
 */

public class StepWatcher extends Fragment {

    private static final String STATE_PLAYBACK_POSITION = "playback_position";
    private static final String STATE_CURRENT_WINDOW = "current_window";
    private static final String STATE_PLAY_WHEN_READY = "play_when_ready";

    private static final String TAG = StepWatcher.class.getSimpleName();

    TextView mShortDescriptionTV;
    TextView mLongDescriptionTV;
    Button mPreviousButton;
    Button mNextButton;

    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mPlayer;
    boolean mPlayWhenReady;
    int mCurrentWindow;
    long mPlaybackPosition;
    String mVideoURL;

    // TODO: Add the two buttons on the bottom of the screen to easily be able to move onto the
    // next or previous step

    // TODO: Show the thumbnail associated to a step if it exists and if the video URL is empty

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_watcher, container, false);
        mShortDescriptionTV = (TextView) rootView.findViewById(R.id.step_watcher_short_description);
        mLongDescriptionTV = (TextView) rootView.findViewById(R.id.step_watcher_long_description);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.step_watcher_player_view);
        mPreviousButton = (Button) rootView.findViewById(R.id.step_watcher_previous_button);
        mNextButton = (Button) rootView.findViewById(R.id.step_watcher_next_button);

        Bundle receivedBundle = this.getArguments();
        if (receivedBundle != null){
            Step currentStep = receivedBundle.getParcelable(StepAdapter.BUNDLE_KEY_CURRENT_STEP);
            /*
            These views only exist if the phone is placed into portrait orientation. Therefore, we
            only want to set the text on these Views if they exist.
             */

            // TODO: If the SimpleExoPlayerView doesn't exist because there was no URL, then
            // maybe we should still show these TVs?
            if (mShortDescriptionTV != null && mLongDescriptionTV != null){
                mShortDescriptionTV.setText(currentStep.getShortDescription());
                mLongDescriptionTV.setText(currentStep.getLongDescription());
            }
            mVideoURL = currentStep.getURL();
        }

        if (savedInstanceState != null){
            mPlaybackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPlaybackPosition = mPlayer.getCurrentPosition();
        mCurrentWindow = mPlayer.getCurrentWindowIndex();
        mPlayWhenReady = mPlayer.getPlayWhenReady();
        outState.putLong(STATE_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putInt(STATE_CURRENT_WINDOW, mCurrentWindow);
        outState.putBoolean(STATE_PLAY_WHEN_READY, mPlayWhenReady);

        super.onSaveInstanceState(outState);
    }

    private void initializePlayer(){
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );

        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

        if (!(mVideoURL.isEmpty())) {
            mPlayerView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(mVideoURL);
            MediaSource mediaSource = buildMediaSource(uri);
            mPlayer.prepare(mediaSource, true, false);
        }
        else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(),
                null,
                null);
    }

    private void releasePlayer(){
//        Log.d(TAG, "releasePlayer() has been called\n");
        if (mPlayer != null){
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void hideSystemUI() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null){
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
    }



}
