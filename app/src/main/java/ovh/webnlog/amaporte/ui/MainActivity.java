package ovh.webnlog.amaporte.ui;

import android.content.ComponentName;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.mapbox.mapboxsdk.maps.MapView;

import ovh.webnlog.amaporte.R;

import ovh.webnlog.amaporte.business.MarkerBusiness;
import ovh.webnlog.amaporte.data.MarkerData;
import ovh.webnlog.amaporte.ui.search.SearchResultManager;
import ovh.webnlog.amaporte.ui.maps.MapsManager;

public class MainActivity extends AppCompatActivity {

    private MapsManager mapsManager;
    private ListView searchResultListView;
    private SearchResultManager searchResultManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MarkerBusiness markerBusiness = new MarkerBusiness();
        markerBusiness.getAmap(this,null);
        initMapManager(savedInstanceState);
        initSearchManager();
        initNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        ComponentName componentName = getComponentName();
        searchResultManager.initSearchManager(menu, inflater, componentName);

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

    //region Private methods
    private void initNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initMapManager(Bundle savedInstanceState) {
        MapView mapView = findViewById(R.id.mapView);

        mapsManager = new MapsManager(mapView, this);
        mapsManager.initMapBox(savedInstanceState);
    }

    private void initSearchManager() {
        searchResultListView = findViewById(R.id.searchResultListView);
        searchResultManager = new SearchResultManager(searchResultListView,this,mapsManager);
    }

    //endregion

    //region Activity override methods

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

    //endregion
}