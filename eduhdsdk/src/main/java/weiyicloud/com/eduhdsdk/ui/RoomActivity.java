package weiyicloud.com.eduhdsdk.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.classroomsdk.ShareDoc;
import com.classroomsdk.WBFragment;
import com.classroomsdk.WBStateCallBack;
import com.classroomsdk.WhiteBoradManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.talkcloud.roomsdk.IRoomManagerCbk;
import com.talkcloud.roomsdk.RoomControler;
import com.talkcloud.roomsdk.RoomManager;
import com.talkcloud.roomsdk.RoomUser;
import com.talkcloud.roomsdk.Stream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.SurfaceViewRenderer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weiyicloud.com.eduhdsdk.R;
import weiyicloud.com.eduhdsdk.adapter.ChatListAdapter;
import weiyicloud.com.eduhdsdk.adapter.FaceGVAdapter;
import weiyicloud.com.eduhdsdk.adapter.FileListAdapter;
import weiyicloud.com.eduhdsdk.adapter.FileListEXAdapter;
import weiyicloud.com.eduhdsdk.adapter.MediaEXListEXAdapter;
import weiyicloud.com.eduhdsdk.adapter.MediaListAdapter;
import weiyicloud.com.eduhdsdk.adapter.MemberListAdapter;
import weiyicloud.com.eduhdsdk.entity.ChatData;
import weiyicloud.com.eduhdsdk.entity.MoveVideoInfo;
import weiyicloud.com.eduhdsdk.entity.TimeMessage;
import weiyicloud.com.eduhdsdk.entity.VideoItem;
import weiyicloud.com.eduhdsdk.interfaces.TranslateCallback;
import weiyicloud.com.eduhdsdk.message.BroadcastReceiverMgr;
import weiyicloud.com.eduhdsdk.message.RoomClient;
import weiyicloud.com.eduhdsdk.message.RoomSession;
import weiyicloud.com.eduhdsdk.tools.KeyBoardUtil;
import weiyicloud.com.eduhdsdk.tools.LayoutZoomOrIn;
import weiyicloud.com.eduhdsdk.tools.PersonInfo_ImageUtils;
import weiyicloud.com.eduhdsdk.tools.SoundPlayUtils;
import weiyicloud.com.eduhdsdk.tools.Tools;
import weiyicloud.com.eduhdsdk.tools.Translate;
import weiyicloud.com.eduhdsdk.tools.VideoTtemLayoutUtils;

public class RoomActivity extends FragmentActivity implements IRoomManagerCbk, View.OnClickListener, WBStateCallBack, TranslateCallback {

    WBFragment wbFragment;
    //视频块
    LinearLayout vdi_teacher;
    LinearLayout vdi_stu_in_sd;

    VideoItem teacherItem;
    VideoItem stu_in_sd;

    TextView txt_all_mute;
    TextView txt_all_unmute;
    TextView txt_all_send_gift;

    //顶部工具条
    ImageView img_back;
    LinearLayout lin_back_and_name;
    //    ImageView img_servers;
    TextView txt_room_name;
    ImageView img_file_list;
    ImageView img_media_list;
    ImageView img_member_list;
    LinearLayout lin_top_buttons;
    TextView txt_class_begin;
    //聊天区域
    RelativeLayout rel_chat_part;
    TextView txt_chat_input;
    TextView txt_send_msg;
    ListView list_chat;
    //时间
    TextView txt_hour;
    TextView txt_min;
    TextView txt_ss;
    TextView txt_mao_01;
    TextView txt_mao_02;
    //MP3音频
    ImageView img_disk;
    LinearLayout lin_audio_control;
    ImageView img_play_mp3;
    TextView txt_mp3_name;
    TextView txt_mp3_time;
    SeekBar sek_mp3;
    ImageView img_voice_mp3;
    SeekBar sek_voice_mp3;
    ImageView img_close_mp3;
    LinearLayout lin_control_tool;
    ImageView img_back_play_back;
    ArrayList<VideoItem> videoItems = new ArrayList<VideoItem>();
    HashMap<String, Boolean> playingMap = new HashMap<String, Boolean>();

    List<RoomUser> playingList = Collections.synchronizedList(new ArrayList<RoomUser>());
    ArrayList<RoomUser> memberList = new ArrayList<RoomUser>();
    ArrayList<ChatData> chatList = new ArrayList<ChatData>();

    MemberListAdapter memberListAdapter = new MemberListAdapter(this, memberList, RoomSession.publishSet);
    FileListAdapter fileListAdapter;
    MediaListAdapter mediaListAdapter;

    MediaEXListEXAdapter mediaEXListEXAdapter;
    FileListEXAdapter fileListEXAdapter;

    //ServerListAdapter serverListAdapter;

    ChatListAdapter chlistAdapter;

    PowerManager pm;
    PowerManager.WakeLock mWakeLock;

    //    public static int myRrole = -1;
    private int roomType = -1;
    //    public static boolean isClassBegin = false;
    private long localTime;
    private long classStartTime;
    private long serviceTime;
    Timer timerAddTime;
    Timer timerbefClassbegin;

    //    public static boolean isExit = false;
    Animation operatingAnim;
//    Timer t;

    private String host = "";
    private int port = 80;
    private String nickname = "";
    private String userid = "";
    private String serial = "";
    private String password = "";
    private int userrole = -1;
    private String path = "";
    private int type = -1;
    private String servername;

    private String param = "";
    private String domain = "";
    private String mobilename = "";
    private boolean mobilenameNotOnList = true;
    private AsyncHttpClient client = new AsyncHttpClient();

    private TextView txt_hand_up;
    private LinearLayout lin_hand_and_photo;
    private TextView txt_send_up_photo;
    private View v_students;
    private RelativeLayout rel_students;

    //需要计算尺寸的布局
    RelativeLayout rel_tool_bar;
    LinearLayout lin_time;
    LinearLayout lin_menu;
    LinearLayout lin_main;
    RelativeLayout rel_w;
    RelativeLayout rel_tool_bar_play_back;

    FragmentManager fragmentManager;
    FragmentTransaction ft;
    VideoFragment videofragment;
    FrameLayout fm_video_container;
    RelativeLayout rel_parent;
    RelativeLayout rel_wb_container;

    //    LinearLayout lin_self_av_control;
//    TextView txt_video;
//    TextView txt_audio;

    //回放使用的控件
    private RelativeLayout rel_play_back;
    private ImageView img_play_back;
    private SeekBar sek_play_back;
    private TextView txt_play_back_time;

    private boolean isInRoom = false;
    private final int REQUEST_CODED = 3;
    private boolean canClassDissMiss = false;
    int tipnum = 0;
    private boolean isZoom = false;
    private ArrayList<TimeMessage> timeMessages = new ArrayList<TimeMessage>();

    public static int maxVideo = 6;
    private boolean isBackApp = false;
    private boolean isVideoShow = false;

    private boolean processVideo = false;
    private ArrayList<String> unpublishlist = new ArrayList<String>();
    AudioManager audioManager;
    //    Timer checkNetTimer = null;
    int netBreakCount = 0;
    public static boolean isFromList;
    private String mp3Duration = "00:00";
    private Stream stream;
    private double vol = 0.5;
    private static boolean isMediaMute = false;
    private String sendgiftlock = "sendgiftlock";
    private boolean isSending = false;
    Timer tSendGift = null;
    private boolean isPlayBackPlay = true;
    private boolean isEnd = false;

    private ScreenFragment screenFragment;
    private PopupWindow popupWindowPhoto;

    JSONObject videoarr = null;
    Iterator<String> sIterator = null;
    Map<String, MoveVideoInfo> stuMoveInfoMap = new HashMap<String, MoveVideoInfo>();
    boolean isPauseLocalVideo = false;
    boolean isOpenCamera = false;
    NotificationManager nm = null;
    NotificationCompat.Builder mBuilder = null;

    private List<String> staticFacesList;
    private EditText edt_input;
    private GridView chart_face_gv;

    JSONArray screen = null;
    ArrayList<String> screenID = new ArrayList<>();
    private PopupWindow studentPopupWindow;
    private PopupWindow chatWindow;

    static final int NONE = 0;
    int mode = NONE; // 当前的事件
    static final int DRAG = 1; // 拖动中
    static final int ZOOM = 2; // 缩放中
    float beforeLenght; // 两触点距离
    float afterLenght; // 两触点距离
    double printWidth;
    double printHeight;
    Map<String, Float> scalemap = new HashMap<String, Float>();

    /*private ImageView img_teacher_screen;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_room);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initTimeMessage();
        resultActivityBackApp();
        lin_control_tool = (LinearLayout) findViewById(R.id.lin_control_tool);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back_play_back = (ImageView) findViewById(R.id.img_back_play_back);
        lin_back_and_name = (LinearLayout) findViewById(R.id.lin_back_and_name);
//        img_servers = (ImageView) findViewById(R.id.img_servers);
        txt_room_name = (TextView) findViewById(R.id.txt_pad_name);
        img_file_list = (ImageView) findViewById(R.id.img_file_list);
        img_media_list = (ImageView) findViewById(R.id.img_media_list);
        img_member_list = (ImageView) findViewById(R.id.img_member_list);
        lin_top_buttons = (LinearLayout) findViewById(R.id.lin_top_buttons);
        txt_class_begin = (TextView) findViewById(R.id.txt_class_begin);
        txt_all_mute = (TextView) findViewById(R.id.txt_all_mute);
        txt_all_unmute = (TextView) findViewById(R.id.txt_all_unmute);
        txt_all_send_gift = (TextView) findViewById(R.id.txt_all_send_gift);
        rel_chat_part = (RelativeLayout) findViewById(R.id.rel_chat_part);
        txt_chat_input = (TextView) findViewById(R.id.txt_chat_input);
        txt_send_msg = (TextView) findViewById(R.id.txt_send_msg);
        list_chat = (ListView) findViewById(R.id.list_chat);
        txt_hour = (TextView) findViewById(R.id.txt_hour);
        txt_min = (TextView) findViewById(R.id.txt_min);
        txt_ss = (TextView) findViewById(R.id.txt_ss);
        txt_hand_up = (TextView) findViewById(R.id.txt_hand_up);
        lin_hand_and_photo = (LinearLayout) findViewById(R.id.lin_hand_and_photo);
        txt_send_up_photo = (TextView) findViewById(R.id.txt_send_up_photo);

        v_students = (View) findViewById(R.id.v_student);
        rel_students = (RelativeLayout) findViewById(R.id.rel_students);
        rel_tool_bar = (RelativeLayout) findViewById(R.id.rel_tool_bar);
        rel_tool_bar_play_back = (RelativeLayout) findViewById(R.id.rel_tool_bar_play_back);

        lin_time = (LinearLayout) findViewById(R.id.lin_time);
        lin_menu = (LinearLayout) findViewById(R.id.lin_menu);
        lin_main = (LinearLayout) findViewById(R.id.lin_main);
        rel_w = (RelativeLayout) findViewById(R.id.rel_w);
        rel_parent = (RelativeLayout) findViewById(R.id.rel_parent);
        rel_wb_container = (RelativeLayout) findViewById(R.id.rel_wb_container);
        txt_mao_01 = (TextView) findViewById(R.id.txt_mao_01);
        txt_mao_02 = (TextView) findViewById(R.id.txt_mao_02);

        rel_play_back = (RelativeLayout) findViewById(R.id.rel_play_back);
        img_play_back = (ImageView) findViewById(R.id.img_play_back);
        sek_play_back = (SeekBar) findViewById(R.id.sek_play_back);
        txt_play_back_time = (TextView) findViewById(R.id.txt_play_back_time);

        fm_video_container = (FrameLayout) findViewById(R.id.video_container);

//        lin_self_av_control = (LinearLayout) findViewById(R.id.lin_self_av_control);
//        txt_video = (TextView) findViewById(R.id.txt_video);
//        txt_audio = (TextView) findViewById(R.id.txt_audio);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        getExData();
        initVideoItem();
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 75, 0);
        initMediaView();
        initPlayBackView();

        RoomManager.getInstance().globalInitialize(getApplicationContext());
        RoomManager.getInstance().setCallbBack(this);

        Translate.getInstance().setCallback(this);

        chlistAdapter = new ChatListAdapter(chatList, this);
        list_chat.setAdapter(chlistAdapter);

//        img_back.setOnClickListener(this);
        lin_back_and_name.setOnClickListener(this);
//        img_servers.setOnClickListener(this);

        img_file_list.setOnClickListener(this);
        img_media_list.setOnClickListener(this);
        img_member_list.setOnClickListener(this);
        txt_class_begin.setOnClickListener(this);
        txt_class_begin.setBackgroundResource(R.drawable.round_back_red_black);
        txt_class_begin.setClickable(false);
        txt_all_mute.setOnClickListener(this);
        txt_all_unmute.setOnClickListener(this);
        txt_all_send_gift.setOnClickListener(this);
        txt_send_msg.setOnClickListener(this);
        txt_chat_input.setOnClickListener(this);
        img_back_play_back.setOnClickListener(this);
        txt_send_up_photo.setOnClickListener(this);
//        txt_video.setOnClickListener(this);
//        txt_audio.setOnClickListener(this);

        operatingAnim = AnimationUtils.loadAnimation(RoomActivity.this, R.anim.disk_aim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        memberListAdapter = new MemberListAdapter(this, memberList, RoomSession.publishSet);

        fileListAdapter = new FileListAdapter(this, serial);
        fileListEXAdapter = new FileListEXAdapter(this, serial);
        mediaEXListEXAdapter = new MediaEXListEXAdapter(this, serial);

        fileListAdapter = new FileListAdapter(this, serial);
        mediaListAdapter = new MediaListAdapter(this, serial);

//        serverListAdapter = new ServerListAdapter(this);

//        checkNetTimer = new Timer();
//        checkNetTimer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                if (!isNetworkAvailable(RoomActivity.this) && isInRoom && netBreakCount == 10) {
//                    RoomActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            wbFragment.roomDisConnect();
//
//                        }
//                    });
//                }
//                netBreakCount++;
//            }
//        }, 0, 1000);
//        Tools.ShowProgressDialog(this, getResources().getString(R.string.joining_classroom));
        wbFragment = new WBFragment();

        if (path != null && !path.isEmpty()) {
            wbFragment.setPlayBack(true);
        }
        WhiteBoradManager.getInstance().setWBCallBack(this);
        WhiteBoradManager.getInstance().setFileServierUrl(host);
        WhiteBoradManager.getInstance().setFileServierPort(port);
        WhiteBoradManager.getInstance().setLocalControl(wbFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (!wbFragment.isAdded()) {
            ft.add(R.id.wb_container, wbFragment);
            ft.commit();
        }
        RoomManager.getInstance().setWhiteBoard(wbFragment);

        initStaticFaces();
        initWidAndHid();
        initPopupWindow();
    }

    private void initWidAndHid() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int disWid = 2048;
        printWidth = (int) (wid * ((double) 240 / disWid));
        printHeight = (int) ((printWidth * ((double) 224 / (double) 240)));
    }

    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            staticFacesList.clear();
            String[] faces = getAssets().list("face");
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((edt_input.getText()));
        int iCursorEnd = Selection.getSelectionEnd((edt_input.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) edt_input.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((edt_input.getText()));
        ((Editable) edt_input.getText()).insert(iCursor, text);
    }

    private void delete() {
        if (edt_input.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(edt_input.getText());
            int iCursorStart = Selection.getSelectionStart(edt_input.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "[em_1]";
                        ((Editable) edt_input.getText()).delete(iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) edt_input.getText()).delete(iCursorEnd - 1, iCursorEnd);
                    }
                } else {
                    ((Editable) edt_input.getText()).delete(iCursorStart, iCursorEnd);
                }
            }
        }
    }

    private boolean isDeletePng(int cursor) {
        String st = "[em_1]";
        String content = edt_input.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\[em_)\\d{1}(\\])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            String[] splitText = png.split("\\.");
            String tempText = "[" + splitText[0] + "]";
            Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open("face/" + png));
            Drawable drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, 50, 50);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            sb.append(tempText);
            sb.setSpan(span, sb.length() - tempText.length(), sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    private void initTimeMessage() {
        timeMessages.clear();
        String one = "<font color='#FFD700'>1</font>";
        TimeMessage tms1 = new TimeMessage();
        tms1.message = getString(R.string.classroom_tip_01_01) + "<font color='#FFD700'>1</font>" + getString(R.string.classroom_tip_01_02);
        TimeMessage tms2 = new TimeMessage();
        tms2.message = getString(R.string.classroom_tip_02_01) + "<font color='#FFD700'>1</font>" + getString(R.string.classroom_tip_02_02);
        TimeMessage tms3 = new TimeMessage();
        tms3.message = getString(R.string.classroom_tip_03_01) + "<font color='#FFD700'>1</font>" + getString(R.string.classroom_tip_03_02);
        TimeMessage tms4 = new TimeMessage();
        tms4.message = getString(R.string.classroom_tip_04_01) + "<font color='#FFD700'>3</font>" + getString(R.string.classroom_tip_04_02) + "<font color='#FFD700'>2</font>" + getString(R.string.classroom_tip_04_03);
        TimeMessage tms5 = new TimeMessage();
        tms5.message = getString(R.string.classroom_tip_05);
        tms5.hasKonwButton = false;
        timeMessages.add(tms1);
        timeMessages.add(tms2);
        timeMessages.add(tms3);
        timeMessages.add(tms4);
        timeMessages.add(tms5);
    }

    private void initMediaView() {
        //mp3
        img_disk = (ImageView) findViewById(R.id.img_disk);
        lin_audio_control = (LinearLayout) findViewById(R.id.lin_audio_control);
        img_play_mp3 = (ImageView) findViewById(R.id.img_play_mp3);
        txt_mp3_name = (TextView) findViewById(R.id.txt_mp3_name);
        txt_mp3_time = (TextView) findViewById(R.id.txt_mp3_time);
        sek_mp3 = (SeekBar) findViewById(R.id.sek_mp3);
        img_voice_mp3 = (ImageView) findViewById(R.id.img_voice_mp3);
        sek_voice_mp3 = (SeekBar) findViewById(R.id.sek_voice_mp3);
        img_close_mp3 = (ImageView) findViewById(R.id.img_close_mp3);

        img_close_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomManager.getInstance().unPublishMedia();
                lin_audio_control.setVisibility(View.INVISIBLE);
            }
        });
        img_play_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stream != null) {
                    if (RoomSession.isPublish) {
                        RoomManager.getInstance().playMedia((Boolean) stream.attrMap.get("pause") == null ? false : (Boolean) stream.attrMap.get("pause"));
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
            }
        });
        sek_mp3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
//        if(AudioPlayer.getInstance().isMute()){
//            img_voice_mp3.setImageResource(R.drawable.btn_mute_pressed);
//        }else{
//            img_voice_mp3.setImageResource(R.drawable.btn_volume_pressed);
//        }
        img_voice_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMediaMute) {
                    RoomManager.getInstance().setRemoteStreamAudioVolume(vol);
                    img_voice_mp3.setImageResource(R.drawable.btn_volume_pressed);
                    sek_voice_mp3.setProgress((int) (vol * 100));
                } else {
                    RoomManager.getInstance().setRemoteStreamAudioVolume(0);
                    img_voice_mp3.setImageResource(R.drawable.btn_mute_pressed);
                    sek_voice_mp3.setProgress(0);
                }
                isMediaMute = !isMediaMute;
            }
        });
        sek_voice_mp3.setProgress((int) (vol * 100));
        sek_voice_mp3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = (float) progress / (float) seekBar.getMax();
                if (vol > 0) {
                    img_voice_mp3.setImageResource(R.drawable.btn_volume_pressed);
                } else {
                    img_voice_mp3.setImageResource(R.drawable.btn_mute_pressed);
                }
//                isMediaMute = !(progress>0);
                RoomManager.getInstance().setRemoteStreamAudioVolume(vol);
                if (fromUser) {
                    RoomActivity.this.vol = vol;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initPlayBackView() {
        rel_play_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        sek_play_back.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    this.progress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long pos = (long) (((double) progress / 100) * (endtime - starttime) + starttime);
                if (isEnd) {
                    img_play_back.setImageResource(R.drawable.btn_pause_normal);
                    RoomManager.getInstance().playPlayback();
                    isPlayBackPlay = !isPlayBackPlay;
                }
                isEnd = false;
                RoomManager.getInstance().seekPlayback(pos);
            }
        });
        img_play_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayBackPlay) {
                    RoomManager.getInstance().pausePlayback();
                    img_play_back.setImageResource(R.drawable.btn_play_normal);
                } else {
                    if (isEnd) {
                        RoomManager.getInstance().seekPlayback(starttime);
                        currenttime = starttime;
                        RoomManager.getInstance().playPlayback();
                        img_play_back.setImageResource(R.drawable.btn_pause_normal);
                        isEnd = false;
                    } else {
                        RoomManager.getInstance().playPlayback();
                        img_play_back.setImageResource(R.drawable.btn_pause_normal);
                    }
                }
                isPlayBackPlay = !isPlayBackPlay;
                WhiteBoradManager.getInstance().playbackPlayAndPauseController(isPlayBackPlay);
            }
        });
    }

    /*private void showDialog() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.chat_edit_pop, null);
        dialog.setContentView(view);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getHeight() * 0.85);
        p.height = (int) (d.getWidth() * 0.45);


        dialogWindow.setAttributes(p);

        TextView txt_close_chat = (TextView) view.findViewById(R.id.txt_close_chat);
        TextView txt_send = (TextView) view.findViewById(R.id.txt_send);
        final ImageView iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
        final ImageView iv_broad = (ImageView) view.findViewById(R.id.iv_broad);
        edt_input = (EditText) view.findViewById(R.id.edt_input);
        edt_input.requestFocus();
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_broad.setVisibility(View.VISIBLE);
                iv_chat.setVisibility(View.GONE);
                KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
                showChatExpressionPop();
            }
        });
        iv_broad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_chat.setVisibility(View.VISIBLE);
                iv_broad.setVisibility(View.GONE);
                if (chatWindow != null) {
                    chatWindow.dismiss();
                }
                KeyBoardUtil.showKeyBoard(RoomActivity.this, edt_input);
            }
        });
        txt_close_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatWindow != null) {
                    chatWindow.dismiss();
                }
                KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
                dialog.dismiss();
            }
        });
        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_input.getText().toString().trim();
                if (msg != null && !msg.isEmpty()) {
                    edt_input.setText("");
                    RoomManager.getInstance().sendMessage(msg, 0);
                    KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
                    dialog.dismiss();
                }
            }
        });
    }*/

    private void showChatEditPop() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int hid = dm.heightPixels;
        View contentView = LayoutInflater.from(RoomActivity.this).inflate(R.layout.chat_edit_pop, null);
//        final PopupWindow popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, hid * 4 / 5);

