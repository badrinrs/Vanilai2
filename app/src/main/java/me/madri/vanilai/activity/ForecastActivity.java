package me.madri.vanilai.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import madri.me.vanilai.R;
import me.madri.vanilai.adapter.HourlyForecastListAdapter;
import me.madri.vanilai.adapter.WeeklyForecastAdapter;
import me.madri.vanilai.beans.Forecast;
import me.madri.vanilai.beans.HourlyDetailForecast;
import me.madri.vanilai.beans.WeeklyDetailForecast;
import me.madri.vanilai.fragment.DividerItemDecoration;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forecast);
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        Intent forecastIntent = getIntent();
        Gson gson = new Gson();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.forecast_layout);
        relativeLayout.setBackgroundResource(forecastIntent.getIntExtra("background", R.drawable.clear));
        Forecast forecast = gson.fromJson(forecastIntent.getStringExtra("forecast"), Forecast.class);
        String city = forecastIntent.getStringExtra("city");
        String forecastType = forecastIntent.getStringExtra("forecastType");
        TextView location = (TextView) findViewById(R.id.location);
        TextView detailTitle = (TextView) findViewById(R.id.detail_title);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        location.setText(city);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        if(forecastType.equalsIgnoreCase("hourly")) {
            detailTitle.setText("Today's Forecast");
            List<HourlyDetailForecast> hourlyDetailForecasts = forecast.getHourlyForecast().getHourlyDetailForecastList();
            HourlyForecastListAdapter hourlyForecastAdapter = new HourlyForecastListAdapter(this, hourlyDetailForecasts);
            hourlyForecastAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(hourlyForecastAdapter);
        } else {
            detailTitle.setText("This Week's Forecast");
            List<WeeklyDetailForecast> weeklyDetailForecasts = forecast.getWeeklyForecast().getWeeklyDetailForecastList();
            WeeklyForecastAdapter weeklyForecastAdapter = new WeeklyForecastAdapter(this, weeklyDetailForecasts);
            weeklyForecastAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(weeklyForecastAdapter);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
