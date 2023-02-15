package co.kirel.drops;

import android.graphics.Color;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import co.kirel.drops.databinding.ActivityDonorHomeBinding;

public class ProfileFragment extends Fragment {
    Button donation, request;

    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        super.onCreate(savedInstanceState);
        replaceFragment(new DonationsFragment());
        donation = view.findViewById(R.id.donationbtn);
        request = view.findViewById(R.id.requestbtn);
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new DonationsFragment());
                donation.setBackgroundColor(Color.RED);
                donation.setTextColor(Color.BLACK);
                request.setBackgroundColor(Color.GRAY);
                request.setTextColor(Color.WHITE);
            }

        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RequestsFragment());
                request.setBackgroundColor(Color.RED);
                request.setTextColor(Color.BLACK);
                donation.setBackgroundColor(Color.GRAY);
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