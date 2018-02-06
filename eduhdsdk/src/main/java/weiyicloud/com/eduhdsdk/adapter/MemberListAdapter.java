package weiyicloud.com.eduhdsdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.talkcloud.roomsdk.RoomControler;
import com.talkcloud.roomsdk.RoomManager;
import com.talkcloud.roomsdk.RoomUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;

import weiyicloud.com.eduhdsdk.R;
import weiyicloud.com.eduhdsdk.message.RoomSession;
import weiyicloud.com.eduhdsdk.tools.Tools;

/**
 * Created by Administrator on 2017/5/27.
 */

public class MemberListAdapter extends BaseAdapter {

    ArrayList<RoomUser> userList = new ArrayList<RoomUser>();
    //    ArrayList<RoomUser> playinglist = new ArrayList<RoomUser>();
//    HashSet<String> publishSet = new HashSet<String>();
    Context context;
    private String pubFailUserId = "";

    public void setPubFailUserId(String pubFailUserId) {
        this.pubFailUserId = pubFailUserId;
    }


    public MemberListAdapter(Context context, ArrayList<RoomUser> userList, HashSet<String> publishSet) {
        this.context = context;
        this.userList = userList;
//        this.publishSet = publishSet;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (userList.size() > 0) {
            if (convertView == null) {

                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.member_list_item, null);
                holder.txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
                holder.img_hand_up = (ImageView) convertView.findViewById(R.id.img_hand_up);
                holder.img_tool = (ImageView) convertView.findViewById(R.id.img_draw);
                holder.img_up_sd = (ImageView) convertView.findViewById(R.id.img_up_sd);
                holder.img_audio = (ImageView) convertView.findViewById(R.id.img_audio);
                holder.img_video = (ImageView) convertView.findViewById(R.id.img_video);
                holder.im_type = (ImageView) convertView.findViewById(R.id.im_type);
               /* holder.txt_degree = (TextView) convertView.findViewById(R.id.txt_degree);*/

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final RoomUser user = userList.get(position);
            if (user != null) {

                holder.txt_user_name.setText(user.nickName);
                if (user.properties.containsKey("raisehand")) {
                    boolean israisehand = Tools.isTure(user.properties.get("raisehand"));
                    if (israisehand) {
                        holder.img_hand_up.setVisibility(View.VISIBLE);//正在举手

                    } else {
                        holder.img_hand_up.setVisibility(View.INVISIBLE);//同意了，或者拒绝了
                    }
                } else {
                    holder.img_hand_up.setVisibility(View.INVISIBLE);//还没举手
                }

                /*if (user.role == 2) {
                    holder.txt_degree.setText("(" + context.getResources().getString(R.string.student) + ")");
                } else if (user.role == 4) {
                    holder.txt_degree.setText("(" + context.getResources().getString(R.string.lass_patrol) + ")");
                } else if (user.role == 1) {
                    holder.txt_degree.setText("(" + context.getResources().getString(R.string.assistant) + ")");
                }*/
                if (user.properties.containsKey("devicetype")) {
                    String type = (String) user.properties.get("devicetype");
                    int udpstate = 1;
                    switch (type) {
                        case "AndroidPad":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.androidpad_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.android_hd);
                            }
                            break;
                        case "iPad":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.ipad_icon_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.ipad_hd);
                            }
                            break;
                        case "AndroidPhone":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.android_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.android);
                            }
                            break;
                        case "iPhone":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.iphone_icon_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.iphone);
                            }
                            break;
                        case "WindowClient":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.win_client_icon_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.win_client);
                            }
                            break;
                        case "WindowPC":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.win_pc_icon_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.win_pc);
                            }
                            break;
                        case "MacClient":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.mac_client_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.mac_client);
                            }
                            break;
                        case "MacPC":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.mac_pc_icon_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.mac_pc);
                            }
                            break;
                        case "AndroidTV":
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.icon_tv_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.icon_tv_white);
                            }
                            break;
                        default:
                            if (udpstate > 1) {
                                holder.im_type.setImageResource(R.drawable.icon_unknown_red);
                            } else {
                                holder.im_type.setImageResource(R.drawable.icon_unknown_white);
                            }
                            break;
                    }
                }

                if (user.properties.containsKey("candraw")) {
                    boolean candraw = Tools.isTure(user.properties.get("candraw"));
                    if (candraw) {
                        holder.img_tool.setImageResource(R.drawable.btn_tools_02_normal);//可以画图
                    } else {
                        holder.img_tool.setImageResource(R.drawable.btn_tools_01_normal);//不可以画图
                    }
                } else {
                    holder.img_tool.setImageResource(R.drawable.btn_tools_01_normal);//没给过画图权限
                }

                if (user.publishState > 0) {//只要有流
                    if (user.role == 0) {
                        holder.img_up_sd.setImageResource(R.drawable.teacher_up);
                    } else if (user.role == 1) {
                        holder.img_up_sd.setImageResource(R.drawable.assistant_up);
                    } else if (user.role == 2) {
                        holder.img_up_sd.setImageResource(R.drawable.student_up);
                    }
                } else {
                    if (user.role == 0) {
                        holder.img_up_sd.setImageResource(R.drawable.teacher_down);
                    } else if (user.role == 1) {
                        holder.img_up_sd.setImageResource(R.drawable.assistant_down);
                    } else if (user.role == 2) {
                        holder.img_up_sd.setImageResource(R.drawable.student_down);
                    }
                }
                if (user.disableaudio) {
                    holder.img_audio.setImageResource(R.drawable.audio_disable);
                } else {
                    if (user.publishState == 1 || user.publishState == 3) {//音频状态改的开启
                        holder.img_audio.setImageResource(R.drawable.btn_audio_02_normal);
                    } else {
                        holder.img_audio.setImageResource(R.drawable.btn_audio_01_normal);
                    }
                }
                if (user.disablevideo) {
                    holder.img_video.setImageResource(R.drawable.video_disable);
                } else {
                    if (user.publishState == 2 || user.publishState == 3) {//视频状态改的开启
                        holder.img_video.setImageResource(R.drawable.video_state_on);
                    } else {
                        holder.img_video.setImageResource(R.drawable.video_state_off);
                    }
                }
                if (RoomSession.isClassBegin) {
                    if (user.role == 1 && !RoomControler.isShowAssistantAV()) {
                        holder.img_up_sd.setVisibility(View.INVISIBLE);
                        holder.img_video.setVisibility(View.INVISIBLE);
                        holder.img_audio.setVisibility(View.INVISIBLE);
                        holder.img_tool.setVisibility(View.INVISIBLE);
                    } else {
                        holder.img_up_sd.setVisibility(View.VISIBLE);
                        holder.img_video.setVisibility(View.VISIBLE);
                        holder.img_audio.setVisibility(View.VISIBLE);
                        holder.img_tool.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.img_up_sd.setVisibility(View.INVISIBLE);
                    holder.img_audio.setVisibility(View.INVISIBLE);
                    holder.img_tool.setVisibility(View.INVISIBLE);
                    holder.img_video.setVisibility(View.INVISIBLE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if((!RoomActivity.isClassBegin||
//                                ((RoomActivity.publishSet.size()+RoomActivity.pandingSet.size())>=RoomActivity.maxVideo&&user.publishState<=1||RoomActivity.pandingSet.contains(user.peerId)))
//                                &&!user.peerId.equals(pubFailUserId)||user.role == 1){
//                            if((RoomActivity.publishSet.size()+RoomActivity.pandingSet.size())>=RoomActivity.maxVideo){
//                                Toast.makeText(context,R.string.member_overload,Toast.LENGTH_LONG).show();
//                            }
//                            return;
//                        }
                        if (!RoomSession.isClassBegin) {
                            return;
                        }
//                        if(((Integer)user.properties.get("udpstate"))>1){//用户udp不通，不能上台！
//                            Toast.makeText(context, R.string.member_overload, Toast.LENGTH_LONG).show();
//                            return;
//                        }
                        try {
                            if (RoomSession.publishSet.size() + RoomSession.pandingSet.size() >= RoomManager.getInstance().getRoomProperties().getInt("maxvideo") && user.publishState <= 1) {
                                Toast.makeText(context, R.string.member_overload, Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (RoomSession.pandingSet.contains(user.peerId)) {
                            return;
                        }

                        if (user.role == 1 && !RoomControler.isShowAssistantAV()) {
                            return;
                        }
                        if (user.publishState == 0 && user.properties.containsKey("isInBackGround") && Tools.isTure(user.properties.get("isInBackGround"))) {
                            Toast.makeText(context, user.nickName + context.getResources().getString(R.string.select_back_hint), Toast.LENGTH_LONG).show();
                            return;
                        }
                        RoomSession.pandingSet.add(user.peerId);
                        user.properties.put("passivityPublish", true);
                        if (user.publishState >= 1) {//只要有流开启就是上台
                            RoomManager.getInstance().changeUserPublish(user.peerId, 0);
                            if (user.role == 2) {
                                RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", false);
                            }
                        } else if (!user.disablevideo && !user.disableaudio) {
                            RoomManager.getInstance().changeUserPublish(user.peerId, 3);
                            RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "raisehand", false);

                        } else if (!user.disableaudio) {
                            RoomManager.getInstance().changeUserPublish(user.peerId, 1);
                            if (user.role == 2) {
                                RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", false);
                                RoomManager.getInstance().changeUserProperty(user.peerId, "__all", "raisehand", false);
                            }
                        } else if (!user.disablevideo) {
                            RoomManager.getInstance().changeUserPublish(user.peerId, 2);
                        } else if (user.disableaudio || user.disablevideo) {
                            Toast.makeText(context, R.string.device_disable, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView txt_user_name/*, txt_degree*/;
        ImageView img_hand_up;
        ImageView img_tool;
        ImageView img_up_sd;
        ImageView img_audio;
        ImageView img_video;
        ImageView im_type;
    }
}
