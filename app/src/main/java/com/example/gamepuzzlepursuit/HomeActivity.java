package com.example.gamepuzzlepursuit;

import android.Manifest.permission;
import android.annotation.SuppressLint;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{
    //Yêu cầu mã cho yêu cầu cấp phép vị trí.
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    //Khai báo đối tượng Google Map
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Lấy đối tượng Google Map ra:
        // Lấy SupportMapFragment và nhận thông báo khi bản đồ sẵn sàng được sử dụng.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // yêu cầu cấp phép vị trí.
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        mMap.setMyLocationEnabled(true);
    }

    /**
     * Kích hoạt lớp Vị trí của tôi nếu quyền truy cập vị trí chính xác đã được cấp.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Kiểm tra xem quyền có được cấp hay không, nếu có, hãy bật lớp vị trí của tôi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //Thêm MarketOption cho Map:
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            LatLng latLng=new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            MarkerOptions option=new MarkerOptions();
            option.title("Vị trí của bạn");
            option.snippet("Name:Player01");
            option.position(latLng);
            Marker currentMarker= mMap.addMarker(option);
            currentMarker.showInfoWindow();
            return;

        }

        // 2. Nếu không, hãy yêu cầu quyền truy cập vị trí từ người dùng.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Đã nhấp vào nút Vị trí của tôi", Toast.LENGTH_SHORT).show();
        // Trả về false để chúng ta không sử dụng sự kiện và hành vi mặc định vẫn xảy ra
        // (Camera hoạt hình theo vị trí hiện tại của người dùng).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Kích hoạt lớp vị trí của tôi nếu quyền đã được cấp.
            enableMyLocation();
        } else {
            // Quyền đã bị từ chối. Hiển thị thông báo lỗi
            // Hiển thị hộp thoại lỗi quyền bị thiếu khi các đoạn tiếp tục.
            permissionDenied = true;
        }
    }


    /**
     * Hiển thị hộp thoại có thông báo lỗi giải thích rằng quyền truy cập vị trí bị thiếu.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Quyền không được cấp, hiển thị hộp thoại báo lỗi.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }


}
