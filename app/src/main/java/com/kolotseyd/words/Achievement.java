package com.kolotseyd.words;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class Achievement {
    private Drawable achievementCorrectBackground;
    private Drawable achievementIncorrectBackground;
    private String achievementTitle;
    private String achievementDescription;
    private String achievementProgress;
    private boolean isCompleted;

    public Achievement(Drawable achievementCorrectBackground, Drawable achievementIncorrectBackground, String achievementTitle, String achievementDescription, String achievementProgress, boolean isCompleted) {
        this.achievementCorrectBackground = achievementCorrectBackground;
        this.achievementIncorrectBackground = achievementIncorrectBackground;
        this.achievementTitle = achievementTitle;
        this.achievementDescription = achievementDescription;
        this.achievementProgress = achievementProgress;
        this.isCompleted = isCompleted;
    }

    public Drawable getAchievementCorrectBackground() {
        return achievementCorrectBackground;
    }

    public void setAchievementCorrectBackground(Drawable achievementCorrectBackground) {
        this.achievementCorrectBackground = achievementCorrectBackground;
    }

    public Drawable getAchievementIncorrectBackground() {
        return achievementIncorrectBackground;
    }

    public void setAchievementIncorrectBackground(Drawable achievementIncorrectBackground) {
        this.achievementIncorrectBackground = achievementIncorrectBackground;
    }

    public String getAchievementTitle() {
        return achievementTitle;
    }

    public void setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }

    public String getAchievementProgress() {
        return achievementProgress;
    }

    public void setAchievementProgress(String achievementProgress) {
        this.achievementProgress = achievementProgress;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}