package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class myRewardData extends AppCompatActivity {
    TextView company,gift,rewcode;
    ImageView logo,copy;
    String Co,Gi,code;
    FirebaseFirestore db;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward_data);
        company = findViewById(R.id.CoCo);
        gift = findViewById(R.id.GiGi);
        rewcode = findViewById(R.id.RewCode);
        logo = findViewById(R.id.company_img);
        copy = findViewById(R.id.imageView11);
        Co = getIntent().getStringExtra("company");
        Gi = getIntent().getStringExtra("gift");
        code = getIntent().getStringExtra("code");
        storageReference = FirebaseStorage.getInstance().getReference("coImages/"+Co+".png");

        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            logo.setImageBitmap(bitmap);

                            Toast.makeText(myRewardData.this,"Image Loaded",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("error",e.toString());
                            Toast.makeText(myRewardData.this,"Image not Loaded",Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        company.setText(Co.toUpperCase());
        gift.setText(Gi);
        rewcode.setText(code);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Reward Value",code);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "The Reward Code Copied To Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }
}