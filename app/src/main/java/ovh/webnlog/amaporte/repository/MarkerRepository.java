package ovh.webnlog.amaporte.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import ovh.webnlog.amaporte.business.IMarkerBusinessListener;

public class MarkerRepository {
    public IMarkerBusinessListener listener;

    public MarkerRepository(IMarkerBusinessListener listener) {
        this.listener = listener;
    }

    public void getAmap(Context context) {

        JSONObject job = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.amaporte.webnlog.ovh";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, job,
            response -> {
                listener.success(response);
            }, error -> {
                listener.error(error);
            });
        queue.add(req);
    }
}
