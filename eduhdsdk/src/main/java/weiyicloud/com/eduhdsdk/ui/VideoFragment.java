package weiyicloud.com.eduhdsdk.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.classroomsdk.Config;
import com.classroomsdk.ShareDoc;
import com.classroomsdk.WhiteBoradManager;
import com.talkcloud.roomsdk.IRoomVideoWhiteBoard;
import com.talkcloud.roomsdk.RoomControler;
import com.talkcloud.roomsdk.RoomManager;
import com.talkcloud.roomsdk.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.EglRenderer;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.text.SimpleDateFormat;
import java.util.Date;

import weiyicloud.com.eduhdsdk.R;
import weiyicloud.com.eduhdsdk.message.JSVideoWhitePadInterface;
import weiyicloud.com.eduhdsdk.message.RoomSession;
import weiyicloud.com.eduhdsdk.message.VideoWBCallback;

//import weiyicloud.com.eduhdsdk.adapter.MediaEXListEXAdapter;

//import weiyicloud.com.eduhdsdk.adapter.MediaListAdapter;


public class VideoFragment extends Fragment implements IRoomVideoWhiteBoard, VideoWBCallback {

    private static String sync = "";
    static private VideoFragment mInstance = null;
    private View fragmentView;
    //MP4视频
    LinearLayout lin_video_play;
    SurfaceViewRenderer suf_mp4;

    ImageView img_close_mp4;
    LinearLayout lin_video_control;
    ImageView img_play_mp4;
    TextView txt_mp4_name;
    TextView txt_mp4_time;
    SeekBar sek_mp4;
    ImageView img_voice_mp4;
    XWalkView xWalkView;
    SeekBar sek_voice_mp4;
    //    AudioManager audioManager;
    boolean isPlay = true;
    private Stream stream;
    private double vol = 0.5;
    private boolean isMute = false;
    private double ratio = (double) 16 / (double) 9;

