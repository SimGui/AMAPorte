package ovh.webnlog.amaporte.data;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.webnlog.amaporte.model.Amap;

public class MarkerData {

    public void getAmap(Context context, LatLng latLng) {

        try {
            JSONObject job = new JSONObject();
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://api.amaporte.webnlog.ovh";
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, job,
                response -> {
                    Log.e("Success", "Success");
                //callback.onSuccess(parseSupporter(response.toString()));
                }, error -> {
                    Log.e("Error","Error");
                //callback.onFailure(error);
                });
            queue.add(req);
        } catch (Exception ex) {
            Log.e("Catch","Catch");
            //callback.onFailure(ex);
        }

//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        final String url = "https://api.amaporte.webnlog.ovh";
//        // prepare the Request
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // display response
//                        Log.d("Response", response.toString());
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.getMessage());
//                    }
//                }
//
//
//        );
//
//// add it to the RequestQueue
//        queue.add(getRequest);
    }
}
