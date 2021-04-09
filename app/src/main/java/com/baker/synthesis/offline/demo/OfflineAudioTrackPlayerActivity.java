package com.baker.synthesis.offline.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baker.synthesis.offline.BakerCallback;
import com.baker.synthesis.offline.OfflineBakerSynthesizer;

/**
 * @author hsj55
 */
public class OfflineAudioTrackPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private OfflineBakerSynthesizer bakerSynthesizer;
    private static AudioTrackPlayer audioTrackPlayer;
    private EditText editText;
    private ImageView checkImg1, checkImg2, checkImg3, checkImg4, checkImg5;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            setParams();
            bakerSynthesizer.start();
            handler.sendEmptyMessageDelayed(102, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_audio_track_player);

        initView();

        //初始化sdk
        bakerSynthesizer = new OfflineBakerSynthesizer(OfflineAudioTrackPlayerActivity.this,
                SharedPreferencesUtil.getClientId(),
                SharedPreferencesUtil.getClientSecret(), true);
        audioTrackPlayer = new AudioTrackPlayer();
    }

    private void initView() {
        editText = findViewById(R.id.edit_content);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.voice_1).setOnClickListener(this);
        findViewById(R.id.voice_2).setOnClickListener(this);
        findViewById(R.id.voice_3).setOnClickListener(this);
        findViewById(R.id.voice_4).setOnClickListener(this);
        findViewById(R.id.voice_5).setOnClickListener(this);
        checkImg1 = findViewById(R.id.checkbox_1);
        checkImg2 = findViewById(R.id.checkbox_2);
        checkImg3 = findViewById(R.id.checkbox_3);
        checkImg4 = findViewById(R.id.checkbox_4);
        checkImg5 = findViewById(R.id.checkbox_5);
    }

    BakerCallback bakerCallback = new BakerCallback() {
        /**
         * 开始合成
         */
        @Override
        public void onSynthesisStarted() {
            Log.d("Offline","--onSynthesisStarted--");
        }

        /**
         * 合成完成。
         * 当onBinaryReceived方法中endFlag参数=1，即最后一条消息返回后，会回调此方法。
         */
        @Override
        public void onSynthesisCompleted() {
            Log.d("Offline","--onSynthesisCompleted--");
        }

        /**
         * 第一帧数据返回时的回调
         */
        @Override
        public void onPrepared() {
            Log.d("Offline","--onPrepared--");
            //清除掉播放器之前的缓存数据
            audioTrackPlayer.cleanAudioData();
        }

        /**
         * 流式持续返回数据的接口回调
         *
         * @param data 合成的音频数据
         * @param audioType  音频类型，如audio/pcm
         * @param interval  音频interval信息，
         * @param endFlag  是否时最后一个数据块，false：否，true：是
         */
        @Override
        public void onBinaryReceived(byte[] data, String audioType, String interval, boolean endFlag) {
//            HLogger.d("data.length==" + data.length + ", interval=" + interval);
            audioTrackPlayer.setAudioData(data);
        }

        /**
         * 合成失败
         * @param errorMsg 返回msg内容格式为：{"code":40000,"message":"…","trace_id":" 1572234229176271"}
         */
        @Override
        public void onTaskFailed(final int errorCode, final String errorMsg, String traceId) {
            Log.d("Offline","errorCode==" + errorCode + ",errorMsg==" + errorMsg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OfflineAudioTrackPlayerActivity.this,
                            "errorCode==" + errorCode + ",errorMsg==" + errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    /**
     * 设置相关参数
     */
    private void setParams() {
        if (bakerSynthesizer == null) {
            return;
        }
        /**********************以下是必填参数**************************/
        //设置要转为语音的合成文本
        bakerSynthesizer.setText(editText.getText().toString().trim());
        //设置返回数据的callback
        bakerSynthesizer.setBakerCallback(bakerCallback);
        /**********************以下是选填参数**************************/
        //设置播放的语速，在50～200之间，不传时默认为105
        bakerSynthesizer.setSpeed(105);
        //设置语音的音量，在50～200之间（只支持整型值），不传时默认值为105
        bakerSynthesizer.setVolume(105);
        
        /**
         * 可不填，不填时默认为4, 16K采样率的pcm格式
         * audiotype=4 ：返回16K采样率的pcm格式
         * audiotype=5 ：返回8K采样率的pcm格式
         * audiotype=6 ：返回16K采样率的wav格式
         * audiotype=6&rate=1 ：返回8K的wav格式
         */
//        bakerSynthesizer.setAudioType(BakerConstants.AUDIO_TYPE_PCM_16K);
    }

    @Override
    protected void onDestroy() {
        audioTrackPlayer.stop();
        if (bakerSynthesizer != null) {
            bakerSynthesizer.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                //开始合成，合成结束后会自动stop
                setParams();
                bakerSynthesizer.start();
                if (audioTrackPlayer != null) {
                    audioTrackPlayer.play();
                }
                break;
            case R.id.stop:
                audioTrackPlayer.stop();
                break;
            case R.id.voice_1:
                checkImg1.setVisibility(View.VISIBLE);
                checkImg2.setVisibility(View.GONE);
                checkImg3.setVisibility(View.GONE);
                checkImg4.setVisibility(View.GONE);
                checkImg5.setVisibility(View.GONE);
                switchVoice("标准女声");
                break;
            case R.id.voice_2:
                checkImg1.setVisibility(View.GONE);
                checkImg2.setVisibility(View.VISIBLE);
                checkImg3.setVisibility(View.GONE);
                checkImg4.setVisibility(View.GONE);
                checkImg5.setVisibility(View.GONE);
                switchVoice("甜美女声");
                break;
            case R.id.voice_3:
                checkImg1.setVisibility(View.GONE);
                checkImg2.setVisibility(View.GONE);
                checkImg3.setVisibility(View.VISIBLE);
                checkImg4.setVisibility(View.GONE);
                checkImg5.setVisibility(View.GONE);
                switchVoice("小君儿童");
                break;
            case R.id.voice_4:
                checkImg1.setVisibility(View.GONE);
                checkImg2.setVisibility(View.GONE);
                checkImg3.setVisibility(View.GONE);
                checkImg4.setVisibility(View.VISIBLE);
                checkImg5.setVisibility(View.GONE);
                switchVoice("标准男声");
                break;
            case R.id.voice_5:
                checkImg1.setVisibility(View.GONE);
                checkImg2.setVisibility(View.GONE);
                checkImg3.setVisibility(View.GONE);
                checkImg4.setVisibility(View.GONE);
                checkImg5.setVisibility(View.VISIBLE);
                switchVoice("磁性男声");
                break;
            default:
                break;
        }
    }

    private void switchVoice(String s) {
        if (bakerSynthesizer != null) {
            long res = bakerSynthesizer.setVoice(s);
            Log.d("Offline","switchVoice res = " + res);
        }
    }
}
