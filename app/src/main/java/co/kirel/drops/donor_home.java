package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import co.kirel.drops.databinding.ActivityDonorHomeBinding;
import co.kirel.drops.databinding.ActivityMainBinding;

public class donor_home extends AppCompatActivity {
    ActivityDonorHomeBinding binding;
    String semail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDonorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        semail = intent.getStringExtra("Email");

        SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("donoremail", semail);
        editor.apply();

        String source = intent.getStringExtra("source");
        String frame = "Donations";
        if(source != null && source.equals("reward")){
            replaceFragment(new RewardFragment());
        } else if (source != null && source.equals("profile")) {
            if(intent.getStringExtra("frame").equals("Donations")){
                replaceFragment(new ProfileFragment(frame));
            }else{
                frame = "Requests";
                replaceFragment(new ProfileFragment(frame));
            }
        } else{
            replaceFragment(new HomeFragment());
        }

        String finalFrame = frame;
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.placeholder:
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment(finalFrame));
                    break;
                case R.id.reward:
                    replaceFragment(new RewardFragment());
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
}