package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import co.kirel.drops.databinding.ActivityDonorHomeBinding;
import co.kirel.drops.databinding.ActivityHospitalHomeBinding;

public class hospital_home extends AppCompatActivity {

    ActivityHospitalHomeBinding binding;
    String semail,honame,code;
    ImageView newReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHospitalHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HosOrgHomeFragment());
        newReq=findViewById(R.id.new_req);

        Intent intent = getIntent();
        semail = intent.getStringExtra("Email");
        honame = intent.getStringExtra("honame");
        code = intent.getStringExtra("code");

        newReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(hospital_home.this,new_Req.class);
                i.putExtra("honame",honame);
                i.putExtra("code",code);
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

    public String getMyData() {
        return semail;
    }

    public String getHoname() { return honame; }
    public String getCode(){return code;}
}