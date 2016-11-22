package com.yzq.android.experimenteight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzq.android.experimenteight.R;
import com.yzq.android.experimenteight.Util.BirthItem;

import java.util.List;

/**
 * Created by YZQ on 2016/11/21.
 */

public class BirthAdapter extends BaseAdapter {

    private Context context;
    private List<BirthItem> birthItemList;

    public BirthAdapter(Context context, List<BirthItem> birthItemList) {
        this.context = context;
        this.birthItemList = birthItemList;
    }

    @Override
    public int getCount() {
        if (birthItemList == null) {
            return 0;
        }
        return birthItemList.size();
    }

    @Override
    public Object getItem(int i) {
        if(birthItemList == null) {
            return null;
        }
        return birthItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;

        if(view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.birth_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.birth = (TextView)convertView.findViewById(R.id.birth);
            viewHolder.gift = (TextView)convertView.findViewById(R.id.gift);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(birthItemList.get(i).getName());
        viewHolder.birth.setText(birthItemList.get(i).getBirth());
        viewHolder.gift.setText(birthItemList.get(i).getGift());

        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView birth;
        public TextView gift;
    }
}
