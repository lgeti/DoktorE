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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class Appointments extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView apps;
    private String url = "https://doktore.azurewebsites.net/api/AppointmentsAPI";

    public static final String EXTRA_MESSAGE = "com.igeti.doktore.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        apps = (TextView) findViewById(R.id.prikazAppTextView);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.appointments);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.schedule:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.appointments:
                        return true;
                    case R.id.clinics:
                        startActivity(new Intent(getApplicationContext(),Clinics.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public  void prikaziApps(View view){
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
                    String doctorID = object.getString("doctorID");
                    String patientID = object.getString("patientID");
                    String doctorsNote = object.getString("doctorsNote");
                    String appointmentDate = object.getString("appointmentDate");
                    StringBuffer date= new StringBuffer(appointmentDate);
                    date.delete(10, 19);


                    data.add(i+1 + ". Doktor ID: " + doctorID + "   ||       Patient ID: " + patientID + "     ||   Date: " + date + "     ||   Note: " + doctorsNote);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            apps.setText("");


            for (String row: data){
                String currentText = apps.getText().toString();
                apps.setText(currentText + "\n\n" + row);
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