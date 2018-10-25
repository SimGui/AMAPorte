package ovh.webnlog.amaporte.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import ovh.webnlog.amaporte.business.IMarkerBusinessListener;

public class MarkerRepository {
    public IMarkerBusinessListener listener;

    public MarkerRepository(IMarkerBusinessListener listener) {
        this.listener = listener;
    }

    public void getAmap(Context context) {

        JSONArray job = new JSONArray();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.amaporte.webnlog.ovh";
        JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, job,
            response -> {
                listener.success(response);
            }, error -> {
                listener.error(error);
            });
        queue.add(req);
    }
}
