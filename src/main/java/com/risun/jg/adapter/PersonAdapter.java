package com.risun.jg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.risun.jg.R;
import com.risun.jg.pojo.ItemContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by whd on 2018/1/2.
 */

public class PersonAdapter extends BaseAdapter {
    private Context context;
    private List<ItemContent> list;

    public PersonAdapter(Context context, List<ItemContent> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ItemContent getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){

            view=View.inflate(context, R.layout.perinfo_item,null);
            viewHolder=new ViewHolder(view);

            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        ItemContent item=getItem(i);
        if(item!=null){
            viewHolder.tv_name.setText(item.getName());
            viewHolder.tv_content.setText(item.getContent());
        }
        return view;
    }
    class ViewHolder{
        @BindView(R.id.tv_name) TextView tv_name;
        @BindView(R.id.tv_content) TextView tv_content;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
