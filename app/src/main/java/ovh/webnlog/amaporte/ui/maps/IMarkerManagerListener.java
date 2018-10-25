package ovh.webnlog.amaporte.ui.maps;

import com.android.volley.VolleyError;

import java.util.List;

import ovh.webnlog.amaporte.model.Amap;

public interface IMarkerManagerListener {
    void success(List<Amap> amapList);
    void error(VolleyError error);
}
