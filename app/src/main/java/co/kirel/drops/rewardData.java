package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class rewardData extends AppCompatActivity {
    TextView company,gift;
    String Co,Gi,email;
    ImageView companyimg;
    Integer myCoins,cost;
    Button redbtn;
    StorageReference storageReference;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_data);
        redbtn = findViewById(R.id.redbtn);
        company = findViewById(R.id.CoCo);
        gift = findViewById(R.id.GiGi);
        companyimg = findViewById(R.id.company_img);

        Co = getIntent().getStringExtra("company");
        Gi = getIntent().getStringExtra("gift");
        cost = getIntent().getIntExtra("cost",0);
        email = getIntent().getStringExtra("email");
        company.setText(Co);
        gift.setText(Gi);
        storageReference = FirebaseStorage.getInstance().getReference("coImages/"+Co+".png");

        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            companyimg.setImageBitmap(bitmap);

                            Toast.makeText(rewardData.this,"Image Loaded",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("error",e.toString());
                            Toast.makeText(rewardData.this,"Image not Loaded",Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get a reference to the document
        DocumentReference docRef = firestore.collection("Donor").document(email);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the data of a single field
                    myCoins = Math.toIntExact(documentSnapshot.getLong("LifeCoins"));
                }
            }
        });
        redbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cost <= myCoins){
                    Map<String,Object> data= new HashMap<>();
                    Map<String,Object> dataD= new HashMap<>();
                    data.put("Redeemer",email);
                    dataD.put("LifeCoins",myCoins-cost);
                    firestore.collection("Donor").document(email).update(dataD);
                    firestore.collection("Rewards").document(Co).update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(rewardData.this, "Yes", Toast.LENGTH_SHORT).show();
                                    Intent i= new Intent(rewardData.this,donor_home.class);
                                    i.putExtra("Email",email);
                                    i.putExtra("source","reward");
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(rewardData.this, "FireStoreNo", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }
}