package ovh.webnlog.amaporte.ui.maps;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.business.IMarkerBusinessListener;
import ovh.webnlog.amaporte.business.MarkerBusiness;
import ovh.webnlog.amaporte.model.Amap;
import ovh.webnlog.amaporte.ui.MainActivity;

public class MarkerManager implements IMarkerManagerListener {

    public List<Amap> amapList;
    private Context context;

    public MarkerManager() {
        this.amapList = new ArrayList<>();
    }

    public void initMarkerManager(Context context) {
        this.context = context;
        MarkerBusiness markerBusiness = new MarkerBusiness(this);
        markerBusiness.getAmap(context);
    }

    public List<Amap> filterMarkersForLocation(LatLng latLng) {
        return amapList;
    }

    public LatLngBounds getBoundsForMarkers(List<Amap> amapList, LatLng userLocation) {
        List<LatLng> listLatLng = new ArrayList<>();
        listLatLng.add(userLocation);

        for (Amap amap : amapList ) {
            LatLng latLngAmap = new LatLng(amap.latitude,amap.longitude);
            listLatLng.add(latLngAmap);
        }

        if(listLatLng.size() < 2) {
            return null;
        }

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listLatLng).build();
        return latLngBounds;
    }

    public void addMarkersOnMap(List<Amap> amapList, LatLng userLocation, MapboxMap map) {
        map.removeAnnotations();
        IconFactory iconFactory = IconFactory.getInstance(context);
        Icon icon = iconFactory.fromResource(R.drawable.ic_location_blue_dot);

        map.addMarker(new MarkerOptions()
        .position(userLocation)
        .icon(icon));

        for (Amap amap : amapList ) {
            if(calculDistance(amap, userLocation) <= 30) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(amap.latitude, amap.longitude))
                        .title(amap.title)
                        .snippet(amap.description));
            }
        }
    }

    //region Private methods

    private double calculDistance(Amap amap, LatLng userLocation) {
        //Degrees to Radians
        double radLatitudeAmap = Math.toRadians(amap.latitude);
        double radLongitudeAmap = Math.toRadians(amap.longitude);
        double radLatitudeUserLocation = Math.toRadians(userLocation.getLatitude());
        double radLongitudeUserLocation = Math.toRadians(userLocation.getLongitude());

        //Cos
        double cosLatitudeAmap = Math.cos(radLatitudeAmap);
        double cosLatitudeUserLocation = Math.cos(radLatitudeUserLocation);
        double cosLongitudeAmapAndUserLocation = Math.cos(radLongitudeAmap - radLongitudeUserLocation);

        //Sin
        double sinLatitudeAmap = Math.sin(radLatitudeAmap);
        double sinLatitudeUserLocation = Math.sin(radLatitudeUserLocation);

        //Multiplication
        double multiplicationCos = cosLatitudeAmap * cosLatitudeUserLocation * cosLongitudeAmapAndUserLocation;
        double multiplicationSin = sinLatitudeAmap * sinLatitudeUserLocation;
        double distanceInKm = 6371 * Math.acos(multiplicationCos + multiplicationSin);

        double round = Math.floor(distanceInKm);
        return round;
    }

    //endregion

    //region MarkerListener

    @Override
    public void success(List<Amap> amapList) {
        this.amapList = amapList;
    }

    @Override
    public void error(VolleyError error) {

    }

    //endregion
}
