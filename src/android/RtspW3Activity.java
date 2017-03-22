package org.apache.cordova.rtspw3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import android.net.Uri;
import android.view.Window;
/*import android.widget.MediaController;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
*/

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import org.apache.cordova.rtspw3.FakeR;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import android.util.Log;

import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import java.util.Collections; 
import java.util.HashMap; 
public class RtspW3Activity extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

    private String link_rtsp;
    private FakeR fakeR;
    private VideoView videoView;
    private MediaController mController;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;

    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        fakeR = new FakeR(this);
        setContentView(fakeR.getId("layout", "rtsp_w3_activity"));
        
        //PEGA PARAMETRO
        link_rtsp = getIntent().getStringExtra("LINK_RTSP");
        //inicializa plugin
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)){
            return;
        }
        /*videoView = (VideoView) findViewById(fakeR.getId("id", "videoview"));

        videoView.setVideoPath(link_rtsp);
        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        //videoView.setBufferSize(2048);
        videoView.requestFocus();
        videoView.start();
        videoView.setMediaController(new MediaController(this));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });*/

        mPreview = (SurfaceView) findViewById(fakeR.getId("id", "videoview"));
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888); 

    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d("TAG", "onPrepared called");
        mMediaPlayer.start();
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        // Log.d("TAG", "onBufferingUpdate percent:" + percent);

    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d("TAG", "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d("TAG", "surfaceDestroyed called");
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d("TAG", "surfaceChanged called");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mMediaPlayer = new MediaPlayer(this);
            HashMap<String, String> options = new HashMap<String, String>();
            options.put("rtsp_transport", "tcp"); // udp

            mMediaPlayer.setDataSource(link_rtsp,options);
            mMediaPlayer.setDisplay(holder);

            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);

            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            Log.e("TAG", "error: " + e.getMessage(), e);
        }
    }


}
