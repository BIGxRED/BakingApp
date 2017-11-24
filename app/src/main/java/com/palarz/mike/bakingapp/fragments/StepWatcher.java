/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.fragments;

import android.content.Context;
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
import com.palarz.mike.bakingapp.utilities.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/*
Primary purpose: This fragment is responsible for showing the details for a given step of a
recipe. It plays the video of the step (if it is available) as well as a text description of the
current step.
 */
public class StepWatcher extends Fragment {

    // Keys used for the Bundle within onSaveInstanceState()
    private static final String BUNDLE_SIS_KEY_PLAYBACK_POSITION = "playback_position";
    private static final String BUNDLE_SIS_KEY_CURRENT_WINDOW = "current_window";
    private static final String BUNDLE_SIS_KEY_PLAY_WHEN_READY = "play_when_ready";

    // Keys used for the Bundle within StepWatcher.newInstance()
    private static final String ARGS_RECIPE_ID = "recipe_id";
    private static final String ARGS_STEP_INDEX = "step_id";

    // Views within the StepWatcher layout. We use data binding through the Butterknife library
    // to make our lives easier.
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

    /*
    This interface is responsible for handling the click events of the next and previous buttons
    within StepWatcher. The StepDisplay activity has implemented this interface since each time
    either of the buttons are clicked, a FragmentTransaction occurs. Specifically, the current
    StepWatcher fragment is replaced by a new StepWatcher, which corresponds to the next or
    previous step.
     */
    private StepSwitcher mCallback;

    public interface StepSwitcher{
        void switchStep(int stepIndex);
    }

    public StepWatcher(){
    }

    /*
    A helper method for properly creating a StepWatcher fragment. The bundle that is attached
    to the StepWatcher is used later on in onCreate().
     */
    public static StepWatcher newInstance(int recipeID, int stepIndex){
        Bundle arguments = new Bundle();
        arguments.putInt(ARGS_RECIPE_ID, recipeID);
        arguments.putInt(ARGS_STEP_INDEX, stepIndex);

        StepWatcher fragment = new StepWatcher();
        fragment.setArguments(arguments);
        return fragment;
    }

    // We've overriden onAttach() to ensure that the hosting activity has implemented the
    // StepSwitcher interface.
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

    /*
    We obtain the Bundle that was set as the arguments to the fragment within
    StepWatcher.newInstance() and obtain a reference to the necessary variables.
     */
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
        View rootView = inflater.inflate(R.layout.fragment_step_watcher, container, false);

        ButterKnife.bind(this, rootView);

