package weiyicloud.com.eduhdsdk.tools;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.webrtc.RendererCommon;

import weiyicloud.com.eduhdsdk.entity.VideoItem;

/**
 * Created by Administrator on 2017/11/22/022.
 */

public class LayoutSizeUilts {


    /***
     * VideoItem 1/4 大小
     */
    public static void quarter(VideoItem it, RelativeLayout rel_wb_container) {
        RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
        relparam.width = rel_wb_container.getWidth() / 2;
        relparam.height = rel_wb_container.getHeight() / 2;
        it.parent.setLayoutParams(relparam);

        LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) it.rel_video_label.getLayoutParams();
        linparam.width = rel_wb_container.getWidth() / 2;
        linparam.height = rel_wb_container.getHeight() / 2 - it.lin_name_label.getHeight();
        it.rel_video_label.setLayoutParams(linparam);

        RelativeLayout.LayoutParams sf_videoParam = (RelativeLayout.LayoutParams) it.sf_video.getLayoutParams();
        sf_videoParam.width = rel_wb_container.getWidth() / 2;
        sf_videoParam.height = rel_wb_container.getHeight() / 2;
        it.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        it.sf_video.setLayoutParams(sf_videoParam);

        LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) it.lin_name_label.getLayoutParams();
        nameparam.width = rel_wb_container.getWidth() / 2;
        nameparam.height = it.lin_name_label.getHeight();
        it.lin_name_label.setLayoutParams(nameparam);
    }

    /***
     * VideoItem 1/2 大小
     * @param it
     */
    public static void AHalf(VideoItem it, RelativeLayout rel_wb_container) {

        RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
        relparam.width = rel_wb_container.getWidth() / 2;
        relparam.height = rel_wb_container.getHeight();
        it.parent.setLayoutParams(relparam);

        LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) it.rel_video_label.getLayoutParams();
        linparam.width = rel_wb_container.getWidth() / 2;
        linparam.height = rel_wb_container.getHeight() - it.lin_name_label.getHeight();
        it.rel_video_label.setLayoutParams(linparam);

        RelativeLayout.LayoutParams sf_videoParam = (RelativeLayout.LayoutParams) it.sf_video.getLayoutParams();
        sf_videoParam.width = rel_wb_container.getWidth() / 2;
        sf_videoParam.height = rel_wb_container.getHeight();
        it.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        it.sf_video.setLayoutParams(sf_videoParam);

        LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) it.lin_name_label.getLayoutParams();
        nameparam.width = rel_wb_container.getWidth() / 2;
        nameparam.height = it.lin_name_label.getHeight();
        it.lin_name_label.setLayoutParams(nameparam);
    }

    /***
     * VideoItem 1/6 大小
     */
    public static void oneOfSixth(VideoItem it, RelativeLayout rel_wb_container) {
        RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
        relparam.width = rel_wb_container.getWidth() / 3;
        relparam.height = rel_wb_container.getHeight() / 2;
        it.parent.setLayoutParams(relparam);

        LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) it.rel_video_label.getLayoutParams();
        linparam.width = rel_wb_container.getWidth() / 3;
        linparam.height = rel_wb_container.getHeight() / 2 - it.lin_name_label.getHeight();
        it.rel_video_label.setLayoutParams(linparam);

        RelativeLayout.LayoutParams sf_videoParam = (RelativeLayout.LayoutParams) it.sf_video.getLayoutParams();
        sf_videoParam.width = rel_wb_container.getWidth() / 3;
        sf_videoParam.height = rel_wb_container.getHeight() / 2;
        it.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        it.sf_video.setLayoutParams(sf_videoParam);

        LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) it.lin_name_label.getLayoutParams();
        nameparam.width = rel_wb_container.getWidth() / 3;
        nameparam.height = it.lin_name_label.getHeight();
        it.lin_name_label.setLayoutParams(nameparam);

    }

    /***
     * VideoItem 1/8 大小
     */
    public static void oneOfEighth(VideoItem it, RelativeLayout rel_wb_container) {
        RelativeLayout.LayoutParams relparam = (RelativeLayout.LayoutParams) it.parent.getLayoutParams();
        relparam.width = rel_wb_container.getWidth() / 4;
        relparam.height = rel_wb_container.getHeight() / 2;
        it.parent.setLayoutParams(relparam);

        LinearLayout.LayoutParams linparam = (LinearLayout.LayoutParams) it.rel_video_label.getLayoutParams();
        linparam.width = rel_wb_container.getWidth() / 4;
        linparam.height = rel_wb_container.getHeight() / 2 - it.lin_name_label.getHeight();
        it.rel_video_label.setLayoutParams(linparam);

        RelativeLayout.LayoutParams sf_videoParam = (RelativeLayout.LayoutParams) it.sf_video.getLayoutParams();
        sf_videoParam.width = rel_wb_container.getWidth() / 4;
        sf_videoParam.height = rel_wb_container.getHeight() / 2;
        it.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        it.sf_video.setLayoutParams(sf_videoParam);

        LinearLayout.LayoutParams nameparam = (LinearLayout.LayoutParams) it.lin_name_label.getLayoutParams();
        nameparam.width = rel_wb_container.getWidth() / 4;
        nameparam.height = it.lin_name_label.getHeight();
        it.lin_name_label.setLayoutParams(nameparam);
    }

}
