package madri.me.vanilai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import madri.me.vanilai.R;
import madri.me.vanilai.adapter.EarthquakeListAdapter;
import madri.me.vanilai.beans.Earthquake;
import madri.me.vanilai.beans.EarthquakeInformation;
import madri.me.vanilai.beans.EarthquakeProperties;
import madri.me.vanilai.fragment.DividerItemDecoration;

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
        Earthquake earthquake = gson.fromJson(forecastIntent.getStringExtra("earthquake"), Earthquake.class);
        String city = forecastIntent.getStringExtra("city");

        TextView location = (TextView) findViewById(R.id.location);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_earthquake);

        location.setText(city);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        List<EarthquakeProperties> earthquakeProperties = earthquake.getEarthquakeProperties();
        Log.v("Earthquake", "Place: "+earthquakeProperties.get(0).getEarthquakeInformation().getPlace());
        EarthquakeListAdapter earthquakeListAdapter = new EarthquakeListAdapter(this, earthquakeProperties);
        earthquakeListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(earthquakeListAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
