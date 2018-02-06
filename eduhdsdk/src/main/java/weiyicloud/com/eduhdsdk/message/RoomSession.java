package weiyicloud.com.eduhdsdk.message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * Created by Administrator on 2017/12/12.
 */

public class RoomSession {
    public static boolean isClassBegin = false;
    public static HashSet<String> publishSet = new HashSet<String>();
    public static HashSet<String> pandingSet = new HashSet<String>();
    public static JSONArray jsVideoWBTempMsg = new JSONArray();
    public static boolean isShowVideoWB = false;

    private static String sync = "";
    static private RoomSession mInstance = null;

    public static boolean isPublish = false;
    public static boolean isPlay = false;

    static public RoomSession getInstance() {
        synchronized (sync) {
            if (mInstance == null) {
                mInstance = new RoomSession();
            }
            return mInstance;
        }
    }

    public void addTempVideoWBRemoteMsg(boolean add, String id, String name, long ts, Object data, String fromID, String associatedMsgID, String associatedUserID) {
        if (add) {
            if(name.equals("VideoWhiteboard")){
                isShowVideoWB = true;
            }
            JSONObject jsobj = new JSONObject();
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

                if (associatedMsgID.equals("VideoWhiteboard")||id.equals("VideoWhiteboard")) {
                    jsVideoWBTempMsg.put(jsobj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            isShowVideoWB = false;
        }

    }

}