        final BasePopupWindow popupWindow = new BasePopupWindow(this);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(hid * 4 / 5);

        TextView txt_close_chat = (TextView) contentView.findViewById(R.id.txt_close_chat);
        TextView txt_send = (TextView) contentView.findViewById(R.id.txt_send);
        final ImageView iv_chat = (ImageView) contentView.findViewById(R.id.iv_chat);
        final ImageView iv_broad = (ImageView) contentView.findViewById(R.id.iv_broad);
        edt_input = (EditText) contentView.findViewById(R.id.edt_input);
        edt_input.requestFocus();
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_broad.setVisibility(View.VISIBLE);
                iv_chat.setVisibility(View.GONE);
                KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
                showChatExpressionPop();
            }
        });
        iv_broad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_chat.setVisibility(View.VISIBLE);
                iv_broad.setVisibility(View.GONE);
                if (chatWindow != null) {
                    chatWindow.dismiss();
                }
                KeyBoardUtil.showKeyBoard(RoomActivity.this, edt_input);
            }
        });
        txt_close_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatWindow != null) {
                    chatWindow.dismiss();
                }
                KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
                popupWindow.dismiss();
            }
        });
        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_input.getText().toString().trim();
                if (msg != null && !msg.isEmpty()) {
                    edt_input.setText("");
                    RoomManager.getInstance().sendMessage(msg, 0);
                    KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setContentView(contentView);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

        popupWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.TOP, 0, 100);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                KeyBoardUtil.hideKeyBoard(RoomActivity.this, edt_input);
            }
        });
    }

    private void showChatExpressionPop() {
        View contentView = LayoutInflater.from(RoomActivity.this).inflate(R.layout.chat_expression_pop, null);
        chatWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chart_face_gv = (GridView) contentView.findViewById(R.id.chart_face_gv);
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(staticFacesList, this);
        chart_face_gv.setAdapter(mGvAdapter);
        chart_face_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        chart_face_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == 41) {
                        delete();
                    }
                    if (position < 8) {
                        insert(getFace(staticFacesList.get(position)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        chatWindow.setBackgroundDrawable(new BitmapDrawable());
        chatWindow.setOutsideTouchable(true);
        chatWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onStart() {
//        if (isPauseLocalVideo) {
//            RoomManager.getInstance().resumeLocalCamera();
//            isPauseLocalVideo = !isPauseLocalVideo;
//        }
        if (RoomManager.getInstance().getMySelf() != null) {
            if (nm != null) {
                nm.cancel(2);
            }
            isBackApp = false;
//            RoomManager.getInstance().delMsg("userEnterBackGround", "userEnterBackGround__"+RoomManager.getInstance().getMySelf().peerId, "__allSuperUsers", new HashMap<String, Object>());
            if (RoomManager.getInstance().getMySelf() != null) {
                if (RoomManager.getInstance().getMySelf().role == 2 || RoomManager.getInstance().getMySelf().role == 0) {
                    RoomManager.getInstance().setInBackGround(false);
                    RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "isInBackGround", false);
                }
            }
        }
        isOpenCamera = false;
        mWakeLock.acquire();

        super.onStart();
        if (isInRoom) {
            initViewByRoomTypeAndTeacher();
        }
        doPlayVideo(null);

        if (RoomManager.getInstance().getMySelf() == null) {
            return;
        }
        if (RoomManager.getInstance().getMySelf().publishState == 1 || RoomManager.getInstance().getMySelf().publishState == 3) {
            RoomManager.getInstance().enableSendMyVoice(true);
        }
        RoomManager.getInstance().enableOtherAudio(false);
        RoomManager.getInstance().setMuteAllStream(false);
        closeSpeaker();
        if (RoomManager.getInstance() != null) {
            if (RoomManager.getInstance().getRoomStatus() > 0 && RoomManager.getInstance().getRoomStatus() < 6) {
                RoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", "__all", null, false, "ClassBegin", null);
            }
        }
      /*  RoomManager.getInstance().useLoudSpeaker(true);*/
    }

    private int currVolume = 0;

    /**
     * 打开扬声器
     */
    private void openSpeaker() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.ROUTE_SPEAKER);
            currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if (!audioManager.isSpeakerphoneOn()) {
                //setSpeakerphoneOn() only work when audio mode set to MODE_IN_CALL.
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {

        if (!isFinishing()) {
//            if (!isPauseLocalVideo) {
//                RoomManager.getInstance().pauseLocalCamera();
//                isPauseLocalVideo = !isPauseLocalVideo;
//            }
//            RoomManager.getInstance().enableSendMyVoice(false);
//            RoomManager.getInstance().enableOtherAudio(true);
//            RoomManager.getInstance().setMuteAllStream(true);
            if (!isBackApp) {
                nm.notify(2, mBuilder.build());
            }
//            RoomManager.getInstance().pubMsg("userEnterBackGround","userEnterBackGround__"+RoomManager.getInstance().getMySelf().peerId,"__allSuperUsers",new HashMap<String,Object>(),true,null,RoomManager.getInstance().getMySelf().peerId);
            if (RoomManager.getInstance().getMySelf() != null) {
                if (RoomManager.getInstance().getMySelf().role == 2 || RoomManager.getInstance().getMySelf().role == 0) {
                    RoomManager.getInstance().setInBackGround(true);
                    RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "isInBackGround", true);
                }
            }
        }
        mWakeLock.release();
        if (isFinishing()) {
            if (timerAddTime != null) {
                timerAddTime.cancel();
                timerAddTime = null;
            }
            if (timerbefClassbegin != null) {
                timerbefClassbegin.cancel();
                timerbefClassbegin = null;
            }
//            if (checkNetTimer != null) {
//                checkNetTimer.cancel();
//                checkNetTimer = null;
//            }
            timeMessages.clear();
        }

        FileListEXAdapter.isAdminExpand = true;
        FileListEXAdapter.isClassExpand = false;
        MediaEXListEXAdapter.isClassExpand = false;
        MediaEXListEXAdapter.isAdminExpand = true;

        super.onStop();
    }

    private void initVideoItem() {
        vdi_teacher = (LinearLayout) findViewById(R.id.vdi_teacher);
        vdi_stu_in_sd = (LinearLayout) findViewById(R.id.vdi_stu_in_sd);

       /* img_teacher_screen = (ImageView) findViewById(R.id.img_teacher_screen);*/

        teacherItem = new VideoItem();
        teacherItem.parent = vdi_teacher;
        teacherItem.sf_video = (SurfaceViewRenderer) vdi_teacher.findViewById(R.id.sf_video);
        teacherItem.sf_video.init(EglBase.create().getEglBaseContext(), null);
        teacherItem.img_mic = (ImageView) vdi_teacher.findViewById(R.id.img_mic);
        teacherItem.img_pen = (ImageView) vdi_teacher.findViewById(R.id.img_pen);
        teacherItem.img_hand = (ImageView) vdi_teacher.findViewById(R.id.img_hand_up);
        teacherItem.txt_name = (TextView) vdi_teacher.findViewById(R.id.txt_name);
        teacherItem.txt_gift_num = (TextView) vdi_teacher.findViewById(R.id.txt_gift_num);
        teacherItem.rel_group = (RelativeLayout) vdi_teacher.findViewById(R.id.rel_group);
        teacherItem.img_video_back = (ImageView) vdi_teacher.findViewById(R.id.img_video_back);
        teacherItem.lin_gift = (LinearLayout) vdi_teacher.findViewById(R.id.lin_gift);
        teacherItem.lin_gift.setVisibility(View.INVISIBLE);
        teacherItem.lin_name_label = (LinearLayout) vdi_teacher.findViewById(R.id.lin_name_label);
        teacherItem.rel_video_label = (RelativeLayout) vdi_teacher.findViewById(R.id.rel_video_label);

        teacherItem.re_background = (RelativeLayout) vdi_teacher.findViewById(R.id.re_background);
        teacherItem.tv_home = (TextView) vdi_teacher.findViewById(R.id.tv_home);

        stu_in_sd = new VideoItem();
        stu_in_sd.parent = vdi_stu_in_sd;
        stu_in_sd.sf_video = (SurfaceViewRenderer) vdi_stu_in_sd.findViewById(R.id.sf_video);
        stu_in_sd.sf_video.init(EglBase.create().getEglBaseContext(), null);
        stu_in_sd.img_cam = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_cam);
        stu_in_sd.img_mic = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_mic);
        stu_in_sd.img_pen = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_pen);
        stu_in_sd.img_hand = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_hand_up);
        stu_in_sd.txt_name = (TextView) vdi_stu_in_sd.findViewById(R.id.txt_name);
        stu_in_sd.txt_gift_num = (TextView) vdi_stu_in_sd.findViewById(R.id.txt_gift_num);
        stu_in_sd.rel_group = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.rel_group);
        stu_in_sd.img_video_back = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_video_back);
        stu_in_sd.lin_gift = (LinearLayout) vdi_stu_in_sd.findViewById(R.id.lin_gift);
        stu_in_sd.lin_name_label = (LinearLayout) vdi_stu_in_sd.findViewById(R.id.lin_name_label);
        stu_in_sd.rel_video_label = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.rel_video_label);

        stu_in_sd.re_background = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.re_background);
        stu_in_sd.tv_home = (TextView) vdi_stu_in_sd.findViewById(R.id.tv_home);
    }

    private void initViewByRoomTypeAndTeacher() {
        if (roomType == 0) {//一对一
            lin_control_tool.setVisibility(View.GONE);
           /* rel_students.setVisibility(View.GONE);*/
            txt_all_send_gift.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params.weight = 1.0f;

            if (RoomManager.getInstance().getMySelf().role == 0) {//老师
                txt_chat_input.setVisibility(View.VISIBLE);
                lin_top_buttons.setVisibility(View.VISIBLE);
                if (!RoomControler.isShowClassBeginButton())
                    txt_class_begin.setVisibility(View.VISIBLE);
                lin_hand_and_photo.setVisibility(View.GONE);
//                lin_self_av_control.setVisibility(View.GONE);
                rel_play_back.setVisibility(View.INVISIBLE);
//                img_servers.setVisibility(View.VISIBLE);
            } else if (RoomManager.getInstance().getMySelf().role == 2) {//学生
                txt_chat_input.setVisibility(View.VISIBLE);
                lin_top_buttons.setVisibility(View.INVISIBLE);
                txt_class_begin.setVisibility(View.GONE);
                lin_hand_and_photo.setVisibility(View.VISIBLE);
                txt_send_up_photo.setVisibility(View.VISIBLE);
                txt_hand_up.setVisibility(View.GONE);
                rel_play_back.setVisibility(View.INVISIBLE);
//                img_servers.setVisibility(View.VISIBLE);
            } else if (RoomManager.getInstance().getMySelf().role == 4) {
                if (!RoomControler.isShowClassBeginButton())
                    txt_class_begin.setVisibility(View.GONE);
                txt_chat_input.setVisibility(View.GONE);
                lin_top_buttons.setVisibility(View.INVISIBLE);
                lin_hand_and_photo.setVisibility(View.GONE);
                rel_play_back.setVisibility(View.INVISIBLE);
//                img_servers.setVisibility(View.VISIBLE);
            } else if (RoomManager.getInstance().getMySelf().role == -1) {
                lin_top_buttons.setVisibility(View.INVISIBLE);
                rel_play_back.setVisibility(View.VISIBLE);
                lin_time.setVisibility(View.GONE);
//                img_servers.setVisibility(View.GONE);
            }
        } else {//一对多
            vdi_stu_in_sd.setVisibility(View.GONE);
            if (RoomManager.getInstance().getMySelf().role == 0) {//老师
//                lin_control_tool.setVisibility(View.VISIBLE);
                lin_top_buttons.setVisibility(View.VISIBLE);
                if (!RoomControler.isShowClassBeginButton())
                    txt_class_begin.setVisibility(View.VISIBLE);
                lin_hand_and_photo.setVisibility(View.GONE);
                txt_chat_input.setVisibility(View.VISIBLE);
                rel_play_back.setVisibility(View.INVISIBLE);
//                lin_self_av_control.setVisibility(View.GONE);
            } else if (RoomManager.getInstance().getMySelf().role == 2) {//学生
                txt_all_send_gift.setVisibility(View.GONE);
                lin_control_tool.setVisibility(View.GONE);
                lin_top_buttons.setVisibility(View.INVISIBLE);
                txt_class_begin.setVisibility(View.GONE);
                lin_hand_and_photo.setVisibility(View.VISIBLE);
                txt_chat_input.setVisibility(View.VISIBLE);
                rel_play_back.setVisibility(View.INVISIBLE);
//                lin_self_av_control.setVisibility(View.VISIBLE);
            } else if (RoomManager.getInstance().getMySelf().role == 4) {
                if (!RoomControler.isShowClassBeginButton())
                    txt_class_begin.setVisibility(View.GONE);
                txt_chat_input.setVisibility(View.GONE);
                lin_top_buttons.setVisibility(View.INVISIBLE);
                lin_hand_and_photo.setVisibility(View.GONE);
                rel_play_back.setVisibility(View.INVISIBLE);
            } else if (RoomManager.getInstance().getMySelf().role == -1) {
                lin_hand_and_photo.setVisibility(View.GONE);
                lin_top_buttons.setVisibility(View.INVISIBLE);
                rel_play_back.setVisibility(View.VISIBLE);
                lin_time.setVisibility(View.GONE);
            }
        }
      /*  if (!isClassBegin) {
            lin_hand_and_photo.setVisibility(View.GONE);
//            lin_self_av_control.setVisibility(View.GONE);
        }*/
        doLayout();
//        wbFragment.setAddPagePermission(myRrole == 0 && isClassBegin);
    }

    private void doPlayVideo(String peerId) {

        if (isZoom || TextUtils.isEmpty(peerId)) {
            return;
        }

        teacherItem.rel_group.setVisibility(View.INVISIBLE);
        stu_in_sd.rel_group.setVisibility(View.INVISIBLE);
        stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);

        getPlayingList();
        boolean hasAssistant = false;
        for (int i = 0; i < playingList.size(); i++) {
            final RoomUser user = playingList.get(i);
            if (user == null) {
                return;
            }
            if (roomType == 0) {
                //1对1
                if (user.role == 0) {//老师
                    doTeacherVideoPlay(user);
                } else if (user.role == 2) {//学生
                    stu_in_sd.rel_group.setVisibility(View.VISIBLE);
                    stu_in_sd.lin_gift.setVisibility(View.VISIBLE);
                    stu_in_sd.txt_name.setText(user.nickName);
                    if (user.disablevideo) {
                        stu_in_sd.img_cam.setImageResource(R.drawable.icon_video_disable);
                    } else {
                        stu_in_sd.img_cam.setImageResource(R.drawable.icon_video);
                        if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                            stu_in_sd.img_cam.setVisibility(View.INVISIBLE);
                        } else {
                            stu_in_sd.img_cam.setVisibility(View.VISIBLE);
                        }
                    }

                    if (user.disableaudio) {
                        stu_in_sd.img_mic.setImageResource(R.drawable.icon_audio_disable);
                    } else {
                        stu_in_sd.img_mic.setImageResource(R.drawable.icon_audio);
                        if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                            stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
                        } else {
                            stu_in_sd.img_mic.setVisibility(View.VISIBLE);
                        }
                    }
                    if (user.publishState > 1 && user.publishState < 4 && !user.disablevideo) {
                        stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                        RoomManager.getInstance().playVideo(user.peerId, stu_in_sd.sf_video);
                        Log.e("xiao", "playSEC+playlistSize = " + playingList.size() + "peerid = " + user.peerId);
                    } else {
                        stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                    }
                } else if (user.role == 1) {//助教一对一显示
                    hasAssistant = true;
                    do1vsnStudentPlayVideo(user, peerId != null);
                }
            } else {//1对多
                if (user.role == 0) {
                    doTeacherVideoPlay(user);
                } else {
                    do1vsnStudentPlayVideo(user, peerId != null);
                }
            }

