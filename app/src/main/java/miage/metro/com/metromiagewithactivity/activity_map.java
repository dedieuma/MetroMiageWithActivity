package miage.metro.com.metromiagewithactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * Created by Andréas on 16/05/2018.
 */

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_map extends FragmentActivity implements OnMapReadyCallback {

private GoogleMap mMap;

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng test = new LatLng(21, 57);
        mMap.addMarker(new
                MarkerOptions().position(test).title("Test"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(test));
    }

}
