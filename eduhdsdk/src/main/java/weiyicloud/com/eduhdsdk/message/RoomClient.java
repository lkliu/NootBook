package weiyicloud.com.eduhdsdk.message;

//import com.weiyicloud.whitepad.SharePadMgr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import weiyicloud.com.eduhdsdk.R;
import weiyicloud.com.eduhdsdk.interfaces.CheckForUpdateCallBack;
import weiyicloud.com.eduhdsdk.interfaces.JoinmeetingCallBack;
import weiyicloud.com.eduhdsdk.interfaces.MeetingNotify;
import weiyicloud.com.eduhdsdk.tools.SoundPlayUtils;
import weiyicloud.com.eduhdsdk.ui.RoomActivity;
import weiyicloud.com.eduhdsdk.ui.RoomMouldOneActivity;

/***
 * xiaoyang for Customer
 */
public class RoomClient {
    private MeetingNotify notify;
    private JoinmeetingCallBack callBack;
    private static String sync = "";
    static private RoomClient mInstance = null;
    public static int Kickout_ChairmanKickout = 0;
    public static int Kickout_Repeat = 1;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static String webServer = "global.talk-cloud.net";
//    public static String webServer = "global.talk-cloud.neiwang";
//    public static String webServer = "demo.talk-cloud.net";

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    private boolean isExit = false;

    static public RoomClient getInstance() {
        synchronized (sync) {
            if (mInstance == null) {
                mInstance = new RoomClient();
            }
            return mInstance;
        }
    }

    public void joinRoom(Activity activity, Map<String, Object> map) {
        checkRoom(activity, map);
    }

    public void joinRoom(Activity activity, String temp) {
        temp = Uri.decode(temp);

        String[] temps = temp.split("&");
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < temps.length; i++) {
            String[] t = temps[i].split("=");
            map.put(t[0], t[1]);
        }
        if (map.containsKey("path")) {
            String tempPath = "http://" + map.get("path");
            map.put("path", tempPath);
        }
        joinRoom(activity, map);
    }

    public void joinPlayBackRoom(Activity activity, String temp) {
        joinRoom(activity, temp);
    }

    public void checkRoom(final Activity activity, Map<String, Object> map) {
        final String host = map.get("host") instanceof String ? (String) map.get("host") : "";
        final int port = map.get("port") instanceof Integer ? (Integer) map.get("port") : 80;
        final String serial = map.get("serial") instanceof String ? (String) map.get("serial") : "";
        final String nickname = map.get("nickname") instanceof String ? (String) map.get("nickname") : "";
        final String userid = map.get("userid") instanceof String ? (String) map.get("userid") : "";
        final int userrole = map.get("userrole") instanceof Integer ? (Integer) map.get("userrole") : 2;
        final String password = map.get("password") instanceof String ? (String) map.get("password") : "";
        final String param = map.get("param") instanceof String ? (String) map.get("param") : "";
        final String domain = map.get("domain") instanceof String ? (String) map.get("domain") : "";
        final String servername = map.get("servername") instanceof String ? (String) map.get("servername") : "";
        final String path = map.get("path") instanceof String ? (String) map.get("path") : "";

        String url = "http://" + host + ":" + port + "/ClientAPI/checkroom";
        RequestParams params = new RequestParams();

        if (param != null && !param.isEmpty())
            params.put("param", param);
        params.put("serial", serial);
        final String finalnickname = Uri.encode(nickname);
        params.put("nickname", finalnickname);
        if (password != null && !password.isEmpty()) {
            params.put("password", password);
        }
        if (domain != null && !domain.isEmpty())
            params.put("domain", domain);
        if (servername != null && !servername.isEmpty())
            params.put("servername", servername);
        if (path != null && !path.isEmpty()) {
            params.put("playback", true);
        } else {
            params.put("userrole", userrole);
        }
//
// params.put("instflag", 0 + "");
        Log.d("classroom", "url=" + url + "params=" + params);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    int nRet = response.getInt("result");
                    String mouldType = response.optString("padlayout", "default");
                    if (nRet == 0) {
                        Intent intent = null;
                        if (mouldType.equals("default")) {
                            intent = new Intent(activity, RoomActivity.class);
                        } else if (mouldType.equals("sharktop")) {
                            intent = new Intent(activity, RoomMouldOneActivity.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("host", host);

                        bundle.putInt("port", port);
                        bundle.putString("nickname", finalnickname);
                        bundle.putString("userid", userid);
                        bundle.putString("password", password);
                        bundle.putString("serial", serial);
                        bundle.putInt("userrole", userrole);
                        if (param != null && !param.isEmpty()) {
                            bundle.putString("param", param);
                        }
                        if (domain != null && !domain.isEmpty()) {
                            bundle.putString("domain", domain);
                        }
                        if (servername != null && !servername.isEmpty()) {
                            bundle.putString("servername", servername);
                        }
                        if (path != null && !path.isEmpty()) {
                            bundle.putString("path", path);
                        }
//                        intent.putExtras(bundle);
//                        activity.startActivity(intent);
                        RoomClient.getInstance().joinRoomcallBack(0);
                        getmobilename(activity, bundle, host, port, intent);
//                        intent.putExtras(bundle);
////                        RoomSession.getInstance().setServiceHost(host);
////                        RoomSession.getInstance().setServicePort(port);
//                        activity.startActivity(intent);
                    } else {
                        if (callBack != null) {
                            callBack.callBack(nRet);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error");
                RoomClient.getInstance().joinRoomcallBack(-1);
            }
        });
    }

    String apkDownLoadUrl = "";

    public void checkForUpdate(final Activity activity, String url, final CheckForUpdateCallBack ack) {
        url = "http://" + url + ":" + 80 + "/ClientAPI/getupdateinfo";

        RequestParams params = new RequestParams();
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            int version = info.versionCode;
            params.put("type", 2);
            params.put("version", version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("classroom", "url=" + url + "params=" + params);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    final int nRet = response.getInt("result");
                    if (nRet == 0) {
                        apkDownLoadUrl = response.optString("updateaddr");
                        final int code = response.optInt("updateflag");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ack.callBack(code);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error");
            }
        });
    }

    String target;

    public void downLoadApp(final Activity activity) {
        target = Environment.getExternalStorageDirectory().getAbsolutePath() + "/talkcloudHD.apk";
        if (apkDownLoadUrl != null && !apkDownLoadUrl.isEmpty()) {
            HttpUtils http = new HttpUtils();
            http.download(apkDownLoadUrl, target, new RequestCallBack<File>() {

                @Override
                public void onFailure(HttpException exception, String msg) {
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    dialog.dismiss();
                    // 安装apk
                    installApk(activity);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    initProgressDialog(activity, total, current);
                }
            });
        }
    }

    protected void initProgressDialog(Activity activity, long total, long current) {
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
        }
        dialog.setTitle(activity.getString(R.string.updateing));//设置标题
        dialog.setMessage("");//设置dialog内容
