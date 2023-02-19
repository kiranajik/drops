package co.kirel.drops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    TextView uname;
    String Name;
    FirebaseFirestore firestore;
    private ArrayList<Requirements> reqsArraylist;
    private String[] reqsNames;
    private String[] reqsgp;
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        donor_home activity = (donor_home) getActivity();
        String myEmail = activity.getMyData();
        firestore=FirebaseFirestore.getInstance();
        uname=view.findViewById(R.id.uname);

        dataInitialize();

        recyclerView= view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        MyAdapter myAdapter = new MyAdapter(getContext(),reqsArraylist);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        DocumentReference docRef = firestore.collection("Donor").document(myEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Name= document.getString("Name");
                        uname.setText(Name);
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });
    }

    private void dataInitialize() {
        reqsArraylist = new ArrayList<>();

        reqsNames= new String[]{

                getString(R.string.head1),
                getString(R.string.head2),
                getString(R.string.head3),
                getString(R.string.head4),
                getString(R.string.head5),
                getString(R.string.head6),
        };

        reqsgp= new String[]{
                getString(R.string.bg1),
                getString(R.string.bg2),
                getString(R.string.bg3),
                getString(R.string.bg4),
                getString(R.string.bg5),
                getString(R.string.bg6),
        };

        for (int i=0; i< reqsNames.length; i++)
        {
            Requirements requirements= new Requirements(reqsNames[i],reqsgp[i]);
            reqsArraylist.add(requirements);
        }

    }
}