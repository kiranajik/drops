package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Button cancelqr;
    String ReqId,qrshoname,bldGrp,dnremail;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    String dDntnId = randomString(6);
    String DntnId="D"+dDntnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_qr);

        qrhoname=findViewById(R.id.qrhoname);
        qrimg=findViewById(R.id.qrcode);
        cancelqr=findViewById(R.id.bscan);

        Intent i=getIntent();
        ReqId = i.getStringExtra("ReqId");
        qrshoname = i.getStringExtra("honame");
        bldGrp=i.getStringExtra("BldGrp");

        qrhoname.setText(qrshoname);

        //QT Maker
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix= writer.encode(ReqId, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            qrimg.setImageBitmap(bitmap);

            InputMethodManager manager = (InputMethodManager) getSystemService(
                    Context .INPUT_METHOD_SERVICE
            );
        } catch (WriterException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getSharedPreferences("myNewKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("DntnId", DntnId);
        editor.apply();

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

        Toast.makeText(this, DntnId, Toast.LENGTH_SHORT).show();

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