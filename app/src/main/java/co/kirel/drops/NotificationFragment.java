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

public class NotificationFragment extends Fragment {
    FirebaseFirestore firestore;
    private ArrayList<Donations> donsArraylist;
    private RecyclerView notifrecview;
    String myEmail;
    notifAdapter notifAdapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        donor_home activity = (donor_home) getActivity();
        myEmail = activity.getMyData();
        firestore= FirebaseFirestore.getInstance();

        dataInitialize();

        notifrecview=view.findViewById(R.id.hosnotifrecview);
        notifrecview.setLayoutManager(new LinearLayoutManager(getContext()));
        notifrecview.setHasFixedSize(true);
        notifAdapter = new notifAdapter(getContext(),donsArraylist,myEmail);
        notifrecview.setAdapter(notifAdapter);
        notifAdapter.notifyDataSetChanged();
    }
    private void dataInitialize() {
        donsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Donations")
                        .whereEqualTo("DonorId",myEmail)
                        .whereEqualTo("notified","No")
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
                                    notifAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }
}