//            if (!TextUtils.isEmpty(path)) {
//                if (roomType == 0) {
//                    do1vsnStudentPlayVideo(user, peerId != null && peerId.equals(user.peerId));
//                }else {
//                    stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
//                }
//            }
        }
       /* if (RoomManager.getInstance().getRoomType() == 0) {
            rel_students.setVisibility(hasAssistant ? View.VISIBLE : View.INVISIBLE);
        }*/
    }

    private int sitpos = -1;

    private void do1vsnStudentPlayVideo(final RoomUser user, boolean force) {
        boolean hasSit = false;
        sitpos = -1;
        for (int i = 0; i < videoItems.size(); i++) {
            if (videoItems.get(i).peerid.equals(user.peerId)) {
                hasSit = true;
                sitpos = i;
            }
        }
        if (!hasSit) {
            final VideoItem stu = new VideoItem();
            final LinearLayout vdi_stu = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.video_item, null);
            stu.parent = vdi_stu;
            stu.sf_video = (SurfaceViewRenderer) vdi_stu.findViewById(R.id.sf_video);
            stu.sf_video.init(EglBase.create().getEglBaseContext(), null);
            stu.sf_video.setZOrderMediaOverlay(true);
            stu.img_cam = (ImageView) vdi_stu.findViewById(R.id.img_cam);
            stu.img_mic = (ImageView) vdi_stu.findViewById(R.id.img_mic);
            stu.img_pen = (ImageView) vdi_stu.findViewById(R.id.img_pen);
            stu.img_hand = (ImageView) vdi_stu.findViewById(R.id.img_hand_up);
            stu.txt_name = (TextView) vdi_stu.findViewById(R.id.txt_name);
            stu.txt_gift_num = (TextView) vdi_stu.findViewById(R.id.txt_gift_num);
            stu.rel_group = (RelativeLayout) vdi_stu.findViewById(R.id.rel_group);
            stu.img_video_back = (ImageView) vdi_stu.findViewById(R.id.img_video_back);
            stu.lin_gift = (LinearLayout) vdi_stu.findViewById(R.id.lin_gift);
            stu.lin_name_label = (LinearLayout) vdi_stu.findViewById(R.id.lin_name_label);
            stu.rel_video_label = (RelativeLayout) vdi_stu.findViewById(R.id.rel_video_label);
            stu.re_background = (RelativeLayout) vdi_stu.findViewById(R.id.re_background);
            stu.tv_home = (TextView) vdi_stu.findViewById(R.id.tv_home);
            stu.peerid = user.peerId;
            stu.txt_name.setText(user.nickName);

            if (!RoomSession.isClassBegin) {
                stu.img_pen.setVisibility(View.INVISIBLE);
                stu.img_hand.setVisibility(View.INVISIBLE);
                stu.img_mic.setVisibility(View.INVISIBLE);
            }

            if (user.peerId.equals(RoomManager.getInstance().getMySelf().peerId)) {
                if (RoomSession.isClassBegin) {
                    stu.img_hand.setVisibility(View.VISIBLE);
                }
            } else {
                stu.img_hand.setVisibility(View.GONE);
            }
            if (user.disablevideo) {
                stu.img_cam.setImageResource(R.drawable.icon_video_disable);
            } else {
                stu.img_cam.setImageResource(R.drawable.icon_video);
                if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                    stu.img_cam.setVisibility(View.INVISIBLE);
                } else {
                    if (RoomSession.isClassBegin) {
                        stu.img_cam.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (user.disableaudio) {
                stu.img_mic.setImageResource(R.drawable.icon_audio_disable);
            } else {
                stu.img_mic.setImageResource(R.drawable.icon_audio);
                if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                    stu.img_mic.setVisibility(View.INVISIBLE);
                } else {
                    if (RoomSession.isClassBegin) {
                        stu.img_mic.setVisibility(View.VISIBLE);
                    }
                }
            }

            vdi_stu.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (userrole == 4) {
                        return false;
                    }
                    if (!RoomManager.getInstance().getMySelf().canDraw) {
                        return false;
                    }
                    if (stu.isSplitScreen) {
                        return false;
                    }
                    stu.canMove = true;
                    vdi_stu.setAlpha(0.5f);
                    return false;
                }
            });
            vdi_stu.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (userrole == 4) {
                        return false;
                    }

                    if (!RoomManager.getInstance().getMySelf().canDraw) {
                        return false;
                    }

                    if (stu.isSplitScreen) {
                        return false;
                    }
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            if (!stu.isMoved) {
                                stu.oldX = event.getRawX();
                                stu.oldY = event.getRawY() - rel_tool_bar.getHeight();
                            }
                            mode = DRAG;
                            if (!stu.canMove) {
                                return false;
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            mode = ZOOM;
                            beforeLenght = spacing(event);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mode == ZOOM) {
                                afterLenght = spacing(event);
                                float gapLenght = afterLenght - beforeLenght;
                                if (Math.abs(gapLenght) > 5f) {
                                    float scale = afterLenght / beforeLenght;
                                    LayoutZoomOrIn.zoomVideoItem(stu, scale, rel_students, v_students);
                                    beforeLenght = afterLenght;
                                }
                            } else if (mode == DRAG) {
                                if (!stu.canMove) {
                                    return false;
                                }
                                if (event.getRawX() - stu.parent.getWidth() / 2 >= 0 &&
                                        event.getRawX() + stu.parent.getWidth() / 2 <= rel_students.getRight() &&
                                        event.getRawY() - stu.parent.getHeight() >= rel_students.getTop() &&
                                        event.getRawY() <= rel_students.getBottom()) {

                                    LayoutZoomOrIn.layoutVideo(stu, (int) (event.getRawX() - stu.parent.getWidth() / 2),
                                            (int) (event.getRawY() - stu.parent.getHeight()));
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (mode == DRAG) {
                                vdi_stu.setAlpha(1f);
                                if (!stu.canMove) {
                                    return false;
                                }
                                stu.canMove = false;

                                if (vdi_stu.getBottom() < lin_audio_control.getBottom()) {
                                    stu.isMoved = true;
                                } else {
                                    stu.isMoved = false;
                                    stu.isSefMoved = false;
                                }

                                if (screenID.size() > 0 && !screenID.contains(stu.peerid)) {
                                    screenID.add(stu.peerid);
                                }
                                do1vsnStudentVideoLayout();
                                studentMoveOrScreen();
                                mode = NONE;
                            } else if (mode == ZOOM) {
                                vdi_stu.setAlpha(1f);

                                sendScaleVideoItem(true);
                                sendStudentMove();
                                mode = NONE;
                            }
                            break;

//                        case MotionEvent.ACTION_POINTER_UP:
//                            if(mode == 2){
//                                afterLenght = spacing(event);// 获取两点的距离
//
//                                float gapLenght = afterLenght - beforeLenght;// 变化的长度
//
//                                if (Math.abs(gapLenght) > 5f) {
//                                    float scale_temp = afterLenght / beforeLenght;// 求的缩放的比例
//
////                                    ViewHelper.setScaleX(vdi_stu, scale_temp );// x方向上缩小
////                                    if(scale_temp<1){
////                                        scale_temp = 1;
////                                    }
////                                    stu.scale(scale_temp);
//                                    RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) stu.parent.getLayoutParams();
//                                    relparam.width = (int) (stu.parent.getWidth()*scale_temp);
//                                    relparam.height = (int) (stu.parent.getHeight()*scale_temp);
//                                    stu.parent.setLayoutParams(relparam);
//                                    LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) stu.rel_video_label.getLayoutParams();
//                                    linparam.width = (int) (stu.rel_video_label.getWidth()*scale_temp);
//                                    linparam.height = (int) (stu.rel_video_label.getHeight()*scale_temp);
//                                    stu.rel_video_label.setLayoutParams(linparam);
//                                    LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) stu.lin_name_label.getLayoutParams();
//                                    nameparam.width = (int) (stu.lin_name_label.getWidth()*scale_temp);
//                                    stu.lin_name_label.setLayoutParams(nameparam);
//                                    stu.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
//
//
//                                    beforeLenght = afterLenght;
//                                }
//                            }
//
//                            break;
                    }
                    return true;
                }
            });
            if (user.role == 2) {
                stu.lin_gift.setVisibility(View.VISIBLE);
            } else {
                stu.lin_gift.setVisibility(View.INVISIBLE);
            }
            videoItems.add(stu);
            rel_students.addView(vdi_stu);

            do1vsnStudentVideoLayout();
            if (user.publishState > 1 && user.publishState < 4 && !RoomSession.isPublish || (stream != null && !stream.isVideo())) {
                stu.sf_video.setVisibility(View.VISIBLE);
                RoomManager.getInstance().playVideo(user.peerId, stu.sf_video);
                Log.e("xiao", "playvideo-------peerid=" + user.peerId);
            } else {
                stu.sf_video.setVisibility(View.INVISIBLE);
            }
        } else if (force) {
            if (sitpos != -1) {
                if (user.publishState > 1 && user.publishState < 4 && !RoomSession.isPublish || (stream != null && !stream.isVideo())) {
                    videoItems.get(sitpos).sf_video.setVisibility(View.VISIBLE);
                    videoItems.get(sitpos).img_cam.setVisibility(View.VISIBLE);
                    videoItems.get(sitpos).sf_video.setVisibility(View.VISIBLE);
                    RoomManager.getInstance().playVideo(user.peerId, videoItems.get(sitpos).sf_video);
                } else {
                    videoItems.get(sitpos).sf_video.setVisibility(View.INVISIBLE);
                    videoItems.get(sitpos).img_cam.setVisibility(View.INVISIBLE);
                }
            }
        }
        for (int i = 0; i < videoItems.size(); i++) {
            final VideoItem it = videoItems.get(i);
            it.parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (screenID.size() > 0) {
                        if (screenID.contains(it.peerid)) {
                            it.isSplitScreen = true;
                        }
                        do1vsnStudentVideoLayout();
                    }
                    if (scalemap != null && scalemap.size() > 0) {
                        if (scalemap.containsKey(it.peerid) && scalemap.get(it.peerid) != null) {
                            double scale = scalemap.get(it.peerid);
                            LayoutZoomOrIn.zoomMsgVideoItem(it, scale, printWidth, printHeight);
                            scalemap.remove(it.peerid);
                        }
                    }
                    if (!it.isMoved) {
                        if (stuMoveInfoMap.containsKey(it.peerid) && stuMoveInfoMap.get(it.peerid) != null) {
                            String peerId = RoomManager.getInstance().getMySelf().peerId;
                            MoveVideoInfo mi = stuMoveInfoMap.get(it.peerid);
                            moveStudent(it.peerid, mi.top, mi.left, mi.isDrag);
                            stuMoveInfoMap.remove(it.peerid);
                        }
                        it.parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    private void sendStudentMove() {
        try {
            JSONObject data = new JSONObject();
            JSONObject moveData = new JSONObject();
            for (int i = 0; i < videoItems.size(); i++) {
                VideoItem it = videoItems.get(i);
                JSONObject md = new JSONObject();
                if (it.isMoved) {
                    double wid = rel_students.getWidth() - it.parent.getWidth();
                    double hid = rel_students.getHeight() - it.parent.getHeight();
                    double top = it.parent.getTop() / hid;
                    double left = it.parent.getLeft() / wid;
                    md.put("percentTop", top);
                    md.put("percentLeft", left);
                    md.put("isDrag", true);
                } else {
                    md.put("percentTop", 0);
                    md.put("percentLeft", 0);
                    md.put("isDrag", false);
                }
                moveData.put(it.peerid, md);
            }
            data.put("otherVideoStyle", moveData);
            RoomManager.getInstance().pubMsg("videoDraghandle", "videoDraghandle", "__allExceptSender", data.toString(), true, "ClassBegin", null);
            Log.e("mxl", "发送videoDraghandle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void sendScaleVideoItem(boolean isZoom) {
        try {
            JSONObject data = new JSONObject();
            JSONObject scaleData = new JSONObject();
            for (int i = 0; i < videoItems.size(); i++) {
                VideoItem it = videoItems.get(i);
                JSONObject md = new JSONObject();
                double scale;
                if (isZoom) {
                    scale = it.parent.getHeight() / printHeight;
                } else {
                    scale = 1.0;
                }
                md.put("scale", scale);
                scaleData.put(it.peerid, md);
            }
            data.put("ScaleVideoData", scaleData);
            RoomManager.getInstance().pubMsg("VideoChangeSize", "VideoChangeSize", "__allExceptSender", data.toString(), true, "ClassBegin", null);
            Log.e("mxl", "发送VideoChangeSize");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }
        return 0.0f;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void moveStudent(String peerid, float top, float left, boolean isDrag) {

        for (int i = 0; i < videoItems.size(); i++) {
            VideoItem it = videoItems.get(i);
            if (videoItems.get(i).peerid.equals(peerid)) {
                if (isDrag) {
                    int wid = rel_students.getWidth() - it.parent.getWidth();
                    int hid = rel_students.getHeight() - it.parent.getHeight();
                    top = top * hid;
                    left = left * wid;
                    it.isMoved = isDrag;
                    it.isSefMoved = true;
                    it.isMoved = true;
                    if (left < 0) {
                        left = 0;
                    }
                    if (top < 0) {
                        top = 0;
                    }
                    LayoutZoomOrIn.layoutVideo(it, Math.round(left), Math.round(top));
                } else {
                    it.isMoved = false;
                }
            }
        }
        do1vsnStudentVideoLayout();
    }

    private void doUnPlayVideo(RoomUser user) {

        if (roomType == 0) {
            //1对1
            if (user.role == 0) {//老师
                doTeacherVideoUnPlay(user);
            } else if (user.role == 1) {
                do1vsnStudentUnPlayVideo(user);
            } else if (user.role == 2) {//学生
                stu_in_sd.rel_group.setVisibility(View.INVISIBLE);
            }
        } else {//1对多
            if (user.role == 0) {
                doTeacherVideoUnPlay(user);
            } else {
                do1vsnStudentUnPlayVideo(user);
            }
        }
    }

    private void do1vsnStudentUnPlayVideo(RoomUser user) {
        for (int i = 0; i < videoItems.size(); i++) {
            if (videoItems.get(i).peerid.equals(user.peerId)) {
                videoItems.get(i).sf_video.release();
                rel_students.removeView(videoItems.get(i).parent);
                videoItems.remove(i);
                do1vsnStudentVideoLayout();
            }
        }
    }

    private void doTeacherVideoPlay(final RoomUser user) {
        teacherItem.rel_group.setVisibility(View.VISIBLE);
        if (user.publishState > 1 && user.publishState < 4 && !user.disablevideo) {
            teacherItem.sf_video.setVisibility(View.VISIBLE);
            RoomManager.getInstance().playVideo(user.peerId, teacherItem.sf_video);
        } else {
            teacherItem.sf_video.setVisibility(View.INVISIBLE);
        }
        teacherItem.txt_name.setText(user.nickName);
        vdi_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeacherControlPop(user);
            }
        });
    }

    private void doTeacherVideoUnPlay(final RoomUser user) {
        if (teacherItem.rel_group.getVisibility() == View.VISIBLE) {
//            if (RoomManager.getInstance().getUser(user.peerId) != null) {
//                RoomManager.getInstance().unPlayVideo(user.peerId);
//            }
            if (RoomManager.getInstance().getMySelf().peerId.equals(user.peerId)) {
                teacherItem.sf_video.setVisibility(View.INVISIBLE);
                teacherItem.img_mic.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showStudentControlPop(final RoomUser user, final int index) {
        if (!RoomSession.isClassBegin && RoomManager.getInstance().getMySelf().role == 0) {
            return;
        }
        if (!(RoomManager.getInstance().getMySelf().role == 0) && !user.peerId.endsWith(RoomManager.getInstance().getMySelf().peerId)) {
            return;
        }
        if (user.peerId.endsWith(RoomManager.getInstance().getMySelf().peerId) && !RoomControler.isAllowStudentControlAV()) {
            return;
        }
        if (roomType != 0 && index >= videoItems.size()) {
            return;
        }

        if (!RoomSession.isClassBegin) {
            if (!RoomControler.isReleasedBeforeClass()) {
                return;
            }
        }

        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_student_control, null);
        ImageView img_down_arr = (ImageView) contentView.findViewById(R.id.down_arr);
        ImageView img_right_arr = (ImageView) contentView.findViewById(R.id.right_arr);
        LinearLayout lin_candraw = (LinearLayout) contentView.findViewById(R.id.lin_candraw);
        LinearLayout lin_up_sd = (LinearLayout) contentView.findViewById(R.id.lin_up_sd);
        LinearLayout lin_audio = (LinearLayout) contentView.findViewById(R.id.lin_audio);
        LinearLayout lin_gift = (LinearLayout) contentView.findViewById(R.id.lin_gift);
        final ImageView img_candraw = (ImageView) contentView.findViewById(R.id.img_candraw);
        final ImageView img_up_sd = (ImageView) contentView.findViewById(R.id.img_up_sd);
        final ImageView img_audio = (ImageView) contentView.findViewById(R.id.img_audio);
        final TextView txt_candraw = (TextView) contentView.findViewById(R.id.txt_candraw);
        final TextView txt_up_sd = (TextView) contentView.findViewById(R.id.txt_up_sd);
        final TextView txt_audio = (TextView) contentView.findViewById(R.id.txt_audio);
        TextView txt_gift = (TextView) contentView.findViewById(R.id.txt_gift);

        LinearLayout lin_plit_screen = (LinearLayout) contentView.findViewById(R.id.lin_plit_screen);
        TextView txt_plit_screen = (TextView) contentView.findViewById(R.id.txt_plit_screen);
        ImageView img_plit_screen = (ImageView) contentView.findViewById(R.id.img_plit_screen);

        if (RoomManager.getInstance().getMySelf().role == 0 && index != -1) {
            lin_plit_screen.setVisibility(View.VISIBLE);
        } else {
            lin_plit_screen.setVisibility(View.GONE);
        }

        if (RoomManager.getInstance().getMySelf().role == 2) {
            lin_candraw.setVisibility(View.GONE);
        }

        if (index >= 0) {
            if (videoItems.get(index).isSplitScreen) {
                lin_candraw.setVisibility(View.GONE);
                txt_plit_screen.setText(getResources().getText(R.string.recovery));
                img_plit_screen.setImageDrawable(getResources().getDrawable(R.drawable.icon_reply_normal_only));
            } else {
                if (user.role != 1 && RoomManager.getInstance().getMySelf().role == 0) {
                    lin_candraw.setVisibility(View.VISIBLE);
                } else {
                    lin_candraw.setVisibility(View.GONE);
                }
                txt_plit_screen.setText(getResources().getText(R.string.plitscreen));
                img_plit_screen.setImageDrawable(getResources().getDrawable(R.drawable.icon_screen_normal));
            }
        }

        LinearLayout lin_video_control = (LinearLayout) contentView.findViewById(R.id.lin_video_control);
        final ImageView img_video_control = (ImageView) contentView.findViewById(R.id.img_camera);
        final TextView txt_video = (TextView) contentView.findViewById(R.id.txt_camera);
        if (user.role == 1) {
            lin_gift.setVisibility(View.GONE);
        } else {
            if (user.peerId.equals(RoomManager.getInstance().getMySelf().peerId)) {
                lin_gift.setVisibility(View.GONE);
                lin_up_sd.setVisibility(View.GONE);
            } else {
                lin_gift.setVisibility(View.VISIBLE);
                lin_up_sd.setVisibility(View.VISIBLE);
            }
        }
        if (userrole == 0) {
            //视频永久不显示2017-10-11
            lin_video_control.setVisibility(View.GONE);
        }

        if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
            img_video_control.setImageResource(R.drawable.icon_control_camera_01);
            txt_video.setText(R.string.video_on);
        } else {
            img_video_control.setImageResource(R.drawable.icon_control_camera_02);
            txt_video.setText(R.string.video_off);
        }

        if (user.disableaudio) {
            lin_audio.setVisibility(View.GONE);
        } else {
            lin_audio.setVisibility(View.VISIBLE);
            if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                img_audio.setImageResource(R.drawable.icon_control_audio);
                txt_audio.setText(R.string.open_audio);
            } else {
                img_audio.setImageResource(R.drawable.icon_control_mute);
                txt_audio.setText(R.string.close_audio);
            }
        }

        if (user.properties.containsKey("candraw")) {
            boolean candraw = Tools.isTure(user.properties.get("candraw"));
            if (candraw) {
                //可以画图
                img_candraw.setImageResource(R.drawable.icon_control_tools_02);
                txt_candraw.setText(R.string.no_candraw);
            } else {
                //不可以画图
                img_candraw.setImageResource(R.drawable.icon_control_tools_01);
                txt_candraw.setText(R.string.candraw);
            }
        } else {
            //没给过画图权限
            img_candraw.setImageResource(R.drawable.icon_control_tools_01);
            txt_candraw.setText(R.string.candraw);
        }

        if (user.publishState > 0) {//只要视频开启就是上台
            img_up_sd.setImageResource(R.drawable.icon_control_down);
            txt_up_sd.setText(R.string.down_std);
        } else {
            img_up_sd.setImageResource(R.drawable.icon_control_up);
            txt_up_sd.setText(R.string.up_std);
        }
        studentPopupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        studentPopupWindow.setContentView(contentView);
        lin_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                    img_audio.setImageResource(R.drawable.icon_control_mute);
                    txt_audio.setText(R.string.open_audio);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 0 || user.publishState == 4 ? 1 : 3);
                    RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "raisehand", false);
                } else {
                    img_audio.setImageResource(R.drawable.icon_control_audio);
                    txt_audio.setText(R.string.close_audio);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 3 ? 2 : 4);
                }
                studentPopupWindow.dismiss();
            }
        });

        lin_candraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.properties.containsKey("candraw")) {
                    boolean candraw = Tools.isTure(user.properties.get("candraw"));
                    if (candraw) {
                        //不可以画图
                        img_candraw.setImageResource(R.drawable.icon_control_tools_01);
                        txt_candraw.setText(R.string.candraw);
                        RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", false);

                    } else {
                        //可以画图
                        img_candraw.setImageResource(R.drawable.icon_control_tools_02);
                        txt_candraw.setText(R.string.no_candraw);
                        RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", true);
                    }
                } else {
                    //可以画图
                    img_candraw.setImageResource(R.drawable.icon_control_tools_02);
                    txt_candraw.setText(R.string.no_candraw);
                    RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", true);
                }
                studentPopupWindow.dismiss();
            }
        });

        lin_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                long giftnumber = 0;
