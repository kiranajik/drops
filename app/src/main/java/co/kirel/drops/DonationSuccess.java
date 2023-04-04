package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DonationSuccess extends AppCompatActivity {

    String dnrEml;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_success);

        donor_home activity = new donor_home();
        dnrEml = activity.getMyData();



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
                    DocumentReference docRefx = db.collection("Donor").document(dnrEml);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("Referral_status", "2");

                    docRefx.update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                    DocumentReference docRef = db.collection("Donor").document(dnrEml); // replace with your collection name
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String referred_by = documentSnapshot.getString("Referred_by"); // replace with your field name

                                findDocumentNameByFieldValue("Donor", "Referred_by", referred_by, new DonationSuccess.OnDocumentNameCallback(){
                                    @Override
                                    public void onDocumentNameCallback(String documentName) {
                                        if (documentName != null) {

                                            add_coins(documentName);
                                        } else {

                                        }
                                    }
                                });

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

    public void findDocumentNameByFieldValue(String collectionName, String fieldName, String fieldValue, OnDocumentNameCallback callback) {
        db.collection(collectionName)
                .whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentName = documentSnapshot.getId();
                        callback.onDocumentNameCallback(documentName);
                        return;
                    }
                    // if no matching documents found, call callback with null
                    callback.onDocumentNameCallback(null);
                })
                .addOnFailureListener(e -> {
                    callback.onDocumentNameCallback(null);
                });
    }

    public interface OnDocumentNameCallback {
        void onDocumentNameCallback(String documentName);
    }

    public void add_coins(String user)
    {
        DocumentReference docRef = db.collection("Donor").document(user);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the current value of the "score" field
                    int coins = documentSnapshot.getLong("LifeCoins").intValue();

                    if (coins >= 0) {
                        // Add 50 to the current value of the "score" field
                        int ncoins = coins + 150;

                        // Update the "score" field in Firestore with the new value
                        docRef.update("LifeCoins", ncoins)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("T", "Score successfully updated!");
                                        Toast.makeText(DonationSuccess.this, "Score successfully updated!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("T", "Error updating score", e);
                                    }
                                });
                    } else {
                        Log.d("T", "Score field is null");
                    }
                } else {
                    Log.d("T", "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("T", "Error getting document: " + e);
            }
        });

    }
}