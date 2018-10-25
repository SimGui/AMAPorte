package ovh.webnlog.amaporte.ui.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;

import java.util.ArrayList;
import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.business.MarkerBusiness;
import ovh.webnlog.amaporte.model.Amap;
import ovh.webnlog.amaporte.utils.Constant;

import static android.content.Context.LOCATION_SERVICE;

public class MapsManager implements OnMapReadyCallback, PermissionsListener {

    public MapView mapView;
    public MapboxMap map;
    public LatLng location;
    private LocalizationPlugin localizationPlugin;
    private LocationComponent locationComponent;
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

        setMapBoxLanguage(mapboxMap);
        setLocationComponent();
    }

    @SuppressLint("MissingPermission")
    public void setLocationComponent() {
        if (permissionDenied()) {
            initPermissionManager();
            return;
        }

        locationComponent = map.getLocationComponent();
        locationComponent.activateLocationComponent(context);
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setRenderMode(RenderMode.NORMAL);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.zoomWhileTracking(12, 2000);
    }

    public void disableLocationComponent() {
        locationComponent.setLocationComponentEnabled(false);
    }

    @SuppressLint("MissingPermission")
    public LatLng locateUser() {

        if(permissionDenied()) {
            initPermissionManager();
            return null;
        }

        if (haveToFindPosition()) {
            locationComponent.setLocationComponentEnabled(true);
            Toast.makeText(context, "Recherche de votre position...Veuillez patienter", Toast.LENGTH_LONG).show();
            return null;
        }

        Location location = locationComponent.getLastKnownLocation();

        if(location == null) {
            Toast.makeText(context, context.getString(R.string.unknow_position), Toast.LENGTH_LONG).show();
            return null;
        }

        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void moveCamera(LatLngBounds latLngBounds) {

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,50));

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

    //region PRIVATE METHODS

    private void initPermissionManager() {
        if(permissionDenied()) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions((Activity) context);
        }
    }

    private void setMapBoxLanguage(MapboxMap mapboxMap) {
        localizationPlugin = new LocalizationPlugin(mapView, mapboxMap);
        localizationPlugin.matchMapLanguageWithDeviceDefault();
    }

    private boolean permissionDenied() {
        return !PermissionsManager.areLocationPermissionsGranted(context);
    }

    @SuppressLint("MissingPermission")
    private boolean haveToFindPosition() {
        return !locationComponent.isLocationComponentEnabled() && !gpsIsTurnOff() && locationComponent.getLastKnownLocation() == null;
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

    private boolean gpsIsTurnOff() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //endregion
}
