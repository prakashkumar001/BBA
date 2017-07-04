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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.R;
import com.bba.ministries.adapter.SpinnerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by v-62 on 12/8/2016.
 */

public class PrayerRequest extends Fragment {

    Spinner spinner;
    EditText name,email,specialrequest,phone;
    String selectedtext;
    Button submit;
    TextView text;
    ArrayList<String> datas;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prayer_request, container, false);

        spinner=(Spinner)view.findViewById(R.id.spinner);
        name=(EditText)view.findViewById(R.id.name);
        email=(EditText)view.findViewById(R.id.email);
        phone=(EditText)view.findViewById(R.id.phone);
        specialrequest=(EditText)view.findViewById(R.id.specialrequest);
        submit=(Button)view.findViewById(R.id.submit);


        prayer_Spinner();




        // Initializing an ArrayAdapter

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
                if(phone.getText().toString().equalsIgnoreCase(""))
                {
                    phone.setError("Please enter phone number");

                }
                if(specialrequest.getText().toString().equalsIgnoreCase(""))
                {
                    specialrequest.setError("Please enter specialrequest");

                }

                if(selectedtext.equalsIgnoreCase("---Please select---"))
                {
                    text.setError("Select a request");
                }



                if(name.length()>0 && email.length()>0 && specialrequest.length()>0 && !selectedtext.equalsIgnoreCase("---Please select---"))
                {
                    PrayerRequest();
                }
            }
        });



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageView image=(ImageView)view.findViewById(R.id.lines);
                image.setVisibility(View.INVISIBLE);

                text=(TextView)view.findViewById(R.id.expandedListItem);
                selectedtext=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void PrayerRequest() {
        String tag_json_obj = "json_obj_req";

        String url = GlobalClass.webUrl+"prayer-insert";

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
                                builder1.setMessage("Successfully submitted your request");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                name.setText("");
                                                email.setText("");
                                                phone.setText("");
                                                specialrequest.setText("");
                                                prayer_Spinner();

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


                Log.i("name","name"+name.getText().toString());
                Log.i("email","email"+email.getText().toString());
                Log.i("phone","phone"+phone.getText().toString());
                Log.i("message","message"+specialrequest.getText().toString());
                Log.i("prayerfor","prayerfor"+selectedtext);




                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("message", specialrequest.getText().toString());
                params.put("prayer_for", selectedtext);





                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjReq);
    }


    public void prayer_Spinner()
    {
        // Initializing a String Array
        String[] data = new String[]{
                "---Please select---",
                "Salvation",
                "Healing",
                "Need a job",
                "Family",
                "Finances",
                "Others"
        };
        datas = new ArrayList<>(Arrays.asList(data));
        SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(getActivity(),R.layout.prayer_request_item,datas);
        spinner.setAdapter(spinnerArrayAdapter);





    }
}
