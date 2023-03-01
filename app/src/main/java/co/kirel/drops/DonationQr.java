package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class DonationQr extends AppCompatActivity {

    TextView qrhoname;
    ImageView qrimg;
    Button cancelqr,done;
    String ReqId,qrshoname,bldGrp,dnremail,qrid,dntnSts;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    String dDntnId = randomString(6);
    String DntnId="D"+dDntnId;
    String check="no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_qr);

        qrhoname=findViewById(R.id.qrhoname);
        qrimg=findViewById(R.id.qrcode);
        cancelqr=findViewById(R.id.bscan);
        done=findViewById(R.id.bdone);

        Intent i=getIntent();
        ReqId = i.getStringExtra("ReqId");
        qrshoname = i.getStringExtra("honame");
        bldGrp=i.getStringExtra("BldGrp");

        qrid=ReqId+DntnId;
        qrhoname.setText(qrshoname);

        //QT Maker
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix= writer.encode(qrid, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            qrimg.setImageBitmap(bitmap);

            InputMethodManager manager = (InputMethodManager) getSystemService(
                    Context .INPUT_METHOD_SERVICE
            );
        } catch (WriterException e) {
            e.printStackTrace();
        }

//        SharedPreferences sharedPref = getSharedPreferences("myNewKey", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("DntnId", DntnId);
//        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        dnremail = sharedPreferences.getString("donoremail","");

        Map<String,Object> dntndata= new HashMap<>();
        dntndata.put("DonationId",DntnId);
        dntndata.put("RequirementId",ReqId);
        dntndata.put("BloodGroup",bldGrp);
        dntndata.put("DonorId",dnremail);
        dntndata.put("DonationStatus","No");

        firestore.collection("Donations").document(DntnId).set(dntndata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DonationQr.this, "Donation Added Succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DonationQr.this, "Donation Not Added", Toast.LENGTH_SHORT).show();
                    }
                });

            DocumentReference docRefnc = firestore.collection("Donations").document(DntnId);
            docRefnc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            dntnSts = document.getString("DonationStatus");
                        } else {
                            Log.d("error", "No such document");
                        }
                    } else {
                        Log.d("error", "Firestore Error", task.getException());
                    }
                }
            });

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//
//                    if (dntnSts.equals("Yes"))
//                    {
//                        check="yes";
//                        Intent intent = new Intent(DonationQr.this,DonationSuccess.class);
//                        startActivity(intent);
//                    }
//                }
//            }, 1000);

        Toast.makeText(this, DntnId, Toast.LENGTH_SHORT).show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonationQr.this,DonationSuccess.class);
                startActivity(intent);
            }
        });

        cancelqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public String getDntnData() {
        return DntnId;
    }
}