package max.test.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceData {

    public static final String QUERY_FOR_CITY = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_id = "https://www.metaweather.com/api/location/";

    Context context;
    String cityId;

    public ServiceData(Context context) {
        this.context = context;
    }

public  interface  VolleyResponseListener{
        void onError(String message);

        void onResponse(String cityId);
}

    public  interface  ForeCastByIdResponse{
        void onError(String message);

        void onResponse(WeatherReportModel forecast);
    }

    public  void getCityId(String cityName, VolleyResponseListener volleyResponseListener){
        String url = QUERY_FOR_CITY +cityName;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            String cityId = "";
            try {
                JSONObject cityInfo = response.getJSONObject(0);
                cityId = cityInfo.getString("woeid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//                Toast.makeText(context, "City ID = " + cityId, Toast.LENGTH_SHORT).show();
            volleyResponseListener.onResponse(cityId);
        }, error -> {
//                Toast.makeText(context, "An ERROR", Toast.LENGTH_SHORT).show();
            volleyResponseListener.onError("Something wrong");
        });
        // Add the request to the RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(request);
    }


//    public List<WeatherReportModel> getCityForecastByName (String cityId){
//
//    }
//
    public void getCityForecastById (String cityId, ForeCastByIdResponse forecast) {
        String url = QUERY_FOR_CITY_WEATHER_BY_id + cityId;
        List<WeatherReportModel> report = new ArrayList<>();
//        get the json object
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            try {
                JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

//                    get the first item in the array
                WeatherReportModel first_day =new WeatherReportModel();

                JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(0);
                first_day.setId(first_day_from_api.getInt("id"));
                first_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                first_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                first_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                first_day.setCreated(first_day_from_api.getString("created"));
                first_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                first_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                first_day.setMax_temp(first_day_from_api.getLong("max_temp"));
                first_day.setThe_temp(first_day_from_api.getLong("the_temp"));
                first_day.setWind_speed(first_day_from_api.getLong("wind_speed"));
                first_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                first_day.setAir_pressure(first_day_from_api.getLong("air_pressure"));
                first_day.setHumidity(first_day_from_api.getInt("humidity"));
                first_day.setVisibility(first_day_from_api.getLong("visibility"));
                first_day.setPredictability(first_day_from_api.getInt("predictability"));

                forecast.onResponse(first_day);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
