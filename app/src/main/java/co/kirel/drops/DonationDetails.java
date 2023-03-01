package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.List;

public class DonationDetails extends AppCompatActivity implements OnMapReadyCallback {

    TextView horgname;
    Button bDate,bTime,bconfirm;
    String ReqId,sdate,stime,honame,BldGrp;
    FirebaseFirestore firestore;
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Boolean loc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);

        bDate=findViewById(R.id.bdate);
        bTime=findViewById(R.id.btime);
        horgname=findViewById(R.id.horgname);
        bconfirm=findViewById(R.id.bconfirm);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent i=getIntent();
        ReqId=i.getStringExtra("ReqId");
        BldGrp=i.getStringExtra("BldGrp");
        Toast.makeText(this, "Req Id"+ReqId, Toast.LENGTH_SHORT).show();

        firestore= FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("Requirements").document(ReqId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        sdate= document.getString("End Date");
                        stime= document.getString("End Time");
                        honame= document.getString("honame");
                        bDate.setText(sdate);
                        bTime.setText(stime);
                        horgname.setText(honame);
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                Geocoder geocoder = new Geocoder(DonationDetails.this);
                                try {
                                    List<Address> addresses = geocoder.getFromLocationName(honame, 1);
                                    if (addresses != null && !addresses.isEmpty()) {
                                        Address address = addresses.get(0);
                                        LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                                        googleMap.addMarker(new MarkerOptions().position(location).title(honame));
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DonationDetails.this,DonationQr.class);
                i.putExtra("ReqId",ReqId);
                i.putExtra("honame",honame);
                i.putExtra("BldGrp",BldGrp);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        } else {
            loc = true;
        }
        googleMap.setMyLocationEnabled(loc);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loc = true;
            } else {
                loc = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}