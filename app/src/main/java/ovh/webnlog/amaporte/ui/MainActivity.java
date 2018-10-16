package ovh.webnlog.amaporte.ui;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;

import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.utils.Constant;


public class MainActivity extends NavigationActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineListener {

    private MapView mapView;
    private MapboxMap map;
    private LocalizationPlugin localizationPlugin;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationEngineProvider locationEngineProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissionManager();
        initMapBox(savedInstanceState);
        initNavigation();
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;

        setMapBoxLanguage(mapboxMap);
        setLocationEngine();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted) {
            setLocationEngine();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        if(!permissionDenied() && locationEngine != null) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }
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

        locationEngine.removeLocationEngineListener(this);
        locationEngine.removeLocationUpdates();
        locationEngine.deactivate();
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

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        if(!permissionDenied()) {
            locationEngine.requestLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        moveCamera(location);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("MissingPermission")
    private void setLocationEngine() {
        if(!permissionDenied()) {
            locationEngineProvider = new LocationEngineProvider(this);
            locationEngine = locationEngineProvider.obtainBestLocationEngineAvailable();
            locationEngine.addLocationEngineListener(this);
            locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
            locationEngine.activate();
        }
    }

    private void setMapBoxLanguage(MapboxMap mapboxMap) {
        localizationPlugin = new LocalizationPlugin(mapView, mapboxMap);
        localizationPlugin.matchMapLanguageWithDeviceDefault();
    }

    private void moveCamera(Location location) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(12)
                .build();

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 5000);

        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.ic_location_blue_dot);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .icon(icon));
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

    //endRegion
}