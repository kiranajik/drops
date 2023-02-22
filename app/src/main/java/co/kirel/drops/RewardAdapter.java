package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {

    Context context;

    ArrayList<Rewards> rewardsArrayList;

    public RewardAdapter(Context context, ArrayList<Rewards> rewardsArrayList) {
        this.context=context;
        this.rewardsArrayList= rewardsArrayList;
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
        holder.tvhn.setText(rewards.company);
        holder.tvbg.setText(rewards.gift);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,rewardData.class);
                i.putExtra("company",rewards.company);
                i.putExtra("gift",rewards.gift);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return rewardsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvhn,tvbg;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhn=itemView.findViewById(R.id.textView12);
            tvbg=itemView.findViewById(R.id.textView14);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
