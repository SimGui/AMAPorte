package ovh.webnlog.amaporte.data;

import android.content.Context;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.io.InputStream;

import ovh.webnlog.amaporte.R;

public class MarkerData {

    public String getAmap(Context context, LatLng latLng) {
        String json = null;
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
        return json;
    }
}
