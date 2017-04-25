package com.davidinchina.recorder;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czt.mp3recorder.AudioRecordPopWindow;
import com.czt.mp3recorder.util.FileUtils;
import com.czt.mp3recorder.util.TimeUtils;
import com.czt.mp3recorder.util.Util;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AudioRecordPopWindow.EndRecordListener {
    private Button btnRecord;
    private TextView tvRecordPath;
    private ImageView ivAudioPlay;
    private FrameLayout flPlayRecord;
    private static final int REQUEST_CODE_SETTING = 300;
    private MediaPlayer player;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        flPlayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlaying();
            }
        });

    }

    public void initView() {
        btnRecord = (Button) findViewById(R.id.btn_record);
        tvRecordPath = (TextView) findViewById(R.id.tv_record_path);
        ivAudioPlay = (ImageView) findViewById(R.id.iv_audio_play);
        flPlayRecord = (FrameLayout) findViewById(R.id.fl_play_record);
    }

    public void requestPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(100)
    private void getLocationYes(List<String> grantedPermissions) {
        requestAudio();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private void getLocationNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        }
    }

    public void requestAudio() {
        //这里是录音点击事件
        String root = Environment.getExternalStorageDirectory().getPath() + "/"
                + "/AudioRecord";
        FileUtils.createFolder(root);//生成文件夹
        String AUDIO_FILE_PATH = root + "/" + TimeUtils.getCurrentMillis() + ".mp3";
        AudioRecordPopWindow recordPopWindow = new AudioRecordPopWindow(this, AUDIO_FILE_PATH, this);
        recordPopWindow.showAtLocation(findViewById(R.id.lin_content_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void recordFinish(boolean cancel, String audioPath, double audioDuRation) {
        //之类是录音结束的回调
        if (cancel) {
            Toast.makeText(this, "录制已取消", Toast.LENGTH_SHORT);
        } else {
            filePath = audioPath;
            tvRecordPath.setText(filePath);
        }
    }

    /**
     * 播放或者暂停
     */
    public void togglePlaying() {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });
    }

    private boolean isPlaying() {
        try {
            return player != null && player.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    private void startPlaying() {
        if (!"".equals(filePath))
            try {
                player = new MediaPlayer();
                player.setDataSource(filePath);
                player.prepare();
                ivAudioPlay.setVisibility(View.GONE);
                player.start();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        ivAudioPlay.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void stopPlaying() {
        if (player != null) {
            try {
                player.stop();
                ivAudioPlay.setVisibility(View.VISIBLE);
                player.reset();
            } catch (Exception e) {
            }
        }

    }
}
