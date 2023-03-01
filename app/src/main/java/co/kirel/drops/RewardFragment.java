package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RewardFragment extends Fragment {


    String Name;
    ImageView redrewbtn;

    TextView coincount;



    private ArrayList<Rewards> rewsArraylist;

    private RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseFirestore firestore;
    MyRewardAdapter RAdapter;

    public RewardFragment() {
        // Required empty public constructor
    }
    public static RewardFragment newInstance(String param1, String param2) {
        RewardFragment fragment = new RewardFragment();
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
        return inflater.inflate(R.layout.fragment_reward, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        redrewbtn = view.findViewById(R.id.redrewbtn);
        donor_home activity = (donor_home) getActivity();
        String myEmail = activity.getMyData();
        redrewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(),allRewards.class);
                i.putExtra("email",myEmail);
                startActivityForResult(i, 'R');
                startActivity(i);
            }
        });

        firestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        dataInitialize(myEmail);

        recyclerView = view.findViewById(R.id.recyclerView2);
        coincount = view.findViewById(R.id.coincount);
        int numColumns = 2; // Set the number of columns in the grid
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numColumns);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RAdapter = new MyRewardAdapter(getContext(), rewsArraylist);
        recyclerView.setAdapter(RAdapter);
        RAdapter.notifyDataSetChanged();

        DocumentReference docRef = firestore.collection("Donor").document(myEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String myCoins = document.getLong("LifeCoins").toString();
                        coincount.setText(myCoins);
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });


    }

    private void dataInitialize(String myEmail) {
        rewsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code

        db.collection("Rewards").whereEqualTo("Redeemer",myEmail)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                rewsArraylist.add(dc.getDocument().toObject(Rewards.class));
                            }
                            RAdapter.notifyDataSetChanged();
                        }

                    }
                });

    }
}