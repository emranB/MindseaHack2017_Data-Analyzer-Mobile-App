package edu.school.nick.climatemaps;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ClimateMap extends FragmentActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
    CSVParser parser = new CSVParser();

    public ArrayList<ClimateData> masterList = null;
    public ArrayList<ClimateData> temperatureSummer = new ArrayList<>();
    public ArrayList<ClimateData> temperatureWinter = new ArrayList<>();
    public ArrayList<ClimateData> temperatureFall = new ArrayList<>();
    public ArrayList<ClimateData> temperatureSpring = new ArrayList<>();
    public ArrayList<ClimateData> temperatureAnnual = new ArrayList<>();
    public ArrayList<ClimateData> waterShortage = new ArrayList<>();
    public ArrayList<ClimateData> waterSurplus = new ArrayList<>();
    public ArrayList<ClimateData> precipitationSummer = new ArrayList<>();
    public ArrayList<ClimateData> precipitationWinter = new ArrayList<>();
    public ArrayList<ClimateData> precipitationSpring = new ArrayList<>();
    public ArrayList<ClimateData> precipitaitonFall = new ArrayList<>();
    public ArrayList<ClimateData> precipitationAnnual = new ArrayList<>();
    public ArrayList<ClimateData> coldDays = new ArrayList<>();
    public ArrayList<ClimateData> hotDays = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private RelativeLayout parameterCell;

    private Spinner parameterSpinner;
    private Spinner locationSpinner;
    private Spinner subparamSpinner;
    private SeekBar seekBar;

    private TextView subParameterTextView;

    private boolean subparamsVisible;

    private ArrayList<String> parameters;
    private ArrayList<String> locations;

    private int selectedLocation;
    private int selectedParameter;
    private int selectedSubParameter;

    private GoogleMap mMap;
    int radius = 1000;
    ArrayList<CircleOptions> circleOptions = new ArrayList<>(Arrays.asList(
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7FFF0000)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7FFF3300)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7Fff6600)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7Fff9900)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7FFFCC00)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7FFFFF00 )  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7Fccff00)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7F99ff00)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7F66ff00)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7F33ff00)  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5),
            new CircleOptions()
                    .radius(radius)   //set radius in meters
                    .fillColor(0x7F00FF00 )  //Transparent Greem
                    .strokeColor(0x10000000)
                    .strokeWidth(5)
    ));

    public ClimateMap() throws Exception {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_climate_map);
        AssetManager assetManager = getAssets();
        try {

            InputStream parseingFile = assetManager.open("ns_climate_change_data.csv");
            masterList = parser.ParseData(parseingFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        divideLists(masterList);

        parameterCell = (RelativeLayout) findViewById(R.id.activity_climate_map_parameters);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.activity_climate_map_toolbar);
        toolbarTitleTextView = (TextView) toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTitleTextView.setText("Climate Map");
        toolbarTitleTextView.setTextColor(Color.BLACK);

        subParameterTextView = (TextView) parameterCell.findViewById(R.id.cell_parameters_sub_parameter_text_view);

        parameters = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Parameters)));
        locations = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Locations)));

        locationSpinner = (Spinner) parameterCell.findViewById(R.id.cell_parameters_location_spinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = position;

                // Update the map
                buildMapParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        parameterSpinner = (Spinner) parameterCell.findViewById(R.id.cell_parameters_parameter_spinner);
        parameterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Call configureSubParameters() with your parameter string which will update your subparameters view

                selectedParameter = position;
                configureSubParameters(parameters.get(position));

                // Update the map
                buildMapParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        subparamSpinner = (Spinner) parameterCell.findViewById(R.id.cell_parameters_sub_parameter_spinner);
        subparamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubParameter = position;

                // Update the map
                buildMapParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        seekBar = (SeekBar) parameterCell.findViewById(R.id.cell_parameters_seek_bar);
        seekBar.setOnSeekBarChangeListener(this);

        configureParameters();
        configureSubParameters("");
        configureLocations();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_climate_map_map);
        mapFragment.getMapAsync(this);
    }

    private void configureParameters() {
        ArrayAdapter<CharSequence> parametersAdapter = ArrayAdapter.createFromResource(this, R.array.Parameters, R.layout.custom_spinner_item);
        parametersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parameterSpinner.setAdapter(parametersAdapter);
    }

    private void configureSubParameters(String parameter) {
        ArrayAdapter<CharSequence> subParametersAdapter;

        switch (parameter) {
            case "Temperature":
                subParametersAdapter = ArrayAdapter.createFromResource(this, R.array.Temperature_Subparams, R.layout.custom_spinner_item);
                break;
            case "Water":
                subParametersAdapter = ArrayAdapter.createFromResource(this, R.array.Water_Subparams, R.layout.custom_spinner_item);
                break;
            default:
                subParametersAdapter = null;
        }

        if (subParametersAdapter != null) {
            subParametersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subparamSpinner.setAdapter(subParametersAdapter);

            subparamsVisible = true;
            updateView();
            return;
        }

        subparamsVisible = false;
        updateView();

    }

    private void configureLocations() {
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.Locations, R.layout.custom_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);
    }

    private void updateView() {
        if (subparamSpinner != null) {
            subparamSpinner.setVisibility(subparamsVisible ? View.VISIBLE : View.INVISIBLE);
        }

        if (subParameterTextView != null) {
            subParameterTextView.setVisibility(subparamsVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void buildMapParams() {
        updateCircles(masterList, mMap, locations.get(selectedLocation));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // If the slider updates, you'll get the callback here, so update map
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng halifax = new LatLng(44.648763500, -63.575238700);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(halifax));



        LatLng latLng = new LatLng(44.6488, -63.5752);



    }

    public void updateCircles(ArrayList<ClimateData> list, GoogleMap mMap, String location){
        mMap.clear();
        double min = getMin(list);
        double max = getMax(list);
        for(ClimateData data: list){
            try {
                if (data.GET_region().equals(location)) {
                    mMap.addCircle(circleOptions.get((int) getColorValue(data, min, max)).center(getLatLng(data)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng(data)));
                }
            }
            catch (Exception exc) {
                Log.e("ErrorUpdatingCircle", "Error Updateing circle's", exc);
            }
        }
    }

    public double getMin(ArrayList<ClimateData> list){
        double min = 100000;
        for(ClimateData data: list){
            if(data.GET_value()<min)
                min = (int) data.GET_value();
        }
        return min;
    }
    public double getMax(ArrayList<ClimateData> list){
        double max = -10000;
        for(ClimateData data: list){
            if(data.GET_value()>max)
                max = (int) data.GET_value();
        }
        return max;
    }
    //assign a value from 0 - 10

    public double getColorValue(ClimateData data, double listMin, double listMax){
        double value = data.GET_value();
        if(value == listMin)
            return 0;
        else
            return Math.floor((value-listMin)/(listMax-listMin)*10);
    }

    public LatLng getLatLng(ClimateData data){
        String region = data.GET_region();
        switch(region) {
            case("Amherst"):
                return new LatLng(45.816667000, -64.216720600);
            case("Annapolis"):
                return new LatLng(44.742226000, -65.515822000);
            case("Annapolis Valley"):
                return new LatLng(44.916666700, -65.166666700);
            case("Cape Breton West"):
                return new LatLng(46.029337000, -60.236447200);
            case("Guysborough"):
                return new LatLng(45.390607500, -61.498962700);
            case("HRM"):
                return new LatLng(44.648763500, -63.575238700);
            case("Kentville"):
                return new LatLng(45.076911500, -64.494473500);
            case("Liverpool"):
                return new LatLng(44.032977200, -64.717678300);
            case("Lunenburg"):
                return new LatLng(44.377005300, -64.318835400);
            case("Pictou-Antigonish"):
                return new LatLng(45.679332000, -62.720603000);
            case("Sydney"):
                return new LatLng(46.136789900, -60.194224000);
            case("Truro"):
                return new LatLng(45.365773300, -63.286940700);
            case("Yarmouth"):
                return new LatLng(43.837457600, -66.117382000);
            default:
                return null;
        }
    }
    public void divideLists(ArrayList<ClimateData> list){
        String region;
        for(ClimateData data: list){
            region = data.GET_region();
            switch(region){
                case("Cold Days (Tmax < -10)"):
                    coldDays.add(data);
                    break;
                case("Hot Days (Tmax > 30)"):
                    hotDays.add(data);
                    break;
                case("Precipitation - Annual"):
                    precipitationAnnual.add(data);
                    break;
                case("Precipitation - Autumn"):
                    precipitaitonFall.add(data);
                    break;
                case("Precipitation - Spring"):
                    precipitationSpring.add(data);
                    break;
                case("Precipitation - Summer"):
                    precipitationSummer.add(data);
                    break;
                case("Precipitation - Winter"):
                    precipitationWinter.add(data);
                    break;
                case("Temperature - Annual"):
                    temperatureAnnual.add(data);
                    break;
                case("Temperature - Autumn"):
                    temperatureFall.add(data);
                    break;
                case("Temperature - Spring"):
                    temperatureSpring.add(data);
                    break;
                case("Temperature - Summer"):
                    temperatureSummer.add(data);
                    break;
                case("Temperature - Winter"):
                    temperatureWinter.add(data);
                    break;
                case("Water Deficit"):
                    waterShortage.add(data);
                    break;
                case("Water Surplus"):
                    waterSurplus.add(data);
                    break;
            }
        }
    }
}
