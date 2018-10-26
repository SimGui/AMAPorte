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

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listLatLng).build();
        return latLngBounds;
    }

    public void addMarkersOnMap(List<Amap> amapList, LatLng userLocation, MapboxMap map) {
        // Create an Icon object for the marker to use
        IconFactory iconFactory = IconFactory.getInstance(context);
        Icon icon = iconFactory.fromResource(R.drawable.ic_location_small_white_dot);

        map.addMarker(new MarkerOptions()
        .position(userLocation)
        .icon(icon)
        .title("Votre position"));

        for (Amap amap : amapList ) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(amap.latitude, amap.longitude))
                    .title(amap.title)
                    .snippet(amap.description));
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
