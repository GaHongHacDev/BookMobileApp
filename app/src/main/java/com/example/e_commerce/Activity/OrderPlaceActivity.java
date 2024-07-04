package com.example.e_commerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.e_commerce.Adapter.PlacesAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.example.e_commerce.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class OrderPlaceActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        RoutingListener,
        ActivityCompat.OnRequestPermissionsResultCallback  {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Location destinationLocation = null;
    private SearchView mapSearchView;
    Button btnContinue;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private PlacesAdapter placesAdapter;
    private ProgressBar progressBar;
    private GoogleMap gMap;
    protected LatLng start = null;
    private List<Polyline> polylines=null;
    protected LatLng end = null;
    Location currentLocation;
    Address savedLocation;
    FusedLocationProviderClient fusedClient;
    LatLng storeLocation = new LatLng(10.8411276, 106.809883);
    TextView tvDistance;
    float distance = 0;
    private static final int  REQUEST_CODE = 101;
//    FrameLayout map;
    private static final String TAG = "activity_order_place";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

        binding();
    }

    private void binding() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.order_map);
        String apiKey = "AIzaSyAoyv7HCpdrqPILu9KcSccUzDYJp6NYhUY";
        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey);
        }
        tvDistance = findViewById(R.id.tvDistance);
        placesClient = Places.createClient(this);
        sessionToken = AutocompleteSessionToken.newInstance();
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        mapSearchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);

        mapFragment.getMapAsync(this);
        placesAdapter = new PlacesAdapter(this);
        ListView listPlaces = findViewById(R.id.listPlaces);
        listPlaces.setVisibility(View.GONE);
        btnContinue = findViewById(R.id.btnContinue);
        getLocation();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set intent to go to next activity
                Intent intent = new Intent(OrderPlaceActivity.this, OrderActivity.class);
                intent.putExtra("savedLocation", savedLocation);
                intent.putExtra("distance", distance);
                startActivity(intent);
            }
        });

        listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (placesAdapter.getCount() > 0){
                    detailPlace(placesAdapter.predictions.get(position).getPlaceId());

                    ListView listPlaces = findViewById(R.id.listPlaces);
                    listPlaces.setVisibility(View.GONE);
                }
            }
        });
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    searchPlaces(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task =  fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment  mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.order_map);
                    mapFragment.getMapAsync(OrderPlaceActivity.this);
                }
            }
        });
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }


        gMap.setMyLocationEnabled(true);
        gMap.addMarker(new MarkerOptions().position(storeLocation).title("Cửa hàng").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        if (currentLocation == null) return;
        getDistance(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        LatLng latLng = new LatLng(storeLocation.latitude, storeLocation.longitude);

        gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //start route finding
                clearMarkers();

                gMap.addMarker(new MarkerOptions().position(latLng).title("Điểm đến"));
                getDistance(latLng);
                getSavedLocation(latLng);
//                Findroutes(start, end);
            }
        });

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap = googleMap;
        getMyLocation();
    }
    private void clearMarkers() {
        gMap.clear();
        gMap.addMarker(new MarkerOptions().position(storeLocation).title("Cửa hàng").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }
    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
//            Toast.makeText(OrderPlace.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

//            Routing routing = new Routing.Builder()
//                    .travelMode(AbstractRouting.TravelMode.DRIVING)
//                    .withListener(this)
//                    .alternativeRoutes(true)
//                    .waypoints(Start, End)
//                    .key("AIzaSyDGVnULcrCHxUKUhh7nWEuQn-XrrEfd9Bg")  //also define your api key here.
//                    .build();
//            routing.execute();
        }
    }
    private void searchPlaces(String query){
        progressBar.setVisibility(View.VISIBLE);
        final LocationBias bias = RectangularBounds.newInstance(
                new LatLng(10.364637, 105.427209),
                new LatLng(14.058324, 108.277199)
        );
        LatLng latLng =  new LatLng(10.364637, 105.427209);

        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setQuery(query)
                .setLocationBias(bias)
                .setCountries("VN")
                .build();

        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                List<AutocompletePrediction> predictions = findAutocompletePredictionsResponse.getAutocompletePredictions();
                placesAdapter.setPredictions(predictions);
                ListView listPlaces = findViewById(R.id.listPlaces);
                listPlaces.setAdapter(placesAdapter);
                listPlaces.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e("MainActivity", "Place not found: " + apiException.getStatusCode());
                }

            }
        });
    }

    private void detailPlace(String placeId){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                progressDialog.dismiss();
                Place place = fetchPlaceResponse.getPlace();
                LatLng latLng = place.getLatLng();

                if (latLng != null) {
                    mapSearchView.setQuery(place.getAddress(), false);
                    clearMarkers();
                    gMap.addMarker(new MarkerOptions().position(latLng).title(place.getAddress()));
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    placesAdapter.setPredictions(new ArrayList<AutocompletePrediction>());
                    getDistance(latLng);
                    getSavedLocation(latLng);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                if (e instanceof ApiException) {
                    final ApiException apiException = (ApiException) e;
                    Log.e("MainActivity", "Place not found: " + e.getMessage());
                    final int statusCode = apiException.getStatusCode();
                }
            }
        });
    }
    private void getSavedLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(OrderPlaceActivity.this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addresses.get(0);
            savedLocation = address;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getDistance(LatLng latLngLocation) {
        float[]  results = new float[10];
        Location.distanceBetween(
                latLngLocation.latitude,
                latLngLocation.longitude,
                storeLocation.latitude,
                storeLocation.longitude,
                results
        );

        tvDistance.setText("Khoảng cách: " + String.format("%.2f",  results[0]/1000) + " km");
        distance = results[0]/1000;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
            else {
                Toast.makeText(this,"Allow Location permission to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}