package ovh.webnlog.amaporte.ui.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;

import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.ui.MainActivity;
import ovh.webnlog.amaporte.utils.Constant;

import static android.content.Context.LOCATION_SERVICE;

public class MapsManager implements OnMapReadyCallback, PermissionsListener, LocationListener {

    public MapView mapView;
    public MapboxMap map;
    private LatLng userLocation;
    LocationManager locationManager;
    private LocalizationPlugin localizationPlugin;
    private PermissionsManager permissionsManager;
    private Context context;

    public MapsManager(MapView mapView, Context context) {
        this.mapView = mapView;
        this.context = context;
    }

    public void initMapBox(Bundle savedInstanceState) {
        if(gpsIsTurnOff()) {
            showGPSDisabledAlertToUser();
        }

        Mapbox.getInstance(context, Constant.MAPBOX_TOKEN);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;

        initLocationManager();
        defineUserLocation();
        setMapBoxLanguage(mapboxMap);
    }

    @SuppressLint("MissingPermission")
    public void defineUserLocation() {
        if (permissionDenied()) {
            initPermissionManager();
            return;
        }

        Boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location networkLocation = null;
        Location gpsLocation = null;

        if (gps_enabled)
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(gpsLocation == null && networkLocation == null) {
            Toast.makeText(context, context.getString(R.string.unknow_position), Toast.LENGTH_LONG).show();
            return;
        }

        setUserLocation(gpsLocation, networkLocation);

        MainActivity mainActivity = (MainActivity) context;
        mainActivity.locateUser(userLocation);
    }

    @SuppressLint("MissingPermission")
    public LatLng getUserLocation() {

        if(permissionDenied()) {
            initPermissionManager();
            return null;
        }

        if(userLocation == null) {
            Toast.makeText(context, context.getString(R.string.unknow_position), Toast.LENGTH_LONG).show();
            return null;
        }

        return userLocation;
    }

    public void moveCamera(LatLngBounds latLngBounds) {
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,300));
    }

    public void moveCamera(LatLng latLng) {
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng) // Sets the new camera position
                .zoom(10) // Sets the zoom to level 10
                .build(); // Builds the CameraPosition object from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),4000);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted) {
            defineUserLocation();
        }
    }

    //region PRIVATE METHODS

    private void initPermissionManager() {
        if(permissionDenied()) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions((Activity) context);
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5000, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5000, this);
    }

    private void setMapBoxLanguage(MapboxMap mapboxMap) {
        localizationPlugin = new LocalizationPlugin(mapView, mapboxMap);
        localizationPlugin.matchMapLanguageWithDeviceDefault();
    }

    private boolean permissionDenied() {
        return !PermissionsManager.areLocationPermissionsGranted(context);
    }

    private boolean gpsIsTurnOff() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.ask_gps_activation)
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            dialog.cancel();
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void setUserLocation(Location gpsLocation, Location networkLocation) {
        Location finalLocation = null;
        if (gpsLocation != null && networkLocation != null) {
            if (gpsLocation.getAccuracy() > networkLocation.getAccuracy()) {
                finalLocation = gpsLocation;
            } else {
                finalLocation = networkLocation;
            }
        } else {
            if (gpsLocation != null) {
                finalLocation = gpsLocation;
            } else if (networkLocation != null) {
                finalLocation = networkLocation;
            }
        }

        this.userLocation = new LatLng(finalLocation.getLatitude(), finalLocation.getLongitude());
    }

    //endregion

    //region LocationEngineListener methods

    @Override
    public void onLocationChanged(Location location) {
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //endregion
}
