package ovh.webnlog.amaporte.business;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IMarkerBusinessListener {
   void success(JSONObject response);
   void error(VolleyError error);
}
