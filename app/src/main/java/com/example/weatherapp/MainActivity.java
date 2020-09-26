package com.example.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView locationTV;
    TextView tempTV;
    ImageView weatherImg;

    JSONObject jsonObject = null;

    private static String jsonVal;

    private TextView temp;
    private  TextView lat;
    private TextView lon;
    private TextView time;
    private TextView wSpeed;
    private TextView wDir;
    private TextView rain;
    TextView sunset;
    TextView sunrisee;
    TextView presure;
    TextView humidity;
    String t;
    String latit;
    String longi;
    String timeZ;
    String query;
    String utc;
    String wSp;
    String r;
    String wD;
    String press;
    String humid;
    String sunR;
    String sunS;

    String apiUrl = "http://api.weatherstack.com/current?access_key=994a299da6ed0166887bd2617bef3f4a&query=";
    String forecastUrl =  "http://api.weatherstack.com/forecast?access_key=994a299da6ed0166887bd2617bef3f4a&query=";
    String tideUrl = "https://api.stormglass.io/v2/tide/extremes/point";
    String apiTides = "c602afae-ffe2-11ea-a78a-0242ac130002-c602b062-ffe2-11ea-a78a-0242ac130002";
    String openWeatherApi = "8576bac5484e2aee898d86bd9e5e9235";
    String openWeatherUrl = "https://api.openweathermap.org/data/2.5/onecall?";
    String mapUrl = "https://tile.openweathermap.org/map/precipitation_new/10/20/20.png?appid=8576bac5484e2aee898d86bd9e5e9235";
    SearchView searchView;

    Instant instantS;
    Instant instantR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        weatherImg = findViewById(R.id.weatherImage);
        locationTV = findViewById(R.id.location);
        lon = findViewById(R.id.longitude);
        lat = findViewById(R.id.lat);
        time = findViewById(R.id.time_zone);
        temp = findViewById(R.id.temperature);
        wDir = findViewById(R.id.windD);
        wSpeed = findViewById(R.id.windS);
        rain = findViewById(R.id.rainR);
        presure = findViewById(R.id.pressureVal);
        humidity = findViewById(R.id.humVal);
        sunrisee = findViewById(R.id.SunriseVal);
        sunset = findViewById(R.id.SunsetVal);

        searchView = (SearchView) findViewById(R.id.searchb);

//        getLocationFromAddress(getApplicationContext());



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQ) {
                //Do your search
                query = searchQ;
                getLocationFromAddress(getApplicationContext());
//                apiUrl = "http://api.weatherstack.com/current?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
//                forecastUrl =  "http://api.weatherstack.com/forecast?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
                openWeatherUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+latit+"&lon="+longi+"&exclude=weekly&appid="+openWeatherApi+"&units=metric";
                Log.d("API", apiUrl);

                BackgroundTask task = new BackgroundTask();
                task.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if(newText.isEmpty()) clearSearch();
                return false;
            }
        });

//
//        String[] items = new String[]{"Cork", "Dublin", "Stockholm"};
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);
//
//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
//                query = (String) parent.getItemAtPosition(position);
//                apiUrl = "http://api.weatherstack.com/current?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
//                Log.d("API", apiUrl);
//
//                BackgroundTask task = new BackgroundTask();
//                task.execute();
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                getLocationFromAddress(getApplicationContext());
//                apiUrl = "http://api.weatherstack.com/current?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
//                forecastUrl =  "http://api.weatherstack.com/forecast?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
//                tideUrl = "https://api.stormglass.io/v2/tide/extremes/point?lat="+latit+"&lng="+longi+"&start=2020-09-25&end=2020-09-25";
//                tideUrl = "https://api.stormglass.io/v2/tide/extremes/point?lat=52.0442&lng=7.9297&start=2020-09-24&end=2020-09-25?c602afae-ffe2-11ea-a78a-0242ac130002-c602b062-ffe2-11ea-a78a-0242ac130002";
                openWeatherUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+latit+"&lon="+longi+"&exclude=weekly&appid="+openWeatherApi+"&units=metric";

            }
        });

        BackgroundTask task = new BackgroundTask();
        task.execute();

        temp.setText(t);


        locationTV.setText(jsonVal);

