package ovh.webnlog.amaporte.business;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.List;

import ovh.webnlog.amaporte.model.Amap;

public interface IMarkerBusinessListener {
   void success(JSONObject response);
   void error(VolleyError error);
}