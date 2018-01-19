package com.risun.jg.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by whd on 2018/1/11.
 */

public class SDHelper {
    public static void saveFile(String url){
        try{
            String state=Environment.getExternalStorageState();
//            MediaStore.Images.Media.DATA
            String data=MediaStore.Images.Media.DATA;
            if(Environment.MEDIA_MOUNTED.equals(state)){
                File file=Environment.getExternalStorageDirectory();
                String sdDir=file.getPath();
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
//                    FileOutputStream fis=context.openFileOutput("father.jpg", Context.MODE_PRIVATE);
                    FileOutputStream fis=new FileOutputStream(sdDir+"/father.jpg");
                    BufferedOutputStream outputStream=new BufferedOutputStream(fis);
                    while((len=is.read(buffer))!=-1){
                        outputStream.write(buffer,0,len);
                    }
                }
            }
        }catch (Exception e){

        }
    }
}
