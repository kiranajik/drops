package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {

    Context context;

    ArrayList<Rewards> rewardsArrayList;
    String email;

    public RewardAdapter(Context context, ArrayList<Rewards> rewardsArrayList,String email) {
        this.context=context;
        this.rewardsArrayList= rewardsArrayList;
        this.email = email;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.reward_card,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Rewards rewards = rewardsArrayList.get(position);
        holder.tvhn.setText(String.valueOf(rewards.cost));
        holder.tvbg.setText(rewards.gift);

        Context c = context.getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+rewards.company, null, c.getPackageName());
        holder.logo.setImageResource(id);
//          holder.logo.setImageResource(R.drawable.amazon);
//        holder.tvco.setText(rewards.company);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,rewardData.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("company",rewards.company);
                i.putExtra("gift",rewards.gift);
                i.putExtra("email",email);
                i.putExtra("cost",rewards.cost);
                i.putExtra("code",rewards.code);
                context.startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() {
        return rewardsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvhn,tvbg,tvco;
        CardView cardView;
        ImageView logo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhn=itemView.findViewById(R.id.coins);
//            tvco=itemView.findViewById(R.id.company_name);
            tvbg=itemView.findViewById(R.id.textView14);
            logo=itemView.findViewById(R.id.imageView7);

            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
