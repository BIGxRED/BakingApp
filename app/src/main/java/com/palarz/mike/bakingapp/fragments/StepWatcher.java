package com.palarz.mike.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.Random;

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
    ImageView mThumbnailIV;

    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mPlayer;

    boolean mPlayWhenReady;
    int mCurrentWindow;
    long mPlaybackPosition;
    String mVideoURL;
    String mThumbnailURL;

    // TODO: Add the two buttons on the bottom of the screen to easily be able to move onto the
    // next or previous step

    // TODO: Show the thumbnail associated to a step if it exists and if the video URL is empty

    public static final int [] STEP_IMAGES = {
            R.drawable.step1,
            R.drawable.step2,
            R.drawable.step3,
            R.drawable.step4,
            R.drawable.step5,
            R.drawable.step6,
            R.drawable.step7,
            R.drawable.step8,
            R.drawable.step9,
            R.drawable.step10,
            R.drawable.step11,
            R.drawable.step12,
            R.drawable.step13,
            R.drawable.step14,
            R.drawable.step15,
            R.drawable.step16,
            R.drawable.step17,
            R.drawable.step18,
            R.drawable.step19,
            R.drawable.step20,
            R.drawable.step21,
            R.drawable.step22,
            R.drawable.step23,
            R.drawable.step24
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_watcher, container, false);
        mShortDescriptionTV = (TextView) rootView.findViewById(R.id.step_watcher_short_description);
        mLongDescriptionTV = (TextView) rootView.findViewById(R.id.step_watcher_long_description);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.step_watcher_player_view);
        mPreviousButton = (Button) rootView.findViewById(R.id.step_watcher_previous_button);
        mNextButton = (Button) rootView.findViewById(R.id.step_watcher_next_button);
        mThumbnailIV = (ImageView) rootView.findViewById(R.id.step_watcher_thumbnail);

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
            mThumbnailURL = currentStep.getThumbnail();

            // If we weren't provided a URL for the video, then let's at least load an image to be
            // shown in the Step
            if (mVideoURL.isEmpty()){

                mThumbnailIV.setVisibility(View.VISIBLE);
                mPlayerView.setVisibility(View.GONE);

                // TODO: Once you create the Singleton class, update this part of the code to update
                // an individual Step's thumbnail

                // However, there is the case where both the video and thumbnail URLs weren't
                // provided. In that case, we'll still show an image, but it will be a random image
                // from our drawables
                if (mThumbnailURL.isEmpty()){
                    Picasso.with(getContext())
                            .load(getRandomImageResource())
                            .placeholder(R.drawable.hourglass)
                            .into(mThumbnailIV);
                }
                // Otherwise, if the thumbnail URL was provided, then we will download and display it
                else {
                    Picasso.with(getContext())
                            .load(mThumbnailURL)
                            .placeholder(R.drawable.hourglass)
                            .error(getRandomImageResource())
                            .into(mThumbnailIV);
                }

            }

            // If we do have a URL for the video, then we'll hide the thumbnail ImageView and show
            // the video instead
            else {

                mThumbnailIV.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);

                // If the device is in landscape mode, then we hide all of the system UI buttons
                // in order to have a fullscreen experience
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    hideSystemUI();
                }
            }
        }

        if (savedInstanceState != null){
            mPlaybackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);
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

    // TODO: Do we need to make another function, such as showSystemUI()?
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

    public int getRandomImageResource(){
        int randomIndex = new Random().nextInt(STEP_IMAGES.length );

        return STEP_IMAGES[randomIndex];
    }

}
