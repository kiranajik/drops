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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HosOrgHomeFragment extends Fragment {

    TextView honame;
    String hoName;
    FirebaseFirestore firestore;
    private ArrayList<Requirements> reqsArraylist;
    private RecyclerView horecyclerView;
    ReqAdapter reqAdapter;

    public HosOrgHomeFragment() {
        // Required empty public constructor
    }

    public static HosOrgHomeFragment newInstance(String param1, String param2) {
        HosOrgHomeFragment fragment = new HosOrgHomeFragment();
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
        return inflater.inflate(R.layout.fragment_hos_org_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospital_home activity = (hospital_home) getActivity();
        String myEmail = activity.getMyData();

        firestore= FirebaseFirestore.getInstance();
        honame=view.findViewById(R.id.honame);

        DocumentReference docRef = firestore.collection("Organization").document(myEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        hoName= document.getString("Organization Name");
                        honame.setText(hoName);
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        dataInitialize();

        horecyclerView=view.findViewById(R.id.horecyclerView);
        horecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        horecyclerView.setHasFixedSize(true);
        reqAdapter = new ReqAdapter(getContext(),reqsArraylist);
        horecyclerView.setAdapter(reqAdapter);
        reqAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        reqsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();;
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Requirements")
                        .whereEqualTo("honame",hoName)

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
                                        reqsArraylist.add(dc.getDocument().toObject(Requirements.class));
                                    }
                                    reqAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }

}