package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import co.kirel.drops.databinding.ActivityDonorHomeBinding;
import co.kirel.drops.databinding.ActivityMainBinding;

public class donor_home extends AppCompatActivity {
    ActivityDonorHomeBinding binding;
    String semail;
    String nxtDntnDate;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDonorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        semail = intent.getStringExtra("Email");

        DocumentReference docRefnc = firestore.collection("Donor").document(semail);
        docRefnc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nxtDntnDate = document.getString("nxtDntnDate");
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "Firestore Error", task.getException());
                }
            }
        });

        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("donoremail", semail);
        editor.apply();

        String source = intent.getStringExtra("source");
        String frame = "Donations";
        if(source != null && source.equals("reward")){
            replaceFragment(new RewardFragment());
        } else if (source != null && source.equals("profile")) {
                replaceFragment(new ProfileFragment());

        } else{
            replaceFragment(new HomeFragment());
        }
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
                    replaceFragment(new ProfileFragment());
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

    public String getNxtDntnDate() { return nxtDntnDate; }
}