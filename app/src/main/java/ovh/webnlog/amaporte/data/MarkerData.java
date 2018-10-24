package ovh.webnlog.amaporte.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import ovh.webnlog.amaporte.business.IMarkerBusinessListener;

public class MarkerData {

    public void getAmap(Context context, LatLng latLng, IMarkerBusinessListener iMarkerBusinessListener) {

        JSONObject job = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.amaporte.webnlog.ovh";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, job,
            response -> {
                iMarkerBusinessListener.success(response);
            }, error -> {
                iMarkerBusinessListener.error(error);
            });
        queue.add(req);
    }
}
