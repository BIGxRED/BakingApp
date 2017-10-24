package com.palarz.mike.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    TextView mShortDescriptionTV;
    TextView mLongDescriptionTV;

    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mPlayer;
    boolean mPlayWhenReady;
    int mCurrentWindow;
    long mPlaybackPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_watcher, container, false);
        mShortDescriptionTV = (TextView) rootView.findViewById(R.id.step_watcher_short_description);
        mLongDescriptionTV = (TextView) rootView.findViewById(R.id.step_watcher_long_description);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.step_watcher_player_view);

        Bundle receivedBundle = this.getArguments();
        if (receivedBundle != null){
            Step currentStep = receivedBundle.getParcelable(StepAdapter.BUNDLE_KEY_CURRENT_STEP);
            mShortDescriptionTV.setText(currentStep.getShortDescription());
            mLongDescriptionTV.setText(currentStep.getLongDescription());
        }

        return rootView;
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

        Uri uri = Uri.parse(getString(R.string.step_watcher_player_uri_testing));
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(),
                null,
                null);
    }

    private void releasePlayer(){
        if (mPlayer != null){
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
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
