package max.test.weatherapiapp;

import android.content.Context;


import com.android.volley.Request;
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

    public ServiceData(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String cityId);
    }

    public interface ForeCastByIdResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityId(String cityName, VolleyResponseListener volleyResponseListener) {
        String url = QUERY_FOR_CITY + cityName;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            String cityId = "";
            try {
                JSONObject cityInfo = response.getJSONObject(0);
                cityId = cityInfo.getString("woeid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            volleyResponseListener.onResponse(cityId);
        }, error -> volleyResponseListener.onError("Something wrong"));
          MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getCityForecastById(String cityId, final ForeCastByIdResponse foreCastByIdResponse) {
        String url = QUERY_FOR_CITY_WEATHER_BY_id + cityId;
        List<WeatherReportModel> weatherReports = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                for (int i = 0; i < consolidated_weather_list.length(); i++) {
                    WeatherReportModel day_weather = new WeatherReportModel();
                    JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);

                    day_weather.setId(first_day_from_api.getInt("id"));
                    day_weather.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                    day_weather.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                    day_weather.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                    day_weather.setCreated(first_day_from_api.getString("created"));
                    day_weather.setApplicable_date(first_day_from_api.getString("applicable_date"));
                    day_weather.setMin_temp(first_day_from_api.getLong("min_temp"));
                    day_weather.setMax_temp(first_day_from_api.getLong("max_temp"));
                    day_weather.setThe_temp(first_day_from_api.getLong("the_temp"));
                    day_weather.setWind_speed(first_day_from_api.getLong("wind_speed"));
                    day_weather.setWind_direction(first_day_from_api.getLong("wind_direction"));
                    day_weather.setAir_pressure(first_day_from_api.getLong("air_pressure"));
                    day_weather.setHumidity(first_day_from_api.getInt("humidity"));
                    day_weather.setVisibility(first_day_from_api.getLong("visibility"));
                    day_weather.setPredictability(first_day_from_api.getInt("predictability"));
                    weatherReports.add(day_weather);
                }
                foreCastByIdResponse.onResponse(weatherReports);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface  GetCityForecastByNameCallback{
        void  onError(String message);
        void onResponse(List<WeatherReportModel>weatherReportModels);
    }

    public void getCityForecastByName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback) {
        getCityId(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityId) {
                getCityForecastById(cityId, new ForeCastByIdResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        getCityForecastByNameCallback.onResponse(weatherReportModels);
                    }
                });
            }
        });
    }
}
