package com.bba.ministries.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v-62 on 11/21/2016.
 */

public class ContactUS extends Fragment {
    EditText name,email,subject,message;
    Button submit;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_us, container, false);
        name=(EditText)view.findViewById(R.id.name);
        email=(EditText)view.findViewById(R.id.email);
        subject=(EditText)view.findViewById(R.id.subject);
        message=(EditText)view.findViewById(R.id.message);
        submit=(Button)view.findViewById(R.id.submit);


        name.setText(name.getText().toString());
        email.setText(email.getText().toString());
        subject.setText(subject.getText().toString());
        message.setText(message.getText().toString());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().equalsIgnoreCase(""))
                {
                    name.setError("Please enter name");
                }
                if(email.getText().toString().equalsIgnoreCase(""))
                {
                    email.setError("Please enter email");

                }
                if(subject.getText().toString().equalsIgnoreCase(""))
                {
                    subject.setError("Please enter subject");

                }




                if(name.length()>0 && email.length()>0 && subject.length()>0)
                {
                    Contact();
                }
            }
        });




        return view;
    }
    public void Contact() {
        String tag_json_obj = "json_obj_req";

        String url = GlobalClass.webUrl+"contact-insert";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());


                        try {
                           JSONObject object = new JSONObject(response);
                            String status=object.getString("success");

                            if(status.equalsIgnoreCase("true"))
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setMessage("We will reach you shortly");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                name.setText("");
                                                email.setText("");
                                                message.setText("");
                                                subject.setText("");

                                            }
                                        });



                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                            }


                            pDialog.hide();


                        } catch (Exception e) {

                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Repsonse", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {

            @Override

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("subject", subject.getText().toString());
                params.put("message", message.getText().toString());

                Log.i("name","name"+name.getText().toString());
                Log.i("email","email"+email.getText().toString());
                Log.i("subject","subject"+subject.getText().toString());
                Log.i("message","message"+message.getText().toString());



                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjReq);
    }
}
