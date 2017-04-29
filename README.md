# Mp3Recorder

 
Android平台音频录制工具，可以录制不限时长的mp3文件，支持暂停录制、取消录制，采用弹出录制窗口方式。	

  ![demo](https://image.ibb.co/jDzay5/device_2017_04_29_102624.png)
  ![demo](https://image.ibb.co/bt3Dkk/device_2017_04_29_102654.png)


# Installation

* 暂时未上传jCenter
```gradle
    compile '*'
```
# Permissions
需要以下两种权限：
 ```java
  <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 ```
# Usage
  
  在调用页面添加如下方法:
  * 调用 **开始录音**:
 ```java
  String root = Environment.getExternalStorageDirectory().getPath() + "/"
                + "/AudioRecord";
        FileUtils.createFolder(root);
        String AUDIO_FILE_PATH = root + "/" + TimeUtils.getCurrentMillis() + ".mp3";//设置音频文件存储位置
        AudioRecordPopWindow recordPopWindow = new AudioRecordPopWindow(this, AUDIO_FILE_PATH, this);
        recordPopWindow.showAtLocation(findViewById(R.id.lin_content_view),//设置popWindow弹出位置
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
 ```
 

 在添加调用代码后添加回调方法，实现 AudioRecordPopWindow.EndRecordListener接口:
 ```java
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
 ```

 
# License
```license
Copyright 2017 DavidinChina

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
