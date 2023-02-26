package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

public class referFriend extends AppCompatActivity {

    FirebaseFirestore firestore;
    private ArrayList<Donor> donsArraylist;

    private RecyclerView recyclerView;
    referAdapter rAdapter;
    String email,ReqId;
    String documentName;
    List<String> docsArray=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
        firestore= FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");
        ReqId = getIntent().getStringExtra("ReqId");
        dataInitialize();

        recyclerView= findViewById(R.id.recyclerViewRefer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        rAdapter = new referAdapter(getApplicationContext(),donsArraylist, docsArray,email,ReqId);
        recyclerView.setAdapter(rAdapter);
        rAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        donsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code

        firestore.collection("Donor")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!= null)
                        {
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges())
                        {
                            documentName = dc.getDocument().getId();
                            if (dc.getType() == DocumentChange.Type.ADDED && !documentName.equals(email))
                            {
                                donsArraylist.add(dc.getDocument().toObject(Donor.class));
                                docsArray.add(documentName);
                            }
                            rAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }
}