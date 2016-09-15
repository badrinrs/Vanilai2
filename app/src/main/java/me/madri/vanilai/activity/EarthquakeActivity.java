package me.madri.vanilai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import madri.me.vanilai.R;
import me.madri.vanilai.adapter.EarthquakeListAdapter;
import me.madri.vanilai.beans.Earthquake;
import me.madri.vanilai.beans.EarthquakeProperties;
import me.madri.vanilai.fragment.DividerItemDecoration;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_earthquake);
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        Intent forecastIntent = getIntent();
        Gson gson = new Gson();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.earthquake_layout);
        relativeLayout.setBackgroundResource(forecastIntent.getIntExtra("background", R.drawable.clear));
        Earthquake earthquake = gson.fromJson(forecastIntent.getStringExtra("earthquake"), Earthquake.class);
        String city = forecastIntent.getStringExtra("city");

        TextView location = (TextView) findViewById(R.id.location);
        List<EarthquakeProperties> earthquakeProperties = earthquake.getEarthquakeProperties();
        Log.v("Earthquake", "Properties Size: "+earthquakeProperties.size());
        location.setText(city);
        TextView noEarthquakesFoundTextView = (TextView) findViewById(R.id.no_earthquakes_text);
        if(earthquakeProperties.size()!=0) {
            noEarthquakesFoundTextView.setVisibility(View.GONE);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_earthquake);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
            Log.v("Earthquake", "Place: " + earthquakeProperties.get(0).getEarthquakeInformation().getPlace());
            EarthquakeListAdapter earthquakeListAdapter = new EarthquakeListAdapter(this, earthquakeProperties);
            earthquakeListAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(earthquakeListAdapter);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
