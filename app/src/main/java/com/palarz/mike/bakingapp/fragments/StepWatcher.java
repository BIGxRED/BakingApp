package com.palarz.mike.bakingapp.fragments;

import android.content.Context;
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
import android.widget.Toast;

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
import com.palarz.mike.bakingapp.model.Step;
import com.palarz.mike.bakingapp.utilities.Bakery;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by mpala on 10/20/2017.
 */

public class StepWatcher extends Fragment {

    private static final String STATE_PLAYBACK_POSITION = "playback_position";
    private static final String STATE_CURRENT_WINDOW = "current_window";
    private static final String STATE_PLAY_WHEN_READY = "play_when_ready";

    private static final String ARGS_RECIPE_ID = "recipe_id";
    private static final String ARGS_STEP_INDEX = "step_id";

    @BindView(R.id.step_watcher_short_description) TextView mShortDescriptionTV;
    @BindView(R.id.step_watcher_long_description) TextView mLongDescriptionTV;
    @BindView(R.id.step_watcher_previous_button) Button mPreviousButton;
    @BindView(R.id.step_watcher_next_button) Button mNextButton;
    @BindView(R.id.step_watcher_thumbnail) ImageView mThumbnailIV;
    @BindView(R.id.step_watcher_player_view) SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mPlayer;

    boolean mPlayWhenReady;
    int mCurrentWindow;
    long mPlaybackPosition;
    String mVideoURL;
    String mThumbnailURL;
    Step[] mSteps;
    int mCurrentStepIndex;
    Step mCurrentStep;
    private StepSwitcher mCallback;


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

    public interface StepSwitcher{
        void switchStep(int stepIndex);
    }

    public StepWatcher(){
    }

