package com.bimoid.android;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex_xpert on 22.05.2014.
 */
public class CLAdapter extends BaseAdapter {

    private List<CLItem> data = new ArrayList<CLItem>();  //Наша коллекция
    private LayoutInflater inflater;

    public CLAdapter(Context mContext) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //Инфлейтер чтобы получить View из XML
    }

    public void setData(List<CLItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CLItem getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.contact_list_item, null);
        CLItem currentItem = getItem(i);
        TextView.class.cast(view.findViewById(R.id.name)).setText(currentItem.getName());
        TextView.class.cast(view.findViewById(R.id.status)).setText(currentItem.getStatus());
        //try {
            //ImageView.class.cast(view.findViewById(R.id.avatar))
                    //.setImageBitmap(BitmapFactory.decodeByteArray(currentItem.getAvatar(), 0, currentItem.getAvatar().length));
        //} catch(Exception ex) {
            //ex.printStackTrace();
        //}
        return view;
    }

}
