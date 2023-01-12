package com.igeti.doktore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Clinics extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView clics;
    private String url = "https://doktore.azurewebsites.net/api/ClinicsAPI";

    public static final String EXTRA_MESSAGE = "com.igeti.doktore.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        clics = (TextView) findViewById(R.id.prikazClinicsTextView);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.clinics);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.appointments:
                        startActivity(new Intent(getApplicationContext(),Appointments.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.clinics:
                        return true;
                    case R.id.schedule:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public  void prikaziClinics(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener)
            {
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ApiKey", "SecretKey");
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object =response.getJSONObject(i);
                    String id = object.getString("id");
                    String typeOfClinic = object.getString("typeOfClinic");

                    data.add(i+1 + ". Clinic ID: " + id + "      ||       type of Clinic: " + typeOfClinic);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            clics.setText("");


            for (String row: data){
                String currentText = clics.getText().toString();
                clics.setText(currentText + "\n\n" + row);
            }

        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };
}