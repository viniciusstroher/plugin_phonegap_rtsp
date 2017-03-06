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

import org.apache.cordova.rtspw3.FakeR;

public class RtspW3Activity extends Activity{
    private String link_rtsp;
    private FakeR fakeR;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        fakeR = new FakeR(this);
        setContentView(fakeR.getId("layout", "rtsp_w3_activity"));
        
        //PEGA PARAMETRO
        link_rtsp = getIntent().getStringExtra("LINK_RTSP");

        VideoView videoView = (VideoView) findViewById(fakeR.getId("id", "videoview"));

        Uri video = Uri.parse(link_rtsp);
        videoView.setVideoURI(video);
        
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          @Override       
          public void onPrepared(MediaPlayer mp) {
               mp.setLooping(true);
               videoView.start();
            }
        });
    }

}
