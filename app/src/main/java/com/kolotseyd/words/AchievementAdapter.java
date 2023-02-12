package com.kolotseyd.words;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Achievement> achievementList;

    AchievementAdapter(Context context, List<Achievement> achievementList){
        this.achievementList = achievementList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.achievement_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = achievementList.get(position);
        if (achievement.isCompleted()){
            holder.llAchievement.setBackground(achievement.getAchievementCorrectBackground());
        } else {
            holder.llAchievement.setBackground(achievement.getAchievementIncorrectBackground());
        }
        holder.tvAchievementTitle.setText(achievement.getAchievementTitle());
        holder.tvAchievementDescription.setText(achievement.getAchievementDescription());
        holder.tvAchievementProgress.setText(achievement.getAchievementProgress());
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAchievementTitle, tvAchievementDescription, tvAchievementProgress;
        final LinearLayout llAchievement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAchievementTitle = itemView.findViewById(R.id.tvAchievementTitle);
            tvAchievementDescription = itemView.findViewById(R.id.tvAchievementDescription);
            tvAchievementProgress = itemView.findViewById(R.id.tvAchievementProgress);
            llAchievement = itemView.findViewById(R.id.llAchievement);
        }
    }
}
