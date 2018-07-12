package pl.piotrskiba.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Step;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.tv_step_description)
    TextView mStepDescriptionTextView;

    SimpleExoPlayer mStepVideoPlayer;

    @BindView(R.id.step_video)
    SimpleExoPlayerView mStepVideoPlayerView;

    Step mStep;

    public final static String TAG = "FRAGMENT_STEP_DETAIL";

    public StepDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        setRetainInstance(true);

        updateUI();

        return rootView;
    }

    public void setStep(Step step){
        this.mStep = step;
    }

    public void updateUI(){
        mStepDescriptionTextView.setText(mStep.getDescription());

        if(mStep.getVideoUrl().length() != 0) {
            initializeVideoPlayer(Uri.parse(mStep.getVideoUrl()));
        }
        else{
            // there's no video, so remove the player
            ViewGroup parent = (ViewGroup) mStepVideoPlayerView.getParent();
            parent.removeView(mStepVideoPlayerView);
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
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
