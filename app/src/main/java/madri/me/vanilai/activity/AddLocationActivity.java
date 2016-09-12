package madri.me.vanilai.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import madri.me.vanilai.R;
import madri.me.vanilai.adapter.LocationListDialogAdapter;
import madri.me.vanilai.beans.AddressComponent;
import madri.me.vanilai.beans.AddressResult;
import madri.me.vanilai.beans.City;
import madri.me.vanilai.beans.GeocoderAddress;
import madri.me.vanilai.beans.Geometry;
import madri.me.vanilai.db.VanilaiSqlLiteHelper;
import madri.me.vanilai.fragment.DividerItemDecoration;
import madri.me.vanilai.service.AddressGeocodingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddLocationActivity extends AppCompatActivity {

    private static final String TAG = AddLocationActivity.class.getSimpleName();

    private List<City> mCities;
    private LocationListDialogAdapter mLocationListDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_location);
        mCities = new ArrayList<>();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.add_location_layout);
        relativeLayout.setBackgroundResource(R.drawable.clear);
        final VanilaiSqlLiteHelper helper = new VanilaiSqlLiteHelper(getApplicationContext());
        List<Map<String, Object>> citiesMapList = helper.getAllCities();
        for(Map<String, Object> cityMap: citiesMapList) {
            City city = new City();
            city.setCity(cityMap.get("city").toString());
            city.setLatitude(Double.parseDouble(cityMap.get("latitude").toString()));
            mCities.add(city);
        }
        RecyclerView locationListView = (RecyclerView) findViewById(R.id.location_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        locationListView.setLayoutManager(layoutManager);
        locationListView.setItemAnimator(new DefaultItemAnimator());
        locationListView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL));
        Log.v(TAG, locationListView.toString());
        mLocationListDialogAdapter = new LocationListDialogAdapter(getApplicationContext(), mCities, helper);
        mLocationListDialogAdapter.notifyDataSetChanged();
        locationListView.setAdapter(mLocationListDialogAdapter);


        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText cityEditText = (EditText) findViewById(R.id.city_text);
                EditText zipEditText = (EditText) findViewById(R.id.zip_code);
                Log.v(TAG, "City: " + cityEditText.getText().toString().isEmpty());
                Log.v(TAG, "Zip: " + zipEditText.getText().toString().isEmpty());
                if (cityEditText.getText().toString().isEmpty() && zipEditText.getText().toString().isEmpty()) {
                    Log.e(TAG, "Please provide atleast one!!!");
                } else {
                    StringBuilder locationBuilder = new StringBuilder();
                    if (!cityEditText.getText().toString().isEmpty()) {
                        locationBuilder.append(cityEditText.getText().toString()).append(" ");
                    }
                    if (!zipEditText.getText().toString().isEmpty()) {
                        locationBuilder.append(zipEditText.getText().toString());
                    }
                    String location = locationBuilder.toString();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    AddressGeocodingService geocodingService = retrofit.create(AddressGeocodingService.class);
                    Call<GeocoderAddress> geocoderAddressCall = geocodingService
                            .getAddress(location, getResources().getString(R.string.geocoding_api));
                    Log.v(TAG, "Geocoding Call: " + geocoderAddressCall.request().url().toString());
                    geocoderAddressCall.enqueue(new Callback<GeocoderAddress>() {
                        @Override
                        public void onResponse(Call<GeocoderAddress> call, Response<GeocoderAddress> response) {
                            GeocoderAddress geocoderAddress = response.body();
                            final List<City> cities = new ArrayList<>();
                            for (AddressResult addressResult : geocoderAddress.getAddressResultsList()) {
                                City city = new City();
                                for (AddressComponent addressComponent : addressResult.getAddressComponentList()) {
                                    if (addressComponent.getAddressTypes().contains("locality")) {
                                        city.setCity(addressComponent.getLongName());
                                    }
                                    if (addressComponent.getAddressTypes().contains("administrative_area_level_1")) {
                                        city.setState(addressComponent.getShortName());
                                    }
                                    if (addressComponent.getAddressTypes().contains("country")) {
                                        city.setCountry(addressComponent.getLongName());
                                    }
                                }
                                Geometry geometry = geocoderAddress.getAddressResultsList().get(0).getGeometry();
                                city.setLatitude(geometry.getGeoLocation().getLatitude());
                                city.setLongitude(geometry.getGeoLocation().getLongitude());
                                cities.add(city);
                            }
                            if (cities.size() > 1) {
                                Log.w(TAG, "More than 1 city in search result");
                                View view = AddLocationActivity.this.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                final List<String> cityOptions = new ArrayList<>();
                                for(City city: cities) {
                                    cityOptions.add(city.getCity()+", "+city.getState()+", "+city.getCountry());
                                }
                                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(AddLocationActivity.this)
                                        .autoDismiss(false).items(cityOptions).title("Choose from: ")
                                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        Log.v(TAG, "Choice: "+text);
                                        mCities.add(cities.get(cityOptions.indexOf(text)));
                                        helper.insertRecord(cities.get(0).getCity(), cities.get(0).getLatitude(), cities.get(0).getLongitude());
                                        dialog.dismiss();
                                        mLocationListDialogAdapter.notifyDataSetChanged();
                                        return true;
                                    }
                                }).positiveText("Choose");
                                dialogBuilder.show();
                            } else {
                                mCities.add(cities.get(0));
                                Log.v(TAG, "City Found: " + cities.get(0).getCity());
                                helper.insertRecord(cities.get(0).getCity(), cities.get(0).getLatitude(), cities.get(0).getLongitude());
                                View view = AddLocationActivity.this.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }

                                mLocationListDialogAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<GeocoderAddress> call, Throwable t) {
                            Log.e(TAG, "Please try again later!");
                        }
                    });
                }
            }
        });
    }
}
