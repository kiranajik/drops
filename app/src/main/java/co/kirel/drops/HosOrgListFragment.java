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

public class HosOrgListFragment extends Fragment {

    String code;
    FirebaseFirestore firestore;
    private ArrayList<Requirements> reqsArraylist;
    private RecyclerView allreqrecview;
    ReqAdapter reqAdapter;

    public HosOrgListFragment() {
        // Required empty public constructor
    }

    public static HosOrgListFragment newInstance(String param1, String param2) {
        HosOrgListFragment fragment = new HosOrgListFragment();
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
        return inflater.inflate(R.layout.fragment_hos_org_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospital_home activity = (hospital_home) getActivity();
        code = activity.getHoCode();

        firestore= FirebaseFirestore.getInstance();



        dataInitialize();

        allreqrecview=view.findViewById(R.id.alldonrecview);
        allreqrecview.setLayoutManager(new LinearLayoutManager(getContext()));
        allreqrecview.setHasFixedSize(true);
        reqAdapter = new ReqAdapter(getContext(),reqsArraylist);
        allreqrecview.setAdapter(reqAdapter);
        reqAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        reqsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Requirements")
                        .whereEqualTo("Hospital Code",code)
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