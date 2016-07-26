package madri.me.vanilai.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import madri.me.vanilai.R;
import madri.me.vanilai.adapter.HourlyForecastListAdapter;
import madri.me.vanilai.adapter.WeeklyForecastAdapter;
import madri.me.vanilai.beans.Forecast;
import madri.me.vanilai.beans.HourlyDetailForecast;
import madri.me.vanilai.beans.WeeklyDetailForecast;
import madri.me.vanilai.fragment.DividerItemDecoration;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent forecastIntent = getIntent();
        Gson gson = new Gson();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
