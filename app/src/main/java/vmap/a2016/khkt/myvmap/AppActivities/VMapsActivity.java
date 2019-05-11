package vmap.a2016.khkt.myvmap.AppActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import vmap.a2016.khkt.myvmap.DatabaseHelper.Database;
import vmap.a2016.khkt.myvmap.Modules.DirectionFinder;
import vmap.a2016.khkt.myvmap.Modules.DirectionFinderListener;
import vmap.a2016.khkt.myvmap.Modules.Route;
import vmap.a2016.khkt.myvmap.R;

import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.GetMarker;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.loadPlaces;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.onDirectButtonClick;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.onHistoryButtonClick;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.onImageButtonClick;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.onLocationButtonClick;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.onSpecialityButtonClick;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.onVideoButtonClick;
import static vmap.a2016.khkt.myvmap.DatabaseHelper.DatabaseHelper.setVisibleMarker;
import static vmap.a2016.khkt.myvmap.R.id.map;
public class VMapsActivity extends FragmentActivity implements DirectionFinderListener, OnCameraChangeListener , GoogleMap.OnMarkerClickListener, OnMapReadyCallback, View.OnClickListener {
    final String DATABASE_NAME = "vmapsdb.sqlite";
    private GoogleMap mMap;
    private float currentZoom = -1;
    ImageButton btn_1, btn_2, btn_3, btn_4, btn_5, btn_6;
    TextView searchText;
    ImageButton gasBtn, coffeeBtn, motelBtn, marketBtn, restaurantBtn, hospitalBtn;
    ImageButton cancelBtn;
    String thisMarker = "";
    private ArrayList<Marker> MarkerList;
    ArrayList<Polyline> myline;
    ImageButton wayButton;
    private ProgressDialog progressDialog;
    private Marker ImHere;
    private int myLevel;
    LatLng thisPlace;
    boolean isDirecting;
    private String etOrigin;
    private String etDestination;
    LatLng myLocation;
    boolean firstTime = true;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleApiClient client;
    TextView DistanceText;
    String iconPath = "Icons/Mylocation_Icon-2.png";
    ArrayList<Marker> gasList, hospitalList, coffeeList, marketList, motelList, restaurantList;
    boolean gasBl, hospitalBl, coffeeBl, marketBl, motelBl, restaurantBl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmaps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        getLocation();
        addControls();
        ImageButton searchBtn = (ImageButton) findViewById(R.id.searchbtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buttonVisible(View.INVISIBLE);
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                        .build(VMapsActivity.this);
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void getLocation() {
        LocationManager locationManager;
        LocationListener listener;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                location.getBearing();
                if(myLocation != null && firstTime)
                {mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
                    ImHere = mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("Tôi đang ở đây!")
                            .icon(BitmapDescriptorFactory.fromAsset(iconPath)));
                    ImHere.setTag(0);
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    mMap.getUiSettings().setZoomGesturesEnabled(true);
                    buttonVisible(View.INVISIBLE);
                    firstTime = false;}
                if(isDirecting){
                    ImHere.setPosition(myLocation);
                }
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }
            locationManager.requestLocationUpdates("gps",0, 5, listener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                thisPlace = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thisPlace, 16));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.       
            }
        }
        if(requestCode == 2) {
            if (resultCode == RESULT_OK) {
                etOrigin = data.getStringExtra("Origin");
                etDestination = data.getStringExtra("Destination");
                sendRequest();
            }
        }
    }
    private void addControls()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int CSize = getCS(size);
        if(CSize <= 480){
            iconPath = "Icons/Mylocation_Icon-2.png";
        }
        gasList = new ArrayList<>();
        hospitalList= new ArrayList<>();
        coffeeList = new ArrayList<>();
        marketList = new ArrayList<>();
        motelList = new ArrayList<>();
        DistanceText = (TextView) findViewById(R.id.distancetext);
        myLocation = null;
        MarkerList = new ArrayList<>();
        wayButton = (ImageButton) findViewById(R.id.imageButton);
        btn_1 = (ImageButton)findViewById(R.id.lBtn);
        btn_2 = (ImageButton)findViewById(R.id.hBtn);
        btn_3 = (ImageButton)findViewById(R.id.findingBtn);
        btn_4 = (ImageButton)findViewById(R.id.imgBtn);
        btn_5 = (ImageButton)findViewById(R.id.videoBtn);
        btn_6 = (ImageButton)findViewById(R.id.specialities);
        wayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VMapsActivity.this, direct_activity.class);
                startActivityForResult(intent, 2);
            }
        });
        cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myline != null) {
                    DistanceText.setText("");
                    buttonSearchVisible(View.INVISIBLE);
                    cancelBtn.setVisibility(View.INVISIBLE);
                    isDirecting = false;
                    if (myline != null) {
                        for (Polyline polyline:myline ) {
                            polyline.remove();
                        }
                        myline.clear();
            }
        }}});
        searchText = (TextView) findViewById(R.id.textSearch);
        gasBtn =(ImageButton) findViewById(R.id.gasBtn);
        coffeeBtn = (ImageButton) findViewById(R.id.coffeeBtn);
        motelBtn = (ImageButton) findViewById(R.id.motelBtn);
        marketBtn =(ImageButton) findViewById(R.id.marketBtn);
        hospitalBtn =(ImageButton) findViewById(R.id.hospitalBtn);
        restaurantBtn = (ImageButton) findViewById(R.id.restaurantBtn);
        btnControl();
    }
    private void btnControl(){
        gasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gasBl){
                    gasList = loadPlaces(VMapsActivity.this, "GasStation", myline, mMap,"Icons/gasstation_icon_mini.png",2);
                    gasBl = false;
                } else {
                    for (int i =0 ; i < gasList.size(); i++) gasList.get(i).remove();
                    gasList.clear();
                    gasBl = true;
                    }
        }
        });
        motelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(motelBl){
                    motelList = loadPlaces(VMapsActivity.this, "Hotel", myline, mMap,"Icons/motel_icon_mini.png",3);
                    motelBl = false;
                } else {
                    for (int i =0 ; i < motelList.size(); i++) motelList.get(i).remove();
                    motelList.clear();
                    motelBl = false;}
            }
        });
        marketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marketBl){
                    marketList = loadPlaces(VMapsActivity.this, "Market", myline, mMap,"Icons/supermarket_icon_mini.png",4);
                    marketBl = false;
                } else {
                    for (int i =0 ; i < marketList.size(); i++) marketList.get(i).remove();
                    marketList.clear();
                    marketBl = true;}
            }
        });
        hospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hospitalBl){
                    hospitalList = loadPlaces(VMapsActivity.this, "Hospital", myline, mMap,"Icons/hospital_icon_mini.png",5);
                    hospitalBl = false;
                } else {
                    for (int i =0 ; i < hospitalList.size(); i++) hospitalList.get(i).remove();
                    hospitalList.clear();
                    hospitalBl = true;}
            }
        });
        coffeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coffeeBl){
                    coffeeList = loadPlaces(VMapsActivity.this, "Coffee", myline, mMap,"Icons/cafe_icon_mini.png",6);
                    coffeeBl = false;
                } else {
                    for (int i = 0 ; i < coffeeList.size(); i++) coffeeList.get(i).remove();
                    coffeeList.clear();
                    coffeeBl = true;}
            }
        });
        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restaurantBl){
                    restaurantList = loadPlaces(VMapsActivity.this, "Restaurant", myline, mMap,"Icons/restaurant_icon_mini.png",7);
                    restaurantBl = false;
                } else {
                    for (int i =0 ; i < restaurantList.size(); i++) restaurantList.get(i).remove();
                    restaurantList.clear();
                    restaurantBl = true;
                }
            }
        });
    }
    private void buttonSearchVisible(int visible) {
        searchText.setVisibility(visible);
        gasBtn.setVisibility(visible);
        coffeeBtn.setVisibility(visible);
        hospitalBtn.setVisibility(visible);
        motelBtn.setVisibility(visible);
        marketBtn.setVisibility(visible);
        restaurantBtn.setVisibility(visible);
        if (visible == View.VISIBLE) {
            gasBl = true;
            hospitalBl = true;
            coffeeBl = true;
            marketBl = true;
            motelBl = true;
            restaurantBl = true;
        }else {
            checking(gasList,gasBl);
            checking(hospitalList,hospitalBl);
            checking(coffeeList,coffeeBl);
            checking(marketList,marketBl);
            checking(motelList,motelBl);
            checking(restaurantList,restaurantBl);
            gasBl = false;
            hospitalBl = false;
            coffeeBl = false;
            marketBl = false;
            motelBl = false;
            restaurantBl = false;
        }
    }
    private void checking(ArrayList<Marker> arrayList, boolean checkingVar){
        if(!checkingVar){
            for(int i = 0; i < arrayList.size();i++){
                arrayList.get(i).remove();
            }
            arrayList.clear();
        }
    }
    private void buttonVisible(int visible){
        btn_1.setVisibility(visible);
        btn_2.setVisibility(visible);
        btn_3.setVisibility(visible);
        btn_4.setVisibility(visible);
        btn_5.setVisibility(visible);
        btn_6.setVisibility(visible);
    }
    private void sendRequest() {
        String origin = etOrigin;
        String destination = etDestination;
        if (origin.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm đi!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm đến!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            new DirectionFinder((DirectionFinderListener) this, URLEncoder.encode(origin, "utf-8"), URLEncoder.encode(destination, "utf-8")).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCameraChange(CameraPosition position) {

        if (position.zoom != currentZoom) {
            currentZoom = position.zoom;
        }
        myLevel = setVisibleMarker(VMapsActivity.this,currentZoom,myLevel,MarkerList);
    }
    @Override
    public void onClick(View v) {
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        int a = (int)marker.getTag();
        switch (a)
         {
             case 1:{
                 String id_Marker = marker.getTitle();
                 if (thisMarker.equals(id_Marker)){
                     buttonVisible(View.INVISIBLE);
                     thisMarker = "";
                     break;
                 } else {
                     thisMarker = id_Marker;
                     LatLng LLTemp = marker.getPosition();
                     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LLTemp, 16));
                     mMap.getUiSettings().setScrollGesturesEnabled(false);
                     mMap.getUiSettings().setZoomGesturesEnabled(false);
                     buttonVisible(View.VISIBLE);
                     btn_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLocationButtonClick(VMapsActivity.this, thisMarker);}
                     });
                     btn_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHistoryButtonClick(VMapsActivity.this, thisMarker);}
                     });
                     btn_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDirectButtonClick(VMapsActivity.this, thisMarker, myLocation, ImHere, mMap);
                        buttonVisible(View.INVISIBLE);
                        isDirecting = true;}
                     });
                     btn_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImageButtonClick(VMapsActivity.this, thisMarker);}
                     });
                     btn_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onVideoButtonClick(VMapsActivity.this, thisMarker);}
                     });
                     btn_6.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             onSpecialityButtonClick(VMapsActivity.this,thisMarker);
                         }
                     });
                     thisMarker = id_Marker;}
                 break;
            }
             case 2:{
                 SQLiteDatabase database = Database.initDatabase(VMapsActivity.this, "vmapsdb.sqlite");
                 Cursor c = database.query("GasStation",new String[]{"title","GioLamViec"},"title = ?", new String[]{marker.getTitle()},null,null,null,null);
                 Dialog dialog = new Dialog(VMapsActivity.this);
                 dialog.setContentView(R.layout.gas_station_dialog);
                 TextView gastextView = (TextView)dialog.findViewById(R.id.GioLamViecTextView);
                 dialog.setTitle("Trạm xăng");
                 c.moveToFirst();
                 gastextView.setText(c.getString(1));
                 c.close();
                 dialog.show();
                 break;
             }
             case 3:{
                 Intent intent = new Intent(VMapsActivity.this, hotel_activity.class);
                 intent.putExtra("TITLE",marker.getTitle());
                 startActivity(intent);
                 break;
             }
             case 4:{
                 break;
             }
             case 5:{
                 break;
             }
             case 6:{
                 Intent intent = new Intent(VMapsActivity.this, restaurant_and_coffee_activity.class);
                 intent.putExtra("TITLE",marker.getTitle());
                 intent.putExtra("TYPE",6);
                 intent.putExtra("PLACETYPE","Coffee");
                 startActivity(intent);
                 break;
             }
             case 7:{
                 Intent intent = new Intent(VMapsActivity.this, restaurant_and_coffee_activity.class);
                 intent.putExtra("TITLE",marker.getTitle());
                 intent.putExtra("TYPE",7);
                 intent.putExtra("PLACETYPE","Restaurant");
                 startActivity(intent);
                 break;
             }
         }
        return false;
     }
    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(VMapsActivity.this, "Đang tìm đường đi...", "Vui lòng chờ đợi...", true);
        if (myline != null) {
            for (Polyline polyline:myline ) {
                polyline.remove();
            }
            myline.clear();
            DistanceText.setText("");
            buttonSearchVisible(View.INVISIBLE);
            isDirecting = false;
            cancelBtn.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        myline = new ArrayList<>();
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            String tmp = "    Khoảng cách: " + route.distance.text;
            DistanceText.setText(tmp);
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);
            for (int i = 0; i < route.points.size(); i++){
                polylineOptions.add(route.points.get(i));
            }
            myline.add(mMap.addPolyline(polylineOptions));
            buttonVisible(View.INVISIBLE);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            buttonSearchVisible(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onMapReady (GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) VMapsActivity.this);
        mMap.setOnCameraChangeListener((OnCameraChangeListener) VMapsActivity.this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point){
                buttonVisible(View.INVISIBLE);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                thisMarker = "";
         }
        });
        MarkerList.clear();
        MarkerList = GetMarker(mMap, VMapsActivity.this,iconPath);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        ImageButton myLocationBtn = (ImageButton) findViewById(R.id.imgMyLocation);
        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLocation != null){mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
                    ImHere.remove();
                    ImHere = mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("Tôi đang ở đây!")
                            .icon(BitmapDescriptorFactory.fromAsset(iconPath)));
                ImHere.setTag(0);}
            }
        });
    }
    private  int getCS(Point size){
        int width = size.x;
        int height = size.y;
        if(width < height)return width; else return height;
    }
}