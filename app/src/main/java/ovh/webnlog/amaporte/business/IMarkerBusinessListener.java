package ovh.webnlog.amaporte.business;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface IMarkerBusinessListener {
   void success(JSONArray response);
   void error(VolleyError error);
}