package ovh.webnlog.amaporte.ui.maps;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ovh.webnlog.amaporte.business.IMarkerBusinessListener;
import ovh.webnlog.amaporte.business.MarkerBusiness;
import ovh.webnlog.amaporte.model.Amap;

public class MarkerManager implements IMarkerManagerListener {

    public List<Amap> amapList;

    public MarkerManager() {
        this.amapList = new ArrayList<>();
    }

    public void initMarkerManager(Context context) {
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

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listLatLng).build();
        return latLngBounds;
    }

    public void addMarkersOnMap(List<Amap> amapList, LatLng userLocation, MapboxMap map) {
        map.addMarker(new MarkerOptions()
        .position(userLocation)
        .title("Votre position"));

        for (Amap amap : amapList ) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(amap.latitude, amap.longitude))
                    .title(amap.title));
        }
    }

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
