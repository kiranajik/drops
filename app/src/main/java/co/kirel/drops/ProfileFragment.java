package co.kirel.drops;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    Button donation, request;
    View view;
    String pname;
    String myEmail;
    static String email="" ;
    FirebaseFirestore firestore;
    TextView unamep;
    TextView refbtn;

    FloatingActionButton editdnrimg;
    ImageView DnrImg;
    StorageReference storageReference;
    ActivityResultLauncher<String> mTakePhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        super.onCreate(savedInstanceState);
        donor_home activity = (donor_home) getActivity();
        myEmail = activity.getMyData();
        replaceFragment(new DonationsFragment());


        firestore=FirebaseFirestore.getInstance();

        donation = view.findViewById(R.id.donationbtn);
        request = view.findViewById(R.id.requestbtn);
        unamep = view.findViewById(R.id.profileuname);
        refbtn = view.findViewById(R.id.refbtn);

        editdnrimg = view.findViewById(R.id.editimg);
        DnrImg = view.findViewById(R.id.DnrImg);

        DocumentReference docRef = firestore.collection("Donor").document(myEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        pname= document.getString("Name");
                        unamep.setText(pname);
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        //SETTING IMAGE
        storageReference = FirebaseStorage.getInstance().getReference("dnrImages/"+myEmail);

        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            DnrImg.setImageBitmap(bitmap);

                            Toast.makeText(getContext(),"Image Loaded",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("error",e.toString());
                            Toast.makeText(getContext(),"Image not Loaded",Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        DnrImg.setImageURI(result);
                        //imageUri=result;
                        storageReference = FirebaseStorage.getInstance().getReference("dnrImages/"+myEmail);
                        storageReference.putFile(result)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Toast.makeText(getContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Image not Uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

        //EDIT Image


        //EDIT Image

        //SETTING IMAGE



        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new DonationsFragment());
                donation.setBackgroundColor(Color.parseColor("#E26864"));
                donation.setTextColor(Color.WHITE);
                request.setBackgroundColor(Color.parseColor("#4B69D7"));
                request.setTextColor(Color.WHITE);
            }
        });

        refbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),Referrals.class);
                startActivity(i);
            }
        });

        editdnrimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhoto.launch("image/*");
                Toast.makeText(getContext(),"Edit Profile Image",Toast.LENGTH_SHORT).show();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RequestsFragment());
                request.setBackgroundColor(Color.parseColor("#E26864"));
                request.setTextColor(Color.WHITE);
                donation.setBackgroundColor(Color.parseColor("#4B69D7"));
                donation.setTextColor(Color.WHITE);
            }
        });
        return view;

    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager= getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("email", myEmail);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}