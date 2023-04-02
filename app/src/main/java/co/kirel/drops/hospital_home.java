package co.kirel.drops;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import com.travijuu.numberpicker.library.NumberPicker;

import java.util.HashMap;
import java.util.Map;

import co.kirel.drops.databinding.ActivityDonorHomeBinding;
import co.kirel.drops.databinding.ActivityHospitalHomeBinding;

public class hospital_home extends AppCompatActivity {

    ActivityHospitalHomeBinding binding;
    String semail,honame,hoCode;
    FloatingActionButton newReq,scanQr;

    //FOR SCANNER
    Button dcancel,dconfirm;
    String ReqId,QRid,DntnId,shoName;
    String sgethoname,sbotlno,sgotbtlno;
    String sreqbltno;
    FirebaseFirestore firestore,db;
    NumberPicker numberPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHospitalHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HosOrgHomeFragment());
        scanQr=findViewById(R.id.fabscanr);
        newReq=findViewById(R.id.fab);

        firestore= FirebaseFirestore.getInstance();
        db=FirebaseFirestore.getInstance();
        hospital_home activity = new hospital_home();
        shoName = activity.getHoname();
        scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        Intent intent = getIntent();
        semail = intent.getStringExtra("Email");
        honame = intent.getStringExtra("honame");
        hoCode = intent.getStringExtra("hoCode");

        newReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(hospital_home.this,new_Req.class);
                i.putExtra("honame",honame);
                startActivity(i);
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.hohome:
                    replaceFragment(new HosOrgHomeFragment());
                    break;
                case R.id.hodonations:
                    replaceFragment(new HosOrgListFragment());
                    break;
                case R.id.hoplaceholder:
                    break;
                case R.id.hoprofile:
                    replaceFragment(new HosOrgProfileFragment());
                    break;
                case R.id.hoscanner:
                    replaceFragment(new HosOrgScanner());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    public String getMyData() { return semail; }

    public String getHoname() { return honame; }

    public String getHoCode() { return hoCode; }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScannerActivity.class);
        options.setBarcodeImageEnabled(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        Toast.makeText(hospital_home.this,result.getContents(),Toast.LENGTH_SHORT).show();
        QRid=result.getContents();
        ReqId=QRid.substring(0,7);
        DntnId=QRid.substring(7,14);
        Toast.makeText(hospital_home.this,DntnId,Toast.LENGTH_SHORT).show();
        //ReqId="RO7H5M7";

        //Firebase
        DocumentReference docRefnc = firestore.collection("Requirements").document(ReqId);
        docRefnc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        sgethoname = document.getString("honame");
                        sgotbtlno= document.getString("btlsgot");
                        sreqbltno= document.getString("NoofBottles");
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "Firestore Error", task.getException());
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (/*sgethoname.equals(shoName)*/true) {

                    View alertCustomDialog = LayoutInflater.from(hospital_home.this).inflate(R.layout.bottle_dialog, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(hospital_home.this);//TRY
                    builder.setView(alertCustomDialog);

                    dconfirm=alertCustomDialog.findViewById(R.id.confirm_dntn_dialg_btn);
                    dcancel=alertCustomDialog.findViewById(R.id.cancel_dntn_dialg_btn);
                    numberPicker = (NumberPicker) alertCustomDialog.findViewById(R.id.number_picker);

                    int balancebtl=Integer.parseInt(sreqbltno) - Integer.parseInt(sgotbtlno);
                    Toast.makeText(hospital_home.this,sreqbltno+" "+sgotbtlno,Toast.LENGTH_SHORT).show();
                    numberPicker.setMax(balancebtl);

                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            Toast.makeText(hospital_home.this,"Donation not Done",Toast.LENGTH_SHORT).show();
                        }
                    });

                    dconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int botlno=numberPicker.getValue();
                            sbotlno=String.valueOf(botlno);
                            int balance=Integer.parseInt(sgotbtlno) + Integer.parseInt(sbotlno);
                            String balanceBtl = String.valueOf(balance);
                            Map<String,Object> data= new HashMap<>();
                            data.put("btlsgot",balanceBtl);
                            if (balanceBtl.equals(sreqbltno))
                            {
                                data.put("status","Yes");
                            }
                            firestore.collection("Requirements").document(ReqId).update(data);

                            Map<String,Object> dntndata= new HashMap<>();
                            dntndata.put("DonationStatus","Yes");
                            dntndata.put("btlsdonated",sbotlno);
                            db.collection("Donations").document(DntnId).update(dntndata);

                            Intent i =new Intent(hospital_home.this,DonationSuccess.class);
                            startActivity(i);
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(hospital_home.this, "Invalid QR", Toast.LENGTH_SHORT).show();
                }
            }
        }, 800);
    });
}