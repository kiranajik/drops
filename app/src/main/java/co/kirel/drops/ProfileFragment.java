package co.kirel.drops;

import android.graphics.Color;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    Button donation, request;
    View view;
    String pname;
    FirebaseFirestore firestore;
    TextView unamep;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        super.onCreate(savedInstanceState);
        replaceFragment(new DonationsFragment());

        firestore=FirebaseFirestore.getInstance();

        donor_home activity = (donor_home) getActivity();
        String myEmail = activity.getMyData();

        donation = view.findViewById(R.id.donationbtn);
        request = view.findViewById(R.id.requestbtn);
        unamep = view.findViewById(R.id.profileuname);

        DocumentReference docRef = firestore.collection("Donor").document(myEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        pname= document.getString("Name");
                        unamep.setText(pname);
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new DonationsFragment());
                donation.setBackgroundColor(Color.parseColor("#E26864"));
                donation.setTextColor(Color.WHITE);
                request.setBackgroundColor(Color.parseColor("#4B69D7"));
                request.setTextColor(Color.WHITE);
            }

        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RequestsFragment());
                request.setBackgroundColor(Color.parseColor("#E26864"));
                request.setTextColor(Color.WHITE);
                donation.setBackgroundColor(Color.parseColor("#4B69D7"));
                donation.setTextColor(Color.WHITE);
            }
        });
        return view;

    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}