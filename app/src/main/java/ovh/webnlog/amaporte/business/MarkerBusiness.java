package ovh.webnlog.amaporte.business;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.List;

import ovh.webnlog.amaporte.data.MarkerData;
import ovh.webnlog.amaporte.model.Amap;

public class MarkerBusiness implements IMarkerBusinessListener {

    private IMarkerBusinessListener listener;
    public List<Amap> amapList;

    public MarkerBusiness() {
        listener = this;
    }

    public void getAmap(Context context, LatLng latLng) {
        MarkerData data = new MarkerData();
        data.getAmap(context, latLng, listener);
    }

    @Override
    public void success(JSONObject response) {
        Gson gson = new Gson();
        amapList = gson.fromJson(String.valueOf(response), new TypeToken<List<Amap>>(){}.getType());
        Log.e("","");
    }

    @Override
    public void error(VolleyError error) {

    }
}
