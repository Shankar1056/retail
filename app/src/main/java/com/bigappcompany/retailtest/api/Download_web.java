package com.bigappcompany.retailtest.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bigappcompany.retailtest.Utilz.Utility;
import com.bigappcompany.retailtest.interfaces.OnTaskCompleted;

import org.apache.http.NameValuePair;

import java.util.ArrayList;


public class Download_web extends AsyncTask<String, Integer, String>
    {
        private final Context context;
        private String response="";
        private OnTaskCompleted listener;
        private boolean isGet;
        private ArrayList<NameValuePair> data;
        private static final String TAG = "Download_web : ";

        public Download_web(Context context, OnTaskCompleted listener)
        {
            this.context=context;
            this.listener=listener;
        }
        public void setReqType(boolean isGet)
        {
            this.isGet=isGet;
        }
        public void setData(ArrayList<NameValuePair> data)
        {
            this.data=data;
        }
    
    
    
        @Override
        protected void onPreExecute()
        {
        
            super.onPreExecute();
        }
        
        @Override
        protected String doInBackground(String... params)
        {

            for(String url:params)
            {
                if(isGet)
                {
                    try {
                        response= Utility.executeHttpGet(url);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                else
                {
                    try {
                        response=Utility.executeHttpPost(url,data);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            return response;
        }
    
       
    
        @Override
        protected void onPostExecute(String result)
        {

            if(!result.equals(""))
            {
                listener.onTaskCompleted(result);
            }
            else
            {

                Toast.makeText(context,  "something wrong", Toast.LENGTH_LONG).show();
            }

        }

        


    }