package weiyicloud.com.eduhdsdk.ui;

import android.app.Service;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.classroomsdk.ShareDoc;
import com.classroomsdk.WhiteBoradManager;
import com.talkcloud.roomsdk.RoomManager;
import com.talkcloud.roomsdk.Stream;

import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weiyicloud.com.eduhdsdk.R;
//import weiyicloud.com.eduhdsdk.adapter.MediaListAdapter;

import weiyicloud.com.eduhdsdk.tools.Tools;


public class ScreenFragment extends Fragment {

    private static String sync = "";
    static private ScreenFragment mInstance = null;
    private View fragmentView;
    //MP4视频
    LinearLayout lin_video_play;
    SurfaceViewRenderer suf_mp4;
    private Stream stream;


    public void setStream(Stream stream) {
        this.stream = stream;
        RoomManager.getInstance().playVideo(stream.getExtensionId(), suf_mp4);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static public ScreenFragment getInstance() {
        synchronized (sync) {
            if (mInstance == null) {
                mInstance = new ScreenFragment();
            }
            return mInstance;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_screen, null);
            fragmentView.bringToFront();
            //mp4
            lin_video_play = (LinearLayout) fragmentView.findViewById(R.id.lin_video_play);
            suf_mp4 = (SurfaceViewRenderer) fragmentView.findViewById(R.id.suf_mp4);
            suf_mp4.init(EglBase.create().getEglBaseContext(), null);
//            suf_mp4.setZOrderOnTop(true);
            suf_mp4.setZOrderMediaOverlay(true);

        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (stream != null) {
            suf_mp4.setEnableHardwareScaler(true);
            suf_mp4.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
            RoomManager.getInstance().playVideo(stream.getExtensionId(), suf_mp4);
            suf_mp4.requestLayout();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        suf_mp4.release();
        suf_mp4 = null;
        super.onDestroyView();
        mInstance = null;
    }
}
