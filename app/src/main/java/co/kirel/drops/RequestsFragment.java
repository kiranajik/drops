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
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RequestsFragment extends Fragment {
    FirebaseFirestore firestore;
    private ArrayList<Donations> donsArraylist;
    private RecyclerView reqrecview;
    String myEmail;
    PendingAdapter pendingAdapter;

    public RequestsFragment() {
        // Required empty public constructor
    }

    public static RequestsFragment newInstance() {
        RequestsFragment fragment = new RequestsFragment();
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
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firestore= FirebaseFirestore.getInstance();

        dataInitialize();

        reqrecview=view.findViewById(R.id.reqrecview);
        reqrecview.setLayoutManager(new LinearLayoutManager(getContext()));
        reqrecview.setHasFixedSize(true);
        pendingAdapter = new PendingAdapter(getContext(),donsArraylist);
        reqrecview.setAdapter(pendingAdapter);
        pendingAdapter.notifyDataSetChanged();
    }
    private void dataInitialize() {
        donsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Donations")
                        .whereEqualTo("DonorId",myEmail)
                        .whereEqualTo("DonationStatus","No")
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
                                    pendingAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }
}