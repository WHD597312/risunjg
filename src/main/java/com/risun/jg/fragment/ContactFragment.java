package com.risun.jg.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.risun.jg.R;
import com.risun.jg.activity.AddActivity;
import com.risun.jg.activity.PerInfoActivity;
import com.risun.jg.database.dao.IGroupDao;
import com.risun.jg.database.dao.IPersonDao;
import com.risun.jg.database.dao.impl.GroupDaoImpl;
import com.risun.jg.database.dao.impl.PersonDaoImpl;
import com.risun.jg.adapter.ContactAdapter;
import com.risun.jg.pojo.Group;
import com.risun.jg.pojo.Person;
import com.risun.jg.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by whd on 2017/12/23.
 */

public class ContactFragment extends Fragment{

    private View view;
    private ContactAdapter adapter;
    private List<Group> groups;
    private List<List<Person>> child;

    @BindView(R.id.list_class) ExpandableListView list_class;
    @BindView(R.id.image_insert) ImageButton image_insert;
    private IGroupDao groupDao;
    private IPersonDao personDao;
    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_contact,container,false);
        unbinder=ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        groupDao=new GroupDaoImpl(getActivity());
        personDao=new PersonDaoImpl(getActivity());
        groups=getGroup();
        child=getChild();
        adapter=new ContactAdapter(getActivity(),groups,child);
        list_class.setAdapter(adapter);
        list_class.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                if(child.get(groupPosition).isEmpty()){
                    Utils.show(getActivity(),"该列表为空");
                    return true;
                }
                return false;
            }
        });
        list_class.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                if(child.get(groupPosition).isEmpty()){
                    Utils.show(getActivity(),"该列表为空");
                }else{
                    Person person=child.get(groupPosition).get(childPosition);
                    Intent intent=new Intent(getActivity(), PerInfoActivity.class);

                    intent.putExtra("codes",person.getCodes());
                    startActivity(intent);
                }
                return true;
            }
        });
    }


    /**
     * 获取第一级List
     * @return
     */
    private List<Group> getGroup(){
        groups=new ArrayList<Group>();
        groups=groupDao.findAllGroup();
        return groups;
    }
    private List<List<Person>> getChild(){
        child=new ArrayList<List<Person>>();

        if(groups!=null && groups.size()>0){
            for(Group group:groups){
                if(group!=null){
                    List<Person> persons=personDao.findGroupPerson(group.getCodes());
                    child.add(persons);
                }
            }
        }
        return child;
    }

    @OnClick({R.id.image_insert})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_insert:
                Intent intent=new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
                break;
        }
    }
}
