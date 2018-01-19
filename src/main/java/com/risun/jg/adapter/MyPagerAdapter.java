package com.risun.jg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.j256.ormlite.stmt.query.In;
import com.risun.jg.R;
import com.risun.jg.pojo.Ad;

import java.util.List;

/**
 * Created by whd on 2018/1/16.
 */

public class MyPagerAdapter extends PagerAdapter{

    private Context context;
    private List<Ad> list;
    private String TAG="MyPagerAdapter";
    public MyPagerAdapter(Context context,List<Ad> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.adapter_ad,null);
        ImageView image=(ImageView) view.findViewById(R.id.image);
        Ad ad=list.get(position);
        image.setImageResource(ad.getIconResId());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
