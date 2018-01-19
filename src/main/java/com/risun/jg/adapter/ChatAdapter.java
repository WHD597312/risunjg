package com.risun.jg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.risun.jg.R;
import com.risun.jg.pojo.Chat;

import java.util.List;

/**
 * Created by whd on 2018/1/2.
 */

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<Chat> list;

    public ChatAdapter(Context context, List<Chat> list) {
        this.context = context;
        this.list = list;
    }

    public interface MsgType{
        int IMVT_COM_MSG=0;//接收消息
        int IMVT_FROM_MSG=1;//发送消息
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Chat getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat=getItem(position);
        String isMeSend=chat.getIsMeSend();
        if("1".equals(isMeSend)){
            return MsgType.IMVT_FROM_MSG;
        }else{
            return MsgType.IMVT_COM_MSG;
        }
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        HolderView holderView=new HolderView();
        Chat chat=getItem(i);
        String isMeSend=chat.getIsMeSend();
        View view=null;
        if("1".equals(isMeSend)){
            view=View.inflate(context, R.layout.activity_chat_item_right,null);
            holderView.tv_right_name=view.findViewById(R.id.tv_right_name);
            holderView.tv_right_content=view.findViewById(R.id.tv_right_content);
            holderView.tv_right_name.setText(chat.getName());
            holderView.tv_right_content.setText(chat.getContent());
        }else{
            view=View.inflate(context,R.layout.activity_chat_item_left,null);
            holderView.tv_left_name=view.findViewById(R.id.tv_left_name);
            holderView.tv_left_content=view.findViewById(R.id.tv_left_content);
            holderView.tv_left_name.setText(chat.getName());
            holderView.tv_left_content.setText(chat.getContent());
        }
        return view;
    }

    /**
     * 点击分离器是没有效果的
     * @param position
     * @return
     */
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    class  HolderView{
        TextView tv_left_name;
        TextView tv_left_content;

        TextView tv_right_name;
        TextView tv_right_content;
    }
}
