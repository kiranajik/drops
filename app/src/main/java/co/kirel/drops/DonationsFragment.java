package co.kirel.drops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonationsFragment extends Fragment {
    FirebaseFirestore firestore;
    private ArrayList<Donations> donsArraylist;
    private RecyclerView donrecview;
    String myEmail;
    DonAdapter donAdapter;

    public DonationsFragment() {
        // Required empty public constructor
    }

    public static DonationsFragment newInstance(String param1, String param2) {
        DonationsFragment fragment = new DonationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myEmail= getArguments().getString("email");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donations, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firestore= FirebaseFirestore.getInstance();

        dataInitialize();

        donrecview=view.findViewById(R.id.donrecview);
        donrecview.setLayoutManager(new LinearLayoutManager(getContext()));
        donrecview.setHasFixedSize(true);
        donAdapter = new DonAdapter(getContext(),donsArraylist);
        donrecview.setAdapter(donAdapter);
        donAdapter.notifyDataSetChanged();
    }
    private void dataInitialize() {
        donsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Donations")
                        .whereEqualTo("DonorId",myEmail)
                        .whereEqualTo("DonationStatus","Yes")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (error!= null)
                                {
                                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore error",error.getMessage());
                                    return;
                                }

                                for (DocumentChange dc : value.getDocumentChanges())
                                {
                                    if (dc.getType() == DocumentChange.Type.ADDED)
                                    {
                                        donsArraylist.add(dc.getDocument().toObject(Donations.class));
                                    }
                                    donAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }

}
