package com.example.gamepuzzlepursuit;

import android.Manifest.permission;
import android.annotation.SuppressLint;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        //nut cau hoi
        Button btnquiz = findViewById(R.id.btnquiz);
        btnquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getBaseContext(),googleSignInOptions);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });


    }
    //show cau hoi
    private void openQuizDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_quiz);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
//        //cau hoi//
//        TextView questionTextView;
        Button ansA, ansB, ansC, ansD;
//        Button submitBtn;
//        int currentQuestionIndex = 0;
//        //end//
//        questionTextView = findViewById(R.id.question);
//        ansA = findViewById(R.id.ans_A);
//        ansB = findViewById(R.id.ans_B);
//        ansC = findViewById(R.id.ans_C);
//        ansD = findViewById(R.id.ans_D);
//        submitBtn = findViewById(R.id.submit_btn);
//
//
//        questionTextView.setText(QuestionAnswer.question[currentQuestionIndex]);
//        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
//        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
//        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
//        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);

        dialog.show();
    }



    //Begin location
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
//    public LatLng getRandomLocation(LatLng point, int radius) {
//
//        List<LatLng> randomPoints = new ArrayList<>();
//        List<Float> randomDistances = new ArrayList<>();
//        Location myLocation = new Location("");
//        myLocation.setLatitude(point.latitude);
//        myLocation.setLongitude(point.longitude);
//
//        // Điều này nhằm tạo ra 3 điểm ngẫu nhiên
//
//            double x0 = point.latitude;
//            double y0 = point.longitude;
//
//            Random random = new Random();
//
//            // Chuyển đổi bán kính từ mét sang độ
//            double radiusInDegrees = radius / 111000f;
//
//            double u = random.nextDouble();
//            double v = random.nextDouble();
//            double w = radiusInDegrees * Math.sqrt(u);
//            double t = 2 * Math.PI * v;
//            double x = w * Math.cos(t);
//            double y = w * Math.sin(t);
//
//            // Điều chỉnh tọa độ x để thu hẹp khoảng cách đông-tây
//            double new_x = x / Math.cos(y0);
//
//            double foundLatitude = new_x + x0;
//            double foundLongitude = y + y0;
//            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
//            randomPoints.add(randomLatLng);
//            Location l1 = new Location("");
//            l1.setLatitude(randomLatLng.latitude);
//            l1.setLongitude(randomLatLng.longitude);
//            randomDistances.add(l1.distanceTo(myLocation));
//
//        //Lấy điểm xa tâm nhất
//        int indexOfNearestPointToCentre =
//                randomDistances.indexOf(Collections.max(randomDistances));
//        return randomPoints.get(indexOfNearestPointToCentre);
//    }
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
            LatLng myLocation=new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            MarkerOptions option=new MarkerOptions();
            option.title("Vị trí của bạn");
            option.snippet("Name:Player01");
            option.position(myLocation);
            Marker currentMarker= mMap.addMarker(option);
            currentMarker.showInfoWindow();

            //Tao vi tri hop cau hoi
            LatLng quiz = new LatLng(20.984350, 105.841360);
            mMap.addMarker(new MarkerOptions().position(quiz).title("Vị trí câu đố")
                    .icon(bitmapDescriptor(getApplicationContext(),R.drawable.hopcauhoi1)));
            LatLng quiz1 = new LatLng(20.986854, 105.832304);
            mMap.addMarker(new MarkerOptions().position(quiz1).title("Vị trí câu đố")
                    .icon(bitmapDescriptor(getApplicationContext(),R.drawable.hopcauhoi1)));
            LatLng quiz2 = new LatLng(20.989896, 105.835772);
            mMap.addMarker(new MarkerOptions().position(quiz2).title("Vị trí câu đố")
                    .icon(bitmapDescriptor(getApplicationContext(),R.drawable.hopcauhoi1)));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    if( myLocation == myLocation ){
                        openQuizDialog(Gravity.CENTER);
                    }

                    return false;
                }
            });

            //Zoom my location
            CameraUpdate center = CameraUpdateFactory.newLatLng(myLocation);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.moveCamera(center);
            mMap.moveCamera(zoom);
            return;

        }

        // 2. Nếu không, hãy yêu cầu quyền truy cập vị trí từ người dùng.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Đang di chuyển đến vị trí của bạn!", Toast.LENGTH_SHORT).show();
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
    //end location
    private BitmapDescriptor bitmapDescriptor(Context context,int vectoerResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectoerResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
