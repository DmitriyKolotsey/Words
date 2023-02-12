package com.kolotseyd.words;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

public class AchievementsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES_GAMES_PLAYED = "games_played";
    public static final String APP_PREFERENCES_GAMES_WON = "games_won";
    public static final String APP_PREFERENCES_FIRST_WORD_WINS = "first_word_wins";

    public static final String APP_PREFERENCES_ACHIEVE_5_GAMES_PLAYED = "achieve_5_games_played";
    public static final String APP_PREFERENCES_ACHIEVE_10_GAMES_PLAYED = "achieve_10_games_played";
    public static final String APP_PREFERENCES_ACHIEVE_25_GAMES_PLAYED = "achieve_25_games_played";
    public static final String APP_PREFERENCES_ACHIEVE_50_GAMES_PLAYED = "achieve_50_games_played";
    public static final String APP_PREFERENCES_ACHIEVE_100_GAMES_PLAYED = "achieve_100_games_played";
    public static final String APP_PREFERENCES_ACHIEVE_5_GAMES_WON = "achieve_5_games_won";
    public static final String APP_PREFERENCES_ACHIEVE_10_GAMES_WON = "achieve_10_games_won";
    public static final String APP_PREFERENCES_ACHIEVE_25_GAMES_WON = "achieve_25_games_won";
    public static final String APP_PREFERENCES_ACHIEVE_50_GAMES_WON = "achieve_50_games_won";
    public static final String APP_PREFERENCES_ACHIEVE_100_GAMES_WON = "achieve_100_games_won";
    public static final String APP_PREFERENCES_ACHIEVE_1_FIRST_WORD_WINS = "achieve_1_first_word_wins";
    public static final String APP_PREFERENCES_ACHIEVE_3_FIRST_WORD_WINS = "achieve_3_first_word_wins";
    public static final String APP_PREFERENCES_ACHIEVE_5_FIRST_WORD_WINS = "achieve_5_first_word_wins";
    public static final String APP_PREFERENCES_ACHIEVE_10_FIRST_WORD_WINS = "achieve_10_first_word_wins";

    ArrayList<Achievement> achievements = new ArrayList<>();
    SharedPreferences sharedPrefWords;

    TextView tvAchievementsCompeted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_achievements);

        sharedPrefWords = getSharedPreferences("preferences", MODE_PRIVATE);

        setInitialData();

        RecyclerView recyclerView = findViewById(R.id.rvAchievements);
        tvAchievementsCompeted = findViewById(R.id.tvAchievementsCompleted);
        int achievementsCompleted = 0;
        for (int i = 0; i < achievements.size(); i++){

            if (achievements.get(i).isCompleted()){
                achievementsCompleted++;
            }
        }

        tvAchievementsCompeted.setText(achievementsCompleted + "/14");

        AchievementAdapter adapter = new AchievementAdapter(this, achievements);
        recyclerView.setAdapter(adapter);

    }

    public void onClickBackToStatistics(View view) {
        Intent intent = new Intent(AchievementsActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void setInitialData(){
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Новичок", "Сыграть 5 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_GAMES_PLAYED, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_GAMES_PLAYED, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Игрок", "Сыграть 10 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_GAMES_PLAYED, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_GAMES_PLAYED, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Опытный игрок", "Сыграть 25 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_25_GAMES_PLAYED, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_25_GAMES_PLAYED, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Ветеран", "Сыграть 50 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_50_GAMES_PLAYED, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_50_GAMES_PLAYED, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Игроман", "Сыграть 100 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_100_GAMES_PLAYED, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_100_GAMES_PLAYED, 0)));

        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Умник", "Выиграть 5 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_GAMES_WON, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_GAMES_WON, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Интеллектуал", "Выиграть 10 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_GAMES_WON, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_GAMES_WON, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Гений", "Выиграть 25 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_25_GAMES_WON, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_25_GAMES_WON, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Мегамозг", "Выиграть 50 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_50_GAMES_WON, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_50_GAMES_WON, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Сверхразум", "Выиграть 100 игр",
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_100_GAMES_WON, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_100_GAMES_WON, 0)));

        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Экстрасенс", "Угадать 1 слово с первой попытки",
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_1_FIRST_WORD_WINS, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_1_FIRST_WORD_WINS, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Колдун", "Угадать 3 слова с первой попытки",
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_3_FIRST_WORD_WINS, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_3_FIRST_WORD_WINS, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "ПолуБог", "Угадать 5 слов с первой попытки",
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_FIRST_WORD_WINS, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_FIRST_WORD_WINS, 0)));
        achievements.add(new Achievement(getDrawable(R.drawable.round_stat_layouts_correct), getDrawable(R.drawable.round_stat_layouts), "Бог", "Угадать 10 слов с первой попытки",
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) + "/" +
                        sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_FIRST_WORD_WINS, 0),
                sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) >= sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_FIRST_WORD_WINS, 0)));

    }
}