    private EglRenderer.FrameListener frameListener;
    private RelativeLayout re_laoding;
    private ImageView loadingImageView;
    private AnimationDrawable animationDrawable;

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static public VideoFragment getInstance() {
        synchronized (sync) {
            if (mInstance == null) {
                mInstance = new VideoFragment();
            }
            return mInstance;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {
            RoomManager.getInstance().setVideoWhiteBoard(this);
            fragmentView = inflater.inflate(R.layout.fragment_video, null);
            fragmentView.bringToFront();
            //mp4
            lin_video_play = (LinearLayout) fragmentView.findViewById(R.id.lin_video_play);


            suf_mp4 = (SurfaceViewRenderer) fragmentView.findViewById(R.id.suf_mp4);
            suf_mp4.init(EglBase.create().getEglBaseContext(), null);

            re_laoding = (RelativeLayout) fragmentView.findViewById(R.id.re_laoding);
            loadingImageView = (ImageView) fragmentView.findViewById(R.id.loadingImageView);

            img_close_mp4 = (ImageView) fragmentView.findViewById(R.id.img_close_mp4);
            lin_video_control = (LinearLayout) fragmentView.findViewById(R.id.lin_video_control);
            img_play_mp4 = (ImageView) fragmentView.findViewById(R.id.img_play_mp4);
            txt_mp4_name = (TextView) fragmentView.findViewById(R.id.txt_mp4_name);
            txt_mp4_time = (TextView) fragmentView.findViewById(R.id.txt_mp4_time);
            sek_mp4 = (SeekBar) fragmentView.findViewById(R.id.sek_mp4);
            img_voice_mp4 = (ImageView) fragmentView.findViewById(R.id.img_voice_mp4);
            sek_voice_mp4 = (SeekBar) fragmentView.findViewById(R.id.sek_voice_mp4);
            xWalkView = (XWalkView) fragmentView.findViewById(R.id.video_white_board);
            XWalkPreferences.setValue("enable-javascript", true);
            XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
            XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);

            XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
            XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);

//            xWalkView.setZOrderOnTop(true);
            XWalkSettings webs = xWalkView.getSettings();
            webs.setJavaScriptEnabled(true);
            webs.setCacheMode(WebSettings.LOAD_DEFAULT);
            webs.setDomStorageEnabled(true);
            webs.setDatabaseEnabled(true);
            webs.setAllowFileAccess(true);
            webs.setSupportZoom(false);
            webs.setBuiltInZoomControls(false);

            webs.setLoadWithOverviewMode(false);
            webs.setJavaScriptCanOpenWindowsAutomatically(true);
            webs.setLoadWithOverviewMode(true);
            webs.setDomStorageEnabled(true);
            webs.setUseWideViewPort(true);
            webs.setMediaPlaybackRequiresUserGesture(false);
            webs.setSupportSpatialNavigation(true);
            webs.setAllowFileAccessFromFileURLs(true);

            webs.setLayoutAlgorithm(XWalkSettings.LayoutAlgorithm.NORMAL);
            webs.setUseWideViewPort(true);

            xWalkView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            xWalkView.setHorizontalScrollBarEnabled(false);
            JSVideoWhitePadInterface.getInstance().setVideoWBCallBack(this);
            xWalkView.addJavascriptInterface(JSVideoWhitePadInterface.getInstance(), "JSVideoWhitePadInterface");
            xWalkView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            xWalkView.setBackgroundColor(0);

            xWalkView.setUIClient(new XWalkUIClient(xWalkView) {
                @Override
                protected Object getBridge() {
                    return super.getBridge();
                }

                @Override
                public void onPageLoadStarted(XWalkView view, String url) {
                    super.onPageLoadStarted(view, url);
                }

                @Override
                public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                    super.onPageLoadStopped(view, url, status);

                }
            });
            if (RoomSession.isShowVideoWB) {
//                if(suf_mp4!=null){
//                    suf_mp4.setZOrderMediaOverlay(false);
//                    suf_mp4.setZOrderOnTop(false);
//                }
                xWalkView.setVisibility(View.VISIBLE);
                xWalkView.setZOrderOnTop(true);

            } else {
                xWalkView.setVisibility(View.INVISIBLE);
            }
            if (Config.isWhiteVideoBoardTest) {
                xWalkView.loadUrl("http://192.168.1.182:1314/publish/index.html#/mobileApp_videoWhiteboard");//广生
            } else {
                xWalkView.loadUrl("file:///android_asset/react_mobile_video_whiteboard_publishdir/index.html#/mobileApp_videoWhiteboard");
            }
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
//        audioManager = (AudioManager) getActivity().getSystemService(Service.AUDIO_SERVICE);
//        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 75, 0);
//        mediaPlay();
        return fragmentView;
    }

    public void setVoice() {
        if (img_voice_mp4 != null && sek_voice_mp4 != null) {
            if (isMute) {
                RoomManager.getInstance().setRemoteStreamAudioVolume(0);
                img_voice_mp4.setImageResource(R.drawable.btn_mute_pressed);
                sek_voice_mp4.setProgress(0);
            } else {
                RoomManager.getInstance().setRemoteStreamAudioVolume(vol);
                img_voice_mp4.setImageResource(R.drawable.btn_volume_pressed);
                sek_voice_mp4.setProgress((int) (vol * 100));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        re_laoding.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
        animationDrawable.start();

        //LoadingProgressDialog.createDialog(getActivity());

        if (stream != null) {
            suf_mp4.setZOrderOnTop(true);
            suf_mp4.setZOrderMediaOverlay(true);
           /* suf_mp4.setEnableHardwareScaler(true);*/
            suf_mp4.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
            RoomManager.getInstance().playVideo(stream.getExtensionId(), suf_mp4);
            suf_mp4.requestLayout();
            if (txt_mp4_name != null) {
                txt_mp4_name.setText((String) stream.attrMap.get("filename"));
            }
        }
        lin_video_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if (RoomManager.getInstance().getMySelf().role == 0) {
            lin_video_control.setVisibility(View.VISIBLE);
            img_close_mp4.setVisibility(View.VISIBLE);
        } else {
            lin_video_control.setVisibility(View.INVISIBLE);
            img_close_mp4.setVisibility(View.INVISIBLE);
        }
        img_close_mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
                RoomManager.getInstance().unPublishMedia();
//                ((RoomActivity)getActivity()).removeVideoFragment();
                //LoadingProgressDialog.HideProgressDialog();
            }
        });
        img_play_mp4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (RoomSession.isPublish) {
                            boolean ispause = (Boolean) stream.attrMap.get("pause") == null ? false : (Boolean) stream.attrMap.get("pause");
                            RoomManager.getInstance().playMedia(ispause);
                            if (ispause) {
                                if (RoomManager.getInstance().getMySelf().role == 0 && RoomSession.isClassBegin && RoomControler.isShowVideoWhiteBoard()) {
                                    RoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
                                }
                            } else {
                                if (RoomManager.getInstance().getMySelf().role == 0 && RoomSession.isClassBegin && RoomControler.isShowVideoWhiteBoard()) {
                                    JSONObject js = new JSONObject();
                                    try {
                                        js.put("videoRatio", ratio);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    RoomManager.getInstance().pubMsg("VideoWhiteboard", "VideoWhiteboard", "__all", js.toString(), true, "ClassBegin", null);
                                }
                            }
                        } else {
                            ShareDoc media = WhiteBoradManager.getInstance().getCurrentMediaDoc();
                            WhiteBoradManager.getInstance().setCurrentMediaDoc(media);
                            String strSwfpath = media.getSwfpath();
                            int pos = strSwfpath.lastIndexOf('.');
                            strSwfpath = String.format("%s-%d%s", strSwfpath.substring(0, pos), 1, strSwfpath.substring(pos));
                            String url = "http://" + WhiteBoradManager.getInstance().getFileServierUrl() + ":" + WhiteBoradManager.getInstance().getFileServierPort() + strSwfpath;
                            if (RoomSession.isClassBegin) {
                                RoomManager.getInstance().publishMedia(url, com.classroomsdk.Tools.isMp4(media.getFilename()), media.getFilename(), media.getFileid(), "__all");
                            } else {
                                RoomManager.getInstance().publishMedia(url, com.classroomsdk.Tools.isMp4(media.getFilename()), media.getFilename(), media.getFileid(), RoomManager.getInstance().getMySelf().peerId);
                            }
                        }
                    }
                });

        sek_mp4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pro = 0;
            boolean isfromUser = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    pro = progress;
                    isfromUser = fromUser;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double currenttime = 0;
                if (isfromUser && stream != null) {
                    currenttime = ((double) pro / (double) seekBar.getMax()) * (int) stream.attrMap.get("duration");
                    RoomManager.getInstance().seekMedia((long) currenttime);
                }
            }
        });
        RoomManager.getInstance().setRemoteStreamAudioVolume(vol);
        img_voice_mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMute) {
                    RoomManager.getInstance().setRemoteStreamAudioVolume(vol);
                    img_voice_mp4.setImageResource(R.drawable.btn_volume_pressed);
                    sek_voice_mp4.setProgress((int) (vol * 100));
                } else {
                    RoomManager.getInstance().setRemoteStreamAudioVolume(0);
                    img_voice_mp4.setImageResource(R.drawable.btn_mute_pressed);
                    sek_voice_mp4.setProgress(0);
                }
                isMute = !isMute;
            }
        });
        sek_voice_mp4.setProgress((int) (vol * 100));
        sek_voice_mp4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = (float) progress / (float) seekBar.getMax();
                if (vol > 0) {
                    img_voice_mp4.setImageResource(R.drawable.btn_volume_pressed);
                } else {
                    img_voice_mp4.setImageResource(R.drawable.btn_mute_pressed);
                }
                RoomManager.getInstance().setRemoteStreamAudioVolume(vol);
                if (fromUser) {
                    VideoFragment.this.vol = vol;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        frameListener = new EglRenderer.FrameListener() {
            @Override
            public void onFrame(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null && animationDrawable.isRunning()) {
                            animationDrawable.stop();
                        }
                        re_laoding.setVisibility(View.GONE);
                    }
                });
            }
        };
        suf_mp4.addFrameListener(frameListener, 0);


        if (xWalkView != null) {
            xWalkView.onShow();
            DisplayMetrics dm = new DisplayMetrics();
            if (getActivity() != null) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                final int wid = dm.widthPixels;
                final int hid = dm.heightPixels;
                transmitWindowSize(wid, hid);
            }
        }
    }

    public void controlMedia(Stream stream, long pos, boolean isPlay) {
        if (sek_mp4 != null) {
            int curtime = (int) ((double) pos / (int) stream.attrMap.get("duration") * 100);
            sek_mp4.setProgress(curtime);
        }
        if (img_play_mp4 != null) {
            if (!isPlay) {
                img_play_mp4.setImageResource(R.drawable.btn_pause_normal);
            } else {
                img_play_mp4.setImageResource(R.drawable.btn_play_normal);
            }
        }
        if (txt_mp4_time != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
            Date curDate = new Date(pos);//获取当前时间
            Date daDate = new Date((int) stream.attrMap.get("duration"));
            String strcur = formatter.format(curDate);
            String strda = formatter.format(daDate);
            txt_mp4_time.setText(strcur + "/" + strda);
        }
        if (txt_mp4_name != null) {
            txt_mp4_name.setText((String) stream.attrMap.get("filename"));
        }
        if (stream.attrMap.containsKey("width") && stream.attrMap.containsKey("height") && ((int) stream.attrMap.get("width")) != 0 && ((int) stream.attrMap.get("height")) != 0) {
            int wid = (int) stream.attrMap.get("width");
            int hid = (int) stream.attrMap.get("height");
            ratio = (double) wid / (double) hid;
        }
    }

    @Override
    public void onStop() {
        if (xWalkView != null) {
            xWalkView.onHide();
        }

        if (suf_mp4 != null && frameListener != null) {
            suf_mp4.removeFrameListener(frameListener);
            frameListener = null;
        }

       /* LoadingProgressDialog.HideProgressDialog();*/
        super.onStop();
    }

    @Override
    public void onDestroyView() {
//        RoomManager.getInstance().unPlayVideo(stream.getExtensionId());
        RoomSession.jsVideoWBTempMsg = new JSONArray();
        suf_mp4.release();
        isMute = false;
        suf_mp4 = null;
        if (xWalkView != null) {
            xWalkView.removeAllViews();
            xWalkView.onDestroy();
            xWalkView = null;
        }

        super.onDestroyView();
        RoomManager.getInstance().setVideoWhiteBoard(null);
        mInstance = null;
    }

    @Override
    public boolean onRemoteMsg(boolean add, String id, String name, long ts, Object data, String fromID, String associatedMsgID, String associatedUserID) {
        if (add) {
            if (name.equals("VideoWhiteboard")) {
                if (xWalkView != null) {
                    if (suf_mp4 != null) {
                        suf_mp4.setZOrderMediaOverlay(false);
                        suf_mp4.setZOrderOnTop(false);
                    }
                    xWalkView.setVisibility(View.VISIBLE);
                    xWalkView.setZOrderOnTop(true);
//                    if (Config.isWhiteVideoBoardTest) {
//                        xWalkView.loadUrl("http://192.168.1.182:1314/publish/index.html#/mobileApp_videoWhiteboard");//广生
//                    } else {
//                        xWalkView.loadUrl("file:///android_asset/react_mobile_video_whiteboard_publishdir/index.html#/mobileApp_videoWhiteboard");
//                    }
                }
            }
        } else {
            if (name.equals("VideoWhiteboard")) {
                RoomSession.jsVideoWBTempMsg = new JSONArray();
                if (xWalkView != null) {
                    if (suf_mp4 != null) {
                        suf_mp4.setZOrderMediaOverlay(true);
                    }
                    xWalkView.setVisibility(View.INVISIBLE);
                    xWalkView.setZOrderOnTop(false);
                }
            }
        }

        JSONObject jsobj = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            jsobj.put("id", id);
            jsobj.put("ts", ts);
            jsobj.put("data", data == null ? null : data.toString());
            jsobj.put("name", name);
            jsobj.put("fromID", fromID);
            if (!associatedMsgID.equals("")) {
                jsobj.put("associatedMsgID", associatedMsgID);
            }
            if (!associatedUserID.equals("")) {
                jsobj.put("associatedUserID", associatedUserID);
            }
            if (add) {
                js.put("type", "room-pubmsg");
            } else {
                js.put("type", "room-delmsg");
            }
            js.put("message", jsobj);
            if (associatedMsgID.equals("VideoWhiteboard") || id.equals("VideoWhiteboard")) {
                xWalkView.loadUrl("javascript:MOBILETKSDK.receiveInterface.dispatchEvent(" + js + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void roomManagerRoomLeaved() {
        roomDisConnect();
    }

    @Override
    public void roomManagerPlayBackClearAll() {
        roomPlaybackClearAll();
    }

    @Override
    public void roomManagerRoomConnectFaild() {
        roomDisConnect();
    }

    private void roomPlaybackClearAll() {
        JSONObject js = new JSONObject();
        try {
            js.put("type", "room-playback-clear_all");
            xWalkView.loadUrl("javascript:MOBILETKSDK.receiveInterface.dispatchEvent(" + js.toString() + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void roomDisConnect() {
        if (xWalkView != null) {
            JSONObject js = new JSONObject();
            try {
                js.put("type", "room-disconnected");
                xWalkView.loadUrl("javascript:MOBILETKSDK.receiveInterface.dispatchEvent(" + js.toString() + ")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pubMsg(String js) {
        Log.d("xiao", js);
        try {
            JSONObject jsobj = new JSONObject(js);
            String msgName = jsobj.optString("signallingName");
            String msgId = jsobj.optString("id");
            String toId = jsobj.optString("toID");
            String data = jsobj.optString("data");
            String associatedMsgID = jsobj.optString("associatedMsgID");
            String associatedUserID = jsobj.optString("associatedUserID");
            boolean save = jsobj.optBoolean("do_not_save", false);
            if (jsobj.has("do_not_save")) {
                save = false;
            } else {
                save = true;
            }
            RoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delMsg(String js) {
        Log.d("xiao", js);
        try {
            JSONObject jsobj = new JSONObject(js);
            String msgName = jsobj.optString("signallingName");
            String msgId = jsobj.optString("id");
//            String toId = jsobj.optString("toID");
            String toId = jsobj.optString("toID");
            String data = jsobj.optString("data");
//            Map<String,Object> datamap = new HashMap<String,Object>();
//            if(data!=null){
//                datamap =  toMap(data);
//            }
            boolean save = true;
            //������һ�����Ծ���dataӦ����jsonobject����������ֻ�ܷ���String���ǶԵġ�
            RoomManager.getInstance().delMsg(msgName, msgId, toId, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageFinished() {
        Log.d("xiao", "onPageFinished: ");

        final JSONObject j = new JSONObject();
        try {
            j.put("mClientType", 3);
            j.put("deviceType", 1);
            j.put("isSendLogMessage", true);
            j.put("debugLog", true);
            j.put("myself", RoomManager.getInstance().getMySelf().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    xWalkView.loadUrl("javascript:MOBILETKSDK.receiveInterface.setInitPageParameterFormPhone('" + j.toString() + "')");
                }
            });
        }
        if (RoomSession.jsVideoWBTempMsg.length() > 0) {
            final JSONObject js = new JSONObject();
            try {
                js.put("type", "room-msglist");
                js.put("message", RoomSession.jsVideoWBTempMsg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:MOBILETKSDK.receiveInterface.dispatchEvent(" + js.toString() + ")");
//                        RoomSession.jsVideoWBTempMsg = new JSONArray();
                    }
                });
            }
        }
    }

    public void transmitWindowSize(int wid, int hid) {
        JSONObject js = new JSONObject();
        try {
            js.put("type", "transmitWindowSize");
            JSONObject jsmsg = new JSONObject();
            jsmsg.put("windowWidth", wid);
            jsmsg.put("windowHeight", hid);
            js.put("windowSize", jsmsg);
            if (xWalkView != null) {
                xWalkView.loadUrl("javascript:MOBILETKSDK.receiveInterface.dispatchEvent(" + js.toString() + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
