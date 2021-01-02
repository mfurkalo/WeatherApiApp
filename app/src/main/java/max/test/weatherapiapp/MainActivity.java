package max.test.weatherapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btn_cityId, btn_getWeatherById, btn_getWeatherByName;
    EditText et_dataInput;
    ListView lv_weatherReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assigning values to each control  on the layout.
        btn_cityId = findViewById(R.id.btn_getCityId);
        btn_getWeatherById = findViewById(R.id.btn_getWeatherByCityId);
        btn_getWeatherByName = findViewById(R.id.btn_getWeatherByCityName);

        et_dataInput = findViewById(R.id.et_dataInput);
        lv_weatherReport = findViewById(R.id.lv_weatherReports);

        //click listeners for the buttons
        btn_getWeatherById.setOnClickListener(v -> {

            Toast.makeText(MainActivity.this, "You clicked W Id.", Toast.LENGTH_SHORT).show();
        });

        btn_getWeatherByName.setOnClickListener(v -> {

            Toast.makeText(MainActivity.this, "You typed " + et_dataInput.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        btn_cityId.setOnClickListener(v -> {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            String url = "https://www.metaweather.com/api/location/search/?query=" + et_dataInput.getText().toString();
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String cityId = "";
                    try {
                        JSONObject cityInfo = response.getJSONObject(0);
                        cityId =cityInfo.getString("woeid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "City ID = "+cityId , Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "An ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(request);
        });

    }
}