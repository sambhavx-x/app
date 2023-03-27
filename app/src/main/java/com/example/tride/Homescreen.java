package com.example.tride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.util.Clock;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Homescreen extends AppCompatActivity {
    EditText date;
    String emailid,f,t;
    String FromTime,ToTime,FDate,fromtime,totime;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
    EditText fromTime,toTime;
    AppCompatButton logout,submit;

    final Calendar c=Calendar.getInstance();
    TextView name,check;
    String api="https://us-central1-tride-66c25.cloudfunctions.net/helloWorld";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        fromTime=findViewById(R.id.fromTime);

        toTime=findViewById(R.id.toTime);



                                    ////////







        /////////////////

        //////////logout

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        ////////////////





        ////////////////
        name=findViewById(R.id.name);
        submit=findViewById(R.id.submit);
        ////////////
        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            name.setText(signInAccount.getDisplayName());
            emailid = signInAccount.getEmail();
        }

        date=findViewById(R.id.date);




        /////////////////picking date  and time
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);

                int month = c.get(Calendar.MONTH);

                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        Homescreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                FDate =date.getText().toString();
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);

                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);

                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Homescreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromTime.setText( selectedHour + ":" + selectedMinute);
                        FromTime=fromTime.getText().toString();
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



            }
        });
        ///////////////////////////

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);

                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Homescreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                        toTime.setText( selectedHour + ":" + selectedMinute);
                        ToTime=toTime.getText().toString();

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



            }


        });

        /////////making date and time to absolute milliseconds


        //////////////SUBMIT BUTTON//////////
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the text field is empty or not.
                if (emailid.isEmpty()) {
                    Toast.makeText(Homescreen.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(fromtime.isEmpty() || totime.isEmpty() ||  FDate.isEmpty() ){
//                    Dialog dialog = new Dialog(Homescreen.this);
//                    dialog.setContentView(R.layout.popup);
//                    dialog.show();
//                }
                ///////////////final time

                check =findViewById(R.id.check);
                fromtime =FDate.concat(" ").concat(FromTime);
                totime=FDate.concat(" ").concat(ToTime);

                try {
                   Date d1= sdf.parse(fromtime);
                  f= String.valueOf(d1.getTime());
                    Date d2  = sdf.parse(totime);
                    t=String.valueOf(d2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                ////////////////
                // calling a method to post the data and passing our name and job.
                postDataUsingVolley(emailid.toString(),f,t);
            }

        });


    }





        ///////////////////sending api request//////////
        private void postDataUsingVolley(String emailid,String f, String t ){
            // url to post our data
            String url = "https://us-central1-tride-66c25.cloudfunctions.net/helloWorld/travel/post";



            // creating a new variable for our request queue
            RequestQueue queue = Volley.newRequestQueue(Homescreen.this);


            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(Homescreen.this, "Data added to API", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),Match.class);
                    startActivity(intent);
                    try {

                        JSONObject respObj = new JSONObject(response);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    Toast.makeText(Homescreen.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // below line we are creating a map for
                    // storing our values in key and value pair.
                    Map<String, String> params = new HashMap<String, String>();

                    // on below line we are passing our key
                    // and value pair to our parameters.
                    params.put("userid", emailid);
                    params.put("fromTime", f);
                    params.put("toTime", t);
                    


                    // at last we are
                    // returning our params.
                    return params;
                }
            };
            // below line is to make
            // a json object request.
            queue.add(request);


        }
        //////////////////
    }
