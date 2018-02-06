package weiyicloud.com.eduhdsdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.classroomsdk.Packager;
import com.classroomsdk.ShareDoc;
import com.classroomsdk.WhiteBoradManager;
import com.talkcloud.roomsdk.RoomManager;

import org.json.JSONObject;

import weiyicloud.com.eduhdsdk.R;
import weiyicloud.com.eduhdsdk.message.RoomSession;

/**
 * Created by Administrator on 2018/1/4.
 */

public class FileListEXAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String roomNum;
    PopupWindow pop = null;

    public static boolean isClassExpand = false;
    public static boolean isAdminExpand = true;

    public void setPop(PopupWindow pop) {
        this.pop = pop;
    }

    public FileListEXAdapter(Context context, String Roomnum) {
        this.context = context;
        this.roomNum = Roomnum;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        if (groupPosition == 1) {
            count = WhiteBoradManager.getInstance().getClassDocList().size();
        } else if (groupPosition == 2) {
            count = WhiteBoradManager.getInstance().getAdminDocList().size();
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 1) {
            WhiteBoradManager.getInstance().getClassDocList().get(childPosition);
        } else if (groupPosition == 2) {
            WhiteBoradManager.getInstance().getAdminDocList().get(childPosition);
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
        if (groupPosition == 0) {
            final ShareDoc whiteboard = WhiteBoradManager.getInstance().getmBlankShareDoc();
            convertView = LayoutInflater.from(context).inflate(R.layout.file_list_item, null);
            ImageView img_file_type = (ImageView) convertView.findViewById(R.id.img_file_type);
            TextView txt_file_name = (TextView) convertView.findViewById(R.id.txt_file_name);
            ImageView img_eye = (ImageView) convertView.findViewById(R.id.img_eye);
            ImageView img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            if (whiteboard != null) {
                if (whiteboard.getFileid() == 0) {
                    whiteboard.setFilename(context.getString(R.string.share_pad));
                }
                int icon = getFileIcon(whiteboard.getFilename());
                img_file_type.setImageResource(icon);
                txt_file_name.setText(whiteboard.getFilename());

                if (whiteboard.getFileid() == WhiteBoradManager.getInstance().getCurrentFileDoc().getFileid()) {
                    img_eye.setImageResource(R.drawable.btn_eyes_02_normal);
                } else {
                    img_eye.setImageResource(R.drawable.btn_eyes_01_normal);
                }
                if (RoomManager.getInstance().getMySelf().role == 0) {
                    if (whiteboard.getFileid() == 0) {
                        img_delete.setVisibility(View.GONE);
                    } else {
                        img_delete.setVisibility(View.VISIBLE);
                    }
                } else if (RoomManager.getInstance().getMySelf().role == 2) {
                    img_delete.setVisibility(View.GONE);
                }
                img_eye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (whiteboard.getFileid() == WhiteBoradManager.getInstance().getCurrentFileDoc().getFileid()) {
                            return;
                        }
                        WhiteBoradManager.getInstance().localChangeDoc(whiteboard);
                        notifyDataSetChanged();
                        if (RoomSession.isClassBegin) {
                            JSONObject data = new JSONObject();
                            data = Packager.pageSendData(whiteboard);
                            RoomManager.getInstance().pubMsg("ShowPage", "DocumentFilePage_ShowPage", "__all", data.toString(), true, null, null);
                        }
                        if (pop != null) {
                            pop.dismiss();
                        }
                    }
                });
            }
        } else if (groupPosition > 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.file_group_item, null);
            TextView txt_file_group_type = (TextView) convertView.findViewById(R.id.txt_file_group_type);
            ImageView img_is_expand = (ImageView) convertView.findViewById(R.id.img_is_expand);

            if (groupPosition == 1) {
                txt_file_group_type.setText(R.string.class_file_group);
            } else if (groupPosition == 2) {
                txt_file_group_type.setText(R.string.admin_file_group);
            }

            if (isExpanded) {
                img_is_expand.setImageResource(R.drawable.file_group_down);
                convertView.setBackgroundResource(R.color.file_group_open);
            } else {
                img_is_expand.setImageResource(R.drawable.file_group_left);
                convertView.setBackgroundResource(R.color.file_group_close);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (WhiteBoradManager.getInstance().getDocList().size() > 0) {
        if (convertView == null) {
            holder = new FileListEXAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.file_list_item, null);
            holder.img_file_type = (ImageView) convertView.findViewById(R.id.img_file_type);
            holder.txt_file_name = (TextView) convertView.findViewById(R.id.txt_file_name);
            holder.img_eye = (ImageView) convertView.findViewById(R.id.img_eye);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShareDoc tempFileDoc = null;
        if (groupPosition == 1) {
            tempFileDoc = WhiteBoradManager.getInstance().getClassDocList().get(childPosition);
        } else if (groupPosition == 2) {
            tempFileDoc = WhiteBoradManager.getInstance().getAdminDocList().get(childPosition);
        }
        final ShareDoc fileDoc = tempFileDoc;
        if (fileDoc != null) {
            if (fileDoc.getFileid() == 0) {
                fileDoc.setFilename(context.getString(R.string.share_pad));
            }
            int icon = getFileIcon(fileDoc.getFilename());
            holder.img_file_type.setImageResource(icon);
            holder.txt_file_name.setText(fileDoc.getFilename());

            if (fileDoc.getFileid() == WhiteBoradManager.getInstance().getCurrentFileDoc().getFileid()) {
                holder.img_eye.setImageResource(R.drawable.btn_eyes_02_normal);
            } else {
                holder.img_eye.setImageResource(R.drawable.btn_eyes_01_normal);
            }
            if (RoomManager.getInstance().getMySelf().role == 0) {
                if (fileDoc.getFileid() == 0 || fileDoc.getFilecategory() == 1) {
                    holder.img_delete.setVisibility(View.GONE);
                } else {
                    holder.img_delete.setVisibility(View.VISIBLE);
                }
            } else if (RoomManager.getInstance().getMySelf().role == 2) {
                holder.img_delete.setVisibility(View.GONE);
            }
            holder.img_eye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileDoc.getFileid() == WhiteBoradManager.getInstance().getCurrentFileDoc().getFileid()) {
                        return;
                    }
                    WhiteBoradManager.getInstance().localChangeDoc(fileDoc);
                    notifyDataSetChanged();
                    if (RoomSession.isClassBegin) {
                        JSONObject data = new JSONObject();
                        data = Packager.pageSendData(fileDoc);
                        RoomManager.getInstance().pubMsg("ShowPage", "DocumentFilePage_ShowPage", "__all", data.toString(), true, null, null);

                    }
                    if (pop != null) {
                        pop.dismiss();
                    }
                }
            });
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pop != null) {
                        pop.dismiss();
                    }
                    WhiteBoradManager.getInstance().delRoomFile(roomNum, fileDoc.getFileid(), fileDoc.isMedia(), RoomSession.isClassBegin);
                }
            });
        }
