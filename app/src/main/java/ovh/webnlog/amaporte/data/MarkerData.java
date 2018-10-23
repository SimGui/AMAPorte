package ovh.webnlog.amaporte.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ovh.webnlog.amaporte.R;

public class MarkerData {

    public void getAmap(Context context, LatLng latLng) {

        RequestQueue queue = Volley.newRequestQueue(context);

        final String url = "http://api.amaporte.webnlog.ovh";
// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }


        );

// add it to the RequestQueue
        queue.add(getRequest);

        /*String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.data);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;*/
    }
}
