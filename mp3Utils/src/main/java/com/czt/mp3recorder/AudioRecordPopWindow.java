package com.czt.mp3recorder;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.czt.mp3recorder.util.FileUtils;
import com.czt.mp3recorder.util.TimeUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by davidinchina on 2016/12/15.
 * 用来单纯录音的popwindow,支持暂停和继续
 */

public class AudioRecordPopWindow extends PopupWindow implements View.OnClickListener {
    private ImageView iv_audio_save, iv_audio_delete, iv_audio_stop, iv_record_flash;
    private TextView tv_record_durance;
    private String audioPath;//音频文件地址
    private static double audioDuration;//音频时长,秒
    private View mMenuView;
    private Context mContext;

    private AudioRecordBean mRecorder;
    private EndRecordListener listener;


    public interface EndRecordListener {
        public void recordFinish(boolean cancel, String audioPath, double audioDuRation);
    }

    public static double getDuration() {
        return audioDuration;
    }

    public AudioRecordPopWindow(Context context, String filePath, EndRecordListener listener) {
        super(context);
        this.listener = listener;
        this.mContext = context;
        this.audioPath = filePath;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.record_widow_layout, null);
        iv_audio_save = (ImageView) mMenuView.findViewById(R.id.iv_audio_save);
        iv_audio_stop = (ImageView) mMenuView.findViewById(R.id.iv_audio_stop);
        iv_audio_delete = (ImageView) mMenuView.findViewById(R.id.iv_audio_delete);
        iv_record_flash = (ImageView) mMenuView.findViewById(R.id.iv_record_flash);
        tv_record_durance = (TextView) mMenuView.findViewById(R.id.tv_record_durance);
        //设置按钮监听
        iv_audio_delete.setOnClickListener(this);
        iv_audio_save.setOnClickListener(this);
        iv_audio_stop.setOnClickListener(this);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //设置背景颜色为半透明
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(lp);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.iv_record_flash).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        //这里要判定没有正在录音
                        dismiss();
                    }
                }
                return true;
            }
        });
        FileUtils.deleteFile(audioPath);
        String beginTime = TimeUtils.getCurrentTimeInString("yyyy-MM-dd HH:mm:ss");
        //7.开始录音
        try {
            mRecorder = new AudioRecordBean();
            mRecorder.setmRecorder(new MP3Recorder(new File(audioPath)));
            mRecorder.setFilePath(audioPath);
            mRecorder.setRecordSeconds(0);
            mRecorder.setBeginTime(beginTime);
            mRecorder.getmRecorder().startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(mCountTime).start();//开始计时
        iv_audio_stop.setImageResource(R.mipmap.btn_stop_normal);
        //开始录音动画
        iv_record_flash.setImageResource(R.drawable.audio_player);
        AnimationDrawable rocketAnimation = (AnimationDrawable) iv_record_flash.getDrawable();
        rocketAnimation.start();
    }

    private Runnable mCountTime = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (null != mRecorder) {
                try {
                    Thread.sleep(100);
                    if (null != mRecorder && mRecorder.getmRecorder().isRecording()) {
                        mRecorder.setRecordSeconds(mRecorder.getRecordSeconds() + 0.1);
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_record_durance.setText(TimeUtils.formatSeconds((Double.valueOf(mRecorder.getRecordSeconds())).intValue()));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_audio_delete) {
            if (null != mRecorder) {//在录音,关闭录音
                //8.停止捕获
                mRecorder.getmRecorder().stopRecording();
                String endTime = TimeUtils.getCurrentTimeInString("yyyy-MM-dd HH:mm:ss");
                mRecorder.setEndTime(endTime);
                if (mRecorder.getmRecorder().isRecording()) {
                    //停止动画
                    AnimationDrawable rocketAnimation = (AnimationDrawable) iv_record_flash.getDrawable();
                    rocketAnimation.stop();
                    iv_record_flash.setImageResource(R.mipmap.img_flash_01);
                }
                mRecorder = null;
            }
            FileUtils.deleteFile(audioPath);//删除已录制音频文件
            listener.recordFinish(true, "", 0);//通知录制结束,录制取消
            dismiss();
        } else if (v.getId() == R.id.iv_audio_save) {
            if (null != mRecorder) {
                //8.停止捕获
                mRecorder.getmRecorder().stopRecording();
                String endTime = TimeUtils.getCurrentTimeInString("yyyy-MM-dd HH:mm:ss");
                mRecorder.setEndTime(endTime);
                audioDuration = mRecorder.getRecordSeconds();
                if (mRecorder.getmRecorder().isRecording()) {
                    //停止动画
                    AnimationDrawable rocketAnimation = (AnimationDrawable) iv_record_flash.getDrawable();
                    rocketAnimation.stop();
                    iv_record_flash.setImageResource(R.mipmap.img_flash_01);
                }
                mRecorder = null;

                listener.recordFinish(false, audioPath, audioDuration);
//                    ToastUtils.showInfo(mContext,"返回数据");
                dismiss();
                //这里返回数据
            }
        } else if (v.getId() == R.id.iv_audio_stop) {
            if (null != mRecorder) {
                if (mRecorder.getmRecorder().isRecording()) {
                    //录音暂停
                    mRecorder.getmRecorder().pauseRecording();
                    iv_audio_stop.setImageResource(R.mipmap.btn_play_normal2);
                    //停止动画
                    AnimationDrawable rocketAnimation = (AnimationDrawable) iv_record_flash.getDrawable();
                    rocketAnimation.stop();
                    iv_record_flash.setImageResource(R.mipmap.img_flash_01);
                } else {
                    //录音继续
                    mRecorder.getmRecorder().resumeRecording();
                    iv_audio_stop.setImageResource(R.mipmap.btn_stop_normal);
                    //开始录音动画
                    iv_record_flash.setImageResource(R.drawable.audio_player);
                    AnimationDrawable rocketAnimation = (AnimationDrawable) iv_record_flash.getDrawable();
                    rocketAnimation.start();
                }
            }
        }
    }


}
