package max.test.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ServiceData {

    public static final String QUERY_FOR_CITY = "https://www.metaweather.com/api/location/search/?query=";

    Context context;
    String cityId;

    public ServiceData(Context context) {
        this.context = context;
    }

public  interface  VolleyResponseListener{
        void onError(String message);


        void onResponse(String cityId);

}

    public  void getCityId(String cityName, VolleyResponseListener volleyResponseListener){
        String url = QUERY_FOR_CITY +cityName;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String cityId = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(context, "City ID = " + cityId, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "An ERROR", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong");
            }
        });
        // Add the request to the RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(request);
    }


//    public List<WeatherReportModel> getCityForecastById (String cityId){
//
//    }
//
//    public List<WeatherReportModel> getCityForecastByName (String cityId) {
//
//
//    }
}