//        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private int getFileIcon(String filename) {
        int icon = -1;
        if (filename == null && filename.isEmpty()) {
            icon = R.drawable.icon_empty;
        }
        if (filename.toLowerCase().endsWith(".pptx") || filename.toLowerCase().endsWith(".ppt") || filename.toLowerCase().endsWith(".pps")) {
            icon = R.drawable.icon_ppt;
        } else if (filename.toLowerCase().endsWith(".docx") || filename.toLowerCase().endsWith(".doc")) {
            icon = R.drawable.icon_word;
        } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")
                || filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".gif")
                || filename.toLowerCase().endsWith(".bmp")) {
            icon = R.drawable.icon_images;
        } else if (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx")
                || filename.toLowerCase().endsWith(".xlt") || filename.toLowerCase().endsWith("xlsm")) {
            icon = R.drawable.icon_excel;
        } else if (filename.toLowerCase().endsWith(".pdf")) {
            icon = R.drawable.icon_pdf;
        } else if (filename.equals(context.getResources().getString(R.string.share_pad))) {
            icon = R.drawable.icon_empty;
        } else if (filename.toLowerCase().endsWith(".txt")) {
            icon = R.drawable.icon_text_pad;
        } else if (filename.toLowerCase().endsWith(".zip")) {
            icon = R.drawable.icon_h5;
        }
        return icon;
    }

    class ViewHolder {
        ImageView img_file_type;
        TextView txt_file_name;
        ImageView img_eye;
        ImageView img_delete;
    }
}
