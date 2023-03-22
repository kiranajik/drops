package co.kirel.drops;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HosOrgProfileFragment extends Fragment implements OnMapReadyCallback {

    TextView honame,honamee,tvcompldreq,tvpndgreq,tvpplsdntd,hodesc,tvdescription;
    String hoName,docCount,hoemail;
    ImageView editdesc,closedlg;
    EditText ed_desc;
    String new_desc;
    Button savedes;
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Boolean loc = false;
    FirebaseFirestore firestore;

    public HosOrgProfileFragment() {
        // Required empty public constructor
    }
    public static HosOrgProfileFragment newInstance(String param1, String param2) {
        HosOrgProfileFragment fragment = new HosOrgProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hos_org_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospital_home activity = (hospital_home) getActivity();
        hoName = activity.getHoname();
        hoemail = activity.getMyData();

        firestore= FirebaseFirestore.getInstance();
        honame=view.findViewById(R.id.profilehoname);
        honamee=view.findViewById(R.id.honamee);
        tvcompldreq=view.findViewById(R.id.cmpltdreq);
        tvpndgreq=view.findViewById(R.id.pndgreq);
        mapView = view.findViewById(R.id.mapViewProfile);
        editdesc=view.findViewById(R.id.editDesc);
        hodesc=view.findViewById(R.id.dscrptn);
        tvdescription=view.findViewById(R.id.dscrptn);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        honame.setText(hoName);
        honamee.setText(hoName);


        View alertCustomDialog = LayoutInflater.from(getContext()).inflate(R.layout.description_dialog, null);
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//TRY
        builder.setView(alertCustomDialog);

        savedes=alertCustomDialog.findViewById(R.id.save_desc);
        ed_desc=alertCustomDialog.findViewById(R.id.newdesc);
        closedlg=alertCustomDialog.findViewById(R.id.close_dlg);

        final AlertDialog dialog = builder.create();

        DocumentReference docRef = firestore.collection("Organization").document(hoemail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        hodesc.setText(document.getString("description"));
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        editdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        savedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_desc=ed_desc.getText().toString();
                Map<String,Object> data= new HashMap<>();
                data.put("description",new_desc);
                firestore.collection("Organization").document(hoemail).update(data);

                hodesc.setText(new_desc);
                dialog.cancel();
                Toast.makeText(getContext(),"New Description added",Toast.LENGTH_SHORT).show();
            }
        });

        closedlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Toast.makeText(getContext(),"Dialog Closed",Toast.LENGTH_SHORT).show();
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(hoName, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(location).title(hoName));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Query query1 = firestore.collection("Donations").whereEqualTo("DonationStatus","Yes");
        AggregateQuery countQuery1 = query1.count();

        Query query2 = firestore.collection("Donations").whereEqualTo("DonationStatus","No");
        AggregateQuery countQuery2 = query1.count();

        Query query3 = firestore.collection("Donations").whereEqualTo("DonationStatus","Yes");
        AggregateQuery countQuery3 = query1.count();

        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    docCount= String.valueOf(snapshot.getCount());
                    tvcompldreq.setText(docCount);
                    Toast.makeText(getContext(), docCount, Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });


        countQuery2.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    docCount= String.valueOf(snapshot.getCount());
                    tvpndgreq.setText(docCount);
                    Toast.makeText(getContext(), docCount, Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });

        countQuery3.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    docCount= String.valueOf(snapshot.getCount());
                    tvcompldreq.setText(docCount);
                    Toast.makeText(getContext(), docCount, Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });


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
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}