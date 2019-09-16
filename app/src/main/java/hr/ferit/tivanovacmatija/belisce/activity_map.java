package hr.ferit.tivanovacmatija.belisce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static hr.ferit.tivanovacmatija.belisce.Util.Constants.ERROR_DIALOG_REQUEST;
import static hr.ferit.tivanovacmatija.belisce.Util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static hr.ferit.tivanovacmatija.belisce.Util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class activity_map extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private final LatLng mDefaultLocation = new LatLng(45.684502, 18.402357);
    private static final int DEFAULT_ZOOM = 15;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();
        mMap.setOnInfoWindowClickListener(this);

        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);

        LatLng belisceLoc = new LatLng(45.684502, 18.402357);
        LatLng gaterLoc = new LatLng(45.687462, 18.406967);
        LatLng pticeLoc = new LatLng(45.685030, 18.403122);
        LatLng palacaLoc = new LatLng(45.687643, 18.407619);
        LatLng fontanaLoc = new LatLng(45.686772, 18.406074);
        LatLng mlinLoc = new LatLng(45.685593, 18.411046);
        LatLng muzejLoc = new LatLng(45.687643, 18.407619);
        LatLng bijelaLoc = new LatLng(45.687311, 18.407365);
        LatLng zeleneLoc = new LatLng(45.684660, 18.403887);
        LatLng pekmezLoc = new LatLng(45.686660, 18.407805);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belisceLoc, 14));
        mMap.addMarker(new MarkerOptions().position(gaterLoc).title("Spomen-gater"));
        mMap.addMarker(new MarkerOptions().position(pticeLoc).title("Ptice"));
        mMap.addMarker(new MarkerOptions().position(palacaLoc).title("Palača Gutmann"));
        mMap.addMarker(new MarkerOptions().position(fontanaLoc).title("Fontana"));
        mMap.addMarker(new MarkerOptions().position(mlinLoc).title("Mlin"));
        mMap.addMarker(new MarkerOptions().position(muzejLoc).title("Muzej Belišće"));
        mMap.addMarker(new MarkerOptions().position(bijelaLoc).title("Bijela kuća Činovnički kasino"));
        mMap.addMarker(new MarkerOptions().position(zeleneLoc).title("Kompleks Zelene kuće"));
        mMap.addMarker(new MarkerOptions().position(pekmezLoc).title("Pekmez ulica"));

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        switch (marker.getTitle()) {
            case ("Spomen-gater"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Spomen-gater")));
                break;
            case ("Ptice"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Ptice")));
                break;
            case ("Palača Gutmann"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/_Pala~c~a_Gutmann")));
                break;
            case ("Fontana"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Fontana")));
                break;
            case ("Mlin"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Mlin")));
                break;
            case ("Muzej Belišće"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Muzej_Beli-s--c-e")));
                break;
            case ("Bijela kuća Činovnički kasino"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Bijela_ku-c-a_~C~inovni~c~ki_kasino_~12~Beamtenkasino~13~~2~~C~inovni~c~ki_dom")));
                break;
            case ("Kompleks Zelene kuće"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Kompleks_Zelene_ku-c-e")));
                break;
            case ("Pekmez ulica"):
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://belisce.2roam.today/hr/categories/Znamenitosti/sights/Kompleks_radni~c~kih_zgrada")));
                break;

        }

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
