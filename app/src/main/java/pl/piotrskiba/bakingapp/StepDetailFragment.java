package pl.piotrskiba.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;

public class StepDetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.iv_thumbnail)
    @Nullable
    ImageView mStepThumbnail;

    @BindView(R.id.tv_step_description)
    @Nullable
    TextView mStepDescriptionTextView;

    SimpleExoPlayer mStepVideoPlayer;

    @BindView(R.id.step_video)
    SimpleExoPlayerView mStepVideoPlayerView;

    @BindView(R.id.button_prev)
    @Nullable
    Button mPreviousButton;

    @BindView(R.id.button_next)
    @Nullable
    Button mNextButton;

    Recipe mRecipe;
    int mStepIndex;
    Step mStep;

    android.support.v7.app.ActionBar mSupportActionBar;

    public final static String TAG = "FRAGMENT_STEP_DETAIL";

    public final static String KEY_VIDEO_POSITION = "video_position";
    public final static String KEY_PLAY_WHEN_READY = "video_play_when_ready";

    private long mVideoPos = 0;
    private boolean mPlayWhenReady = false;

    public StepDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        setRetainInstance(true);

        updateUI();

        if (mPreviousButton != null) {
            mPreviousButton.setOnClickListener(this);
            mNextButton.setOnClickListener(this);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_VIDEO_POSITION)){
            long videoPos = savedInstanceState.getLong(KEY_VIDEO_POSITION);
            boolean playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);

            mVideoPos = videoPos;
            mPlayWhenReady = playWhenReady;
        }

        return rootView;
    }

    public void setRecipe(Recipe recipe){
        this.mRecipe = recipe;
    }

    public void setStepIndex(int index){
        this.mStepIndex = index;
    }

    public void setSupportActionBar(android.support.v7.app.ActionBar supportActionBar){
        this.mSupportActionBar = supportActionBar;
    }

    public void updateUI(){
        mStep = mRecipe.getSteps().get(mStepIndex);

        releasePlayer();

        // load step description
        if(mStepDescriptionTextView != null)
            mStepDescriptionTextView.setText(mStep.getDescription());

        // load step thumbnail
        if(mStepThumbnail != null) {
            if (!TextUtils.isEmpty(mStep.getThumbnailUrl())) {
                Picasso.get()
                        .load(mStep.getThumbnailUrl())
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .into(mStepThumbnail);

                mStepThumbnail.setVisibility(View.VISIBLE);
            } else {
                mStepThumbnail.setVisibility(View.GONE);
            }
        }

        // load step movie
        if(!TextUtils.isEmpty(mStep.getVideoUrl())) {
            initializeVideoPlayer(Uri.parse(mStep.getVideoUrl()));
            mStepVideoPlayerView.setVisibility(View.VISIBLE);

            // restore video position
            mStepVideoPlayer.seekTo(mVideoPos);
            mStepVideoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
        else{
            // there's no video, so hide the player
            mStepVideoPlayerView.setVisibility(View.GONE);
        }

        mSupportActionBar.setTitle(mRecipe.getName() + " - " + mStep.getShortDescription());

        // enable/disable buttons
        if(mPreviousButton != null) {
            if (mStepIndex == 0) {
                mPreviousButton.setEnabled(false);
            } else {
                mPreviousButton.setEnabled(true);
            }

            if (mStepIndex == mRecipe.getSteps().size() - 1) {
                mNextButton.setEnabled(false);
            } else {
                mNextButton.setEnabled(true);
            }
        }
    }

    private void initializeVideoPlayer(Uri videoUri){
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mStepVideoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mStepVideoPlayerView.setPlayer(mStepVideoPlayer);

        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        mStepVideoPlayer.prepare(mediaSource);
    }

    private void releasePlayer(){
        if(mStepVideoPlayer != null) {
            mStepVideoPlayer.stop();
            mStepVideoPlayer.release();
            mStepVideoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_prev){
            setStepIndex(mStepIndex - 1);
            updateUI();
        }
        else if(v.getId() == R.id.button_next){
            setStepIndex(mStepIndex + 1);
            updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(KEY_VIDEO_POSITION, mStepVideoPlayer.getCurrentPosition());
        outState.putBoolean(KEY_PLAY_WHEN_READY, mStepVideoPlayer.getPlayWhenReady());

        super.onSaveInstanceState(outState);
    }
}
