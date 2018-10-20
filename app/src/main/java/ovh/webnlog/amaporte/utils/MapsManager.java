package ovh.webnlog.amaporte.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

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
import ovh.webnlog.amaporte.ui.MainActivity;

import static android.content.Context.LOCATION_SERVICE;

public class MapsManager implements OnMapReadyCallback, PermissionsListener {

    public MapView mapView;
    private MapboxMap map;
    private LocalizationPlugin localizationPlugin;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private Context context;
    private final static int CAMERA_MOVE_DURATION = 2000;

    public MapsManager(MapView mapView, Context context) {
        this.mapView = mapView;
        this.context = context;
    }

    public void initMapBox(Bundle savedInstanceState) {
        if(gpsTurnOff()) {
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

    private void setMapBoxLanguage(MapboxMap mapboxMap) {
        localizationPlugin = new LocalizationPlugin(mapView, mapboxMap);
        localizationPlugin.matchMapLanguageWithDeviceDefault();
    }

    @SuppressLint("MissingPermission")
    public void setLocationComponent() {
        if(permissionDenied()) {
            initPermissionManager();
            return;
        }

        locationComponent = map.getLocationComponent();
        locationComponent.activateLocationComponent(context);
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setRenderMode(RenderMode.NORMAL);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.zoomWhileTracking(12,2000);
    }

    private boolean gpsTurnOff() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    public void locateUser() {

        if(permissionDenied()) {
            initPermissionManager();
            return;
        }

        Location location = locationComponent.getLastKnownLocation();

        if(location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            moveCamera(latLng, CAMERA_MOVE_DURATION);
        }
    }

    public void moveCamera(LatLng latLng, int duration) {
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(12)
                .build();

        if(duration > 0) {
            map.easeCamera(CameraUpdateFactory.newCameraPosition(position), duration);
            return;
        }

        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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

    private void initPermissionManager() {
        if(permissionDenied()) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions((Activity) context);
        }
    }

    private boolean permissionDenied() {
        return !PermissionsManager.areLocationPermissionsGranted(context);
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
}
