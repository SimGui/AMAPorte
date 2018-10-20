package ovh.webnlog.amaporte.ui;

import android.annotation.SuppressLint;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.ui.search.SearchAdapter;
import ovh.webnlog.amaporte.utils.Constant;
import ovh.webnlog.amaporte.utils.MapsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, Callback<GeocodingResponse>, AdapterView.OnItemClickListener, SearchView.OnCloseListener {

    private MapsManager mapsManager;
    private ListView searchResultListView;
    private List<CarmenFeature> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchResultListView = findViewById(R.id.searchResultListView);

        initMapManager(savedInstanceState);
        initNavigation();
    }

    private void initMapManager(Bundle savedInstanceState) {
         MapView mapView = findViewById(R.id.mapView);

        mapsManager = new MapsManager(mapView, this);
        mapsManager.initMapBox(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapsManager.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapsManager.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapsManager.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsManager.mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapsManager.mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapsManager.mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapsManager.mapView.onSaveInstanceState(outState);
    }

    //region PRIVATE METHODS

    private void initNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void locateUser(View view) {
        mapsManager.locateUser();
    }

    public void addAmap(View view) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(Constant.MAPBOX_TOKEN)
                .query(s)
                .build();
        mapboxGeocoding.enqueueCall(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
        List<CarmenFeature> results = response.body().features();
        if (results.size() > 0) {
            cityList = results;
            SearchAdapter adapter = new SearchAdapter(MainActivity.this, R.layout.search_item, cityList);
            searchResultListView.setOnItemClickListener(this);
            searchResultListView.setAdapter(adapter);
            searchResultListView.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchResultListView.getWindowToken(), 0);
        }
    }

    @Override
    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
        Toast.makeText(this, getString(R.string.error_geocoder_response), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mapsManager.disableLocationComponent();
        CarmenFeature feature = cityList.get(position);
        LatLng latLng = new LatLng(feature.center().latitude(), feature.center().longitude());
        mapsManager.moveCamera(latLng, 3000);
        searchResultListView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onClose() {
        searchResultListView.setVisibility(View.GONE);
        return false;
    }

    //endRegion
}