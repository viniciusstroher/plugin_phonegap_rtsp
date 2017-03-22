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
import android.media.AudioManager;
import org.apache.cordova.rtspw3.FakeR;

public class RtspW3Activity extends Activity{
    private String link_rtsp;
    private FakeR fakeR;
    private VideoView videoView;
    private MediaController mController;
    private MediaPlayer mediaPlayer;

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

        videoView = (VideoView) findViewById(fakeR.getId("id", "videoview"));
        videoView.setVideoPath(link_rtsp);
        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
        //videoView.setAudioStreamType(AudioManager.STREAM_MUSIC);
        videoView.setBufferSize(1024);
        
        
        videoView.setMediaController(new MediaController(this));

        mediaPlayer = new MediaPlayer(this, true);
        mediaPlayer.setDisplay(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setOnBufferingUpdateListener(this);
        //mediaPlayer.setOnPreparedListener(this);


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer m) {
                //m.getMetadata();
               
                m.setPlaybackSpeed(1.0f);
                m.setVolume(1f , 1f);
                m.setLooping(false);
                m.prepareAsync();
                m.start();
                //videoView.start();
            }
        });

        mediaController.setAnchorView(videoView);
        videoView.requestFocus();
    }

}