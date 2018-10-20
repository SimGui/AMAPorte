package ovh.webnlog.amaporte.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.List;
import ovh.webnlog.amaporte.R;

public class SearchAdapter extends ArrayAdapter<CarmenFeature> {

    private int resId;

    public SearchAdapter(Context context, int resource, List<CarmenFeature> objects) {
        super(context, resource, objects);
        resId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resId,null);

            viewHolder = new SearchViewHolder();
            viewHolder.address = convertView.findViewById(R.id.addressTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchViewHolder) convertView.getTag();
        }

        CarmenFeature feature = getItem(position);

        viewHolder.address.setText(feature.placeName());

        return convertView;
    }
}