        Bundle receivedBundle = this.getArguments();
        if (receivedBundle != null){

            mShortDescriptionTV.setText(mCurrentStep.getShortDescription());
            mLongDescriptionTV.setText(mCurrentStep.getLongDescription());
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

                This is slightly redundant since these views will actually be visible in the
                portrait layout, regardless if the video URL is empty or not. However, instead of
                checking if the device is/is not in landscape, we simply set the view visibility
                correctly for both orientations.
                 */
                mThumbnailIV.setVisibility(View.VISIBLE);
                mShortDescriptionTV.setVisibility(View.VISIBLE);
                mLongDescriptionTV.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mPreviousButton.setVisibility(View.VISIBLE);

                /*
                There is the case where both the video and thumbnail URLs weren't provided. In that
                case, we'll still show an image, but it will be a random image from our drawables.
                 */
                if (mThumbnailURL.isEmpty()){
                    int newThumbnail = Utilities.getRandomStepImageResource();

                    /*
                    Code Section A

                    We set the drawable ID as the new thumbnail property of the current Step so
                    that the same image is shown each time the user refers back to this step. This
                    becomes a slight problem in Code Section B.
                     */
                    mCurrentStep.setThumbnail(Integer.toString(newThumbnail));

                    mThumbnailIV.setImageResource(newThumbnail);

                }
                /*
                Code Section B

                Otherwise, if the thumbnail was not empty, we will either load it from our
                drawables, assuming that it was previously empty and a random drawable was already
                used (from Code Section A), or we will download and display it.
                  */
                else {
                    try {
                        /*
                        First we try to see if the thumbnail String is actually a drawable. The
                        reason it may be a drawable ID is due to Code Section A.
                         */
                        mThumbnailIV.setImageResource(Integer.valueOf(mCurrentStep.getThumbnail()));
                    }
                    catch (NumberFormatException nfe) {
                        // If it wasn't a drawable, then we will attempt to download and display
                        // the image
                        Picasso.with(getContext())
                                .load(mThumbnailURL)
                                .placeholder(R.drawable.hourglass)
                                .into(mThumbnailIV, new Callback() {
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
                                    public void onSuccess() {

                                    }
                                    @Override
                                    public void onError() {
                                        int newThumbnail = Utilities.getRandomStepImageResource();
                                        /*
                                        We do the same thing as was done in Code Section A. We set
                                        the drawable ID as the new thumbnail property of the current
                                        Step so that the same image is shown each time the user
                                        refers back to this step. This becomes a slight problem in
                                        Code Section B.
                                         */
                                        mCurrentStep.setThumbnail(Integer.toString(newThumbnail));
                                        mThumbnailIV.setImageResource(newThumbnail);
                                    }
                                });
                    }
                    // If all else fails, then we will display a Toast message. This should
                    // hopefully never happen...
                    catch (Exception e){
                        Toast.makeText(getContext(), "Could not load image resource",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }

            /*
            If we do have a URL for the video, then we'll hide the system UI if the device is in
              landscape orientation and it is not a tablet. If the device is a tablet, then
              hiding the system UI is undesirable since we are using a different layout for that
              case (it is a master-detail layout) and hiding the system UI will provide a poor UE.
             */
            else {
                if (Utilities.isLandscape(getContext()) && !(Utilities.isTablet(getContext()))){
                    hideSystemUI();
                }
            }
        }

        /*
        If this fragment is being re-created, then we obtain the proper values of the playback
        position, window, and mPlayWhenReady so that the video continues to play from the correct
        position on orientation changes.
         */
        if (savedInstanceState != null){
            mPlaybackPosition = savedInstanceState.getLong(BUNDLE_SIS_KEY_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(BUNDLE_SIS_KEY_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(BUNDLE_SIS_KEY_PLAY_WHEN_READY);
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

    /*
    We store the necessary variables for playback so that the video continues from the correct
    position on orientation changes.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        /*
        There is a special case where if the next/previous buttons are clicked (hence, there are
        fragments in the backstack) and the device is rotated, then onSaveInstanceState() is called
        for every entry within the backstack. However, the previous entries will have a null
        mPlayer since releasePlayer() was called within either onPause() or onResume(). We only
        care about the current instance of StepWatcher that is not null since
        that pertains to the fragment that hasn't been added to the backstack yet. Therefore, the
        following if() is necessary to avoid checking the previous fragments in the backstack.
         */
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            outState.putLong(BUNDLE_SIS_KEY_PLAYBACK_POSITION, mPlaybackPosition);
            outState.putInt(BUNDLE_SIS_KEY_CURRENT_WINDOW, mCurrentWindow);
            outState.putBoolean(BUNDLE_SIS_KEY_PLAY_WHEN_READY, mPlayWhenReady);

            super.onSaveInstanceState(outState);
        }
    }

    /*
    Helper method for properly initializing our ExoPlayer.
     */
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

    /*
    Helper method for creating the media source that the ExoPlayer uses to plat the video.
    */
    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(),
                null,
                null);
    }

    /*
    Helper method for properly releasing the video when it is no longer needed.
    */
    private void releasePlayer(){
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

    /*
    Starting with API 24, multiple windows are supported within Android. Therefore, it is possible
    that our app can be visible but not active within a split window. In that case, we initialize
    our ExoPlayer in onStart().
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            initializePlayer();
        }
    }

    /*
    Prior to API 24, we can initialize our ExoPlayer in onResume().
     */
    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null){
            initializePlayer();
        }
    }

    /*
    Prior to API 24, we can release our ExoPlayer in onPause().
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    /*
    Again, due to the multiple window feature in API 24 and beyond, we release the ExoPlayer in
    onStop().
     */
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    /*
    These are the click handlers for the next and previous buttons. Within either method, we first
    setup mCurrentStepIndex accordingly. Then, we call switchStep(), which is actually
    implemented within the StepDisplay activity.
     */
    @OnClick(R.id.step_watcher_next_button)
    public void displayNextStep(){
        if (mCurrentStepIndex < (mSteps.length - 1)){
            mCurrentStepIndex++;
            mCallback.switchStep(mCurrentStepIndex);
        }
    }

    @OnClick(R.id.step_watcher_previous_button)
    public void displayPreviousStep(){
        if (mCurrentStepIndex > 0){
            mCurrentStepIndex--;
            mCallback.switchStep(mCurrentStepIndex);
        }
    }

}
