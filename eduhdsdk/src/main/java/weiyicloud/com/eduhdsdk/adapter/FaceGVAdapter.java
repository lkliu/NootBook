package weiyicloud.com.eduhdsdk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import weiyicloud.com.eduhdsdk.R;


public class FaceGVAdapter extends BaseAdapter {

    private List<String> list;
    private Context mContext;

    public FaceGVAdapter(List<String> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
    }

    public void clear() {
        this.mContext = null;
    }

    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.face_image, null);
            hodler.iv = (ImageView) convertView.findViewById(R.id.face_img);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        try {
            if (position < 8) {
                Bitmap mBitmap = BitmapFactory.decodeStream(mContext.getAssets().open("face/" + list.get(position)));
                hodler.iv.setImageBitmap(mBitmap);
            }
            if (position == 41) {
                Bitmap mBitmap = BitmapFactory.decodeStream(mContext.getAssets().open("face/" + list.get(8)));
                hodler.iv.setImageBitmap(mBitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHodler {
        ImageView iv;
    }
}