//                if (user.properties.containsKey("giftnumber")) {
//                    giftnumber = user.properties.get("giftnumber") instanceof Integer ? (int) user.properties.get("giftnumber") : (long) user.properties.get("giftnumber");
//                }
//                giftnumber++;
//                RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "giftnumber", giftnumber);
                HashMap<String, RoomUser> receiverMap = new HashMap<String, RoomUser>();
                receiverMap.put(user.peerId, user);
                sendGift(receiverMap);
                studentPopupWindow.dismiss();
            }
        });

        lin_up_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.publishState > 0) {//只要视频开启就是上台
                    img_up_sd.setImageResource(R.drawable.icon_control_up);
                    txt_up_sd.setText(R.string.up_std);
                    RoomManager.getInstance().changeUserPublish(user.peerId, 0);
                    if (user.role != 1) {
                        RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", false);
                    }
                } else {
                    img_up_sd.setImageResource(R.drawable.icon_control_down);
                    txt_up_sd.setText(R.string.down_std);
                    RoomManager.getInstance().changeUserPublish(user.peerId, 3);
                    RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "raisehand", false);
                }
                studentPopupWindow.dismiss();
            }
        });
        lin_video_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                    img_video_control.setImageResource(R.drawable.icon_control_camera_02);
                    txt_video.setText(R.string.video_off);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 0 || user.publishState == 4 ? 2 : 3);
                } else {
                    img_video_control.setImageResource(R.drawable.icon_control_camera_01);
                    txt_video.setText(R.string.video_on);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 2 ? 4 : 1);
                }
                studentPopupWindow.dismiss();
            }
        });
        lin_plit_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentPopupWindow.dismiss();
                if (videoItems.get(index).isSplitScreen) {
                    videoItems.get(index).isSplitScreen = false;
                    videoItems.get(index).isMoved = false;
                    if (screenID.contains(videoItems.get(index).peerid)) {
                        screenID.remove(videoItems.get(index).peerid);
                    }
                } else {
                    videoItems.get(index).isSplitScreen = true;
                    if (!screenID.contains(videoItems.get(index).peerid)) {
                        screenID.add(videoItems.get(index).peerid);
                    }
                }
                do1vsnStudentVideoLayout();
                sendSplitScreen();
                sendStudentMove();
            }
        });
        studentPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        studentPopupWindow.setFocusable(true);
        studentPopupWindow.setOutsideTouchable(true);

        if (roomType == 0) {
            if (user.role == 1) {
                img_down_arr.setVisibility(View.VISIBLE);
                img_right_arr.setVisibility(View.GONE);
                RelativeLayout.LayoutParams arr_param = (RelativeLayout.LayoutParams) img_down_arr.getLayoutParams();
                arr_param.setMargins((videoItems.get(index).parent.getMeasuredWidth() / 2), 0, 0, 0);
                setArrPosition(img_down_arr, index);
                setPopupWindowPosition(index);
            } else {
                img_down_arr.setVisibility(View.GONE);
                img_right_arr.setVisibility(View.VISIBLE);
                int x = 0;
                if (userrole == 0) {
                    x = -vdi_stu_in_sd.getMeasuredWidth();
                } else {
                    x = -vdi_stu_in_sd.getMeasuredWidth() / 4 * 3;
                }
                studentPopupWindow.showAsDropDown(vdi_stu_in_sd, x, -vdi_stu_in_sd.getMeasuredHeight() / 2 - vdi_stu_in_sd.getMeasuredHeight() / 4);
            }
        } else {
            img_down_arr.setVisibility(View.VISIBLE);
            img_right_arr.setVisibility(View.GONE);
            setArrPosition(img_down_arr, index);
            setPopupWindowPosition(index);
        }
    }

    private void setArrPosition(ImageView img_down_arr, int index) {
        RelativeLayout.LayoutParams arr_param = (RelativeLayout.LayoutParams) img_down_arr.getLayoutParams();
        if (videoItems.get(index).isSplitScreen) {
            arr_param.setMargins(100, 0, 0, 0);
        } else {
            if (index == 0) {
                if (videoItems.get(index).isMoved) {
                    arr_param.setMargins(200, 0, 0, 0);
                } else {
                    arr_param.setMargins((videoItems.get(index).parent.getMeasuredWidth() / 2), 0, 0, 0);
                }
            } else if (index > 0 && index < 6) {
                if (videoItems.get(index).isMoved) {
                    arr_param.setMargins(200, 0, 0, 0);
                } else {
                    arr_param.setMargins(videoItems.get(index).parent.getMeasuredWidth() - 60, 0, 0, 0);
                }
            }
        }
        img_down_arr.setLayoutParams(arr_param);
    }

    private void setPopupWindowPosition(int index) {
        if (videoItems.get(index).isSplitScreen) {
            studentPopupWindow.showAsDropDown(videoItems.get(index).parent, 0, -videoItems.get(index).parent.getHeight());
        } else {
            if (videoItems.get(index).parent.getHeight() / printHeight > 1.0 && videoItems.get(index).isMoved) {
                studentPopupWindow.showAsDropDown(videoItems.get(index).parent, -videoItems.get(index).parent.getWidth() / 4,
                        -videoItems.get(index).parent.getHeight() - 40);
            } else {
                studentPopupWindow.showAsDropDown(videoItems.get(index).parent, -videoItems.get(index).parent.getMeasuredWidth() / 2,
                        -videoItems.get(index).parent.getHeight() - videoItems.get(index).parent.getHeight() / 2);
            }
        }
    }

    private void sendSplitScreen() {
        try {
            JSONObject data = new JSONObject();
            JSONArray splitData = new JSONArray();
            for (int i = 0; i < screenID.size(); i++) {
                splitData.put(screenID.get(i));
            }
            data.put("userIDArry", splitData);
            if (screenID.size() > 0) {
                RoomManager.getInstance().pubMsg("VideoSplitScreen", "VideoSplitScreen", "__allExceptSender", data.toString(), true, "ClassBegin", null);
            } else {
                RoomManager.getInstance().delMsg("VideoSplitScreen", "VideoSplitScreen", "__allExceptSender", data.toString());
            }
            Log.e("mxl", "发送VideoSplitScreen");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTeacherControlPop(final RoomUser user) {
        if (!(RoomManager.getInstance().getMySelf().role == 0)) {
            return;
        }
        if (!RoomControler.isReleasedBeforeClass()) {
            if (!RoomSession.isClassBegin) {
                return;
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int hid = dm.heightPixels;
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_av_control, null);
//        contentView.setBackgroundColor(Color.BLUE);
        LinearLayout lin_video_control = (LinearLayout) contentView.findViewById(R.id.lin_video_control);
        LinearLayout lin_audio_control = (LinearLayout) contentView.findViewById(R.id.lin_audio_control);
        LinearLayout lin_plit_screen = (LinearLayout) contentView.findViewById(R.id.lin_plit_screen);
        lin_plit_screen.setVisibility(View.GONE);
        final ImageView img_video_control = (ImageView) contentView.findViewById(R.id.img_camera);
        final ImageView img_audio_control = (ImageView) contentView.findViewById(R.id.img_audio);
        final TextView txt_video = (TextView) contentView.findViewById(R.id.txt_camera);
        final TextView txt_audio = (TextView) contentView.findViewById(R.id.txt_audio);

        final LinearLayout lin_teather_ecovery = (LinearLayout) contentView.findViewById(R.id.lin_teather_ecovery);


        if (RoomManager.getInstance().getRoomType() == 0) {
            lin_teather_ecovery.setVisibility(View.GONE);
        } else {
            if (RoomSession.isClassBegin) {
                lin_teather_ecovery.setVisibility(View.VISIBLE);
            } else {
                lin_teather_ecovery.setVisibility(View.GONE);
            }
        }

        final PopupWindow popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
            img_audio_control.setImageResource(R.drawable.icon_control_audio);
            txt_audio.setText(R.string.open_audio);
        } else {
            img_audio_control.setImageResource(R.drawable.icon_control_mute);
            txt_audio.setText(R.string.close_audio);
        }
        if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
            img_video_control.setImageResource(R.drawable.icon_control_camera_01);
            txt_video.setText(R.string.video_on);
        } else {
            img_video_control.setImageResource(R.drawable.icon_control_camera_02);
            txt_video.setText(R.string.video_off);
        }
        lin_video_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                    img_video_control.setImageResource(R.drawable.icon_control_camera_02);
                    txt_video.setText(R.string.video_off);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 0 || user.publishState == 4 ? 2 : 3);
                } else {
                    img_video_control.setImageResource(R.drawable.icon_control_camera_01);
                    txt_video.setText(R.string.video_on);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 2 ? 4 : 1);
                }
                popupWindow.dismiss();
            }
        });
        lin_audio_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                    img_audio_control.setImageResource(R.drawable.icon_control_mute);
                    txt_audio.setText(R.string.close_audio);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 0 || user.publishState == 4 ? 1 : 3);
                } else {
                    img_audio_control.setImageResource(R.drawable.icon_control_audio);
                    txt_audio.setText(R.string.open_audio);
                    RoomManager.getInstance().changeUserPublish(user.peerId, user.publishState == 3 ? 2 : 4);
                }
                popupWindow.dismiss();
            }
        });

        lin_teather_ecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                recoveryAllVideoTtems();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//        popupWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.RIGHT,0,0);
        popupWindow.showAsDropDown(vdi_teacher, -vdi_teacher.getMeasuredWidth() / 2 - 20, -vdi_teacher.getMeasuredHeight() / 2 - vdi_teacher.getMeasuredHeight() / 4);
    }

    private void recoveryAllVideoTtems() {
        for (int x = 0; x < videoItems.size(); x++) {
            videoItems.get(x).isSplitScreen = false;
            videoItems.get(x).isMoved = false;
        }
        screenID.clear();
        scalemap.clear();
        stuMoveInfoMap.clear();
        do1vsnStudentVideoLayout();
        sendSplitScreen();
        sendScaleVideoItem(false);
        sendStudentMove();
    }

    private void getPlayingList() {
        playingList.clear();

        for (String p : playingMap.keySet()) {
            if (playingMap.get(p)) {
                RoomUser u = RoomManager.getInstance().getUser(p);
                if (playingList.size() <= maxVideo && u != null) {
                    playingList.add(u);
                }
            }
        }
    }

    private void getExData() {
        Bundle bundle = getIntent().getExtras();
        host = bundle.getString("host");
        port = bundle.getInt("port");
        nickname = bundle.getString("nickname");

        if (bundle.containsKey("param")) {
            param = bundle.getString("param");
        } else {
            serial = bundle.getString("serial");
            password = bundle.getString("password");
            userid = bundle.getString("userid");
            userrole = bundle.getInt("userrole");
        }
        domain = bundle.getString("domain");
        if (bundle.containsKey("path")) {
            path = bundle.getString("path");
        }
        if (bundle.containsKey("type")) {
            type = bundle.getInt("type");
        }
        if (bundle.containsKey("servername")) {
            servername = bundle.getString("servername");
        }
        try {
            String brand = android.os.Build.MODEL;
            mobilename = bundle.getString("mobilename");
            if (mobilename != null && !mobilename.isEmpty()) {
                JSONArray mNames = new JSONArray(mobilename);
                for (int i = 0; i < mNames.length(); i++) {
                    if (brand.toLowerCase().equals(mNames.optString(i).toLowerCase())) {
                        mobilenameNotOnList = false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lin_back_and_name || id == R.id.img_back_play_back) {
            showExitDialog();
        } else
//            if (id == R.id.img_servers) {
//            showList(4);
//        } else
            if (id == R.id.img_file_list) {
                showList(2);
            } else if (id == R.id.img_media_list) {
                showList(3);
            } else if (id == R.id.img_member_list) {
                showList(1);
            } else if (id == R.id.txt_class_begin) {

                if (RoomSession.isClassBegin) {
                    try {
                        if (canClassDissMiss || !RoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                            showClassDissMissDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    lin_audio_control.setVisibility(View.INVISIBLE);

                    try {
                        long expires = RoomManager.getInstance().getRoomProperties().getLong("endtime") + 5 * 60;
                        if (RoomControler.isNotLeaveAfterClass()) {
                            RoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
                        }
                        RoomManager.getInstance().pubMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString(), true, expires);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sendClassBeginToPhp();

                }
            } else if (id == R.id.txt_all_mute) {
                getMemberList();
                for (int i = 0; i < memberList.size(); i++) {
                    RoomUser u = memberList.get(i);
                    Log.e("xiao", u.role + "");
                    if (u.role == 2) {
                        if (u.publishState == 3) {
                            RoomManager.getInstance().changeUserPublish(u.peerId, 2);
                        } else if (u.publishState == 1) {
                            RoomManager.getInstance().changeUserPublish(u.peerId, 4);
                        }
                    }
                }
//            txt_all_mute.setBackgroundResource(R.drawable.round_back_red_black);
//            txt_all_mute.setClickable(false);
            } else if (id == R.id.txt_all_unmute) {
                getPlayingList();
                for (int i = 0; i < playingList.size(); i++) {
                    RoomUser u = playingList.get(i);
                    Log.e("xiao", u.role + "");
                    if (u.role == 2) {
                        if (u.publishState == 2) {
                            RoomManager.getInstance().changeUserPublish(u.peerId, 3);
                        } else if (u.publishState == 4) {
                            RoomManager.getInstance().changeUserPublish(u.peerId, 1);
                        }
                    }
                }
            } else if (id == R.id.txt_all_send_gift) {
                HashMap<String, RoomUser> receiverMap = new HashMap<String, RoomUser>();
                for (RoomUser u : RoomManager.getInstance().getUsers().values()) {
                    if (u.role == 2) {
                        receiverMap.put(u.peerId, u);
//                    long giftnumber = 0;
//                    if (u.properties.containsKey("giftnumber")) {
//                        giftnumber = u.properties.get("giftnumber") instanceof Integer ? (int) u.properties.get("giftnumber") : (long) u.properties.get("giftnumber");
//                    }
//                    giftnumber++;
//                    RoomManager.getInstance().changeUserProperty(u.peerId, "__all", "giftnumber", giftnumber);
                    }
                }
                if (receiverMap.size() != 0) {
                    sendGift(receiverMap);
                }
            } else if (id == R.id.txt_send_msg) {
//            String msg = edt_chat_input.getText().toString().trim();
//            if (msg != null && !msg.isEmpty()) {
//                RoomManager.getInstance().sendMessage(msg);
//            }
            } else if (id == R.id.txt_chat_input) {
                showChatEditPop();
//            showDialog();
            } else if (id == R.id.txt_video) {
                RoomUser user = RoomManager.getInstance().getMySelf();
                RoomManager.getInstance().disableLocalVideo(!user.disablevideo);
            } else if (id == R.id.txt_audio) {
                RoomUser user = RoomManager.getInstance().getMySelf();
                if (!user.disableaudio) {
                    RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", false);
                }
                RoomManager.getInstance().disableLocalAudio(!user.disableaudio);
            } else if (id == R.id.txt_send_up_photo) {
                showPhotoControlPop();
            }
    }

    private void showPhotoControlPop() {
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_photo_control, null);
        TextView txt_camera = (TextView) contentView.findViewById(R.id.txt_camera);
        TextView txt_selectphoto = (TextView) contentView.findViewById(R.id.txt_selectphoto);
        TextView txt_cacel = (TextView) contentView.findViewById(R.id.txt_cacel);
        popupWindowPhoto = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowPhoto.setContentView(contentView);
        txt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPauseLocalVideo) {
                    RoomManager.getInstance().pauseLocalCamera();
                    isPauseLocalVideo = !isPauseLocalVideo;
                }
                isOpenCamera = true;
                isBackApp = true;
//                openCamera();
                popupWindowPhoto.dismiss();
            }
        });
        txt_selectphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackApp = true;
                openAlbum();
                popupWindowPhoto.dismiss();
            }
        });
        txt_cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowPhoto.dismiss();
            }
        });
        popupWindowPhoto.setBackgroundDrawable(new BitmapDrawable());
        popupWindowPhoto.setFocusable(true);
        popupWindowPhoto.setOutsideTouchable(true);
        popupWindowPhoto.showAsDropDown(txt_send_up_photo, -txt_send_up_photo.getMeasuredWidth() + txt_send_up_photo.getMeasuredWidth() / 3, -txt_send_up_photo.getMeasuredHeight() * 2 - txt_send_up_photo.getMeasuredHeight() / 2);
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM_IMAGE);
    }

    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int ALBUM_IMAGE = 2; //相册
    public static File tempFile;
    private Uri imageUri;

    public void openCamera() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (PersonInfo_ImageUtils.hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.e("mxl", "存储权限没有开启");
                    return;
                }
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:
                isBackApp = false;
                if (isPauseLocalVideo) {
                    RoomManager.getInstance().resumeLocalCamera();
                    isPauseLocalVideo = !isPauseLocalVideo;
                }
                isOpenCamera = false;
                if (resultCode == RESULT_CANCELED) {
                    Log.e("mxl", "取消了");
                    return;
                }
                if (resultCode == RESULT_OK) {
                    Log.e("mxl", "拍照OK");
                    if (data != null) {
                        uri = data.getData();
                    } else {
                        uri = imageUri;
                    }
                    if (!TextUtils.isEmpty(uri.toString())) {
                        try {
                            String path = PersonInfo_ImageUtils.scaleAndSaveImage(PersonInfo_ImageUtils.getRealFilePath(this,
                                    PersonInfo_ImageUtils.getFileUri(uri, this)), 800, 800, this);
                            WhiteBoradManager.getInstance().uploadRoomFile(
                                    RoomManager.getInstance().getRoomProperties().getString("serial"), path);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case ALBUM_IMAGE:
                isBackApp = false;
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    try {
                        if (Build.VERSION.SDK_INT >= 19) {
                            imagePath = PersonInfo_ImageUtils.getImageAfterKitKat(data, this);
                        } else {
                            imagePath = PersonInfo_ImageUtils.getImageBeforeKitKat(data, this);
                        }
                        if (!TextUtils.isEmpty(imagePath)) {
                            String path = PersonInfo_ImageUtils.scaleAndSaveImage(imagePath, 800, 800, this);
                            WhiteBoradManager.getInstance().uploadRoomFile(
                                    RoomManager.getInstance().getRoomProperties().getString("serial"), path);
                        } else {
                            Toast.makeText(this, "图片选择失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void roomManagerRoomJoined() {
        if (TextUtils.isEmpty(RoomManager.getInstance().get_MP3Url())) {
            SoundPlayUtils.init(this);
        } else {
            SoundPlayUtils.loadMP3(host, port, this);
        }
        netBreakCount = 0;
//        registerIt();
        WhiteBoradManager.getInstance().setUserrole(RoomManager.getInstance().getMySelf().role);
        if (getWindow().isActive()) {
            Tools.HideProgressDialog();
        }
        getGiftNum(serial, RoomManager.getInstance().getMySelf().peerId);
        netBreakCount = 0;
        txt_send_up_photo.setEnabled(false);

        for (RoomUser u : RoomManager.getInstance().getUsers().values()) {
            changeUserState(u);
        }
        try {
            maxVideo = RoomManager.getInstance().getRoomProperties().getInt("maxvideo") > 6 ? 6 : RoomManager.getInstance().getRoomProperties().getInt("maxvideo");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("emm", "RoomJoined");
        isInRoom = true;
//        if (t != null) {
//            t.cancel();
//            t = null;
//        }

        String peerid = RoomManager.getInstance().getMySelf().peerId;
//        getGiftNum(RoomManager.getInstance().getRoomProperties().optString("serial"), peerid);
        Log.d("emm", "getGiftNum");

        closeSpeaker();
        //RoomManager.getInstance().useLoudSpeaker(true);
//        myRrole = RoomManager.getInstance().getMySelf().role;
        if (RoomManager.getInstance().getMySelf().role == 0)
            RoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", "__all", null, false, "ClassBegin", null);
        roomType = RoomManager.getInstance().getRoomType();
        Log.e("xiao", "roomtype = " + roomType);
        String roomname = RoomManager.getInstance().getRoomName();
        roomname = StringEscapeUtils.unescapeHtml4(roomname);
        RoomManager.getInstance().getMySelf().nickName = StringEscapeUtils.unescapeHtml4(RoomManager.getInstance().getMySelf().nickName);
        txt_room_name.setText(roomname);
        initViewByRoomTypeAndTeacher();
//        setDisableState();
        txt_hand_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!RoomSession.isClassBegin) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        RoomUser roomUser = RoomManager.getInstance().getMySelf();
                        if (roomUser != null && roomUser.publishState != 0) {
                            RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        RoomUser user = RoomManager.getInstance().getMySelf();
                        //判断是否在台上
                        if (user.publishState == 0) {
                            if (RoomManager.getInstance().getMySelf().properties.containsKey("raisehand")) {
                                boolean israisehand = Tools.isTure(RoomManager.getInstance().getMySelf().properties.get("raisehand"));
                                if (israisehand) {
                                    RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", false);
                                } else {
                                    RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", true);
                                }
                            } else {
                                RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", true);
                            }
                        } else {
                            RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", false);
                        }
                        break;
                }
                return true;
            }
        });

        txt_all_mute.setBackgroundResource(R.drawable.round_back_red_black);
        txt_all_mute.setClickable(false);

//        wbFragment.setToolBarMode(RoomManager.getInstance().getMySelf().role);
//        wbFragment.setTurnPagePermission(myRrole == 0 || (RoomManager.getInstance().getMySelf().role == 2 && RoomControler.isStudentCanTurnPage()));
//        wbFragment.roomManagerUserJoined();
        if (!RoomSession.isClassBegin) {
            playSelfBeforeClassBegin();
        }
        doLayout();

    }

    private void playSelfBeforeClassBegin() {
        RoomUser me = RoomManager.getInstance().getMySelf();
       /* me.publishState = 3;*/
        if (RoomManager.getInstance().getMySelf().role == 0) {
            //doTeacherVideoPlay(me);
            teacherItem.rel_group.setVisibility(View.VISIBLE);
            teacherItem.sf_video.setVisibility(View.VISIBLE);
            if (RoomControler.isReleasedBeforeClass() && me.publishState == 0) {
                RoomManager.getInstance().changeUserPublish(me.peerId, 3);
            } else {
                /*if (me.publishState > 1 && me.publishState < 4) {
                    teacherItem.sf_video.setVisibility(View.VISIBLE);
                } else {
                    teacherItem.sf_video.setVisibility(View.INVISIBLE);
                }*/
                teacherItem.sf_video.setVisibility(View.VISIBLE);
                RoomManager.getInstance().playVideo(me.peerId, teacherItem.sf_video);
            }
            if (stu_in_sd.rel_group != null && !RoomSession.isClassBegin) {
                if (!RoomControler.isReleasedBeforeClass()) {
                    stu_in_sd.rel_group.setVisibility(View.INVISIBLE);
                }
            }
        } else if (RoomManager.getInstance().getMySelf().role == 2) {
            if (roomType == 0) {
                if (stu_in_sd.sf_video != null) {
                    if (!RoomSession.isClassBegin) {
                        if (!RoomControler.isReleasedBeforeClass()) {
                            teacherItem.rel_group.setVisibility(View.INVISIBLE);
                            stu_in_sd.rel_group.setVisibility(View.VISIBLE);
                            stu_in_sd.img_cam.setVisibility(View.INVISIBLE);
                            stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
                            stu_in_sd.img_hand.setVisibility(View.INVISIBLE);
                            stu_in_sd.img_pen.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (RoomControler.isReleasedBeforeClass()) {
                        if (me.publishState > 1 && me.publishState < 4 && !me.disablevideo) {
                            stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                        } else {
                            stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                    }

                    if (RoomControler.isReleasedBeforeClass() && me.publishState == 0) {
                        RoomManager.getInstance().changeUserPublish(me.peerId, 3);
                    } else {
                        RoomManager.getInstance().playVideo(me.peerId, stu_in_sd.sf_video);
                    }
                }
            } else {
                if (RoomControler.isReleasedBeforeClass()) {

                } else {
                    if (!RoomSession.isClassBegin) {
                        teacherItem.rel_group.setVisibility(View.INVISIBLE);
                    }
                }
                if (RoomControler.isReleasedBeforeClass() && me.publishState == 0) {
                    RoomManager.getInstance().changeUserPublish(me.peerId, 3);
                } else {
                    do1vsnClassBeginPlayVideo(me, me.peerId != null);
                }
            }
        } else if (RoomManager.getInstance().getMySelf().role == 4) {
            if (!RoomSession.isClassBegin) {
                teacherItem.rel_group.setVisibility(View.INVISIBLE);
                stu_in_sd.rel_group.setVisibility(View.INVISIBLE);
                stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void do1vsnClassBeginPlayVideo(final RoomUser user, boolean force) {
        boolean hasSit = false;
        sitpos = -1;
        for (int i = 0; i < videoItems.size(); i++) {
            if (videoItems.get(i).peerid.equals(user.peerId)) {
                hasSit = true;
                sitpos = i;
            }
        }
        if (!hasSit) {
            final VideoItem stu = new VideoItem();
            final LinearLayout vdi_stu = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.video_item, null);
            stu.parent = vdi_stu;
            stu.sf_video = (SurfaceViewRenderer) vdi_stu.findViewById(R.id.sf_video);
            stu.sf_video.init(EglBase.create().getEglBaseContext(), null);
            stu.sf_video.setZOrderMediaOverlay(true);
            stu.img_cam = (ImageView) vdi_stu.findViewById(R.id.img_cam);
            stu.img_mic = (ImageView) vdi_stu.findViewById(R.id.img_mic);
            stu.img_pen = (ImageView) vdi_stu.findViewById(R.id.img_pen);
            stu.img_hand = (ImageView) vdi_stu.findViewById(R.id.img_hand_up);
            stu.txt_name = (TextView) vdi_stu.findViewById(R.id.txt_name);
            stu.txt_gift_num = (TextView) vdi_stu.findViewById(R.id.txt_gift_num);
            stu.rel_group = (RelativeLayout) vdi_stu.findViewById(R.id.rel_group);
            stu.img_video_back = (ImageView) vdi_stu.findViewById(R.id.img_video_back);
            stu.lin_gift = (LinearLayout) vdi_stu.findViewById(R.id.lin_gift);
            stu.lin_name_label = (LinearLayout) vdi_stu.findViewById(R.id.lin_name_label);
            stu.rel_video_label = (RelativeLayout) vdi_stu.findViewById(R.id.rel_video_label);

            stu.re_background = (RelativeLayout) vdi_stu.findViewById(R.id.re_background);
            stu.tv_home = (TextView) vdi_stu.findViewById(R.id.tv_home);

            stu.peerid = user.peerId;
            stu.txt_name.setText(user.nickName);

            if (!RoomSession.isClassBegin) {
                stu.img_pen.setVisibility(View.INVISIBLE);
                stu.img_hand.setVisibility(View.INVISIBLE);
                stu.img_mic.setVisibility(View.INVISIBLE);
            }

            if (user.peerId.equals(RoomManager.getInstance().getMySelf().peerId)) {
                if (RoomSession.isClassBegin) {
                    stu.img_hand.setVisibility(View.VISIBLE);
                }
            } else {
                stu.img_hand.setVisibility(View.GONE);
            }
            if (user.disablevideo) {
                stu.img_cam.setImageResource(R.drawable.icon_video_disable);
            } else {
                stu.img_cam.setImageResource(R.drawable.icon_video);
                if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                    stu.img_cam.setVisibility(View.INVISIBLE);
                } else {
                    if (RoomSession.isClassBegin) {
                        stu.img_cam.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (user.disableaudio) {
                stu.img_mic.setImageResource(R.drawable.icon_audio_disable);
            } else {
                stu.img_mic.setImageResource(R.drawable.icon_audio);
                if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                    stu.img_mic.setVisibility(View.INVISIBLE);
                } else {
                    if (RoomSession.isClassBegin) {
                        stu.img_mic.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (user.role == 2) {
                stu.lin_gift.setVisibility(View.VISIBLE);
            } else {
                stu.lin_gift.setVisibility(View.INVISIBLE);
            }

            if (RoomControler.isReleasedBeforeClass()) {
                if (user.publishState > 1 && user.publishState < 4) {
                    stu.sf_video.setVisibility(View.VISIBLE);
                } else {
                    stu.sf_video.setVisibility(View.INVISIBLE);
                }
            }
            videoItems.add(stu);
            rel_students.addView(vdi_stu);
            do1vsnStudentVideoLayout();
            stu.sf_video.setVisibility(View.VISIBLE);

            RoomManager.getInstance().playVideo(user.peerId, stu.sf_video);
        } else if (force) {
            if (sitpos != -1) {
                if (user.disablevideo) {
                    videoItems.get(sitpos).img_cam.setImageResource(R.drawable.icon_video_disable);
                } else {
                    videoItems.get(sitpos).img_cam.setImageResource(R.drawable.icon_video);
                    if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                        videoItems.get(sitpos).img_cam.setVisibility(View.INVISIBLE);
                    } else {
                        if (RoomSession.isClassBegin) {
                            videoItems.get(sitpos).img_cam.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (user.disableaudio) {
                    videoItems.get(sitpos).img_mic.setImageResource(R.drawable.icon_audio_disable);
                } else {
                    videoItems.get(sitpos).img_mic.setImageResource(R.drawable.icon_audio);
                    if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                        videoItems.get(sitpos).img_mic.setVisibility(View.INVISIBLE);
                    } else {
                        if (RoomSession.isClassBegin) {
                            videoItems.get(sitpos).img_mic.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (RoomControler.isReleasedBeforeClass()) {
                    if (user.publishState > 1 && user.publishState < 4) {
                        videoItems.get(sitpos).sf_video.setVisibility(View.VISIBLE);
                    } else {
                        videoItems.get(sitpos).sf_video.setVisibility(View.INVISIBLE);
                    }
                }
                RoomManager.getInstance().playVideo(user.peerId, videoItems.get(sitpos).sf_video);
            }
        }
    }

    private void unPlaySelfAfterClassBegin() {
        RoomUser me = RoomManager.getInstance().getMySelf();
//        me.publishState = 0;
        if (roomType == 0) {
            RoomManager.getInstance().unPlayVideo(RoomManager.getInstance().getMySelf().peerId);
            stu_in_sd.rel_group.setVisibility(View.INVISIBLE);
        } else if (RoomManager.getInstance().getMySelf().role == 2) {
            do1vsnStudentUnPlayVideo(me);
        }
    }

    @Override
    public void roomManagerRoomLeaved() {
        SoundPlayUtils.release();

        removeVideoFragment();
        romoveScreenFragment();
//        wbFragment.roomDisConnect();
        WhiteBoradManager.getInstance().clear();
        playingMap.clear();
        playingList.clear();
        RoomSession.publishSet.clear();
        RoomSession.isClassBegin = false;
//        myRrole = -1;
        Log.d("emm", "RoomLeaved");
        isInRoom = false;
        mediaListAdapter.setLocalfileid(-1);
        RoomManager.isMediaPublishing = false;
        isWBMediaPlay = false;
        RoomSession.isPublish = false;
        RoomSession.isPlay = false;

        if (RoomClient.getInstance().isExit()) {
            clear();
            finish();
        } else {
            for (int x = 0; x < videoItems.size(); x++) {
                rel_students.removeView(videoItems.get(x).parent);
            }
            videoItems.clear();

            Tools.ShowProgressDialog(this, getString(R.string.connected));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (wbFragment != null) {
//                        wbFragment.setDrawable(false);
//                        wbFragment.roomDisConnect();
                    }
                }
            });
            if (popupWindowPhoto != null) {
                popupWindowPhoto.dismiss();
            }
            txt_send_up_photo.setBackgroundResource(R.drawable.round_back_red_black);
            txt_send_up_photo.setEnabled(false);
        }
        img_disk.clearAnimation();
        img_disk.setVisibility(View.INVISIBLE);
        lin_audio_control.setVisibility(View.INVISIBLE);
//        unregisterIt();

    }

    @Override
    public void roomManagerDidFailWithError(int i) {
        Log.d("emm", "DidFailWithError");
        RoomClient.getInstance().joinRoomcallBack(i);
    }

    @Override
    public void roomManagerUserJoined(RoomUser user, boolean inList) {
        if (inList && user.role != 4) {
            if (user.role == 0 && RoomManager.getInstance().getMySelf().role == 0 ||
                    (RoomManager.getInstance().getRoomType() == 0 && user.role == RoomManager.getInstance().getMySelf().role)) {
                RoomManager.getInstance().evictUser(user.peerId);
            }
        }
        user.nickName = StringEscapeUtils.unescapeHtml4(user.nickName);
        changeUserState(user);
        ChatData ch = new ChatData();
        ch.setState(1);
        ch.setInOut(true);
        ch.setStystemMsg(true);
        ch.setUser(user);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        ch.setTime(str);
        if (user.role != 4) {
            chatList.add(ch);
        }
        chlistAdapter.notifyDataSetChanged();
        list_chat.setSelection(chlistAdapter.getCount());
        getMemberList();
        memberListAdapter.notifyDataSetChanged();

        if (user.properties.containsKey("isInBackGround") && RoomManager.getInstance().getMySelf().role != 2) {
            if (user == null) {
                return;
            }
            boolean isinback = Tools.isTure(user.properties.get("isInBackGround"));
            ChatData ch2 = new ChatData();
            ch.setState(2);
            ch.setHold(isinback);
            ch.setStystemMsg(true);
            ch.setUser(user);
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str2 = formatter2.format(curDate2);
            ch.setTime(str2);
            if (user.role != 4) {
                chatList.add(ch2);
            }
            chlistAdapter.notifyDataSetChanged();
            list_chat.setSelection(chlistAdapter.getCount());
            getMemberList();
            memberListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void roomManagerUserLeft(RoomUser RoomUser) {
        stuMoveInfoMap.remove(RoomUser.peerId);
        ChatData ch = new ChatData();
        ch.setState(1);
        ch.setInOut(false);
        ch.setStystemMsg(true);
        ch.setUser(RoomUser);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        ch.setTime(str);
        if (RoomUser != null && RoomUser.role != 4) {
            chatList.add(ch);
        }
        chlistAdapter.notifyDataSetChanged();
        list_chat.setSelection(chlistAdapter.getCount());
        RoomSession.publishSet.remove(RoomUser.peerId);
        getMemberList();
        memberListAdapter.notifyDataSetChanged();
        changeUserState(RoomUser);
        doUnPlayVideo(RoomUser);
        if (roomType == 0 && RoomUser != null && RoomUser.role == 2) {
            stu_in_sd.txt_name.setText("");
            stu_in_sd.txt_gift_num.setText(0 + "");
        }
    }

    @Override
    public void roomManagerUserChanged(RoomUser RoomUser, Map<String, Object> map, String s) {
        RoomSession.pandingSet.remove(RoomUser.peerId);
        if (playingMap.containsKey(RoomUser.peerId) && RoomUser.publishState > 0) {
            playingMap.put(RoomUser.peerId, RoomUser.publishState >= 1 && RoomUser.publishState <= 4);
            getMemberList();
            memberListAdapter.notifyDataSetChanged();
            if (map.containsKey("publishstate")) {
                if (RoomUser.publishState > 0) {
                    Log.e("xiao", "userchange");
                    doPlayVideo(RoomUser.peerId);
                } else {
                    doUnPlayVideo(RoomUser);
                }
            }
        }
        do1vsnStudentVideoLayout();
        changeUserState(RoomUser);
        checkRaiseHands();
        if (map.containsKey("isInBackGround")) {
            if (RoomUser == null) {
                return;
            }
            boolean isinback = Tools.isTure(map.get("isInBackGround"));
            setBackgroundOrReception(isinback, RoomUser);
        }
        if (RoomSession.isClassBegin) {
//            setDisableState();
            if (RoomManager.getInstance().getMySelf().properties.containsKey("raisehand")) {
                boolean israisehand = Tools.isTure(RoomManager.getInstance().getMySelf().properties.get("raisehand"));
                RoomUser roomUser = RoomManager.getInstance().getMySelf();
                if (israisehand) {
                    if (roomUser != null && roomUser.publishState == 0) {
                        txt_hand_up.setText(R.string.no_raise);
                    } else {
                        txt_hand_up.setText(R.string.raiseing);
                    }
                    txt_hand_up.setBackgroundResource(R.drawable.round_raiseing);
                } else {
                    txt_hand_up.setText(R.string.raise); //同意了，或者拒绝了
                    txt_hand_up.setBackgroundResource(R.drawable.round_back_red);
                }
            } else {
                txt_hand_up.setBackgroundResource(R.drawable.round_back_red);
                txt_hand_up.setText(R.string.raise); //还没举手
            }

            if (map.containsKey("isInBackGround")) {
                boolean isinback = Tools.isTure(map.get("isInBackGround"));
                if (RoomManager.getInstance().getMySelf().role != 2) {
                    ChatData ch = new ChatData();
                    ch.setState(2);
                    ch.setHold(isinback);
                    ch.setStystemMsg(true);
                    ch.setUser(RoomUser);
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String str = formatter.format(curDate);
                    ch.setTime(str);
                    if (RoomUser != null && RoomUser.role != 4) {
                        chatList.add(ch);
                    }
                    chlistAdapter.notifyDataSetChanged();
                    list_chat.setSelection(chlistAdapter.getCount());
                    getMemberList();
                    memberListAdapter.notifyDataSetChanged();
                }
            }
            if (map.containsKey("giftnumber") && !RoomUser.peerId.equals(s)) {
                SoundPlayUtils.play();
                if (roomType == 0) {
                    showGiftAim(stu_in_sd);
                } else if (RoomUser != null && RoomUser.role == 2) {
                    for (int i = 0; i < videoItems.size(); i++) {
                        if (RoomUser.peerId.equals(videoItems.get(i).peerid)) {
                            showGiftAim(videoItems.get(i));
                            break;
                        }
                    }
                }
            }
            if (RoomUser.peerId.equals(RoomManager.getInstance().getMySelf().peerId) && map.containsKey("candraw") && !(RoomManager.getInstance().getMySelf().role == 0)) {
                boolean candraw = Tools.isTure(map.get("candraw"));
//                wbFragment.setDrawable(candraw);
                if (candraw) {
                    txt_send_up_photo.setBackgroundResource(R.drawable.round_back_red);
                    txt_send_up_photo.setEnabled(true);
//                    wbFragment.setTurnPagePermission(true);
                } else {
                    if (popupWindowPhoto != null) {
                        popupWindowPhoto.dismiss();
                    }
                    txt_send_up_photo.setBackgroundResource(R.drawable.round_back_red_black);
                    txt_send_up_photo.setEnabled(false);
//                    wbFragment.setTurnPagePermission(myRrole == 0 || (RoomManager.getInstance().getMySelf().role == 2 && RoomControler.isStudentCanTurnPage()));
                }
            }
        }
        if (RoomUser.peerId.equals(RoomManager.getInstance().getMySelf().peerId) && map.containsKey("servername") && !s.equals(RoomManager.getInstance().getMySelf().peerId)) {
            String servername = (String) map.get("servername");
            SharedPreferences sp = getSharedPreferences("classroom", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("servername", servername);
            editor.commit();
            RoomManager.getInstance().switchService(servername);
        }

        if (RoomUser.peerId.equals(RoomManager.getInstance().getMySelf().peerId) && map.containsKey("volume")) {
            Number n_volume = (Number) map.get("volume");
            int int_volume = n_volume.intValue();
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, int_volume, 0);
        }
        checkMute();
        memberListAdapter.notifyDataSetChanged();
    }


    private void setBackgroundOrReception(boolean b, RoomUser RoomUser) {
        if (RoomUser != null && RoomUser.role == 0) {
            if (b) {
                teacherItem.re_background.setVisibility(View.VISIBLE);
                teacherItem.tv_home.setText(R.string.tea_background);
            } else {
                teacherItem.re_background.setVisibility(View.GONE);
            }
        }

        for (int x = 0; x < videoItems.size(); x++) {
            if (videoItems.get(x).peerid.equals(RoomUser.peerId)) {
                if (b) {
                    videoItems.get(x).re_background.setVisibility(View.VISIBLE);
                    videoItems.get(x).tv_home.setText(R.string.background);
                } else {
                    videoItems.get(x).re_background.setVisibility(View.GONE);
                }
            }
        }
        if (stu_in_sd.parent != null) {
            if (stu_in_sd.peerid != null) {
                if (!stu_in_sd.peerid.isEmpty()) {
                    if (stu_in_sd.peerid.equals(RoomUser.peerId)) {
                        if (b) {
                            stu_in_sd.re_background.setVisibility(View.VISIBLE);
                            stu_in_sd.tv_home.setText(R.string.background);
                        } else {
                            stu_in_sd.re_background.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    private void checkMute() {
        boolean isMute = true;
        for (RoomUser u : RoomManager.getInstance().getUsers().values()) {
            if ((u.publishState == 1 || u.publishState == 3) && u.role == 2) {
                isMute = false;
            }
        }
        if (isMute) {
            txt_all_mute.setBackgroundResource(R.drawable.round_back_red_black);
            txt_all_mute.setClickable(false);
            txt_all_unmute.setBackgroundResource(R.drawable.round_back_unmute_all_able);
            txt_all_unmute.setClickable(true);
        } else {
            txt_all_mute.setBackgroundResource(R.drawable.round_back_red);
            txt_all_mute.setClickable(true);
            txt_all_unmute.setBackgroundResource(R.drawable.round_back_unmute_all_unable);
            txt_all_unmute.setClickable(false);
        }
    }

    @Override
    public void roomManagerUserPublished(RoomUser RoomUser) {
        RoomUser.properties.remove("passivityPublish");
        if (RoomUser.publishState > 0) {
//            if (roomType == 0) {
//                if (RoomUser.role != 1)
//                    playingMap.put(RoomUser.peerId, RoomUser.publishState > 1 && RoomUser.publishState < 4);
//            } else {
            playingMap.put(RoomUser.peerId, RoomUser.publishState > 1 && RoomUser.publishState < 4);
//            }
        }
        RoomSession.pandingSet.remove(RoomUser.peerId);
        if (RoomUser.publishState >= 1 && RoomUser != null && RoomUser.role != 0) {
            RoomSession.publishSet.add(RoomUser.peerId);
        }
        getPlayingList();
        Log.e("xiao", "userpublish");
        doPlayVideo(RoomUser.peerId);
        doLayout();
        do1vsnStudentVideoLayout();
        changeUserState(RoomUser);
        memberListAdapter.notifyDataSetChanged();
//        studentMoveOrScreen();
    }

    @Override
    public void roomManagerUserUnPublished(RoomUser RoomUser) {
        RoomUser.properties.remove("passivityPublish");
        playingMap.remove(RoomUser.peerId);
        RoomSession.publishSet.remove(RoomUser.peerId);
        stuMoveInfoMap.remove(RoomUser.peerId);
        screenID.remove(RoomUser.peerId);
//        if(RoomUser.publishState<=1)
//            RoomManager.getInstance().unPlayVideo(RoomUser.peerId);
//        if (RoomUser.publishState <= 0) {
        doUnPlayVideo(RoomUser);
        doLayout();
        do1vsnStudentVideoLayout();
        changeUserState(RoomUser);
//        }
//        }

        /*if(processVideo)
        {
            unpublishlist.add(RoomUser.peerId);
            return;
        }*/
        //processVideo = true;
        memberListAdapter.notifyDataSetChanged();
        if (RoomUser != null && RoomUser.role != 0) {
            studentMoveOrScreen();
        }
        if (!RoomSession.isClassBegin && RoomManager.getInstance().getMySelf().role == 0 && RoomUser.role == 0 && RoomManager.getInstance().getMySelf().peerId.equals(RoomUser.peerId)) {
            playSelfBeforeClassBegin();
        }
        if (studentPopupWindow != null) {
            studentPopupWindow.dismiss();
        }
        if (chatWindow != null) {
            chatWindow.dismiss();
        }
    }

    @Override
    public void roomManagerSelfEvicted(int reason) {
        RoomClient.getInstance().setExit(true);
        RoomClient.getInstance().kickout(reason == 1 ? RoomClient.Kickout_ChairmanKickout : RoomClient.Kickout_Repeat);
    }

    private void studentMoveOrScreen() {
        boolean isScreen = false;
        for (int x = 0; x < videoItems.size(); x++) {
            if (videoItems.get(x).isSplitScreen) {
                isScreen = true;
                break;
            }
        }
        if (isScreen) {
            sendSplitScreen();
        } else {
            if (RoomManager.getInstance().getMySelf().role == 0) {
                sendStudentMove();
            }
        }
    }

    private void unPlayAll() {
        if (roomType != 0) {
            for (int i = 0; i < playingList.size(); i++) {
                RoomUser u = playingList.get(i);
                if (u.role == 2) {
                    RoomManager.getInstance().unPlayVideo(u.peerId);
                }
            }
        }
    }

    @Override
    public void roomManagerMessageReceived(RoomUser RoomUser, String s, int type, long ts) {
        ChatData ch = new ChatData();
        ch.setUser(RoomUser);
        ch.setStystemMsg(false);
        if (type == 0) {
            ch.setMessage(s);
            ch.setTrans(false);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = null;
            if (StringUtils.isEmpty(path)) {
                curDate = new Date(System.currentTimeMillis());//获取当前时间
            } else {
                curDate = new Date(ts);
            }
            String str = formatter.format(curDate);
            ch.setTime(str);
            chatList.add(ch);
            chlistAdapter.notifyDataSetChanged();
            list_chat.setSelection(chlistAdapter.getCount());
        }
    }

    @Override
    public void roomManagerOnRemoteMsg(boolean add, String id, String name, long ts, Object data, boolean inList, String fromid, String associatedMsgID, String associatedUserID) {
        if (add) {
            OnRemotePubMsg(id, name, ts, data, inList, fromid, associatedMsgID, associatedUserID);
        } else {
            OnRemoteDelMsg(id, name, ts, data, inList, fromid, associatedMsgID, associatedUserID);
        }
    }

    @Override
    public void roomManagerOnMediaStatus(RoomUser RoomUser, Map<String, String> map) {

    }

    @Override
    public void roomManagerMediaPublish(Stream stream) {
        if (wbFragment != null) {
            wbFragment.closeNewPptVideo();
        }
        this.stream = stream;
        RoomSession.isPublish = true;
        RoomSession.isPlay = false;
        RoomManager.isMediaPublishing = false;
        isWBMediaPlay = false;
        isMediaMute = false;
        Object objfileid = stream.attrMap.get("fileid");
        long fileid = -1;
        if (objfileid != null) {
            if (objfileid instanceof String) {
                fileid = Long.valueOf(objfileid.toString());
            } else if (objfileid instanceof Number) {
                fileid = ((Number) objfileid).longValue();
            }
        }
        mediaListAdapter.setLocalfileid(fileid);
        if (stream.isVideo()) {
            readyForPlayVideo(stream);
        } else {
            if (RoomManager.getInstance().getMySelf().role == 0) {
                lin_audio_control.setVisibility(View.VISIBLE);
                img_disk.setVisibility(View.INVISIBLE);
            } else {
                lin_audio_control.setVisibility(View.INVISIBLE);
                img_disk.setVisibility(View.VISIBLE);
                boolean ispause = stream.attrMap.get("pause") == null ? false : (boolean) stream.attrMap.get("pause");
                if (ispause) {
                    img_disk.clearAnimation();
                } else {
                    img_disk.startAnimation(operatingAnim);
                }
            }
            img_voice_mp3.setImageResource(R.drawable.btn_volume_pressed);
            vol = 0.5;
            sek_voice_mp3.setProgress((int) (vol * 100));
            int da = (int) stream.attrMap.get("duration");
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
            Date daDate = new Date(da);
            mp3Duration = formatter.format(daDate);
            txt_mp3_time.setText("00:00" + "/" + mp3Duration);
            if (txt_mp3_name != null) {
                txt_mp3_name.setText((String) stream.attrMap.get("filename"));
            }
        }
    }

    @Override
    public void roomManagerMediaUnPublish(Stream stream) {
        RoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
        mediaListAdapter.setLocalfileid(-1);
        mp3Duration = "00:00";
        if (stream.isVideo()) {
            removeVideoFragment();
        } else {
            lin_audio_control.setVisibility(View.INVISIBLE);
            img_disk.clearAnimation();
            img_disk.setVisibility(View.INVISIBLE);
        }
        if (RoomSession.isPlay) {
            RoomSession.isPlay = false;
            ShareDoc media = WhiteBoradManager.getInstance().getCurrentMediaDoc();
            mediaListAdapter.setLocalfileid(media.getFileid());
            WhiteBoradManager.getInstance().setCurrentMediaDoc(media);
            String strSwfpath = media.getSwfpath();
            int pos = strSwfpath.lastIndexOf('.');
            strSwfpath = String.format("%s-%d%s", strSwfpath.substring(0, pos), 1, strSwfpath.substring(pos));
            String url = "http://" + WhiteBoradManager.getInstance().getFileServierUrl() + ":" + WhiteBoradManager.getInstance().getFileServierPort() + strSwfpath;
            RoomManager.isMediaPublishing = true;
            if (RoomSession.isClassBegin) {
                RoomManager.getInstance().publishMedia(url, com.classroomsdk.Tools.isMp4(media.getFilename()), media.getFilename(), media.getFileid(), "__all");
            } else {
                RoomManager.getInstance().publishMedia(url, com.classroomsdk.Tools.isMp4(media.getFilename()), media.getFilename(), media.getFileid(), RoomManager.getInstance().getMySelf().peerId);
            }
        }
        if (isWBMediaPlay) {
//            int pos = this.url.lastIndexOf('.');
//            this.url = String.format("%s-%d%s", url.substring(0, pos), 1, url.substring(pos));
//            String url = "http://" + WhiteBoradManager.getInstance().getFileServierUrl() + ":" + WhiteBoradManager.getInstance().getFileServierPort() + this.url;
            RoomManager.isMediaPublishing = true;
            if (RoomSession.isClassBegin) {
                RoomManager.getInstance().publishMedia(url, isvideo, "", fileid, "__all");
            } else {
                RoomManager.getInstance().publishMedia(url, isvideo, "", fileid, RoomManager.getInstance().getMySelf().peerId);
            }
            isWBMediaPlay = false;
        }
        RoomSession.isPlay = false;
        RoomSession.isPublish = false;
    }

    @Override
    public void roomManagerUpdateAttributeStream(Stream stream, long pos, boolean isPlay) {
        if (stream.isVideo()) {
            if (videofragment == null) {
                readyForPlayVideo(stream);
                if (wbFragment != null) {
                    wbFragment.closeNewPptVideo();
                }
                this.stream = stream;
                RoomSession.isPublish = true;
                RoomSession.isPlay = false;
                RoomManager.isMediaPublishing = false;
                isWBMediaPlay = false;
                isMediaMute = false;
                Object objfileid = stream.attrMap.get("fileid");
                long fileid = -1;
                if (objfileid != null) {
                    if (objfileid instanceof String) {
                        fileid = Long.valueOf(objfileid.toString());
                    } else if (objfileid instanceof Number) {
                        fileid = ((Number) objfileid).longValue();
                    }
                }
                mediaListAdapter.setLocalfileid(fileid);

            } else {
                videofragment.controlMedia(stream, pos, isPlay);
            }

        } else {

            if (sek_mp3 != null) {
                int curtime = (int) ((double) pos / (int) stream.attrMap.get("duration") * 100);
                sek_mp3.setProgress(curtime);

            }
            if (img_play_mp3 != null) {
                if (!isPlay) {
                    img_play_mp3.setImageResource(R.drawable.btn_pause_normal);
                    if (!(RoomManager.getInstance().getMySelf().role == 0)) {
                        img_disk.startAnimation(operatingAnim);
                    }
                } else {
                    img_play_mp3.setImageResource(R.drawable.btn_play_normal);
                    if (!(RoomManager.getInstance().getMySelf().role == 0)) {
                        img_disk.clearAnimation();
                    }
                }
            }
            if (txt_mp3_time != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
                Date curDate = new Date(pos);//获取当前时间
                Date daDate = new Date((int) stream.attrMap.get("duration"));
                String strcur = formatter.format(curDate);
                String strda = formatter.format(daDate);
                txt_mp3_time.setText(strcur + "/" + strda);
            }
            if (txt_mp3_name != null) {
                txt_mp3_name.setText((String) stream.attrMap.get("filename"));
            }
        }
    }

//    @Override
//    public void roomManagerRoomConnected(RoomUser roomUser) {
//        getGiftNum(serial, roomUser.peerId);
//    }

    @Override
    public void roomManagerPlayBackClearAll() {
        if (chatList != null) {
            chatList.clear();
        }
        if (chlistAdapter != null) {
            chlistAdapter.notifyDataSetChanged();
        }
//        if (wbFragment != null) {
//            wbFragment.roomPlaybackClearAll();
//        }
    }

    @Override
    public void roomManagerPlayBackUpdateTime(long currenttime) {
        if (isEnd) {
            return;
        }
        this.currenttime = currenttime;
        double pos = (double) (currenttime - starttime) / (double) (endtime - starttime);
        sek_play_back.setProgress((int) (pos * 100));

        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
        Date curDate = new Date(currenttime - starttime);//获取当前时间
        Date daDate = new Date(endtime - starttime);
        String strcur = formatter.format(curDate);
        String strda = formatter.format(daDate);
        txt_play_back_time.setText(strcur + "/" + strda);
    }

    long starttime;
    long endtime;
    long currenttime;

    @Override
    public void roomManagerPlayBackDuration(long starttime, long endtime) {
        this.starttime = starttime;
        this.endtime = endtime;
    }

    @Override
    public void roomManagerPlayBackEnd() {
        isPlayBackPlay = false;
        img_play_back.setImageResource(R.drawable.btn_play_normal);
        sek_play_back.setProgress(0);
        RoomManager.getInstance().pausePlayback();
        isEnd = true;
    }

    @Override
    public void roomManagerCameraLost() {

    }

    @Override
    public void roomManagerPublishConnectFailed() {

    }

    @Override
    public void roomManagerScreenPublish(Stream stream) {
        this.stream = stream;
        RoomSession.isPublish = true;
        if (wbFragment != null) {
            wbFragment.closeNewPptVideo();
        }
        for (int i = 0; i < videoItems.size(); i++) {
            videoItems.get(i).sf_video.setZOrderMediaOverlay(false);
            videoItems.get(i).sf_video.setVisibility(View.INVISIBLE);
        }
        screenFragment = ScreenFragment.getInstance();
        screenFragment.setStream(stream);
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (!screenFragment.isAdded()) {
            ft.replace(R.id.video_container, screenFragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void roomManagerScreenUnPublish(Stream stream) {
        romoveScreenFragment();
    }

    @Override
    public void onCapturerStopped() {
        if (isOpenCamera) {
            openCamera();
        }
    }

    @Override
    public void onCapturerStarted(boolean b) {

    }

    /***
     *媒体文件播放失败
     */
    @Override
    public void onPublishError(int i) {
        mediaListAdapter.setLocalfileid(-1);
        mediaEXListEXAdapter.setLocalfileid(-1);
        Toast.makeText(this, getResources().getString(R.string.publisherror), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUDPError(final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = i == 1 ? getString(R.string.udp_alert) : getString(R.string.fire_wall_alert);
                Tools.ShowAlertDialog(RoomActivity.this, msg);
                if (i == 2) {
                    RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "udpstate", 2);
                }
            }
        });

    }

    @Override
    public void roomManagerRoomConnectFaild() {
        RoomClient.getInstance().setExit(true);
        Tools.HideProgressDialog();
        removeVideoFragment();
        romoveScreenFragment();
//        wbFragment.roomDisConnect();
        WhiteBoradManager.getInstance().clear();
        playingMap.clear();
        playingList.clear();
        RoomSession.publishSet.clear();
        RoomSession.isClassBegin = false;
//        myRrole = -1;
        Log.d("emm", "RoomLeaved");
        isInRoom = false;
        mediaListAdapter.setLocalfileid(-1);
        RoomManager.isMediaPublishing = false;
        isWBMediaPlay = false;
        RoomSession.isPublish = false;
        RoomSession.isPlay = false;

        for (int x = 0; x < videoItems.size(); x++) {
            rel_students.removeView(videoItems.get(x).parent);
        }
        videoItems.clear();

        clear();
        finish();
        RoomClient.getInstance().joinRoomcallBack(-1);
    }

    private void romoveScreenFragment() {
        RoomSession.isPublish = false;
        screenFragment = ScreenFragment.getInstance();
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (screenFragment.isAdded()) {
            ft.remove(screenFragment);
            ft.commitAllowingStateLoss();
        }
        if (!isZoom) {
            for (int i = 0; i < videoItems.size(); i++) {
                videoItems.get(i).sf_video.setZOrderMediaOverlay(true);
                videoItems.get(i).sf_video.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPageFinished() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] pers = new String[2];
            if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pers[0] = Manifest.permission.CAMERA;
                }
            }


            if (!(checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    for (int i = 0; i < pers.length; i++) {
                        if (pers[i] == null) {
                            pers[i] = Manifest.permission.RECORD_AUDIO;
                        }
                    }
                }
            }
            if (pers[0] != null) {
                requestPermissions(pers, REQUEST_CODED);
            } else {
                joinRoom();
            }
        } else {
            joinRoom();
        }
    }

    @Override
    public void onRoomDocChange(boolean isdel, boolean ismedia) {
        if (RoomControler.isDocumentClassification()) {
            WhiteBoradManager.getInstance().getClassDocList();
            WhiteBoradManager.getInstance().getAdminDocList();
            WhiteBoradManager.getInstance().getClassMediaList();
            WhiteBoradManager.getInstance().getAdminmMediaList();
            mediaEXListEXAdapter.notifyDataSetChanged();
            fileListEXAdapter.notifyDataSetChanged();
            if (!isdel && exList != null) {
                if (ismedia) {
                    MediaEXListEXAdapter.isClassExpand = true;
                    if (popupWindowList != null && popupWindowList.isShowing()) {
                        exList.expandGroup(0);
                    }
                } else {
                    FileListEXAdapter.isClassExpand = true;
                    if (popupWindowList != null && popupWindowList.isShowing()) {
                        exList.expandGroup(1);
                    }
                }
            }
        } else {
            WhiteBoradManager.getInstance().getDocList();
            WhiteBoradManager.getInstance().getMediaList();
            fileListAdapter.notifyDataSetChanged();
            mediaListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onWhiteBoradZoom(final boolean isZoom) {
        this.isZoom = isZoom;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isZoom) {
                    for (int i = 0; i < videoItems.size(); i++) {
                        videoItems.get(i).sf_video.setZOrderMediaOverlay(false);
                        videoItems.get(i).sf_video.setVisibility(View.INVISIBLE);
                    }
                    lin_menu.setVisibility(View.GONE);
                    rel_students.setVisibility(View.GONE);

                    lin_audio_control.setVisibility(View.GONE);
                    rel_tool_bar.setVisibility(View.GONE);
                    LinearLayout.LayoutParams main_param = (LinearLayout.LayoutParams) lin_main.getLayoutParams();
                    main_param.width = LinearLayout.LayoutParams.MATCH_PARENT;

                    main_param.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    lin_main.setLayoutParams(main_param);
                    LinearLayout.LayoutParams w_param = (LinearLayout.LayoutParams) rel_w.getLayoutParams();
                    main_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    main_param.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    rel_w.setLayoutParams(w_param);
                    LinearLayout.LayoutParams rel_wb_param = (LinearLayout.LayoutParams) rel_wb_container.getLayoutParams();
                    rel_wb_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    rel_wb_param.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    rel_wb_container.setLayoutParams(rel_wb_param);
                } else {
                    for (int i = 0; i < videoItems.size(); i++) {
                        videoItems.get(i).sf_video.setZOrderMediaOverlay(true);
                        videoItems.get(i).sf_video.setVisibility(View.VISIBLE);
                    }
                    lin_menu.setVisibility(View.VISIBLE);
                    if (videoItems.size() > 0) {
                        rel_students.setVisibility(View.VISIBLE);
                    }

                    rel_tool_bar.setVisibility(View.VISIBLE);
                    if (RoomManager.getInstance().getMySelf().role == 0 && RoomSession.isPublish && !com.classroomsdk.Tools.isMp4(WhiteBoradManager.getInstance().getCurrentMediaDoc().getFilename())) {
                        lin_audio_control.setVisibility(View.VISIBLE);
                    } else {
                        lin_audio_control.setVisibility(View.INVISIBLE);
                    }

                    if (RoomManager.getInstance().getMySelf() != null && !TextUtils.isEmpty(RoomManager.getInstance().getMySelf().peerId)) {
                        doPlayVideo(RoomManager.getInstance().getMySelf().peerId);
                    }
                    if (!RoomSession.isClassBegin) {
                        playSelfBeforeClassBegin();
                    }
                   /* doPlayVideo(null);*/
                    doLayout();
                }
            }
        });
    }

    private String url;
    private boolean isvideo;
    private long fileid;
    private boolean isWBMediaPlay = false;

    @Override
    public void onWhiteBoradMediaPublish(String url, boolean isvideo, long fileid) {
        this.url = url;
        this.isvideo = isvideo;
        this.fileid = fileid;
        isWBMediaPlay = true;
    }

    public void joinRoom() {
        Log.d("classroomsdk", "Start!");

        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!param.isEmpty())
            params.put("param", param);

        params.put("userid", userid);
        params.put("password", password);
        params.put("serial", serial);
        params.put("userrole", userrole);
        params.put("nickname", nickname);
        params.put("volume", 100);

        params.put("mobilenameOnList", mobilenameNotOnList);
        if (domain != null && !domain.isEmpty())
            params.put("domain", domain);
        if (servername != null && !servername.isEmpty())
            params.put("servername", servername);

        if (path != null && !path.isEmpty()) {
            params.put("path", path);
            if (type != -1)
                params.put("type", type);
//            RoomManager.getInstance().joinPlayBackRoom(host, port, nickname, params, new HashMap<String, Object>(), true);
        }
//        else {
//            RoomManager.getInstance().setTestServer("192.168.1.252",8890);
        RoomManager.getInstance().setDeviceType("AndroidPad");

        int ret = RoomManager.getInstance().joinRoom(host, port, nickname, params, new HashMap<String, Object>(), true);
        if (ret != RoomManager.ERR_OK) {
            Log.e("RoomActivity", "nonono");
        }
//        }
//        getGiftNumJoinRoom(serial, userid, nickname, params);
    }

    private void checkRaiseHands() {
        int count = 0;
        for (RoomUser u : RoomManager.getInstance().getUsers().values()) {
            if (u.role == 2) {
                if (u.properties.containsKey("raisehand")) {
                    boolean israisehand = Tools.isTure(u.properties.get("raisehand"));
                    if (israisehand) {
                        count++;
                    }
                }
            }
        }
        if (count > 0) {
            img_member_list.setImageResource(R.drawable.icon_users_normal_raise_hand);
        } else {
            img_member_list.setImageResource(R.drawable.img_member_list);
        }
    }

    private void clear() {
        RoomSession.isClassBegin = false;
//        myRrole = -1;
        isFromList = false;
        RoomClient.getInstance().setExit(false);
        playingMap.clear();
        playingList.clear();
        RoomSession.pandingSet.clear();
        chatList.clear();
        RoomManager.getInstance().setCallbBack(null);
        RoomManager.getInstance().setWhiteBoard(null);
        teacherItem.sf_video.release();
        stu_in_sd.sf_video.release();
        for (int i = 0; i < videoItems.size(); i++) {
            videoItems.get(i).sf_video.release();
        }
        RoomClient.getInstance().setExit(false);
        WhiteBoradManager.getInstance().clear();
//        RoomManager.getInstance().useLoudSpeaker(false);
        closeSpeaker();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remind);
        builder.setMessage(R.string.logouts);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RoomClient.getInstance().setExit(true);
                        RoomManager.getInstance().leaveRoom();
                        if (RoomManager.getInstance() != null) {
                            if (RoomManager.getInstance().getRoomStatus() == 0 || RoomManager.getInstance().getRoomStatus() == 3 ||
                                    RoomManager.getInstance().getRoomStatus() == 6) {
                                finish();
                            }
                        }
                    }
                }).setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void showClassDissMissDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remind);
        builder.setMessage(R.string.make_sure_class_dissmiss);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            RoomManager.getInstance().delMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        txt_class_begin.setVisibility(View.GONE);
                        sendClassDissToPhp();
                    }
                }).setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /***
     *
     * @param type 1，人员列表  3，媒体列表 2,文档列表
     */
    ExpandableListView exList;
    View contentView;
    ListView list;
    TextView txt_topic;
    TextView txt_sure;

    private void initPopupWindow() {
        contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_pop, null);
//        contentView.setBackgroundColor(Color.BLUE);
        list = (ListView) contentView.findViewById(R.id.list);
        exList = (ExpandableListView) contentView.findViewById(R.id.ex_list);
        txt_topic = (TextView) contentView.findViewById(R.id.topic);
        txt_sure = (TextView) contentView.findViewById(R.id.txt_sure);
    }

    PopupWindow popupWindowList;

    private void showList(int type) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int hid = dm.heightPixels;
        popupWindowList = new PopupWindow(findViewById(R.id.mainLayout), wid / 3, hid);
        popupWindowList.setContentView(contentView);
        if (type == 1) {
            exList.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

            getMemberList();
            if (memberList != null && memberList.size() > 0) {
                txt_topic.setText(getString(R.string.userlist) + "（" + memberList.size() + "）");
            }
            list.setAdapter(memberListAdapter);
            memberListAdapter.notifyDataSetChanged();
            txt_sure.setVisibility(View.GONE);
        } else if (type == 2) {

            txt_topic.setText(getString(R.string.doclist));
            WhiteBoradManager.getInstance().getDocList();
            WhiteBoradManager.getInstance().getAdminDocList();
            txt_sure.setVisibility(View.GONE);

            if (RoomControler.isDocumentClassification()) {
                exList.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
                exList.setAdapter(fileListEXAdapter);
                fileListEXAdapter.setPop(popupWindowList);
                exList.setGroupIndicator(null);
                if (FileListEXAdapter.isClassExpand && exList != null) {
                    exList.expandGroup(1);
                }
                if (FileListEXAdapter.isAdminExpand && exList != null) {
                    exList.expandGroup(2);
                }
                exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        if (groupPosition == 1) {
                            FileListEXAdapter.isClassExpand = !parent.isGroupExpanded(groupPosition);
                        } else if (groupPosition == 2) {
                            FileListEXAdapter.isAdminExpand = !parent.isGroupExpanded(groupPosition);
                        }
                        return false;
                    }
                });
            } else {
                fileListAdapter.setPop(popupWindowList);
                list.setAdapter(fileListAdapter);
                fileListAdapter.notifyDataSetChanged();
                exList.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }

        } else if (type == 3) {

            txt_topic.setText(getString(R.string.medialist));
            WhiteBoradManager.getInstance().getDocList();
            WhiteBoradManager.getInstance().getAdminmMediaList();
            txt_sure.setVisibility(View.GONE);

            if (RoomControler.isDocumentClassification()) {

                exList.setVisibility(View.VISIBLE);
                exList.setAdapter(mediaEXListEXAdapter);
                mediaEXListEXAdapter.setPop(popupWindowList);
                list.setVisibility(View.GONE);
                exList.setGroupIndicator(null);
                if (MediaEXListEXAdapter.isClassExpand && exList != null) {
                    exList.expandGroup(0);
                }
                if (MediaEXListEXAdapter.isAdminExpand && exList != null) {
                    exList.expandGroup(1);
                }
                exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        if (groupPosition == 0) {
                            MediaEXListEXAdapter.isClassExpand = !parent.isGroupExpanded(groupPosition);
                        } else if (groupPosition == 1) {
                            MediaEXListEXAdapter.isAdminExpand = !parent.isGroupExpanded(groupPosition);
                        }
                        return false;
                    }
                });
            } else {
                list.setAdapter(mediaListAdapter);
                mediaListAdapter.setPop(popupWindowList);
                mediaListAdapter.notifyDataSetChanged();
                exList.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        }

        popupWindowList.setBackgroundDrawable(new BitmapDrawable());
        popupWindowList.setFocusable(true);
        popupWindowList.setOutsideTouchable(true);
        popupWindowList.showAtLocation(findViewById(R.id.mainLayout), Gravity.RIGHT, 0, 0);
    }

    private void getMemberList() {
        memberList.clear();
        for (RoomUser u : RoomManager.getInstance().getUsers().values()) {
            if (!u.peerId.equals(RoomManager.getInstance().getMySelf().peerId) && (u.role == 2 || u.role == 1)) {
                if (u.role == 1) {
                    memberList.add(0, u);
                } else {
                    memberList.add(u);
                }
            }
        }
    }

    private void OnRemotePubMsg(String id, String name, long ts, Object data, boolean inList, String fromid, String associatedMsgID, String associatedUserID) {
        RoomSession.getInstance().addTempVideoWBRemoteMsg(true, id, name, ts, data, fromid, associatedMsgID, associatedUserID);
        if (name.equals("ClassBegin")) {
            if (RoomSession.isClassBegin) {
                return;
            }
            RoomSession.isClassBegin = true;

            txt_hand_up.setBackgroundResource(R.drawable.round_back_red);

            if (!RoomControler.isReleasedBeforeClass()) {
                if (RoomManager.getInstance().getMySelf().role == 0) {
                    RoomManager.getInstance().changeUserPublish(RoomManager.getInstance().getMySelf().peerId, 3);
                } else if (RoomControler.isAutomaticUp() && RoomSession.publishSet.size() < maxVideo && RoomManager.getInstance().getMySelf().role == 2) {
                    RoomManager.getInstance().changeUserPublish(RoomManager.getInstance().getMySelf().peerId, 3);
                }
            } else {
                if (RoomManager.getInstance().getMySelf().role == 0) {
                    RoomManager.getInstance().changeUserPublish(RoomManager.getInstance().getMySelf().peerId, 3);
                } else if (!RoomControler.isAutomaticUp() && RoomManager.getInstance().getMySelf().role == 2) {
                    RoomManager.getInstance().changeUserPublish(RoomManager.getInstance().getMySelf().peerId, 0);
                } else if (RoomControler.isAutomaticUp() &&
                        RoomManager.getInstance().getMySelf().publishState != 3 && RoomSession.publishSet.size() < maxVideo) {
                    if (RoomManager.getInstance().getMySelf().role == 2 || RoomManager.getInstance().getMySelf().role == 0) {
                        RoomManager.getInstance().changeUserPublish(RoomManager.getInstance().getMySelf().peerId, 3);
                    }
                }
            }
            try {
                if (!RoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                    txt_class_begin.setBackgroundResource(R.drawable.round_back_red);
                    txt_class_begin.setText(R.string.classdismiss);
                    txt_class_begin.setClickable(true);
                } else {
                    txt_class_begin.setBackgroundResource(R.drawable.round_back_red_black);
                    txt_class_begin.setText(R.string.classdismiss);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (RoomManager.getInstance().getMySelf().role == 0 && roomType != 0) {
                lin_control_tool.setVisibility(View.VISIBLE);
                txt_all_send_gift.setVisibility(View.VISIBLE);
            }
//                wbFragment.setAddPagePermission(myRrole == 0 && RoomSession.isClassBegin);
//                wbFragment.setTurnPagePermission(myRrole == 0 || (RoomManager.getInstance().getMySelf().role == 2 && RoomControler.isStudentCanTurnPage()));
            if (RoomManager.getInstance().getMySelf().role == 0) {
//                    wbFragment.setDrawable(true);
                wbFragment.localChangeDoc();
            }
            roomType = RoomManager.getInstance().getRoomType();
            initViewByRoomTypeAndTeacher();
//                if(!isMeTeacher){
//                    lin_self_av_control.setVisibility(View.VISIBLE);
//                }
            if (RoomManager.getInstance().getMySelf().role == 4 || RoomManager.getInstance().getMySelf().role == 0) {
                if (!RoomControler.isShowClassBeginButton()) {
//                        if (RoomControler.isAutoClassBegin()) {
//                            txt_class_begin.setVisibility(View.GONE);
//                        } else {
                    txt_class_begin.setVisibility(View.VISIBLE);
//                        }
                } else
                    txt_class_begin.setVisibility(View.GONE);
            } else {
                txt_class_begin.setVisibility(View.GONE);
            }

            classStartTime = ts;
            RoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", RoomManager.getInstance().getMySelf().peerId, null, false, null, null);
            if (timerbefClassbegin != null) {
                timerbefClassbegin.cancel();
                timerbefClassbegin = null;
            }
            if (RoomManager.getInstance().getMySelf().role == 0 && !inList) {
                RoomManager.getInstance().unPublishMedia();
            }
            if (roomType == 0 && userrole == 2 && RoomControler.isAutoHasDraw()) {
                RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "candraw", true);
            }
            if (!RoomControler.isReleasedBeforeClass()) {
                unPlaySelfAfterClassBegin();
            }
            RoomClient.getInstance().onClassBegin();
        } else if (name.equals("UpdateTime")) {
            if (RoomSession.isClassBegin) {
                if (timerbefClassbegin != null) {
                    timerbefClassbegin.cancel();
                    timerbefClassbegin = null;
                }
                lin_time.setVisibility(View.VISIBLE);
                serviceTime = ts;
                localTime = serviceTime - classStartTime;
                if (timerAddTime == null) {
                    timerAddTime = new Timer();
                    timerAddTime.schedule(new AddTime(), 1000, 1000);
                }
            } else {
                if (timerbefClassbegin == null && !RoomSession.isClassBegin && !getfinalClassBeginMode()) {
                    timerbefClassbegin = new Timer();
                    timerbefClassbegin.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        long nowTime = System.currentTimeMillis() / 1000;
                                        long startTime = RoomManager.getInstance().getRoomProperties().getLong("starttime");
                                        long proTime = startTime - nowTime;
                                        if (proTime == 60 && !timeMessages.get(0).isShowed) {
                                            showTimeTipPop(timeMessages.get(0));
                                        }
                                        if (proTime <= -60 && !timeMessages.get(1).isShowed) {
                                            int overtime = Math.abs((int) (proTime / 60));
                                            timeMessages.get(1).message = getString(R.string.classroom_part_01) + "<font color='#FFD700'>" + overtime + "</font> " + getString(R.string.classroom_part_02);
                                            showTimeTipPop(timeMessages.get(1));
                                        }
                                        if (proTime <= 60) {
                                            txt_class_begin.setBackgroundResource(R.drawable.round_back_red);
                                            txt_class_begin.setText(R.string.classbegin);
                                            txt_class_begin.setClickable(true);
                                            Log.e("xiao", "proTime<=60 proTime = " + proTime);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    }, 500, 1000);
                } else if (!RoomSession.isClassBegin) {
                    if (RoomManager.getInstance().getMySelf().role == 0 && getfinalClassBeginMode()) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        try {
                            long expires = RoomManager.getInstance().getRoomProperties().getLong("endtime") + 5 * 60;
                            if (RoomControler.isNotLeaveAfterClass()) {
                                RoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
                            }
                            RoomManager.getInstance().pubMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString(), true, expires);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if (name.equals("ShowPage")) {
            mediaListAdapter.notifyDataSetChanged();
            fileListAdapter.notifyDataSetChanged();
            fileListEXAdapter.notifyDataSetChanged();
            mediaEXListEXAdapter.notifyDataSetChanged();
        }

        if (name.equals("StreamFailure")) {
            Map<String, Object> mapdata = null;
            if (data instanceof String) {
                String str = (String) data;
                try {
                    JSONObject js = new JSONObject(str);
                    mapdata = toMap(js);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mapdata = (Map<String, Object>) data;
            }
            String stupeerid = (String) mapdata.get("studentId");

            RoomSession.pandingSet.remove(stupeerid);
            memberListAdapter.setPubFailUserId(stupeerid);
            memberListAdapter.notifyDataSetChanged();

            if (RoomManager.getInstance().getMySelf().role == 0) {
                RoomUser u = RoomManager.getInstance().getUser(stupeerid);
                if (u != null) {
                    if (u.properties.containsKey("passivityPublish")) {
                        int failuretype = -1;
                        if (u.properties.get("failuretype") != null) {
                            failuretype = (Integer) u.properties.get("failuretype");
                        }
                        switch (failuretype) {
                            case 1:
                                Toast.makeText(this, R.string.udp_faild, Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(this, R.string.publish_faild, Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(this, R.string.member_overload, Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                Toast.makeText(this, u.nickName + getResources().getString(R.string.select_back_hint), Toast.LENGTH_LONG).show();
                                break;
                            case 5:
                                Toast.makeText(this, R.string.udp_break, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                    u.properties.remove("passivityPublish");
                }
            }
        }

        if (name.equals("videoDraghandle")) {
            if (studentPopupWindow != null) {
                studentPopupWindow.dismiss();
            }
            JSONObject mapdata = null;
            if (data instanceof String) {
                String str = (String) data;
                try {
                    mapdata = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mapdata = new JSONObject((Map<String, Object>) data);
            }
            videoarr = mapdata.optJSONObject("otherVideoStyle");
            if (videoarr != null) {
                sIterator = videoarr.keys();
                while (sIterator.hasNext()) {
                    // 获得key
                    String peerid = sIterator.next();
                    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                    JSONObject videoinfo = videoarr.optJSONObject(peerid);
                    float left = (float) videoinfo.optDouble("percentLeft");
                    float top = (float) videoinfo.optDouble("percentTop");
                    boolean isDrag = Tools.isTure(videoinfo.opt("isDrag"));
                    MoveVideoInfo mi = new MoveVideoInfo();
                    if (inList && RoomManager.getInstance().getMySelf().peerId.equals(peerid)) {
                        mi.top = 0;
                        mi.left = 0;
                        mi.isDrag = false;
                    } else {
                        mi.top = top;
                        mi.left = left;
                        mi.isDrag = isDrag;
                    }
                    stuMoveInfoMap.put(peerid, mi);
                    if (inList) {
                        continue;
                    }
                    moveStudent(peerid, top, left, isDrag);
                }
                Log.e("mxl", "msg:videoDraghandle");
            }
            if (inList) {
                JSONObject jsonObject = new JSONObject();
                JSONObject moveData = new JSONObject();
                try {
                    Set set = stuMoveInfoMap.keySet();
                    if (set != null) {
                        Iterator iterator = set.iterator();
                        while (iterator.hasNext()) {
                            JSONObject md = new JSONObject();
                            String peerid = (String) iterator.next();
                            MoveVideoInfo moveVideoInfo = stuMoveInfoMap.get(peerid);
                            md.put("percentTop", moveVideoInfo.top);
                            md.put("percentLeft", moveVideoInfo.left);
                            md.put("isDrag", moveVideoInfo.isDrag);
                            moveData.put(peerid, md);
                        }
                    }
                    jsonObject.put("otherVideoStyle", moveData);
                    RoomManager.getInstance().pubMsg("videoDraghandle", "videoDraghandle", "__allExceptSender", jsonObject.toString(), true, "ClassBegin", null);
                    Log.e("mxl", "videoDraghandle");
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }

        if (name.equals("VideoSplitScreen")) {
            if (studentPopupWindow != null) {
                studentPopupWindow.dismiss();
            }
            JSONObject splitScreen = null;
            if (data instanceof String) {
                String str = (String) data;
                try {
                    splitScreen = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                splitScreen = new JSONObject((Map<String, Object>) data);
            }
            screen = splitScreen.optJSONArray("userIDArry");
            try {
                screenID.clear();
                for (int y = 0; y < screen.length(); y++) {
                    String peerid = (String) screen.get(y);
                    screenID.add(peerid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (videoItems.size() > 0) {
                for (int x = 0; x < videoItems.size(); x++) {
                    if (screenID.contains(videoItems.get(x).peerid)) {
                        videoItems.get(x).isSplitScreen = true;
                    } else {
                        videoItems.get(x).isSplitScreen = false;
                    }
                }
                do1vsnStudentVideoLayout();
            }
            Log.e("mxl", "msg:VideoSplitScreen");
        }

        if (name.equals("VideoChangeSize")) {
            if (studentPopupWindow != null) {
                studentPopupWindow.dismiss();
            }
            JSONObject mapdata = null;
            if (data instanceof String) {
                String str = (String) data;
                try {
                    mapdata = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mapdata = new JSONObject((Map<String, Object>) data);
            }
            JSONObject scaleVideoData = mapdata.optJSONObject("ScaleVideoData");
            Iterator<String> scaleKeys = scaleVideoData.keys();
            while (scaleKeys.hasNext()) {
                String peerid = scaleKeys.next();
                JSONObject videoinfo = scaleVideoData.optJSONObject(peerid);
                float scale = (float) videoinfo.optDouble("scale");
                scalemap.put(peerid, scale);
                for (int x = 0; x < videoItems.size(); x++) {
                    if (videoItems.get(x).peerid.equals(peerid)) {
                        LayoutZoomOrIn.zoomMsgVideoItem(videoItems.get(x), scale, printWidth, printHeight);
                    }
                }
            }
            Log.e("mxl", "msg:VideoChangeSize");
        }
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private void OnRemoteDelMsg(String id, String name, long ts, Object data, boolean inList, String fromid, String associatedMsgID, String associatedUserID) {
        RoomSession.getInstance().addTempVideoWBRemoteMsg(false, id, name, ts, data, fromid, associatedMsgID, associatedUserID);
        if (name.equals("ClassBegin")) {
            RoomSession.isClassBegin = false;

            if (!RoomControler.isNotLeaveAfterClass()) {
                RoomManager.getInstance().changeUserPublish(RoomManager.getInstance().getMySelf().peerId, 0);
                RoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
            }

            lin_control_tool.setVisibility(View.GONE);
            txt_class_begin.setVisibility(View.GONE);
            txt_all_send_gift.setVisibility(View.GONE);
            teacherItem.txt_name.setText("");

//            if(!isMeTeacher){
//                lin_self_av_control.setVisibility(View.VISIBLE);
//            }

            localTime = 0;
            if (timerAddTime != null) {
                timerAddTime.cancel();
                timerAddTime = null;
            }
            try {
                if (!RoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                    if (userrole == 0) {
                        txt_class_begin.setBackgroundResource(R.drawable.round_back_red);
                        txt_class_begin.setText(R.string.classbegin);
                        txt_class_begin.setClickable(true);
                        txt_class_begin.setVisibility(View.VISIBLE);
                    } else {
                        if (!RoomControler.isNotLeaveAfterClass()) {
                            RoomClient.getInstance().setExit(true);
                            RoomManager.getInstance().leaveRoom();
                        }
                    }
                    txt_hand_up.setClickable(false);
                    txt_hand_up.setBackgroundResource(R.drawable.round_back_red_black);
                    txt_hand_up.setText(R.string.raise);
                    lin_time.setVisibility(View.INVISIBLE);
                    txt_hour.setText("00");
                    txt_min.setText("00");
                    txt_ss.setText("00");
                    recoveryAllVideoTtems();
                    memberListAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          /*  playSelfBeforeClassBegin();*/
            RoomClient.getInstance().onClassDismiss();
            RoomManager.getInstance().unPublishMedia();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!RoomControler.isNotLeaveAfterClass()) {
                        chatList.clear();
                        chlistAdapter.notifyDataSetChanged();
                    }
                }
            }, 250);
        }

        if (name.equals("VideoSplitScreen")) {
            if (studentPopupWindow != null) {
                studentPopupWindow.dismiss();
            }
            if (studentPopupWindow != null) {
                studentPopupWindow.dismiss();
            }
            screenID.clear();
            if (videoItems.size() > 0) {
                for (int x = 0; x < videoItems.size(); x++) {
                    videoItems.get(x).isSplitScreen = false;
                }
            }
            do1vsnStudentVideoLayout();
            Log.e("mxl", "msg:VideoSplitScreen");
        }
    }

    private void sendClassDissToPhp() {
        if (!(RoomManager.getInstance().getMySelf().role == 0)) {
            return;
        }
        String webFun_controlroom = "http://" + host + ":" + port + "/ClientAPI" + "/roomover";
        RequestParams params = new RequestParams();
        try {
            params.put("act", 3);
            if (RoomControler.isAutoClassDissMiss()) {
                params.put("endsign", 1);
            }
            params.put("serial", RoomManager.getInstance().getRoomProperties().get("serial"));
            params.put("companyid", RoomManager.getInstance().getRoomProperties().get("companyid"));
            client.post(webFun_controlroom, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                    try {
                        int nRet = response.getInt("result");
                        if (nRet == 0) {
//                            RoomManager.getInstance().delMsg("ClassBegin", "ClassBegin", "__all", new HashMap<String, Object>());
//                            txt_class_begin.setVisibility(View.GONE);
                        } else {
                            Log.e("demo", "下课接口调用失败，失败数据：");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("emm", "error=" + throwable.toString());
//                RoomClient.getInstance().joinRoomcallBack(-1);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void readyForPlayVideo(Stream stream) {
//        goneAllVideo();
        for (int i = 0; i < videoItems.size(); i++) {
            videoItems.get(i).sf_video.setZOrderMediaOverlay(false);
            videoItems.get(i).sf_video.setVisibility(View.INVISIBLE);
        }
        videofragment = VideoFragment.getInstance();
        videofragment.setStream(stream);
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (!videofragment.isAdded()) {
            ft.replace(R.id.video_container, videofragment);
            ft.commitAllowingStateLoss();
        }
//        fm_video_container.bringToFront();
    }

    public void removeVideoFragment() {
        videofragment = VideoFragment.getInstance();
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (videofragment.isAdded()) {
            mediaListAdapter.setLocalfileid(-1);
            mediaEXListEXAdapter.setLocalfileid(-1);
            ft.remove(videofragment);
            ft.commitAllowingStateLoss();
        }
        if (!isZoom) {
            for (int i = 0; i < videoItems.size(); i++) {
                videoItems.get(i).sf_video.setZOrderMediaOverlay(true);
                videoItems.get(i).sf_video.setVisibility(View.VISIBLE);
                RoomManager.getInstance().playVideo(videoItems.get(i).peerid, videoItems.get(i).sf_video);
            }
        }
    }

    private void changeUserState(final RoomUser user) {
        getPlayingList();
//        for (int i = 0; i < playingList.size(); i++) {
//            final RoomUser user = playingList.get(i);
        if (user.role == 0) {
            teacherItem.img_pen.setVisibility(View.INVISIBLE);
            teacherItem.img_hand.setVisibility(View.INVISIBLE);

            if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                teacherItem.img_mic.setVisibility(View.INVISIBLE);
            } else {
                teacherItem.img_mic.setVisibility(View.VISIBLE);
            }
        }

        if (roomType == 0) {
            stu_in_sd.peerid = user.peerId;
            if (!user.peerId.equals(RoomManager.getInstance().getMySelf().peerId) && user.role == RoomManager.getInstance().getMySelf().role) {
                return;
            }
            //小班课
            if (user.role == 2) {
                stu_in_sd.img_cam.setVisibility(View.VISIBLE);
                if (user.disablevideo) {
                    stu_in_sd.img_cam.setImageResource(R.drawable.icon_video_disable);
                } else {
                    stu_in_sd.img_cam.setImageResource(R.drawable.icon_video);
                    if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                        stu_in_sd.img_cam.setVisibility(View.INVISIBLE);
                    } else {
                        stu_in_sd.img_cam.setVisibility(View.VISIBLE);
                    }
                }
                stu_in_sd.img_mic.setVisibility(View.VISIBLE);
                if (user.disableaudio) {
                    stu_in_sd.img_mic.setImageResource(R.drawable.icon_audio_disable);
                } else {
                    stu_in_sd.img_mic.setImageResource(R.drawable.icon_audio);
                    if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                        stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
                    } else {
                        stu_in_sd.img_mic.setVisibility(View.VISIBLE);
                    }
                }

                if (user.properties.containsKey("candraw")) {
                    boolean candraw = Tools.isTure(user.properties.get("candraw"));
                    if (candraw) {
                        stu_in_sd.img_pen.setVisibility(View.VISIBLE);//可以画图
                    } else {
                        stu_in_sd.img_pen.setVisibility(View.INVISIBLE);//不可以画图
                    }
                } else {
                    stu_in_sd.img_pen.setVisibility(View.INVISIBLE);//没给过画图权限
                }

                if (user.properties.containsKey("raisehand")) {
                    boolean israisehand = Tools.isTure(user.properties.get("raisehand"));
                    if (israisehand) {
                        stu_in_sd.img_hand.setVisibility(View.VISIBLE);//正在举手
                    } else {
                        stu_in_sd.img_hand.setVisibility(View.INVISIBLE);//同意了，或者拒绝了
                    }
                } else {
                    stu_in_sd.img_hand.setVisibility(View.INVISIBLE);//还没举手
                }

                if (user.properties.containsKey("giftnumber")) {
                    long giftnumber = user.properties.get("giftnumber") instanceof Integer ? (int) user.properties.get("giftnumber") : (long) user.properties.get("giftnumber");
//                        showGiftAim(stu_in_sd);
                    stu_in_sd.txt_gift_num.setText(giftnumber + "");
                }

                stu_in_sd.rel_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showStudentControlPop(user, -1);
                    }
                });
            }

            if (user.role == 1) {
                for (int i = 0; i < videoItems.size(); i++) {
                    final int finalI = i;
                    videoItems.get(i).parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RoomUser user = RoomManager.getInstance().getUser(videoItems.get(finalI).peerid);
                            showStudentControlPop(user, finalI);
                        }
                    });

                    if (user.peerId.equals(videoItems.get(i).peerid)) {
                        if (user.disableaudio) {
                            videoItems.get(i).img_mic.setImageResource(R.drawable.icon_audio_disable);
                        } else {
                            videoItems.get(i).img_mic.setImageResource(R.drawable.icon_audio);
                            if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                                videoItems.get(i).img_mic.setVisibility(View.INVISIBLE);
                            } else {
                                videoItems.get(i).img_mic.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        } else {
            if (user.role == 2 || user.role == 1) {
                for (int i = 0; i < videoItems.size(); i++) {
                    if (user.peerId.equals(videoItems.get(i).peerid)) {
                        videoItems.get(i).img_cam.setVisibility(View.VISIBLE);
                        if (user.disablevideo) {
                            videoItems.get(i).img_cam.setImageResource(R.drawable.icon_video_disable);
                        } else {
                            videoItems.get(i).img_cam.setImageResource(R.drawable.icon_video);
                            if (user.publishState == 0 || user.publishState == 1 || user.publishState == 4) {
                                videoItems.get(i).img_cam.setVisibility(View.INVISIBLE);
                            } else {
                                videoItems.get(i).img_cam.setVisibility(View.VISIBLE);
                            }
                        }
                        videoItems.get(i).img_mic.setVisibility(View.VISIBLE);
                        if (user.disableaudio) {
                            videoItems.get(i).img_mic.setImageResource(R.drawable.icon_audio_disable);
                        } else {
                            videoItems.get(i).img_mic.setImageResource(R.drawable.icon_audio);
                            if (user.publishState == 0 || user.publishState == 2 || user.publishState == 4) {
                                videoItems.get(i).img_mic.setVisibility(View.INVISIBLE);
                            } else {
                                videoItems.get(i).img_mic.setVisibility(View.VISIBLE);
                            }
                        }
                        if (user.properties.containsKey("candraw")) {
                            boolean candraw = Tools.isTure(user.properties.get("candraw"));
                            if (candraw) {
                                videoItems.get(i).img_pen.setVisibility(View.VISIBLE);//可以画图
                            } else {
                                videoItems.get(i).img_pen.setVisibility(View.INVISIBLE);//不可以画图
                            }
                        } else {
                            videoItems.get(i).img_pen.setVisibility(View.INVISIBLE);//没给过画图权限
                        }

                        if (user.properties.containsKey("raisehand")) {
                            boolean israisehand = Tools.isTure(user.properties.get("raisehand"));
                            if (israisehand) {
                                videoItems.get(i).img_hand.setVisibility(View.VISIBLE);
                               /* if (RoomManager.getInstance().getMySelf().role == 0) {
                                    if (user.role == 2) {
                                        videoItems.get(i).img_hand.setVisibility(View.VISIBLE);//正在举手
                                    }
                                }
                                if (RoomManager.getInstance().getMySelf().role == 2) {
                                    videoItems.get(i).img_hand.setVisibility(View.VISIBLE);//正在举手
                                }*/

                            } else {
                                videoItems.get(i).img_hand.setVisibility(View.INVISIBLE);//同意了，或者拒绝了
                            }
                        } else {
                            videoItems.get(i).img_hand.setVisibility(View.INVISIBLE);//还没举手
                        }

                        if (user.properties.containsKey("giftnumber")) {
                            long giftnumber = user.properties.get("giftnumber") instanceof Integer ? (int) user.properties.get("giftnumber") : (long) user.properties.get("giftnumber");
                            videoItems.get(i).txt_gift_num.setText(giftnumber + "");
//                    showGiftAim(videoItems.get(i - teachermark));
                        }

                    }
                }
                for (int i = 0; i < videoItems.size(); i++) {
                    final int finalI = i;
                    videoItems.get(i).parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (videoItems.size() > finalI) {
                                RoomUser user = RoomManager.getInstance().getUser(videoItems.get(finalI).peerid);
                                showStudentControlPop(user, finalI);
                            }
                        }
                    });
                }
            }
        }
    }

    private void getGiftNum(String roomNum, final String peerId) {

        String url = "http://" + host + ":" + port + "/ClientAPI/getgiftinfo";
        RequestParams params = new RequestParams();
        params.put("serial", roomNum);
        params.put("receiveid", peerId);

        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    int nRet = response.getInt("result");
                    if (nRet == 0) {
                        JSONArray infos = response.optJSONArray("giftinfo");
                        JSONObject info = infos.getJSONObject(0);
                        final long gifnum = info.optInt("giftnumber", 0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RoomManager.getInstance().changeUserProperty(RoomManager.getInstance().getMySelf().peerId, "__all", "giftnumber", gifnum);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error=" + throwable.toString());
//                RoomClient.getInstance().joinRoomcallBack(-1);
            }
        });
    }

    public void getGiftNumJoinRoom(String roomNum, final String peerId, final String nickname, final Map<String, Object> paramsMap) {

        String url = "http://" + host + ":" + port + "/ClientAPI/getgiftinfo";
        final RequestParams params = new RequestParams();
        params.put("serial", roomNum);
        params.put("receiveid", peerId);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    int nRet = response.getInt("result");
                    final Map<String, Object> pert = new HashMap<String, Object>();
                    if (nRet == 0) {
                        JSONArray infos = response.optJSONArray("giftinfo");
                        JSONObject info = infos.getJSONObject(0);
                        long gifnum = info.optInt("giftnumber");
                        if (peerId == null || peerId.isEmpty()) {
                            pert.put("giftnumber", 0);
//                            NotificationCenter.getInstance().postNotificationName(UpdataGiftNum,gifnum);
                        } else {
                            pert.put("giftnumber", gifnum);
                        }

                    }
                    Log.d("emm", "gifnum = " + pert.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RoomManager.getInstance().setTestServer("192.168.1.57", 8890);
                            int ret = RoomManager.getInstance().joinRoom(host, port, nickname, paramsMap, pert, true);
                            if (ret != RoomManager.ERR_OK) {
                                Log.e("RoomActivity", "nonono");
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error=" + throwable.toString());
//                RoomClient.getInstance().joinRoomcallBack(-1);
            }
        });
    }

    @Override
    public void onResult(final int index, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatList.size() > index) {
                    chatList.get(index).setTrans(true);
                    chatList.get(index).setTrans(result);
                    chlistAdapter.notifyDataSetChanged();
                    list_chat.setSelection(index);
                }
            }
        });

    }

    private void showGiftAim(VideoItem item) {
        //初始化 Translate动画
        final ImageView img_gift = new ImageView(this);
        img_gift.setImageResource(R.drawable.ico_gift);
        RelativeLayout.LayoutParams relparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img_gift.setLayoutParams(relparam);
        rel_parent.addView(img_gift);


        int[] loca = new int[2];
        item.lin_gift.getLocationInWindow(loca);
       /* if (item.lin_gift.getVisibility() != View.VISIBLE) {
            return;
        }*/
        float x = loca[0];
        float y = loca[1];
//        int[] giftLoca = new int[2];
//        img_gift.getLocationOnScreen(giftLoca);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int hid = dm.heightPixels;
        float gx = wid / 2 - img_gift.getWidth();
        float gy = hid / 2 - img_gift.getHeight();

        float dx = x - gx;
        float dy = y - gy;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, dx, 0, dy);
//        translateAnimation.setFillAfter(true);
        //初始化 Alpha动画
        //初始化
//        ScaleAnimation scaleAnimationBig = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f);
//        scaleAnimationBig.setStartOffset(1000);
        ScaleAnimation scaleAnimation = new ScaleAnimation(4.0f, 0.1f, 4.0f, 0.1f);
        //scaleAnimation.setFillAfter(true);
        //动画集
        AnimationSet set = new AnimationSet(true);
//        set.setFillAfter(true);
        set.setFillBefore(false);
//        set.addAnimation(scaleAnimationBig);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimation);

        //设置动画时间 (作用到每个动画)
        set.setDuration(3000);
        img_gift.clearAnimation();
        set.cancel();
        img_gift.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_gift.clearAnimation();
                Log.i("oooooooo", "lllllllllll");
                img_gift.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void doLayout() {
        if (isZoom) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int hid = dm.heightPixels;
        final int disWid = 2048;
        final int disHid = 1536;

        //顶部工具栏
        LinearLayout.LayoutParams tool_bar_param = (LinearLayout.LayoutParams) rel_tool_bar.getLayoutParams();
        tool_bar_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        tool_bar_param.height = (int) (hid * ((double) 160 / (double) disHid));
        rel_tool_bar.setLayoutParams(tool_bar_param);

        RelativeLayout.LayoutParams play_back_tool = (RelativeLayout.LayoutParams) rel_tool_bar_play_back.getLayoutParams();
        play_back_tool.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        play_back_tool.height = (int) (hid * ((double) 160 / (double) disHid));
        rel_tool_bar_play_back.setLayoutParams(play_back_tool);

        //左边白板加六路学生视频
        LinearLayout.LayoutParams main_param = (LinearLayout.LayoutParams) lin_main.getLayoutParams();
        main_param.width = (int) (wid * ((double) 1580 / (double) disWid));
        main_param.height = LinearLayout.LayoutParams.MATCH_PARENT;
        lin_main.setLayoutParams(main_param);

        //右边老师视频加聊天
        LinearLayout.LayoutParams menu_param = (LinearLayout.LayoutParams) lin_menu.getLayoutParams();
        menu_param.width = (int) (wid * ((double) 468 / (double) disWid));
        menu_param.height = LinearLayout.LayoutParams.MATCH_PARENT;
        lin_menu.setLayoutParams(menu_param);

        LinearLayout.LayoutParams w_param = (LinearLayout.LayoutParams) rel_w.getLayoutParams();
        main_param.width = (int) (wid * ((double) 1580 / (double) disWid));
        main_param.height = (int) (hid * ((double) 1376 / (double) disHid));
        rel_w.setLayoutParams(w_param);


       /* LinearLayout.LayoutParams time_param = (LinearLayout.LayoutParams) lin_time.getLayoutParams();
        time_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        time_param.height = (int) (hid * ((double) 115 / (double) disHid));
        lin_time.setLayoutParams(time_param);*/

        LinearLayout.LayoutParams lin_audio_control_param = (LinearLayout.LayoutParams) lin_audio_control.getLayoutParams();
        lin_audio_control_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lin_audio_control_param.height = (int) (hid * ((double) 115 / (double) disHid));
        lin_audio_control.setLayoutParams(lin_audio_control_param);

        RelativeLayout.LayoutParams students_param = new RelativeLayout.LayoutParams(0, 0);
        students_param.width = (int) (wid * ((double) 1580 / (double) disWid));
        students_param.height = (int) (students_param.width * ((double) 264 / (double) 1580)) + 20;
        students_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (videoItems.size() > 0) {
            v_students.setLayoutParams(students_param);
            v_students.setVisibility(View.VISIBLE);
        } else {
            v_students.setVisibility(View.GONE);
        }

      /*  RelativeLayout.LayoutParams rel_student_param = (RelativeLayout.LayoutParams) rel_students.getLayoutParams();
        rel_student_param.topMargin = time_param.height;
        rel_students.setLayoutParams(rel_student_param);*/

        LinearLayout.LayoutParams rel_wb_param = (LinearLayout.LayoutParams) rel_wb_container.getLayoutParams();

        if (videoItems.size() > 0) {
            rel_wb_param.height = (hid - tool_bar_param.height - students_param.height - lin_audio_control_param.height);
        } else {
            rel_wb_param.height = (hid - tool_bar_param.height - lin_audio_control_param.height);
        }

        if ((rel_wb_param.height * ((double) 16 / (double) 9)) > (wid - (wid * ((double) 468 / (double) disWid)))) {
            rel_wb_param.width = (int) (wid - (wid * ((double) 468 / (double) disWid)));
            rel_wb_param.height = (int) (rel_wb_param.width * ((double) 9 / (double) 16));
        } else {
            rel_wb_param.width = (int) (rel_wb_param.height * ((double) 16 / (double) 9));
        }

        rel_wb_param.width = (int) (rel_wb_param.height * ((double) 16 / (double) 9));
        rel_wb_param.gravity = Gravity.CENTER;
        rel_wb_container.setLayoutParams(rel_wb_param);
        if (wbFragment != null) {
            wbFragment.transmitWindowSize(rel_wb_param.width, rel_wb_param.height);
        }

//        //menu上的视频
        RelativeLayout.LayoutParams stu_param_menu = (RelativeLayout.LayoutParams) teacherItem.sf_video.getLayoutParams();
        stu_param_menu.width = (int) (wid * ((double) 428 / (double) disWid));
        stu_param_menu.height = (int) (stu_param_menu.width * (double) 3 / (double) 4);

        RelativeLayout.LayoutParams stu_parent_menu = (RelativeLayout.LayoutParams) teacherItem.rel_group.getLayoutParams();
        stu_parent_menu.width = (int) (wid * ((double) 428 / (double) disWid));
        stu_parent_menu.height = (int) (stu_parent_menu.width * (double) 3 / (double) 4);

        LinearLayout.LayoutParams stu_video_label_menu = (LinearLayout.LayoutParams) teacherItem.rel_video_label.getLayoutParams();
        stu_video_label_menu.width = (int) (wid * ((double) 428 / (double) disWid));
        stu_video_label_menu.height = (int) ((stu_video_label_menu.width * (double) 3 / (double) 4));

        LinearLayout.LayoutParams stu_name_menu = (LinearLayout.LayoutParams) teacherItem.lin_name_label.getLayoutParams();
        stu_name_menu.width = (int) (wid * ((double) 428 / (double) disWid));
        stu_name_menu.height = (int) (stu_name_menu.width * ((double) 44 / (double) 428));

        LinearLayout.LayoutParams stu_par_menu = (LinearLayout.LayoutParams) teacherItem.parent.getLayoutParams();
        stu_par_menu.width = (int) (wid * ((double) 428 / (double) disWid));
        stu_par_menu.height = (int) ((stu_par_menu.width * ((double) 364 / (double) 428)));

       /* LinearLayout.LayoutParams img_screen = (LinearLayout.LayoutParams) img_teacher_screen.getLayoutParams();
        img_screen.width = (int) (wid * ((double) 428 / (double) disWid));
        img_screen.height = (int) (stu_param_menu.width * (double) 3 / (double) 4);
        img_teacher_screen.setLayoutParams(img_screen);*/

        teacherItem.parent.setLayoutParams(stu_par_menu);
        teacherItem.sf_video.setLayoutParams(stu_param_menu);
        teacherItem.rel_group.setLayoutParams(stu_parent_menu);
        teacherItem.rel_video_label.setLayoutParams(stu_video_label_menu);
        teacherItem.lin_name_label.setLayoutParams(stu_name_menu);

        stu_in_sd.parent.setLayoutParams(stu_par_menu);
        stu_in_sd.sf_video.setLayoutParams(stu_param_menu);
        stu_in_sd.rel_group.setLayoutParams(stu_parent_menu);
        stu_in_sd.rel_video_label.setLayoutParams(stu_video_label_menu);
        stu_in_sd.lin_name_label.setLayoutParams(stu_name_menu);

    }

    private void do1vsnStudentVideoLayout() {
        if (videoItems.size() == 0) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int wid = dm.widthPixels;
        final int hid = dm.heightPixels;
        final int disWid = 2048;
        final int disHid = 1536;


//        RelativeLayout.LayoutParams stu_param = (RelativeLayout.LayoutParams) videoItems.get(0).sf_video.getLayoutParams();
//        stu_param.width = (int) (wid * ((double) 240 / disWid));
//        stu_param.height = (int) (stu_param.width * (double) 3 / (double) 4);
//
//        RelativeLayout.LayoutParams stu_parent = (RelativeLayout.LayoutParams) videoItems.get(0).rel_group.getLayoutParams();
//        stu_parent.width = (int) (wid * ((double) 240 / disWid));
//        stu_parent.height = (int) (stu_parent.width * (double) 3 / (double) 4);
//
//        LinearLayout.LayoutParams stu_video_label = (LinearLayout.LayoutParams) videoItems.get(0).rel_video_label.getLayoutParams();
//        stu_video_label.width = (int) (wid * ((double) 240 / disWid));
//        stu_video_label.height = (int) ((stu_video_label.width * (double) 3 / (double) 4));
//
//        RelativeLayout.LayoutParams stu_par = (RelativeLayout.LayoutParams) videoItems.get(0).parent.getLayoutParams();
//        stu_par.width = (int) (wid * ((double) 240 / disWid));
//        stu_par.height = (int) ((stu_par.width * ((double) 224 / (double) 240)));
//        stu_par.topMargin = 20;
//        stu_par.bottomMargin = 20;
//        stu_par.leftMargin = 10;
//        stu_par.rightMargin = 10;
//        stu_par.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//
//        if (roomType != 0) {
//            for (int i = 0; i < videoItems.size(); i++) {
//                videoItems.get(i).parent.setLayoutParams(stu_par);
//                videoItems.get(i).sf_video.setLayoutParams(stu_param);
//                videoItems.get(i).rel_group.setLayoutParams(stu_parent);
//                videoItems.get(i).rel_video_label.setLayoutParams(stu_video_label);
//            }
//        }

        for (int i = 0; i < videoItems.size(); i++) {
            if (!videoItems.get(i).isLayoutd) {
                videoItems.get(i).oldX = videoItems.get(i).parent.getLeft();
                videoItems.get(i).oldY = videoItems.get(i).parent.getTop();
                videoItems.get(i).isLayoutd = true;
            }
        }

        ArrayList<VideoItem> notMoveVideoItems = new ArrayList<VideoItem>();
        ArrayList<VideoItem> movedVideoItems = new ArrayList<VideoItem>();
        for (int i = 0; i < videoItems.size(); i++) {
            if (!videoItems.get(i).isMoved) {
                if (videoItems.get(i).isSplitScreen) {
                    movedVideoItems.add(videoItems.get(i));
                } else {
                    notMoveVideoItems.add(videoItems.get(i));
                }
            } else {
                movedVideoItems.add(videoItems.get(i));
            }
        }

        for (int i = 0; i < notMoveVideoItems.size(); i++) {
            //这里我用RelativeLayout布局为列，其他布局设置方法一样，只需改变布局名就行
            RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) notMoveVideoItems.get(i).parent.getLayoutParams();
            layout.width = (int) (wid * ((double) 240 / disWid));
            layout.height = (int) ((layout.width * ((double) 224 / (double) 240)));
            //获得button控件的位置属性，需要注意的是，可以将button换成想变化位置的其它控件
            layout.topMargin = 20;
            layout.bottomMargin = 20;
            layout.leftMargin = 10 * (i + 1) + ((int) (wid * ((double) 240 / disWid))) * i;
            layout.rightMargin = 10;
            layout.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //设置button的新位置属性,left，top，right，bottom
            notMoveVideoItems.get(i).parent.setTranslationX(0);
            notMoveVideoItems.get(i).parent.setTranslationY(0);
            notMoveVideoItems.get(i).parent.setLayoutParams(layout);
            //将新的位置加入button控件中
            LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) notMoveVideoItems.get(i).rel_video_label.getLayoutParams();
            linparam.width = (int) (wid * ((double) 240 / disWid));
            linparam.height = (int) ((linparam.width * (double) 3 / (double) 4));
            notMoveVideoItems.get(i).rel_video_label.setLayoutParams(linparam);
            LinearLayout.LayoutParams stu_name = (LinearLayout.LayoutParams) notMoveVideoItems.get(i).lin_name_label.getLayoutParams();
            stu_name.width = (int) (wid * ((double) 240 / disWid));
            stu_name.height = (int) (stu_name.width * ((double) 44 / (double) 240));
            notMoveVideoItems.get(i).lin_name_label.setLayoutParams(stu_name);
            notMoveVideoItems.get(i).isZoomd = false;
        }

        boolean isScreen = false;
        for (int x = 0; x < movedVideoItems.size(); x++) {
            if (movedVideoItems.get(x).isSplitScreen) {
                isScreen = true;
            }
            if (isScreen) {
                break;
            }
        }
        if (isScreen) {
            for (int x = 0; x < movedVideoItems.size(); x++) {
                movedVideoItems.get(x).isSplitScreen = true;
                if (!screenID.contains(movedVideoItems.get(x).peerid)) {
                    screenID.add(0, movedVideoItems.get(x).peerid);
                }
            }
        }

        ArrayList<VideoItem> splitScreen = new ArrayList<VideoItem>();
        if (screenID.size() > 0 && isScreen) {
            for (int y = 0; y < screenID.size(); y++) {
                for (int x = 0; x < movedVideoItems.size(); x++) {
                    if (screenID.get(y).equals(movedVideoItems.get(x).peerid)) {
                        splitScreen.add(movedVideoItems.get(x));
                        break;
                    }
                }
            }
        }

        if (isScreen) {
            switch (movedVideoItems.size()) {
                case 1:
                    VideoTtemLayoutUtils.screenOne(splitScreen, rel_wb_container);
                    break;
                case 2:
                    VideoTtemLayoutUtils.screenTwo(splitScreen, rel_wb_container);
                    break;
                case 3:
                    VideoTtemLayoutUtils.screenThere(splitScreen, rel_wb_container);
                    break;
                case 4:
                    VideoTtemLayoutUtils.screenFour(splitScreen, rel_wb_container);
                    break;
                case 5:
                    VideoTtemLayoutUtils.screenFive(splitScreen, rel_wb_container);
                    break;
                case 6:
                    VideoTtemLayoutUtils.screenSixth(splitScreen, rel_wb_container);
                    break;
         /*   case 7:
                VideoTtemLayoutUtils.screenSeven(movedVideoItems,rel_wb_container);
                break;*/
            }
        }
    }

//    private void goneAllVideo() {
//
//        for (int i = 0; i < playingList.size(); i++) {
//            RoomManager.getInstance().unPlayVideo(playingList.get(i).peerId);
//        }
//        teacherItem.rel_group.setVisibility(View.GONE);
//        stu_in_sd.rel_group.setVisibility(View.GONE);
//        for (int i = 0; i < videoItems.size(); i++) {
//            videoItems.get(i).rel_group.setVisibility(View.GONE);
//        }
//    }

    class AddTime extends TimerTask {

        @Override
        public void run() {
            serviceTime += 1;
            localTime = serviceTime - classStartTime;
            showTime();
        }

    }

    private void showTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String H = "";
                String M = "";
                String S = "";
                long temps = localTime;
                long tempm = temps / 60;
                long temph = tempm / 60;
                long sec = temps - tempm * 60;
                tempm = tempm - temph * 60;
                H = temph == 0 ? "00" : temph >= 10 ? temph + "" : "0" + temph;
                M = tempm == 0 ? "00" : tempm >= 10 ? tempm + "" : "0" + tempm;
                S = sec == 0 ? "00" : sec >= 10 ? sec + "" : "0" + sec;
                txt_hour.setText(H);
                txt_min.setText(M);
                txt_ss.setText(S);
                try {
                    if (RoomManager.getInstance().getRoomProperties() == null) {
                        return;
                    }
                    long nowTime = System.currentTimeMillis() / 1000;
                    long endTime = RoomManager.getInstance().getRoomProperties().getLong("endtime");
                    long startTime = RoomManager.getInstance().getRoomProperties().getLong("starttime");
                    long proTime = endTime - startTime;
                    boolean isstart = false;
                    if (localTime == 0) {
                        proTime = startTime - nowTime;
                        isstart = false;
                    } else {
                        proTime = endTime - serviceTime;
                        isstart = true;
                    }
                    if (RoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                        if (proTime <= 60) {
                            txt_class_begin.setBackgroundResource(R.drawable.round_back_red);
                            txt_class_begin.setClickable(true);
                            Log.e("xiao", "showtime");
                            canClassDissMiss = true;
                        }
                        if (isstart && proTime < -5 * 60) {//自动下课
                            RoomManager.getInstance().delMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString());
                            sendClassDissToPhp();
                        }

                        if (isstart && proTime <= 60 && !timeMessages.get(2).isShowed) {
                            showTimeTipPop(timeMessages.get(2));
                            txt_hour.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_yel));
                            txt_min.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_yel));
                            txt_ss.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_yel));
                            txt_mao_01.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_yel));
                            txt_mao_02.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_yel));
                        }
                        if (isstart && proTime <= -3 * 60 && !timeMessages.get(3).isShowed) {
                            showTimeTipPop(timeMessages.get(3));
                            txt_hour.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_min.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_ss.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_mao_01.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_mao_02.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                        }
                        if (isstart && proTime <= -5 * 60 + 10 && !timeMessages.get(4).isShowed) {
                            showTimeTipPop(timeMessages.get(4));
                            txt_hour.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_min.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_ss.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_mao_01.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                            txt_mao_02.setTextColor(RoomActivity.this.getResources().getColor(R.color.time_red));
                        }
                    } else {
                        txt_class_begin.setBackgroundResource(R.drawable.round_back_red);
                        txt_class_begin.setClickable(true);
                        Log.e("xiao", "showtime");
                        canClassDissMiss = true;
                    }

