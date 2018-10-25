package ovh.webnlog.amaporte.ui.search;

import android.content.ComponentName;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import ovh.webnlog.amaporte.R;
import ovh.webnlog.amaporte.ui.MainActivity;
import ovh.webnlog.amaporte.utils.Constant;
import ovh.webnlog.amaporte.ui.maps.MapsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultManager implements SearchView.OnQueryTextListener, Callback<GeocodingResponse>, AdapterView.OnItemClickListener, SearchView.OnCloseListener {

    private ListView searchResultListView;
    private List<CarmenFeature> cityList;
    private Context context;
    private MapsManager mapsManager;

    public SearchResultManager(ListView searchResultListView, Context context, MapsManager mapsManager) {
        this.searchResultListView = searchResultListView;
        this.context = context;
        this.mapsManager = mapsManager;
    }

    public void initSearchManager(Menu menu, MenuInflater inflater, ComponentName componentName){

        inflater.inflate(R.menu.menu_main, menu);
        android.app.SearchManager searchManager = (android.app.SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(Constant.MAPBOX_TOKEN)
                .query(s)
                .build();
        mapboxGeocoding.enqueueCall(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
        List<CarmenFeature> results = response.body().features();
        if (results.size() > 0) {
            cityList = results;
            SearchAdapter adapter = new SearchAdapter(context, R.layout.search_item, cityList);
            searchResultListView.setOnItemClickListener(this);
            searchResultListView.setAdapter(adapter);
            searchResultListView.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchResultListView.getWindowToken(), 0);
        }
    }

    @Override
    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
        Toast.makeText(context, context.getString(R.string.error_geocoder_response), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mapsManager.disableLocationComponent();
        CarmenFeature feature = cityList.get(position);
        LatLng latLng = new LatLng(feature.center().latitude(), feature.center().longitude());
        MainActivity mainActivity = (MainActivity) context;

        mainActivity.locateUser(latLng);

        searchResultListView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onClose() {
        searchResultListView.setVisibility(View.GONE);
        return false;
    }

}
