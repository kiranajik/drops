package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class org_reg extends AppCompatActivity {
    EditText horoname,addr,year,authname,authdesig,phno,noofVol;
    TextView org_code_gen;
    Button orgsignup;
    RadioButton typeh,typeo;
    String selected;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    String org_code = randomString(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_reg);

        horoname=findViewById(R.id.edName);
        addr=findViewById(R.id.edAddress);
        year=findViewById(R.id.edyear);
        authname=findViewById(R.id.edauthpers);
        authdesig=findViewById(R.id.eddesig);
        phno=findViewById(R.id.edPhno);
        noofVol=findViewById(R.id.ednoofvol);
        orgsignup=findViewById(R.id.bconfirm);
        typeh=findViewById(R.id.typehos);
        typeo=findViewById(R.id.typeorg);
        org_code_gen = findViewById(R.id.org_code_gen);

        Intent intent = getIntent();
        String sorgemail = intent.getStringExtra("OrgEmail");
        String sorgpaswd = intent.getStringExtra("OrgPassword");



//
//            org_code_gen.setText("Code: "+"O"+org_code);





        orgsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Toast.makeText(org_reg.this, "Email: "+sorgpaswd, Toast.LENGTH_SHORT).show();

                auth.createUserWithEmailAndPassword(sorgemail,sorgpaswd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (typeh.isChecked()) {
                                    selected = typeh.getText().toString();
                                } else if (typeo.isChecked()) {
                                    selected = typeo.getText().toString();
                                }

                                if (selected.equals("Organization"))
                                {
                                    Map<String,Object> data= new HashMap<>();
                                    data.put("Organization Name",horoname.getText().toString());
                                    data.put("Address",addr.getText().toString());
                                    data.put("Year of Establishment",year.getText().toString());
                                    data.put("Authorised Person",authname.getText().toString());
                                    data.put("Designation",authdesig.getText().toString());
                                    data.put("Phone Number",phno.getText().toString());
                                    data.put("Number of Volunteers",noofVol.getText().toString());
                                    data.put("Organization Code","O"+org_code);
                                    data.put("Verified","no");
                                    data.put("Role","ORG");


                                    firestore.collection(selected).document(sorgemail).set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(org_reg.this, "Yes", Toast.LENGTH_SHORT).show();
                                                    Intent i= new Intent(org_reg.this,donor_login.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(org_reg.this, "No", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else if (selected.equals("Hospital"))
                                {
                                    Map<String,Object> data= new HashMap<>();
                                    data.put("Hospital Name",horoname.getText().toString());
                                    data.put("Address",addr.getText().toString());
                                    data.put("Year of Establishment",year.getText().toString());
                                    data.put("Authorised Person",authname.getText().toString());
                                    data.put("Designation",authdesig.getText().toString());
                                    data.put("Phone Number",phno.getText().toString());
                                    data.put("Number of Volunteers",noofVol.getText().toString());
                                    data.put("Hospital Code","H"+org_code);
                                    data.put("Verified","no");
                                    data.put("Role","HOSP");


                                    firestore.collection(selected).document(sorgemail).set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(org_reg.this, "Yes", Toast.LENGTH_SHORT).show();
                                                    Intent i= new Intent(org_reg.this,donor_login.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(org_reg.this, "No", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(org_reg.this,"Unsuccesfull",Toast.LENGTH_SHORT).show();
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
    public void onClickedHospital(View view) {
 //       Toast.makeText(this, "Lamborghini is selected for a drive!", Toast.LENGTH_SHORT).show();
        org_code_gen.setText("Hospital Code: "+"H"+org_code);
    }
    public void onClickedOrg(View view) {
   //     Toast.makeText(this, "Lamborghini is selected for a drive!", Toast.LENGTH_SHORT).show();
        org_code_gen.setText("Oganization Code: "+"O"+org_code);
    }
}