package co.kirel.drops;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.Map;

public class HosOrgScanner extends Fragment {
    Button scan,dcancel,dconfirm;
    EditText botno;
    String ReqId,QRid,DntnId,shoName;
    String sgethoname,sbotlno,sgotbtlno;
    FirebaseFirestore firestore,db;

    public HosOrgScanner() {
        // Required empty public constructor
    }
    public static HosOrgScanner newInstance(String param1, String param2) {
        HosOrgScanner fragment = new HosOrgScanner();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hos_org_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scan=view.findViewById(R.id.bscan);
        firestore= FirebaseFirestore.getInstance();
        db=FirebaseFirestore.getInstance();
        hospital_home activity = (hospital_home) getActivity();
        shoName = activity.getHoname();
        scan.setOnClickListener(v->
        {
            scanCode();
        });
    }

    private void scanCode() {
        ScanOptions  options = new ScanOptions();
        options.setPrompt("Volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScannerActivity.class);
        options.setBarcodeImageEnabled(true);
        barLauncher.launch(options);
    }

    //RO7H5M7

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result -> {
        Toast.makeText(getActivity(),result.getContents(),Toast.LENGTH_SHORT).show();
        QRid=result.getContents();
        ReqId=QRid.substring(0,7);
        DntnId=QRid.substring(7,14);
        Toast.makeText(getActivity(),DntnId,Toast.LENGTH_SHORT).show();
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
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "Firestore Error", task.getException());
                }
            }
        });

//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myNewKey", MODE_PRIVATE);
//        DntnId = sharedPreferences.getString("DntnId","");
//        Toast.makeText(getActivity(), DntnId, Toast.LENGTH_SHORT).show();
//        DonationQr activity = (DonationQr) getActivity();
//        DntnId = activity.getDntnData();
        //DntnId="RP3AN8D";

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (/*sgethoname.equals(shoName)*/true) {

                    View alertCustomDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottle_dialog, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//TRY

                    builder.setView(alertCustomDialog);

                    dconfirm=alertCustomDialog.findViewById(R.id.confirm_dntn_dialg_btn);
                    dcancel=alertCustomDialog.findViewById(R.id.cancel_dntn_dialg_btn);
                    botno=alertCustomDialog.findViewById(R.id.newdesc);
                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            Toast.makeText(getContext(),"Donation not Done",Toast.LENGTH_SHORT).show();
                        }
                    });

                    dconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sbotlno=botno.getText().toString();
                            int balance=Integer.parseInt(sgotbtlno) + Integer.parseInt(sbotlno);
                            String balanceBtl = String.valueOf(balance);
                            Map<String,Object> data= new HashMap<>();
                            data.put("btlsgot",balanceBtl);
                            firestore.collection("Requirements").document(ReqId).update(data);

                            Map<String,Object> dntndata= new HashMap<>();
                            dntndata.put("DonationStatus","Yes");
                            db.collection("Donations").document(DntnId).update(dntndata);

                            Intent i =new Intent(getContext(),DonationSuccess.class);
                            startActivity(i);
                            dialog.cancel();
                        }
                    });
                    dialog.show();

//                    builder.setTitle("Enter the No of Bottles");
//                    final EditText botlno = new EditText(getContext());
//                    botlno.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    builder.setView(botlno);
//                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    sbotlno=botlno.getText().toString();
//                                    int balance=Integer.parseInt(sgotbtlno) + Integer.parseInt(sbotlno);
//
//                                    String balanceBtl = String.valueOf(balance);
//                                    Map<String,Object> data= new HashMap<>();
//                                    data.put("btlsgot",balanceBtl);
//                                    firestore.collection("Requirements").document(ReqId).update(data);
//
//                                    Map<String,Object> dntndata= new HashMap<>();
//                                    dntndata.put("DonationStatus","Yes");
//                                    db.collection("Donations").document(DntnId).update(dntndata);
//
//                                    Intent i =new Intent(getContext(),DonationSuccess.class);
//                                    startActivity(i);
//                                }
//                            });
//                    builder.setNegativeButton("Cancel", null);
//                    builder.show();
                } else {
                    Toast.makeText(getActivity(), "Invalid QR", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);

    });
}