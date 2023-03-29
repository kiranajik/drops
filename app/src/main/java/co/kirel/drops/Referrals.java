package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Referrals extends AppCompatActivity {

    TextView ref_code_owner, ref_code_view;

    String code = "";
    boolean isUnique = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);

        ref_code_owner = findViewById(R.id.ref_code_owner);
        ref_code_view = findViewById(R.id.ref_code_view);

        //logged in user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email_ID = auth.getCurrentUser().getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.
                collection("Donor").document(email_ID);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("Name");
                    ref_code_owner.setText(name);
                    Toast.makeText(Referrals.this, name, Toast.LENGTH_SHORT).show();
                    String rcode = documentSnapshot.getString("referal_code");
                    ref_code_view.setText(rcode);
                }
            }
        });


        String gen_rcode = generateUniqueCode();
        Map<String, Object> new_rcode = new HashMap<>();
        new_rcode.put("referal_code", gen_rcode);

        docRef.update(new_rcode).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {}
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });








        //db finding (Fetching from firestore)
//        db.collection("Donor").document(email_ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists())
//                {
//                    String name = documentSnapshot.getString("Name");
//                    ref_code_owner.setText(name);
//                }
//            }
//        });
//
//        DocumentReference docRefnc = db.collection("Donor").document(email_ID);
//        docRefnc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        String name= document.getString("Name");
//                        Toast.makeText(Referrals.this, name, Toast.LENGTH_SHORT).show();
//                        ref_code_owner.setText(name);
//                    } else {
//                        Log.d("error", "No such document");
//                    }
//                } else {
//                    Log.d("error", "Firestore Error", task.getException());
//                }
//            }});
//
////correct
//
//
//
//        db.collection("Donor").document(email_ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                if(documentSnapshot.exists())
//                {
//                    String ref_c = documentSnapshot.getString("referal_code");
//
//                    if(ref_c.equals(""))
//                    {
//
//                        String new_ref_code = generateUniqueCode();
//
//
//                        db.collection("Donor").document(email_ID).update("referal_code",new_ref_code).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(Referrals.this,"Success", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Referrals.this,"Failure", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                       }
//                    else
//                    {
//                        Toast.makeText(Referrals.this,"Referral Code already exists", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        db.collection("Donor").document(email_ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists())
//                {
//                    String code = documentSnapshot.getString("referal_code");
//                    ref_code_view.setText(code);
//                }
//            }
//        });











    }

    public String generateUniqueCode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            for (int i = 0; i < 6; i++) {
                int index = (int) (Math.random() * alphabet.length());
                code += alphabet.charAt(index);
            }

        return code;
    }

}