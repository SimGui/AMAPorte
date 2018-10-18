package ovh.webnlog.amaporte.ui;

import android.annotation.SuppressLint;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;

import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.utils.Constant;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineListener {

    private MapView mapView;
    private MapboxMap map;
    private LocalizationPlugin localizationPlugin;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissionManager();
        initMapBox(savedInstanceState);
        initNavigation();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        setMapBoxLanguage(mapboxMap);
        setLocationComponent();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted) {
            setLocationComponent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //region PRIVATE METHODS

    private void initMapBox(Bundle savedInstanceState) {
        Mapbox.getInstance(this, Constant.MAPBOX_TOKEN);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

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

    private void setMapBoxLanguage(MapboxMap mapboxMap) {
        localizationPlugin = new LocalizationPlugin(mapView, mapboxMap);
        localizationPlugin.matchMapLanguageWithDeviceDefault();
    }

    @SuppressLint("MissingPermission")
    private void setLocationComponent() {
        if(permissionDenied()) {
            initPermissionManager();
            return;
        }

        locationComponent = map.getLocationComponent();
        locationComponent.activateLocationComponent(this);
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setRenderMode(RenderMode.NORMAL);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.getLocationEngine().addLocationEngineListener(this);
    }

    private void initPermissionManager() {
        if(permissionDenied()) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private boolean permissionDenied() {
        return !PermissionsManager.areLocationPermissionsGranted(this);
    }

    @SuppressLint("MissingPermission")
    private void moveCamera() {

        if(permissionDenied()) {
            initPermissionManager();
            return;
        }

        Location location = locationComponent.getLastKnownLocation();

        if(location != null) {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(12)
                    .build();

            map.easeCamera(CameraUpdateFactory.newCameraPosition(position), 4000);
        }
    }

    @SuppressLint("MissingPermission")
    public void locateUser(View view) {
        moveCamera();
    }

    public void addAmap(View view) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onLocationChanged(Location location) {
        moveCamera();
    }

    //endRegion
}