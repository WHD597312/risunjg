package com.risun.jg.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.risun.jg.R;
import com.risun.jg.custom_view.ToggleView;
import com.risun.jg.custom_view.XCRoundProgressBar;

public class TestActivity2 extends AppCompatActivity {

    ToggleView toggleView;
    XCRoundProgressBar progressBar;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        toggleView=this.findViewById(R.id.toggleView);
        progressBar=this.findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        toggleView.setOnUpdateToggleListener(new ToggleView.OnUpdateToggleListener() {
            @Override
            public void onUpdateListener(boolean state) {
                if (state){
                    toggleView.setToggleText("个人");
                }else{
                    toggleView.setToggleText("车队");
                }
            }
        });
       new ProgressAsyncTask().execute();
    }

    class ProgressAsyncTask extends AsyncTask<Integer,Integer,Integer>{


        @Override
        protected Integer doInBackground(Integer... integers) {
            for(i=1;i<=100;i++){
                publishProgress(i);
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

}
