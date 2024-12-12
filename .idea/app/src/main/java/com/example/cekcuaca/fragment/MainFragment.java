package com.example.cekcuaca.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cekcuaca.R;
import com.example.cekcuaca.RetrofitClient;
import com.example.cekcuaca.WeatherRVAdapter;
import com.example.cekcuaca.WeatherRVModal;
import com.example.cekcuaca.api.WeatherAPIService;
import com.example.cekcuaca.api.WeatherResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private final int PERMISSION_CODE = 1;
    private String cityName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Inisialisasi view
        homeRL = view.findViewById(R.id.idRLHome);
        loadingPB = view.findViewById(R.id.idPBLoading);
        cityNameTV = view.findViewById(R.id.idTVCityName);
        temperatureTV = view.findViewById(R.id.idTVTemperature);
        conditionTV = view.findViewById(R.id.idTVCondition);
        weatherRV = view.findViewById(R.id.idRVWeather);
        cityEdt = view.findViewById(R.id.idEdtCity);
        backIV = view.findViewById(R.id.idIVBack);
        iconIV = view.findViewById(R.id.idIVIcon);
        searchIV = view.findViewById(R.id.idIVSearch);

        // Setup adapter RecyclerView
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(getActivity(), weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        // Setup LocationManager
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // Periksa izin lokasi
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            fetchLocation();
        }

        // Setup tombol pencarian
        searchIV.setOnClickListener(v -> {
            String city = cityEdt.getText().toString();
            if (city.isEmpty()) {
                Toast.makeText(requireActivity(), "Please enter city Name", Toast.LENGTH_SHORT).show();
            } else {
                cityNameTV.setText(city);
                getWeatherInfo(city);
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Permission granted. Fetching location...", Toast.LENGTH_SHORT).show();
                fetchLocation();
            } else {
                Toast.makeText(requireActivity(), "Permission denied. Cannot fetch location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Tidak ada izin
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            cityName = getCityName(location.getLongitude(), location.getLatitude());
            cityNameTV.setText(cityName);
            getWeatherInfo(cityName);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    cityName = getCityName(location.getLongitude(), location.getLatitude());
                    cityNameTV.setText(cityName);
                    getWeatherInfo(cityName);
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    Toast.makeText(requireActivity(), "Provider enabled: " + provider, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    Toast.makeText(requireActivity(), "Provider disabled: " + provider, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(requireActivity().getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityName) {
        loadingPB.setVisibility(View.VISIBLE);
        homeRL.setVisibility(View.GONE);

        WeatherAPIService apiService = RetrofitClient.getRetrofitInstance().create(WeatherAPIService.class);
        Call<WeatherResponse> call = apiService.getWeatherForecast(cityName, 8, "07cd95043e864549bf1deb3ec45d4029");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (requireContext() == null) {
                    return;
                }

                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    weatherRVModalArrayList.clear();
                    List<WeatherResponse.WeatherData> dataArray = response.body().getData();

                    WeatherResponse.WeatherData todayData = dataArray.get(0);
                    String todayIcon = todayData.getWeather().getIcon();
                    String todayDescription = todayData.getWeather().getDescription();

                    String iconUrl = "https://www.weatherbit.io/static/img/icons/" + todayIcon + ".png";
                    Picasso.get().load(iconUrl).into(iconIV);
                    temperatureTV.setText(todayData.getTemp() + "°c");
                    conditionTV.setText(todayDescription);

                    for (int i = 1; i < dataArray.size(); i++) {
                        WeatherResponse.WeatherData dayData = dataArray.get(i);
                        weatherRVModalArrayList.add(new WeatherRVModal(
                                dayData.getValidDate(),
                                dayData.getTemp(),
                                dayData.getWeather().getIcon(),
                                dayData.getWindSpeed(),
                                dayData.getWeather().getDescription()
                        ));
                    }
                    weatherRVAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid city name.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                if (requireContext() == null) {
                    return;
                }

                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Failed to get weather information.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
