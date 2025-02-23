package com.example.weather_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private SearchView cityy;
    private Button fetchWeatherButton;
    private TextView weatherTextView,temp;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "1dacb68720f11ecb8fda57abfa108794";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        cityy = findViewById(R.id.city);
//        fetchWeatherButton = findViewById(R.id.fetch);
        weatherTextView = findViewById(R.id.desc);
        temp=findViewById(R.id.temp1);

        cityy.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchweather(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



//        fetchWeatherButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String city = cityy.getText().toString().trim();
//                if (!city.isEmpty()) {
//                    fetchweather(city);
//                } else {
//                    weatherTextView.setText("Please enter a city name");
//                }
//            }
//        });
    }

    private void fetchweather(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherApi = retrofit.create(WeatherAPI.class);
        Call<WeatherResponse> call = weatherApi.getWeather(cityName, API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    String temperature = String.format("%.2f", weatherResponse.getMain().getTemp() - 273.15)+ "°C"; // Kelvin to Celsius
                    String description = "Description: " + weatherResponse.getWeather()[0].getDescription();
                    weatherTextView.setText(temperature );
                    temp.setText(description);
                } else {
                    weatherTextView.setText("Failed to fetch weather. Check city name.");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherTextView.setText("Error: " + t.getMessage());
            }
        });
    }
}
