package ovh.webnlog.amaporte.ui;

import android.annotation.SuppressLint;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.utils.MapsManager;


public class MainActivity extends AppCompatActivity  {

    private MapsManager mapsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void locateUser(View view) {
        mapsManager.moveCamera();
    }

    public void addAmap(View view) {

    }

    //endRegion
}