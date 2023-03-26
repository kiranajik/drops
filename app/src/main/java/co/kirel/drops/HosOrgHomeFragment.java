package co.kirel.drops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HosOrgHomeFragment extends Fragment {

    TextView honame;
    String hoName,code;
    FirebaseFirestore firestore;
    private ArrayList<Requirements> reqsArraylist;
    private RecyclerView horecyclerView;
    homeReqAdapter hreqAdapter;

    public HosOrgHomeFragment() {
        // Required empty public constructor
    }

    public static HosOrgHomeFragment newInstance(String param1, String param2) {
        HosOrgHomeFragment fragment = new HosOrgHomeFragment();
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
        return inflater.inflate(R.layout.fragment_hos_org_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospital_home activity = (hospital_home) getActivity();
        code = activity.getHoCode();
        hoName = activity.getHoname();

        firestore= FirebaseFirestore.getInstance();
        honame=view.findViewById(R.id.honame);

        honame.setText(hoName);

//        //GETTING CURRENT DATE
//        Date cd = Calendar.getInstance().getTime();
//        System.out.println("Current time => " + cd);
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        String formattedDate = df.format(cd);
//        //ADDING 84 DAYS(12 Weeks)
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(formattedDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        c.add(Calendar.DATE, 40);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
//        String outputdate = sdf1.format(c.getTime());
//        Toast.makeText(getContext(), "Date: "+outputdate, Toast.LENGTH_SHORT).show();
//        //NEXT DONATION DATE DONE

        dataInitialize();

        horecyclerView=view.findViewById(R.id.horecyclerView);
        horecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        horecyclerView.setHasFixedSize(true);
        hreqAdapter = new homeReqAdapter(getContext(),reqsArraylist);
        horecyclerView.setAdapter(hreqAdapter);
        hreqAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        reqsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();;
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Requirements")
                        .whereEqualTo("Hospital Code",code)
                        .whereEqualTo("status","No")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (error!= null)
                                {
                                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore error",error.getMessage());
                                    return;
                                }

                                for (DocumentChange dc : value.getDocumentChanges())
                                {
                                    if (dc.getType() == DocumentChange.Type.ADDED)
                                    {
                                        reqsArraylist.add(dc.getDocument().toObject(Requirements.class));
                                    }
                                    hreqAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }

}