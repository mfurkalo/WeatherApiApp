package max.test.weatherapiapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

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

        final ServiceData serviceData = new ServiceData(MainActivity.this);


        //click listeners for the buttons
        btn_getWeatherById.setOnClickListener(v -> serviceData.getCityForecastById(et_dataInput.getText().toString(), new ServiceData.ForeCastByIdResponse() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<WeatherReportModel> weatherReportModels) {
                ArrayAdapter<WeatherReportModel> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                lv_weatherReport.setAdapter(arrayAdapter);

            }
        }));

        btn_getWeatherByName.setOnClickListener(v -> serviceData.getCityForecastByName(et_dataInput.getText().toString(), new ServiceData.GetCityForecastByNameCallback() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        ArrayAdapter<WeatherReportModel> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                        lv_weatherReport.setAdapter(arrayAdapter);

                    }
                })

        );

        btn_cityId.setOnClickListener(v -> serviceData.getCityId(et_dataInput.getText().toString(), new ServiceData.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String cityId) {
                Toast.makeText(MainActivity.this, "Returned an ID of " + cityId, Toast.LENGTH_SHORT).show();
            }
        }));
    }
}