package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

public class new_Req extends AppCompatActivity {
    Spinner spinner;
    EditText bottleno,endtime,purpose,description;
    TextView reqid;
    Button submit;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    String ReqId,bloodGroup,check="yes";
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    String dReqId = randomString(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_req);
        spinner = (Spinner) findViewById(R.id.BGroupDD);
        bottleno=findViewById(R.id.NoBottles);
        endtime=findViewById(R.id.EndTime);
        purpose=findViewById(R.id.reqPurspose);
        description=findViewById(R.id.ReqDesc);
        reqid=findViewById(R.id.ReqID);
        submit=findViewById(R.id.new_req_submit_btn);

        Intent intent = getIntent();
        String honame = intent.getStringExtra("honame");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ReqId="R"+dReqId;;

        //SHOULD Verify this VALIDATION

//        do {
//            ReqId="RRU2JE2";
//            DocumentReference docRef = firestore.collection("Requirements").document(ReqId);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            String dReqId = randomString(6);
//                            Log.d("error", "Doc exist");
//                        } else {
//                            check="no";
//                            Log.d("error", "No such document");
//                        }
//                    } else {
//                        Log.d("error", "get failed with ", task.getException());
//                    }
//                }
//            });
//        }while (check.equals("yes"));

        reqid.setText(ReqId);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bloodGroup=spinner.getSelectedItem().toString();

                Map<String,Object> data= new HashMap<>();
                data.put("Requirement Id",ReqId);
                data.put("BloodGroup",bloodGroup);
                data.put("NoofBottles",bottleno.getText().toString());
                data.put("End Time",endtime.getText().toString());
                data.put("Purpose",purpose.getText().toString());
                data.put("Description",description.getText().toString());
                data.put("honame",honame);

                firestore.collection("Requirements").document(ReqId).set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(new_Req.this, "Requirement Added Succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(new_Req.this, "Requirement Not Added", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}