//        dialog.setIcon(R.drawable.icon_word);//设置图标，与为Title左侧
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 水平线进度条
        // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//圆形进度条
        dialog.setMax((int) total);//最大值
        dialog.setProgress((int) current);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private ProgressDialog dialog;

    protected void installApk(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
       /* intent.addCategory("android.intent.category.DEFAULT");*/
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File file = new File(target);
            Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);//在AndroidManifest中的android:authorities值
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            activity.startActivity(intent);
        } else {
            Uri data = Uri.parse("file://" + target);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public void regiestInterface(MeetingNotify notify, JoinmeetingCallBack callBack) {
        this.notify = notify;
        this.callBack = callBack;
    }

    public void setNotify(MeetingNotify notify) {
        this.notify = notify;
    }

    public void setCallBack(JoinmeetingCallBack callBack) {
        this.callBack = callBack;
    }

    public void joinRoomcallBack(int code) {
        if (this.callBack != null) {
            callBack.callBack(code);
        }
    }

    private void getmobilename(final Activity activity, final Bundle bundle, String host, int port, final Intent intent) {
        String url = "http://" + host + ":" + port + "/ClientAPI/getmobilename";
        RequestParams params = new RequestParams();
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {

                try {
                    int nRet = response.getInt("result");
                    if (nRet == 0) {
                        bundle.putString("mobilename", response.optJSONArray("mobilename").toString());
                    }
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error");
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                intent.putExtras(bundle);
                activity.startActivity(intent);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                intent.putExtras(bundle);
                activity.startActivity(intent);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void kickout(int res) {
        if (notify != null) {
            notify.onKickOut(res);
        }
    }

    /***
     * 警告权限
     * @param code 1没有视频权限2没有音频权限
     */
    public void warning(int code) {
        if (notify != null) {
            notify.onWarning(code);
        }
    }

    public void onClassBegin() {
        if (notify != null) {
            notify.onClassBegin();
        }
    }

    public void onClassDismiss() {
        if (notify != null) {
            notify.onClassDismiss();
        }
    }
}