    public static StepWatcher newInstance(int recipeID, int stepIndex){
        Bundle arguments = new Bundle();
        arguments.putInt(ARGS_RECIPE_ID, recipeID);
        arguments.putInt(ARGS_STEP_INDEX, stepIndex);

        StepWatcher fragment = new StepWatcher();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // In this case, we first obtain a reference to the hosting activity, StepDisplay,
            // through the context. Then, we ensure that StepDisplay has implemented the interface.
            // Otherwise, we throw an exception.
            mCallback = (StepSwitcher) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepSwitcher");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        int recipeID = arguments.getInt(ARGS_RECIPE_ID);
        
        mCurrentStepIndex = arguments.getInt(ARGS_STEP_INDEX);
        mSteps = Bakery.get().getRecipe(recipeID).getSteps();
        mCurrentStep = Bakery.get().getStep(recipeID, mCurrentStepIndex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView() has been called");
        View rootView = inflater.inflate(R.layout.fragment_step_watcher, container, false);

        ButterKnife.bind(this, rootView);

        Bundle receivedBundle = this.getArguments();
        if (receivedBundle != null){

            /*
            These views only exist if the phone is placed into portrait orientation. Therefore, we
            only want to set the text on these Views if they exist.
             */

            if (mShortDescriptionTV != null && mLongDescriptionTV != null){
                mShortDescriptionTV.setText(mCurrentStep.getShortDescription());
                mLongDescriptionTV.setText(mCurrentStep.getLongDescription());
            }
            mVideoURL = mCurrentStep.getURL();
            mThumbnailURL = mCurrentStep.getThumbnail();

            // If we weren't provided a URL for the video, then let's at least load an image to be
            // shown in the Step
            if (mVideoURL.isEmpty()){

                /*
                We need to be explicit as to which Views are VISIBLE and which are GONE for this app.
                This is because of how the fragment_step_watcher layout is defined for landscape.
                The landscape layout was designed with the assumption that there will always be a
                video, so the SimpleExoPlayerView is VISIBLE and takes up the entire screen
                (match_parent for both width and height). All of the other views are GONE by default.
                However, if we don't have a video URL, then we'd at least want to see the thumbnail
                and all of the other views.

                This is why we are being so explicit on which views are GONE and VISIBLE under this
                scenario.
                 */
                mThumbnailIV.setVisibility(View.VISIBLE);
                mShortDescriptionTV.setVisibility(View.VISIBLE);
                mLongDescriptionTV.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mPreviousButton.setVisibility(View.VISIBLE);
                mPlayerView.setVisibility(View.GONE);

                // There is the case where both the video and thumbnail URLs weren't
                // provided. In that case, we'll still show an image, but it will be a random image
                // from our drawables
                if (mThumbnailURL.isEmpty()){
                    int newThumbnail = getRandomImageResource();
                    mCurrentStep.setThumbnail(Integer.toString(newThumbnail));

                    mThumbnailIV.setImageResource(newThumbnail);

                }
                /* Otherwise, if the thumbnail was not empty, we will either load it from our drawables,
                assuming that it was previously empty and a random drawable was already used, or
                we will download and display it.
                  */
                else {
                    try {
                        // First we try to see if the thumbnail String is actually a drawable
                        mThumbnailIV.setImageResource(Integer.valueOf(mCurrentStep.getThumbnail()));
                    }
                    catch (NumberFormatException nfe) {
                        // If it wasn't a drawable, then we will attempt to download and display
                        // the image
                        Picasso.with(getContext())
                                .load(mThumbnailURL)
                                .placeholder(R.drawable.hourglass)
                                .into(mThumbnailIV, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }
                                    /*
                                    Here we've created a Callback which allows us to control what
                                    happens in certain situations. We are only interested in the
                                    case when we attempt to download the image, but we weren't
                                    successful because the URL was invalid. We could just use
                                    error(getRandomImageResource()) within Picasso. However,
                                    the image will always be different each time we come back
                                    to this step as well as on screen rotation. Therefore,
                                    we've created this Callback so that we can identify when
                                    the error occurs so that we can store the drawable to the
                                    Step so that the same image is loaded the next time the Step
                                    is shown.
                                     */
                                    @Override
                                    public void onError() {
                                        int newThumbnail = getRandomImageResource();
                                        mCurrentStep.setThumbnail(Integer.toString(newThumbnail));
                                        mThumbnailIV.setImageResource(newThumbnail);
                                    }
                                });
                    }
                    // If all else fails, then we will display a Toast message.
                    catch (Exception e){
                        Toast.makeText(getContext(), "Could not load image resource", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            // If we do have a URL for the video, then we'll hide the thumbnail ImageView and show
            // the video instead
            else {

                mThumbnailIV.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);


                // If the device is in landscape mode, then we hide all of the system UI buttons
                // in order to have a fullscreen experience
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
                    mShortDescriptionTV.setVisibility(View.GONE);
                    mLongDescriptionTV.setVisibility(View.GONE);
                    mNextButton.setVisibility(View.GONE);
                    mPreviousButton.setVisibility(View.GONE);
                    hideSystemUI();
                }
                else {
                    mShortDescriptionTV.setVisibility(View.VISIBLE);
                    mLongDescriptionTV.setVisibility(View.VISIBLE);
                    mNextButton.setVisibility(View.VISIBLE);
                    mPreviousButton.setVisibility(View.VISIBLE);
                }
            }
        }

        if (savedInstanceState != null){
            mPlaybackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);
        }

        /* Finally, we only want to show the next and previous buttons if we aren't close to the
        end of mSteps. For example, if we're already on the last Step, then mNextButton should
        be GONE.
          */
        if (mSteps != null){
            // If we're at the last index of mSteps, then mNextButton is GONE
            if (mCurrentStepIndex == (mSteps.length - 1)){
                mNextButton.setVisibility(View.GONE);
            }
            // Likewise, if we're at the very beginning of mSteps, then mPreviousButton is GONE
            else if (mCurrentStepIndex == (0)){
                mPreviousButton.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Timber.d("onSaveInstanceState() has been called. Is mPlayer indeed null?: "
                + (mPlayer == null));
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
            // TODO: See that you're already making mPlayerView GONE here? Maybe you don't have
            // to do so many checks in onCreateView() regarding the video URL being empty
            // because of this. Perhaps you can simplify onCreateView()?
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
        Timber.d("onStart() has been called.");
        if (Util.SDK_INT > 23){
            initializePlayer();
        }
        Timber.d("Is mPlayer initialized?: " + (mPlayer != null));
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume() has been called.");
        if (Util.SDK_INT <= 23 || mPlayer == null){
            initializePlayer();
        }
        Timber.d("Is mPlayer initialized?: " + (mPlayer != null));
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause() has been called.");
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }
        Timber.d("Is mPlayer indeed null?: " + (mPlayer == null));
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop() has been called.");
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
        Timber.d("Is mPlayer indeed null?: " + (mPlayer == null));
    }

    public int getRandomImageResource(){
        int randomIndex = new Random().nextInt(STEP_IMAGES.length );

        return STEP_IMAGES[randomIndex];
    }

    @OnClick(R.id.step_watcher_next_button)
    public void displayNextStep(){
        if (mCurrentStepIndex < (mSteps.length - 1)){
            mCurrentStepIndex++;
            mCallback.switchStep(mCurrentStepIndex);
        }
    }

}
