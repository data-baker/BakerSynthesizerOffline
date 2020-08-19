package com.baker.synthesis.offline.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baker.synthesis.offline.BakerMediaCallback;
import com.baker.synthesis.offline.OfflineBakerSynthesizer;

/**
 * @author hsj55
 */
public class OfflineMediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private OfflineBakerSynthesizer bakerSynthesizer;
    private EditText editText;
    private TextView resultTv;
    private ImageView checkImg1, checkImg2, checkImg3, checkImg4, checkImg5;
    private StringBuffer buffer = new StringBuffer();

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
        setContentView(R.layout.activity_offline_media_player);

        initView();

        //初始化sdk
        bakerSynthesizer = new OfflineBakerSynthesizer(OfflineMediaPlayerActivity.this,
                SharedPreferencesUtil.getClientId(),
                SharedPreferencesUtil.getClientSecret());
    }

    private void initView() {
        editText = findViewById(R.id.edit_content);
        resultTv = findViewById(R.id.tv);
        resultTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pauseOrPlay).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.isPlay).setOnClickListener(this);
        findViewById(R.id.playDuration).setOnClickListener(this);
        findViewById(R.id.duration).setOnClickListener(this);
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

    BakerMediaCallback bakerMediaCallback = new BakerMediaCallback() {

        @Override
        public void onPrepared() {
            appendResult("\n合成准备就绪");
            if (bakerSynthesizer != null) {
                bakerSynthesizer.bakerPlay();
            }
        }

        @Override
        public void onCacheAvailable(int percentsAvailable) {
            appendResult("\n缓存进度：" + percentsAvailable + "%");
        }

        @Override
        public void onCompletion() {
            appendResult("\n播放结束");
//            setParams();
//            bakerSynthesizer.start();
        }

        @Override
        public void onError(final int errorCode, final String errorMsg, String traceId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OfflineMediaPlayerActivity.this,
                            "errorCode==" + errorCode + ",errorMsg==" + errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("Offline","--onError-- errorCode=" + errorCode + ", errorMsg=" + errorMsg + ",traceId==" + traceId);
        }

        @Override
        public void playing() {
            appendResult("\n播放啦");
        }

        @Override
        public void noPlay() {
            appendResult("\n没有播放啦");
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
        bakerSynthesizer.setBakerCallback(bakerMediaCallback);
        /**********************以下是选填参数**************************/
        //设置播放的语速，在50～200之间，不传时默认为105
        bakerSynthesizer.setSpeed(105);
        //设置语音的音量，在0～9之间（只支持整型值），不传时默认值为5
        bakerSynthesizer.setVolume(5);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                setParams();
                bakerSynthesizer.start();
                Log.d("Offline","--start--");
                break;
            case R.id.pauseOrPlay:
                if (bakerSynthesizer != null) {
                    boolean isPlaying = bakerSynthesizer.isPlaying();
                    if (isPlaying) {
                        bakerSynthesizer.bakerPause();
                        appendResult("\n暂停");
                    } else {
                        bakerSynthesizer.bakerPlay();
                        appendResult("\n播放");
                    }
                }
                break;
            case R.id.stop:
                if (bakerSynthesizer != null) {
                    bakerSynthesizer.bakerStop();
                    appendResult("\n停止播放");
                }
                break;
            case R.id.isPlay:
                if (bakerSynthesizer != null) {
                    boolean isPlaying = bakerSynthesizer.isPlaying();
                    appendResult("\n当前播放状态：" + isPlaying);
                }
                break;
            case R.id.playDuration:
                if (bakerSynthesizer != null) {
                    int currentPosition = bakerSynthesizer.getCurrentPosition();
                    appendResult("\n当前播放至：" + currentPosition + "秒");
                }
                break;
            case R.id.duration:
                if (bakerSynthesizer != null) {
                    int duration = bakerSynthesizer.getDuration();
                    appendResult("\n音频总长度：" + duration + "秒");
                }
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
        if (bakerSynthesizer!= null) {
            long res = bakerSynthesizer.setVoice(s);
            Log.d("Offline","switchVoice res = " + res);
        }
    }

    private void appendResult(final String str) {
        resultTv.post(new Runnable() {
            @Override
            public void run() {
                resultTv.append(str);
                int scrollAmount = resultTv.getLayout().getLineTop(resultTv.getLineCount())
                        - resultTv.getHeight();
                if (scrollAmount > 0)
                    resultTv.scrollTo(0, scrollAmount);
                else
                    resultTv.scrollTo(0, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (bakerSynthesizer != null) {
            bakerSynthesizer.onDestroy();
        }
        super.onDestroy();
    }
}
