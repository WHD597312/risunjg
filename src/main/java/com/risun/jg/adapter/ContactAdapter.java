package com.risun.jg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.risun.jg.R;
import com.risun.jg.pojo.Group;
import com.risun.jg.pojo.Person;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by whd on 2017/12/23.
 */

public class ContactAdapter  extends BaseExpandableListAdapter{

    //上下文
    private Context context;
    //一级节点集合列表
    private List<Group> group;
    //二级节点集合列表
    private List<List<Person>> child;

    public ContactAdapter(Context context, List<Group> group, List<List<Person>> child) {
        this.context = context;
        this.group = group;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try{
            return child.get(groupPosition).size();
        }catch (Exception e){
            e.printStackTrace();
        }
       return 0;
    }

    @Override
    public Group getGroup(int groupPostion) {
        return group.get(groupPostion);
    }

    @Override
    public Person getChild(int groupPostion, int childPosition) {
        return child.get(groupPostion).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPostion) {
        return groupPostion;
    }

    @Override
    public long getChildId(int groupPostion, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder=null;
        if(convertView==null){

            convertView=View.inflate(context, R.layout.fragment_contact_group,null);
            groupHolder=new GroupHolder(convertView);

            convertView.setTag(groupHolder);
        }else{
            groupHolder= (GroupHolder) convertView.getTag();
        }
        /**
        *如果一级列展开时，更换向下图标，
         * 未展开时，更换向右图标
         */

        if(isExpanded){
            groupHolder.image_group.setImageResource(R.drawable.down_arrow);
        }else{
            groupHolder.image_group.setImageResource(R.drawable.right_arrow);
        }
        Group group=getGroup(groupPosition);
        if(group!=null){
            groupHolder.text_group.setText(group.getName());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,ViewGroup parent) {
        ChildHolder childHolder=null;
        if(convertView==null){

            convertView=View.inflate(context,R.layout.fragment_contact_child,null);
            childHolder=new ChildHolder(convertView);

            convertView.setTag(childHolder);
        }else{
            childHolder= (ChildHolder) convertView.getTag();
        }
        Person person=getChild(groupPosition,childPosition);
        childHolder.text_child.setText(person.getUsername());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    class GroupHolder{
        @BindView(R.id.image_group) ImageView image_group;
        @BindView(R.id.text_group) TextView text_group;
        public GroupHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
    class ChildHolder{
        @BindView(R.id.image_child) ImageView image_child;
        @BindView(R.id.text_child) TextView text_child;
        public ChildHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
