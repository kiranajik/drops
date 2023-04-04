package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class DonationSuccess extends AppCompatActivity {

    String dnrEml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_success);

        donor_home activity = new donor_home();
        dnrEml = activity.getMyData();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Donations"); // replace with your collection name
        Query query = collectionRef.whereEqualTo("email", dnrEml) // filter by email field
                .whereEqualTo("DonationStatus", "yes"); // add additional condition for DonationStatus field
        Task<QuerySnapshot> task = query.get();
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                int count = querySnapshot.size(); // get count of documents
                // use the count here

                if(count == 1)
                {

                    DocumentReference docRef = db.collection("Donor").document(dnrEml); // replace with your collection name
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String referred_by = documentSnapshot.getString("Referred_by"); // replace with your field name


                            } else {
                                Log.d("TAG", "No such document");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // handle failure here
                            Log.e("TAG", "Error getting document: ", e);
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // handle failure here
                Log.e("TAG", "Error getting documents: ", e);
            }
        });


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
//                Intent i = new Intent(DonationSuccess.this, hospital_home.class);
//                startActivity(i);
                finish();
            }
        }, 4000);
    }
}