//                    showTimeTipPop(proTime,isstart);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void showTimeTipPop(final TimeMessage tms) {
        if (!(RoomManager.getInstance().getMySelf().role == 0) || getfinalClassBeginMode()) {
            return;
        }
        try {
            if (!RoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tms.isShowed = true;
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.time_tip_pop, null);
        final TextView txt_tip = (TextView) contentView.findViewById(R.id.txt_tip);
        final TextView txt_i_know = (TextView) contentView.findViewById(R.id.txt_i_know);
        txt_tip.setText(Html.fromHtml(tms.message));
        final PopupWindow popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        txt_i_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        if (tms.hasKonwButton) {
            txt_i_know.setVisibility(View.VISIBLE);
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tms.count5 == 0) {
                                t.cancel();
                                if (!isFinishing()) {
                                    popupWindow.dismiss();
                                }
                            }
                            txt_i_know.setText(getResources().getString(R.string.i_konw) + tms.count5 + "'");
                            tms.count5--;
                        }
                    });
                }
            }, 1000, 1000);
        } else {
            tms.count5 = 10;
            txt_i_know.setVisibility(View.GONE);
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tms.count5 == 0) {
                                t.cancel();
                                popupWindow.dismiss();
                            }
                            txt_tip.setText(tms.message + " " + tms.count5 + "'");
                            tms.count5--;
                        }
                    });
                }
            }, 1000, 1000);
        }
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    popupWindow.showAsDropDown(lin_time, txt_hour.getWidth() * 4, -lin_time.getMeasuredHeight());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        joinRoom();
        if (grantResults.length == 0 || permissions.length == 0) {
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.CAMERA.equals(permissions[i])) {
                int grantResult = grantResults[0];
                boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    Tools.ShowAlertDialog(this, getString(R.string.camera_hint));
                    RoomClient.getInstance().warning(1);
                }
            }
            if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                int grantResult = grantResults[0];
                boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    Tools.ShowAlertDialog(this, getString(R.string.mic_hint));
                    RoomClient.getInstance().warning(2);
                }
            }
        }
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private void sendGift(final HashMap<String, RoomUser> userMap) {
        synchronized (sendgiftlock) {
            if (isSending) {
                return;
            }
            isSending = true;
            tSendGift = new Timer();
            tSendGift.schedule(new TimerTask() {
                int count = 0;

                @Override
                public void run() {
                    if (count == 2) {
                        isSending = false;
                        tSendGift.cancel();
                    } else {
                        count++;
                    }
                }
            }, 0, 1000);
            String url = "http://" + host + ":" + port + "/ClientAPI/sendgift";
            RequestParams params = new RequestParams();
            params.put("serial", RoomManager.getInstance().getRoomProperties().optString("serial"));
            params.put("sendid", RoomManager.getInstance().getMySelf().peerId);
            params.put("sendname", RoomManager.getInstance().getMySelf().nickName);
            HashMap<String, String> js = new HashMap<String, String>();
            for (RoomUser u : userMap.values()) {
                js.put(u.peerId, u.nickName);
            }
            params.put("receivearr", js);
            client.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                    try {
                        Log.e("xiao", response.toString());
                        int nRet = response.getInt("result");
                        if (nRet == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (RoomUser u : userMap.values()) {
                                        long giftnumber = 0;
                                        if (u.properties.containsKey("giftnumber")) {
                                            giftnumber = u.properties.get("giftnumber") instanceof Integer ? (int) u.properties.get("giftnumber") : (long) u.properties.get("giftnumber");
                                        }
                                        giftnumber++;
                                        RoomManager.getInstance().changeUserProperty(u.peerId, "__all", "giftnumber", giftnumber);
                                    }
//                                    isSending = false;
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("emm", "error=" + throwable.toString());
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
      /*  if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (Tools.isDialogShow) {
                Log.i("111111111", "qqqqqqqqqq");
                return true;
            } else {
                Log.i("111111111", "zzzzzzzzzzzz");
                return false;
            }
        }*/
        return super.onKeyDown(keyCode, event);
    }

    private void sendClassBeginToPhp() {
        if (!(RoomManager.getInstance().getMySelf().role == 0)) {
            return;
        }
        String webFun_controlroom = "http://" + host + ":" + port + "/ClientAPI" + "/roomstart";
        RequestParams params = new RequestParams();
        try {
            params.put("serial", RoomManager.getInstance().getRoomProperties().get("serial"));
            params.put("companyid", RoomManager.getInstance().getRoomProperties().get("companyid"));
            client.post(webFun_controlroom, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                    try {
                        int nRet = response.getInt("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("emm", "error=" + throwable.toString());
//                RoomClient.getInstance().joinRoomcallBack(-1);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean getfinalClassBeginMode() {
        boolean isauto = true;
        try {
            isauto = RoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035") ? true : RoomControler.isAutoClassBegin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isauto;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videofragment != null && stream != null && stream.isVideo()) {
            videofragment.setVoice();
        }

        //RoomManager.getInstance().useLoudSpeaker(true);
//        closeSpeaker();
        if (RoomSession.isClassBegin) {
            if (RoomManager.getInstance().getMySelf() != null && !TextUtils.isEmpty(RoomManager.getInstance().getMySelf().peerId)) {
                doPlayVideo(RoomManager.getInstance().getMySelf().peerId);
            }
        }
    }

    public void closeSpeaker() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                if (audioManager.isWiredHeadsetOn()) {
                    RoomManager.getInstance().useLoudSpeaker(false);
                } else {
                    RoomManager.getInstance().useLoudSpeaker(true);
                    openSpeaker();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;

    private BroadcastReceiverMgr mBroadcastReceiver;

    //按钮1-注册广播
    public void registerIt() {
        mBroadcastReceiver = new BroadcastReceiverMgr();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(B_PHONE_STATE);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    //按钮2-撤销广播
    public void unregisterIt() {
        unregisterReceiver(mBroadcastReceiver);
    }

    //点击通知进入一个Activity，点击返回时进入指定页面。
    public void resultActivityBackApp() {
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setTicker(getString(R.string.app_name));
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(getString(R.string.back_hint));

        //设置点击一次后消失（如果没有点击事件，则该方法无效。）
        mBuilder.setAutoCancel(true);

        //点击通知之后需要跳转的页面
        Intent resultIntent = new Intent(this, RoomActivity.class);

        //使用TaskStackBuilder为“通知页面”设置返回关系
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
//        stackBuilder.addParentStack(RoomActivity.class);
//        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
        // mId allows you to update the notification later on.
//        nm.notify(2, mBuilder.build());
    }
}
