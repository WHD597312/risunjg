package com.risun.jg.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.risun.jg.R;
import com.risun.jg.activity.MyLocationActivity;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.HttpUtils;
import com.risun.jg.utils.ImageLoader;
import com.risun.jg.utils.Utils;
import com.risun.jg.utils.ViewId;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by whd on 2017/12/23.
 */

public class MyFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private View view;

    @ViewId(id=R.id.personal_infolist)
    private ListView personal_infolist;
    private SimpleDraweeView faceimage;
    private SharedPreferences my;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_me,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        my=getActivity().getSharedPreferences("my", Context.MODE_PRIVATE);
        personal_infolist=view.findViewById(R.id.personal_infolist);

        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                LayoutInflater inflater=getActivity().getLayoutInflater();
                View configView=null;
                switch (i){

                    case 0:
                        configView=inflater.inflate(R.layout.head,null,false);
                        faceimage=configView.findViewById(R.id.faceimage);
                        faceimage.setOnClickListener(MyFragment.this);
                        ImageLoader.loadDrawable(faceimage,R.drawable.father);
                        break;
                    case 1:
                        configView=inflater.inflate(R.layout.divider,null,false);
                        configView.setMinimumHeight(50);
                        break;
                    case 2:
                        configView=inflater.inflate(R.layout.update_info,null,false);
                        TextView update_info=configView.findViewById(R.id.text_info);
                        update_info.setText("更改信息");
                        break;
                    case 3:
                        configView=inflater.inflate(R.layout.divider,null,false);
                        configView.setMinimumHeight(10);
                        break;
                    case 4:
                        configView=inflater.inflate(R.layout.update_info,null,false);
                        ImageView image_info=(ImageView) configView.findViewById(R.id.image_info);
                        TextView text_info=(TextView) configView.findViewById(R.id.text_info);
                        image_info.setImageResource(R.drawable.mypicture);
                        text_info.setText("我的位置");
                        break;
                    case 5:
                        configView=inflater.inflate(R.layout.divider,null,false);
                        configView.setMinimumHeight(10);
                        break;
                    default:
                        configView=inflater.inflate(R.layout.update_info,null,false);
                        break;
                }
                return configView;
            }
        };
        personal_infolist.setAdapter(adapter);
        personal_infolist.setOnItemClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.faceimage:
                new LoadImageAsync().execute();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0:


                break;
            case 4:
                Intent intent=new Intent(getActivity(), MyLocationActivity.class);
                startActivity(intent);
                break;
        }
    }
    class LoadImageAsync extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String result=null;
            try{
                String path=getActivity().getFilesDir().getPath()+"//";
                File file=new File(path+"father.jpg");
                if(file.exists()){
                    result= HttpUtils.upLoadFile("http://192.168.1.101:8080/risun-jg/fileUploadAction/upload","father.jpg",file);
                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject msg=jsonObject.getJSONObject("msg");
                    JSONObject cont=msg.getJSONObject("cont");
                    String image=cont.getString("data");
                    SharedPreferences.Editor editor=my.edit();
                    editor.putString("image",image);
                    if(editor.commit()){
                        Map<String,String> map=new HashMap<>();
                        map.put("codes",my.getString("codes",""));
                        map.put("image",image);
                        HttpUtils.getOkHpptRequest("person/update",map);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(Utils.StringIsNullOrEmpty(s)){
                Utils.show(getActivity(),"上传失败");
            }else{
                Utils.show(getActivity(),"上成功");
            }
        }
    }

}
