package ovh.webnlog.amaporte.business;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import ovh.webnlog.amaporte.data.MarkerData;
import ovh.webnlog.amaporte.model.Amap;

public class MarkerBusiness {

    public List<Amap> getAmap(Context context, LatLng latLng) {
        MarkerData data = new MarkerData();
        data.getAmap(context, latLng);

        String jsonAmap = "";
        Gson gson = new Gson();
        List<Amap> listAmap = gson.fromJson(jsonAmap, new TypeToken<List<Amap>>(){}.getType());

        return listAmap;
    }
}
