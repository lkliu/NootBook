package weiyicloud.com.eduhdsdk.tools;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.talkcloud.roomsdk.RoomManager;
import com.talkcloud.roomsdk.RoomUser;

import org.webrtc.RendererCommon;

import java.util.ArrayList;

import weiyicloud.com.eduhdsdk.entity.VideoItem;

/**
 * Created by Administrator on 2017/11/22/022.
 */

public class VideoTtemLayoutUtils {

    public static void screenSeven(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        ArrayList<VideoItem> withOutTeacherList = new ArrayList<VideoItem>();
        VideoItem teacherItem = null;
        for (int i = 0; i < movedVideoItems.size(); i++) {
            RoomUser roomUser = RoomManager.getInstance().getUser(movedVideoItems.get(i).peerid);
            if (roomUser != null) {
                if (roomUser.role != 0) {
                    withOutTeacherList.add(movedVideoItems.get(i));
                } else {
                    teacherItem = movedVideoItems.get(i);
                }
            }
        }
        if (teacherItem != null) {
            LayoutSizeUilts.oneOfSixth(teacherItem, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) teacherItem.parent.getLayoutParams();
            studentParam.topMargin = 0;
            studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
            studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
            studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            teacherItem.parent.setTranslationX(0);
            teacherItem.parent.setTranslationY(0);
            teacherItem.parent.setLayoutParams(studentParam);
        }
        for (int x = 0; x < withOutTeacherList.size(); x++) {
            VideoItem it = withOutTeacherList.get(x);
            if (x < 2) {
                LayoutSizeUilts.oneOfSixth(it, rel_wb_container);
            } else {
                LayoutSizeUilts.oneOfEighth(it, rel_wb_container);
            }
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            /*studentParam.bottomMargin = lin_audio_control.getHeight();*/
            studentParam.topMargin = 0;
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 2) {
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 3) {
                studentParam.leftMargin = rel_wb_container.getWidth() / 4 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 4) {
                studentParam.leftMargin = rel_wb_container.getWidth() / 4 * 2 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 5) {
                studentParam.leftMargin = rel_wb_container.getWidth() / 4 * 3 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenSixth(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            LayoutSizeUilts.oneOfSixth(it, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 2) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 3) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 4) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 5) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenFive(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            if (x < 2) {
                LayoutSizeUilts.quarter(it, rel_wb_container);
            } else {
                LayoutSizeUilts.oneOfSixth(it, rel_wb_container);
            }
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 2) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 3) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 4) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }


    public static void screenFour(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            LayoutSizeUilts.quarter(it, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 2) {
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 3) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }


    public static void screenThere(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            if (x == 0) {
                LayoutSizeUilts.AHalf(it, rel_wb_container);
            }
            if (x != 0) {
                LayoutSizeUilts.quarter(it, rel_wb_container);
            }
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (x == 2) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }


    public static void screenTwo(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            LayoutSizeUilts.AHalf(it, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();

            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }


    public static void screenOne(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container) {
        for (int i = 0; i < movedVideoItems.size(); i++) {
            VideoItem it = movedVideoItems.get(i);

            RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            relparam.width = rel_wb_container.getWidth();
            relparam.height = rel_wb_container.getHeight();
            relparam.topMargin = 0;
            relparam.leftMargin = rel_wb_container.getLeft();
            relparam.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            relparam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(relparam);

            LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) it.rel_video_label.getLayoutParams();
            linparam.width = rel_wb_container.getWidth();
            linparam.height = rel_wb_container.getHeight() - it.lin_name_label.getHeight();
            it.rel_video_label.setLayoutParams(linparam);

            RelativeLayout.LayoutParams sf_videoParam = (RelativeLayout.LayoutParams) it.sf_video.getLayoutParams();
            sf_videoParam.width = rel_wb_container.getWidth();
            sf_videoParam.height = rel_wb_container.getHeight();
            it.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
            it.sf_video.setLayoutParams(sf_videoParam);

            LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) it.lin_name_label.getLayoutParams();
            nameparam.width = rel_wb_container.getWidth();
            nameparam.height = it.lin_name_label.getHeight();
            it.lin_name_label.setLayoutParams(nameparam);
        }
    }

    //new mould
    public static void screenSixth(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            LayoutSizeUilts.oneOfSixth(it, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            studentParam.bottomMargin = lin_audio_control.getHeight();
            studentParam.topMargin = 0;
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 2) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 3) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 4) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 5) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenFive(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            if (x < 2) {
                LayoutSizeUilts.quarter(it, rel_wb_container);
            } else {
                LayoutSizeUilts.oneOfSixth(it, rel_wb_container);
            }
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            studentParam.bottomMargin = lin_audio_control.getHeight();
            studentParam.topMargin = 0;
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 2) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 3) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 4) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 3 * 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenFour(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            LayoutSizeUilts.quarter(it, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            studentParam.bottomMargin = lin_audio_control.getHeight();
            studentParam.topMargin = 0;
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 2) {
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            } else if (x == 3) {
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenThere(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            if (x == 0) {
                LayoutSizeUilts.AHalf(it, rel_wb_container);
            }
            if (x != 0) {
                LayoutSizeUilts.quarter(it, rel_wb_container);
            }
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            studentParam.bottomMargin = lin_audio_control.getHeight();
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 1) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight() + rel_wb_container.getHeight() / 2;
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else if (x == 2) {
                studentParam.topMargin = 0;
                studentParam.topMargin = rel_wb_container.getHeight() / 2;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenTwo(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        for (int x = 0; x < movedVideoItems.size(); x++) {
            VideoItem it = movedVideoItems.get(x);
            LayoutSizeUilts.AHalf(it, rel_wb_container);
            RelativeLayout.LayoutParams studentParam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            if (x == 0) {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else {
                studentParam.topMargin = 0;
                studentParam.leftMargin = rel_wb_container.getWidth() / 2 + rel_wb_container.getLeft();
                studentParam.bottomMargin = lin_audio_control.getHeight();
                studentParam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                studentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(studentParam);
        }
    }

    public static void screenOne(ArrayList<VideoItem> movedVideoItems, RelativeLayout rel_wb_container, LinearLayout lin_audio_control) {
        for (int i = 0; i < movedVideoItems.size(); i++) {
            VideoItem it = movedVideoItems.get(i);

            RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
            relparam.width = rel_wb_container.getWidth();
            relparam.height = rel_wb_container.getHeight();
            relparam.topMargin = 0;
            relparam.leftMargin = rel_wb_container.getLeft();
            relparam.bottomMargin = lin_audio_control.getHeight();
            relparam.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            relparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            it.parent.setTranslationX(0);
            it.parent.setTranslationY(0);
            it.parent.setLayoutParams(relparam);

            LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) it.rel_video_label.getLayoutParams();
            linparam.width = rel_wb_container.getWidth();
            linparam.height = rel_wb_container.getHeight() - it.lin_name_label.getHeight();
            it.rel_video_label.setLayoutParams(linparam);

            RelativeLayout.LayoutParams sf_videoParam = (RelativeLayout.LayoutParams) it.sf_video.getLayoutParams();
            sf_videoParam.width = rel_wb_container.getWidth();
            sf_videoParam.height = rel_wb_container.getHeight();
            it.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
            it.sf_video.setLayoutParams(sf_videoParam);

            LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) it.lin_name_label.getLayoutParams();
            nameparam.width = rel_wb_container.getWidth();
            nameparam.height = it.lin_name_label.getHeight();
            it.lin_name_label.setLayoutParams(nameparam);

        }
    }
}
