package weiyicloud.com.eduhdsdk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.classroomsdk.ShareDoc;
import com.classroomsdk.WhiteBoradManager;
import com.talkcloud.roomsdk.RoomManager;

import weiyicloud.com.eduhdsdk.R;
import weiyicloud.com.eduhdsdk.message.RoomSession;

/**
 * Created by Administrator on 2018/1/4.
 */

public class MediaEXListEXAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private String roomNum;
    private long localfileid = -1;
    PopupWindow pop = null;

    public void setLocalfileid(long localfileid) {
        this.localfileid = localfileid;
    }

    public MediaEXListEXAdapter(Activity context, String roomNum) {
        this.activity = context;
        this.roomNum = roomNum;
    }

    public static boolean isClassExpand = false;
    public static boolean isAdminExpand = true;

    public void setPop(PopupWindow pop) {
        this.pop = pop;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        if (groupPosition == 0) {
            count = WhiteBoradManager.getInstance().getClassMediaList().size();
        } else if (groupPosition == 1) {
            count = WhiteBoradManager.getInstance().getAdminmMediaList().size();
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            WhiteBoradManager.getInstance().getClassMediaList().get(childPosition);
        } else if (groupPosition == 1) {
            WhiteBoradManager.getInstance().getAdminmMediaList().get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressWarnings("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(activity).inflate(R.layout.file_group_item, null);
        TextView txt_file_group_type = (TextView) convertView.findViewById(R.id.txt_file_group_type);
        ImageView img_is_expand = (ImageView) convertView.findViewById(R.id.img_is_expand);

        if (groupPosition == 0) {
            txt_file_group_type.setText(R.string.class_file_group);
        } else if (groupPosition == 1) {
            txt_file_group_type.setText(R.string.admin_file_group);
        }

        if (isExpanded) {
            img_is_expand.setImageResource(R.drawable.file_group_down);
            convertView.setBackgroundResource(R.color.file_group_open);
        } else {
            img_is_expand.setImageResource(R.drawable.file_group_left);
            convertView.setBackgroundResource(R.color.file_group_close);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.file_list_item, null);
            holder.img_media_type = (ImageView) convertView.findViewById(R.id.img_file_type);
            holder.txt_media_name = (TextView) convertView.findViewById(R.id.txt_file_name);
            holder.img_play = (ImageView) convertView.findViewById(R.id.img_eye);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            if (WhiteBoradManager.getInstance().getClassMediaList().size() > 0) {
                final ShareDoc media = WhiteBoradManager.getInstance().getClassMediaList().get(childPosition);
                if (media != null) {
                    holder.img_media_type.setImageResource(getMediaIcon(media.getFilename()));
                    holder.txt_media_name.setText(media.getFilename());
                    holder.img_play.setImageResource(R.drawable.btn_play_normal_list);
                    holder.img_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (media.getFileid() == localfileid || RoomManager.isMediaPublishing) {
                                return;
                            }
                            if (pop != null) {
                                pop.dismiss();
                            }
                            RoomManager.isMediaPublishing = true;
                            RoomSession.isPlay = true;
                            if (localfileid != -1) {
                                RoomManager.getInstance().unPublishMedia();
                                WhiteBoradManager.getInstance().setCurrentMediaDoc(media);
                            } else {
                                localfileid = media.getFileid();
                                WhiteBoradManager.getInstance().setCurrentMediaDoc(media);
                                String strSwfpath = media.getSwfpath();
                                if (strSwfpath != null && !strSwfpath.isEmpty()) {
                                    int pos = strSwfpath.lastIndexOf('.');
                                    if (pos != -1) {
                                        strSwfpath = String.format("%s-%d%s", strSwfpath.substring(0, pos), 1, strSwfpath.substring(pos));
                                        String url = "http://" + WhiteBoradManager.getInstance().getFileServierUrl() + ":" + WhiteBoradManager.getInstance().getFileServierPort() + strSwfpath;
                                        if (RoomSession.isClassBegin) {
                                            RoomManager.getInstance().publishMedia(url, isMp4(media.getFilename()), media.getFilename(), media.getFileid(), "__all");
                                        } else {
                                            RoomManager.getInstance().publishMedia(url, isMp4(media.getFilename()), media.getFilename(), media.getFileid(), RoomManager.getInstance().getMySelf().peerId);
                                        }
                                    }
                                }
                            }
                        }
                    });

                    if (RoomManager.getInstance().getMySelf().role == 0) {
                        holder.img_delete.setVisibility(View.VISIBLE);
                    } else if (RoomManager.getInstance().getMySelf().role == 2) {
                        holder.img_delete.setVisibility(View.GONE);
                    }

                    holder.img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (pop != null) {
                                pop.dismiss();
                            }
                            if (media.getFileid() == localfileid) {
                                RoomManager.getInstance().unPublishMedia();
                            }
                            WhiteBoradManager.getInstance().delRoomFile(roomNum, media.getFileid(), media.isMedia(), RoomSession.isClassBegin);
                        }
                    });
                }
            }
        } else if (groupPosition == 1) {
            if (WhiteBoradManager.getInstance().getAdminmMediaList().size() > 0) {
                final ShareDoc media = WhiteBoradManager.getInstance().getAdminmMediaList().get(childPosition);
                if (media != null) {
                    holder.img_media_type.setImageResource(getMediaIcon(media.getFilename()));
                    holder.txt_media_name.setText(media.getFilename());
                    holder.img_play.setImageResource(R.drawable.btn_play_normal_list);
                    holder.img_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (media.getFileid() == localfileid || RoomManager.isMediaPublishing) {
                                return;
                            }
                            if (pop != null) {
                                pop.dismiss();
                            }
                            RoomManager.isMediaPublishing = true;
                            RoomSession.isPlay = true;
                            if (localfileid != -1) {
                                RoomManager.getInstance().unPublishMedia();
                                WhiteBoradManager.getInstance().setCurrentMediaDoc(media);
                            } else {
                                localfileid = media.getFileid();
                                WhiteBoradManager.getInstance().setCurrentMediaDoc(media);
                                String strSwfpath = media.getSwfpath();
                                if (strSwfpath != null && !strSwfpath.isEmpty()) {
                                    int pos = strSwfpath.lastIndexOf('.');
                                    if (pos != -1) {
                                        strSwfpath = String.format("%s-%d%s", strSwfpath.substring(0, pos), 1, strSwfpath.substring(pos));
                                        String url = "http://" + WhiteBoradManager.getInstance().getFileServierUrl() + ":" + WhiteBoradManager.getInstance().getFileServierPort() + strSwfpath;
                                        if (RoomSession.isClassBegin) {
                                            RoomManager.getInstance().publishMedia(url, isMp4(media.getFilename()), media.getFilename(), media.getFileid(), "__all");
                                        } else {
                                            RoomManager.getInstance().publishMedia(url, isMp4(media.getFilename()), media.getFilename(), media.getFileid(), RoomManager.getInstance().getMySelf().peerId);
                                        }
                                    }
                                }
                            }


                        }
                    });

                    if (RoomManager.getInstance().getMySelf().role == 0) {
                        if (media.getFilecategory() == 1) {
                            holder.img_delete.setVisibility(View.GONE);
                        } else {
                            holder.img_delete.setVisibility(View.VISIBLE);
                        }
                    } else if (RoomManager.getInstance().getMySelf().role == 2) {
                        holder.img_delete.setVisibility(View.GONE);
                    }

                    holder.img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pop != null) {
                                pop.dismiss();
                            }
                            if (media.getFileid() == localfileid) {
                                RoomManager.getInstance().unPublishMedia();
                            }
                            WhiteBoradManager.getInstance().delRoomFile(roomNum, media.getFileid(), media.isMedia(), RoomSession.isClassBegin);
                        }
                    });
                }
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean isMp4(String filename) {
        int icon = -1;
        if (filename.toLowerCase().endsWith("mp4") || filename.toLowerCase().endsWith("webm")) {
            return true;
        } else {
            return false;
        }
    }

    private int getMediaIcon(String filename) {
        int icon = -1;
        if (filename.toLowerCase().endsWith("mp4") || filename.toLowerCase().endsWith("webm")) {
            icon = R.drawable.icon_mp4;
        } else if (filename.toLowerCase().endsWith("mp3") || filename.toLowerCase().endsWith("wav") || filename.toLowerCase().endsWith("ogg")) {
            icon = R.drawable.icon_mp3;
        }
        return icon;
    }

    class ViewHolder {
        ImageView img_media_type;
        ImageView img_play;
        ImageView img_delete;
        TextView txt_media_name;
    }
}
