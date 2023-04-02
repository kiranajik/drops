package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class allRewards extends AppCompatActivity {
    String myEmail;

    private ArrayList<Rewards> rewsArraylist;
    private RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseFirestore firestore;
    RewardAdapter RAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rewards);
        myEmail=getIntent().getStringExtra("email");
        firestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        dataInitialize();

        recyclerView = findViewById(R.id.recyclerView3);
        int numColumns = 2; // Set the number of columns in the grid
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), numColumns);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RAdapter = new RewardAdapter(getApplicationContext(), rewsArraylist,myEmail);
        recyclerView.setAdapter(RAdapter);
        RAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        rewsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code

        db.collection("Rewards").whereEqualTo("Redeemer","")
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
    @Override
    public void onBackPressed() {
        Intent i= new Intent(allRewards.this,donor_home.class);
        i.putExtra("Email",myEmail);
        i.putExtra("source","reward");
        startActivity(i);
        finish();
    }
}