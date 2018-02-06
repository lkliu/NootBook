package weiyicloud.com.eduhdsdk.message;

/**
 * Created by Administrator on 2017/5/11.
 */

public interface VideoWBCallback {

    void pubMsg(String js);

    void delMsg(String js);

    void onPageFinished();

}
