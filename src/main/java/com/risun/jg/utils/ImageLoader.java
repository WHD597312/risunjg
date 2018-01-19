package com.risun.jg.utils;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by whd on 2018/1/10.
 */

public class ImageLoader {
    public static void loadDrawable(SimpleDraweeView draweeView, int resId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
    }
    public static void loadFile(SimpleDraweeView draweeView, String filePath) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
    }
    public static void loadImage(Context context,String url) {
        try{
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call=client.newCall(request);
            Response response=call.execute();
            if(response.isSuccessful()){
                InputStream is=response.body().byteStream();
                byte[] buffer=new byte[1024*10];
                int len=0;
                FileOutputStream fis=context.openFileOutput("father.jpg",Context.MODE_PRIVATE);
                BufferedOutputStream outputStream=new BufferedOutputStream(fis);
                while((len=is.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
