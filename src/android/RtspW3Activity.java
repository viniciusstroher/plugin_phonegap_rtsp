
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
import android.widget.MediaController;
import android.widget.VideoView;
import android.media.MediaPlayer.OnPreparedListener;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.AndroidUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import java.util.List;
import java.util.ArrayList;
import org.apache.cordova.rtspw3.FakeR;
import java.util.Arrays;

import android.widget.Button;
import android.view.KeyEvent;
public class RtspW3Activity extends Activity implements MediaPlayer.EventListener{
    private String link_rtsp;
    private FakeR fakeR;
    
    public final static String TAG = "LibVLCAndroidSample/VideoActivity";

    public final static String LOCATION = "com.compdigitec.libvlcandroidsample.VideoActivity.location";

    private String mFilePath;

    // display surface
    private SurfaceView mSurface;
    private SurfaceHolder holder;

    // media player
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private Media m                  = null;

    private int mVideoWidth;
    private int mVideoHeight;
    private final static int VideoSizeChanged = -1;
    private String [] optionString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoWidth = 400;
        mVideoHeight= 700;
        
        fakeR = new FakeR(this);
        setContentView(fakeR.getId("layout", "rtsp_w3_activity"));
        
        //PEGA PARAMETRO
        link_rtsp = getIntent().getStringExtra("LINK_RTSP");
        try{
            optionString = getIntent().getStringExtra("OPTIONS_VLC").split(" ");
        }catch(Exception e){
            Log.i("RTSP"," PARAMETRO OPTIONS_VLC ERROR: "+e.getMessage());
             RtspW3Activity.this.finish();

        }

        
        final Button button = (Button)findViewById(fakeR.getId("id", "button_id"));
        button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            Log.i("RTSP","FECHANDO VIEW ");

            onBackPressed();
            releasePlayer();
            finish();
            //releasePlayer();
            //RtspW3Activity.this.finish();

          }
        });

        mSurface = (SurfaceView) findViewById(fakeR.getId("id", "surface"));        
        holder   = mSurface.getHolder();
        holder.setFixedSize(mVideoWidth, mVideoHeight);

        createPlayer(link_rtsp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createPlayer(link_rtsp);
    }

    @Override
    protected void onPause() {
        //releasePlayer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createPlayer(String media) {
        try {
            if (media.length() > 0) {
                Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,0);
                toast.show();
            }

            ArrayList<String> options = new ArrayList<String>();
            for(String v : optionString){
                Log.i("RTSP","ADD OPTIONS VLC LIB INSTANCE: "+v);
                options.add(v);
            }
            //para cameras --aout=none --rtsp-tcp
            //--aout=none --rtsp-tcp --rtsp-user= --rtsp-pwd= --avcodec-hw=dxva2  --rtsp-frame-buffer-size=200
            options.add("-vvv"); // verbosity
            Log.i("RTSP","OPTIONS VLC LIB INSTANCE: "+options.toString());
            libvlc = new LibVLC(getApplicationContext(),options);
            
            holder.setKeepScreenOn(true);

            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(this);

            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mSurface);
            vout.attachViews();

            m = new Media(libvlc,Uri.parse(media));
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();

        } catch (Exception e) {
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
            RtspW3Activity.this.finish();
 
        }
    }

    private void releasePlayer() {
        if (libvlc == null){
            return;
        }

        
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
        }

        final IVLCVout vout = mMediaPlayer.getVLCVout();
        if(vout != null){
            vout.detachViews();
        }
        
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }



        if (libvlc != null){
            libvlc.release();
            libvlc = null;
        }
    }


    @Override
    public void onEvent(MediaPlayer.Event event) {

        switch(event.type) {
            case MediaPlayer.Event.EndReached:
                Log.i("RTSP","MediaPlayer.Event.EndReached: ");
                //Toast.makeText(this, "EndReached", Toast.LENGTH_LONG).show();
            
                //mMediaPlayer.release();
            break;
            case MediaPlayer.Event.Playing:
            case MediaPlayer.Event.Paused:
            case MediaPlayer.Event.Stopped:
            default:
            break;
        }
    }
}