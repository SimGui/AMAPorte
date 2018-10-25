package ovh.webnlog.amaporte.business;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ovh.webnlog.amaporte.repository.MarkerRepository;
import ovh.webnlog.amaporte.model.Amap;
import ovh.webnlog.amaporte.ui.maps.IMarkerManagerListener;

public class MarkerBusiness implements IMarkerBusinessListener {

    private IMarkerManagerListener listener;
    public List<Amap> amapList;

    public MarkerBusiness(IMarkerManagerListener listener) {
        this.listener = listener;
        amapList = new ArrayList<>();
    }

    public void getAmap(Context context) {
        MarkerRepository data = new MarkerRepository(this);
        data.getAmap(context);
    }

    @Override
    public void success(JSONArray response) {
        Gson gson = new Gson();
        String stringResponse = response.toString();
        Type listType = new TypeToken<List<Amap>>(){}.getType();
        amapList = gson.fromJson(stringResponse, listType);

        listener.success(amapList);
    }

    @Override
    public void error(VolleyError error) {

    }
}