//        weatherImg.setImageDrawable();

    }

    private class BackgroundTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                getWeatherLibrary();
                Log.d("TAG", openWeatherUrl);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void getWeatherLibrary() throws IOException, JSONException {

//        JSONObject json = JsonConverter.readJsonFromUrl(apiUrl);
//        JSONObject jsonForeCast = JsonConverter.readJsonFromUrl(forecastUrl);
        JSONObject openWeather = JsonConverter.readJsonFromUrl(openWeatherUrl);
//        JSONArray openWeatherArray = JsonConverter.readArrayFromUrl(openWeatherUrl);
//        JSONObject openWeather = JsonConverter.readJsonFromUrl(openWeatherUrl);
//        JSONObject jsonTides = JsonConverter.readJsonFromUrl(tideUrl);


//        t = String.valueOf((Integer) json.getJSONObject("current").get("temperature"));
//        latit = (String) json.getJSONObject("location").get("lat");
//        longi = (String) json.getJSONObject("location").get("lon");
//        timeZ = (String) json.getJSONObject("location").get("localtime");
//        utc = (String) json.getJSONObject("location").get("utc_offset");
//        wSp = String.valueOf((Integer) json.getJSONObject("current").get("wind_speed"));
//        wD = (String) json.getJSONObject("current").get("wind_dir");
////        r = String.valueOf((Integer) json.getJSONObject("current").get("precip"));
//        press = String.valueOf((Integer)json.getJSONObject("current").get("pressure"));
//        humid = String.valueOf((Integer)json.getJSONObject("current").get("humidity"));
//        final String desc = String.valueOf(json.getJSONObject("current").get("weather_descriptions"));
//        final String sunS = String.valueOf(jsonForeCast.getJSONObject("forecast").getJSONObject("2020-09-25").getJSONObject("astro").get("sunset"));
//        final String sunR = String.valueOf(jsonForeCast.getJSONObject("forecast").getJSONObject("2020-09-25").getJSONObject("astro").get("sunrise"));
//        final String loc = (String) json.getJSONObject("request").get("query");
        t = String.valueOf(openWeather.getJSONObject("current").get("temp"));
        wSp = String.valueOf(openWeather.getJSONObject("current").get("wind_speed"));
        wD = String.valueOf(openWeather.getJSONObject("current").get("wind_deg"));
        r = String.valueOf(openWeather.getJSONObject("current").getJSONArray("weather"));
        press = String.valueOf(openWeather.getJSONObject("current").get("pressure"));
        humid = String.valueOf(openWeather.getJSONObject("current").get("humidity"));
        sunS = String.valueOf(openWeather.getJSONObject("current").get("sunset"));
        sunR = String.valueOf(openWeather.getJSONObject("current").get("sunrise"));
        arrayToObj();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

//                apiUrl = "http://api.weatherstack.com/current?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
//                forecastUrl =  "http://api.weatherstack.com/forecast?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;
////                tideUrl = "https://api.stormglass.io/v2/tide/extremes/point?lat="+latit+"&lng="+longi+"&start=2020-09-25&end=2020-09-25";
//                tideUrl = "https://api.stormglass.io/v2/tide/extremes/point?lat=52.0442&lng=7.9297&start=2020-09-24&end=2020-09-25?c602afae-ffe2-11ea-a78a-0242ac130002-c602b062-ffe2-11ea-a78a-0242ac130002";
//                openWeatherUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+latit+"&lon="+longi+"&exclude=weekly&appid="+openWeatherApi+"&units=metric";
                temp.setText(t);
                locationTV.setText(t);

//                time.setText(timeZ + " " + "("+ "+" + utc + ")");
//                lon.setText(longi);
//                lat.setText(latit);
                wDir.setText(wD+"°");
                wSpeed.setText(wSp);
//                rain.setText(r);
                presure.setText(press);
                humidity.setText(humid);
//                sunset.setText(String.valueOf(instantR));
//                sunrisee.setText(String.valueOf(instantS));
//                if(desc.matches("sunny")){
//                  //  weatherImg.setImageDrawable(R.drawable.ic_round_wb_sunny_72);
//                }
//                else{
//                   // weatherImg.setImageDrawable(R.drawable.ic_round_wb_cloudy_72);
//                }


            }
        });


    }

    //gets all of the objects within the json array to be used independently
    private void arrayToObj(){
        try {
            JSONArray weather = new JSONArray(r);

            //Loop the Array
            for(int i=0;i < weather.length();i++) {
                Log.e("Message","loop");
                HashMap<String, String> map = new HashMap<String, String>();
                final JSONObject e = weather.getJSONObject(i);
                map.put("id",  String.valueOf("id"));
                map.put("title", "Title :" + e.getString("main"));
                map.put("company", "Company : " +  e.getString("description"));
                map.put("category", "Category : " +  e.getString("icon"));

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            rain.setText(e.getString("description"));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
//                mylist.add(map);
            }
        } catch(JSONException e) {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
    }


    //gets the lat & lon from the users input (query)
    public void getLocationFromAddress(Context context) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;


        try {
            // May throw an IOException
            if(query!= null){
                address = coder.getFromLocationName(query, 5);
            }
            else{
                address = coder.getFromLocationName("Groningen", 5);
            }

            if (address == null) {
                return;
            }
            Address location = address.get(0);
            latit = String.valueOf(location.getLatitude());
            longi = String.valueOf(location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }
        Log.d("encoder", String.valueOf(latit + " " + longi));


    }

    private void getForecast(){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                apiUrl = "http://api.weatherstack.com/forecast?access_key=994a299da6ed0166887bd2617bef3f4a&query=" + query;

            }
        });
    }

}