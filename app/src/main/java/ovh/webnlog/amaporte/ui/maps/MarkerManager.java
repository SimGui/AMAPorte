package ovh.webnlog.amaporte.ui.maps;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mapbox.mapboxsdk.annotations.Marker;

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

    @Override
    public void success(List<Amap> amapList) {
        this.amapList = amapList;
    }

    @Override
    public void error(VolleyError error) {

    }
}
