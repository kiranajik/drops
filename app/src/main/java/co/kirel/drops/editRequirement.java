package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class editRequirement extends AppCompatActivity {
    EditText bottleno;
    TextView reqid,tvendtime,enddate;
    Button submit,cancel;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    String ReqId,bottles,date,time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_requirement);

        bottleno=findViewById(R.id.NoBottles);
        enddate=findViewById(R.id.endDate);
        tvendtime=findViewById(R.id.endTime);
        reqid=findViewById(R.id.ReqID);
        submit=findViewById(R.id.new_req_submit_btn);
        cancel=findViewById(R.id.new_req_delete_btn);

        Intent intent = getIntent();
        ReqId = intent.getStringExtra("ReqId");
        bottles = intent.getStringExtra("bottles");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");


        reqid.setText(ReqId);
        bottleno.setText(bottles);
        enddate.setText(date);
        tvendtime.setText(time);



        //DATE PICKER

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(editRequirement.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, year, month, day);
                datePickerDialog.show();
            }
        });

        tvendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(editRequirement.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvendtime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> data= new HashMap<>();
                data.put("NoofBottles",bottleno.getText().toString());
                data.put("EndDate",enddate.getText().toString());
                data.put("EndTime",tvendtime.getText().toString());
                firestore.collection("Requirements").document(ReqId).update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(editRequirement.this, "Requirement Updated Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(editRequirement.this, "Requirement Not Updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Requirements").document(ReqId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(editRequirement.this, "Requirement Deleted Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(editRequirement.this, "Requirement Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });

    }
}