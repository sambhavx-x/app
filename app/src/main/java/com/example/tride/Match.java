package com.example.tride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Map;

public class Match extends AppCompatActivity {
   public  String unamea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        replace(new checking());
        Intent intent=getIntent();
        String  emailid=intent.getStringExtra(Homescreen.Em);
        String  date=intent.getStringExtra(Homescreen.Da);
        String  fromTime=intent.getStringExtra(Homescreen.Ft);
        String  toTime=intent.getStringExtra(Homescreen.Tt);
        String  uname=intent.getStringExtra(Homescreen.Un);


        postDataUsingVolley(emailid.toString(),fromTime,toTime,date,uname);
    }


    String url = "https://us-central1-tride-66c25.cloudfunctions.net/helloWorld/travel/check";
    private void postDataUsingVolley(String emailid,String f, String t ,String d,String uname){
        RequestQueue queue = Volley.newRequestQueue(Match.this);
    StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            Toast.makeText(Match.this,response, Toast.LENGTH_LONG).show();


            try {

                JSONObject respObj = new JSONObject(response);
               int sc=respObj.getInt("statuscode");
                if(sc==0){

                   unamea=respObj.getString("uname");


                    replace(new matched());
                }
                else {
//                    unamea="asdff";
                    replace(new unmatched());
                }

               String emailid=respObj.getString("emailid");









            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Match.this,"sub", Toast.LENGTH_SHORT).show();
            }

        }
    }, new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // method to handle errors.
            Toast.makeText(Match.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        protected Map<String, String> getParams() {

            Map<String, String> params = new HashMap<String, String>();

            // on below line we are passing our key
            // and value pair to our parameters.
            params.put("userid", emailid);
            params.put("fromTime", f);
            params.put("toTime", t);
            params.put("date",d);
            params.put("uname",uname);




            return params;
        }
    };


        queue.add(request);


}
    private void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
        Bundle data = new Bundle();
        data.putString("uname",unamea);
        fragment.setArguments(data);
    }





}