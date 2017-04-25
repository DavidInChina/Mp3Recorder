package com.czt.mp3recorder;

/**
 * Created by davidinchina on 2016/12/10.
 */

public class AudioRecordBean {
    //声明Mp3录音机
    private MP3Recorder mRecorder;
    //录音时长
    private double recordSeconds = 0;
    //通话开始时间
    private String beginTime;
    //通话结束时间
    private String endTime;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public AudioRecordBean() {
    }

    public AudioRecordBean(MP3Recorder mRecorder, double recordSeconds, String beginTime, String endTime) {
        this.mRecorder = mRecorder;
        this.recordSeconds = recordSeconds;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public MP3Recorder getmRecorder() {
        return mRecorder;
    }

    public void setmRecorder(MP3Recorder mRecorder) {
        this.mRecorder = mRecorder;
    }

    public double getRecordSeconds() {
        return recordSeconds;
    }

    public void setRecordSeconds(double recordSeconds) {
        this.recordSeconds = recordSeconds;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
