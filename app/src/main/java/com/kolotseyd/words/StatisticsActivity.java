package com.kolotseyd.words;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kolotseyd.words.databinding.ActivityStatisticsBinding;

import java.text.DecimalFormat;

public class StatisticsActivity extends AppCompatActivity {

    ActivityStatisticsBinding binding;
    SharedPreferences sharedPrefWords;

    public static final String APP_PREFERENCES_PROMPTS_USED = "prompts_used";
    public static final String APP_PREFERENCES_GAMES_PLAYED = "games_played";
    public static final String APP_PREFERENCES_GAMES_WON = "games_won";
    public static final String APP_PREFERENCES_FIRST_WORD_WINS = "first_word_wins";
    public static final String APP_PREFERENCES_SECOND_WORD_WINS = "second_word_wins";
    public static final String APP_PREFERENCES_THIRD_WORD_WINS = "third_word_wins";
    public static final String APP_PREFERENCES_FOURTH_WORD_WINS = "fourth_word_wins";
    public static final String APP_PREFERENCES_FIFTH_WORD_WINS = "fifth_word_wins";
    public static final String APP_PREFERENCES_SIXTH_WORD_WINS = "sixth_word_wins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_statistics);

        sharedPrefWords = getSharedPreferences("preferences", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int gamesPlayed = sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0);
        int gamesWon = sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0);
        int firstWordWins = sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0);
        int secondWordWins = sharedPrefWords.getInt(APP_PREFERENCES_SECOND_WORD_WINS, 0);
        int thirdWordWins = sharedPrefWords.getInt(APP_PREFERENCES_THIRD_WORD_WINS, 0);
        int fourthWordWins = sharedPrefWords.getInt(APP_PREFERENCES_FOURTH_WORD_WINS, 0);
        int fifthWordWins = sharedPrefWords.getInt(APP_PREFERENCES_FIFTH_WORD_WINS, 0);
        int sixthWordWins = sharedPrefWords.getInt(APP_PREFERENCES_SIXTH_WORD_WINS, 0);
        int promptsUsed = sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_USED, 0);

        double averageTriesCount;
        if (gamesWon > 0){
            averageTriesCount = ((double) firstWordWins + secondWordWins * 2 + thirdWordWins * 3 + fourthWordWins * 4 + fifthWordWins * 5 + sixthWordWins * 6)/ (double) gamesWon;
        } else averageTriesCount = 0;
        String formattedAverageTriesCount = new DecimalFormat("#0.0").format(averageTriesCount);
        double percentWonGames;
        if (gamesPlayed > 0){
            percentWonGames = ((double) gamesWon / (double) gamesPlayed) * 100;
        } else percentWonGames = 0;
        String formattedPercentWonGames = new DecimalFormat("#0.0").format(percentWonGames);

        binding.tvGamesPlayedValue.setText(String.valueOf(gamesPlayed));
        binding.tvGamesWonValue.setText(String.valueOf(gamesWon));
        binding.tvPercentGamesWonValue.setText(formattedPercentWonGames + " %");
        binding.tvAverageTriesCountValue.setText(formattedAverageTriesCount);
        binding.tvPromptsUsed.setText(String.valueOf(promptsUsed));

        binding.pbFirstWordWinsCount.setMax(gamesWon);
        binding.pbSecondWordWinsCount.setMax(gamesWon);
        binding.pbThirdWordWinsCount.setMax(gamesWon);
        binding.pbFourthWordWinsCount.setMax(gamesWon);
        binding.pbFifthWordWinsCount.setMax(gamesWon);
        binding.pbSixthWordWinsCount.setMax(gamesWon);

        binding.pbFirstWordWinsCount.setProgress(firstWordWins);
        if (firstWordWins != 0){
            binding.tvFirstProgressBarValue.setText(String.valueOf(firstWordWins));
        }

        binding.pbSecondWordWinsCount.setProgress(secondWordWins);
        if (secondWordWins != 0){
            binding.tvSecondProgressBarValue.setText(String.valueOf(secondWordWins));
        }

        binding.pbThirdWordWinsCount.setProgress(thirdWordWins);
        if (thirdWordWins != 0){
            binding.tvThirdProgressBarValue.setText(String.valueOf(thirdWordWins));
        }

        binding.pbFourthWordWinsCount.setProgress(fourthWordWins);
        if (fourthWordWins != 0){
            binding.tvFourthProgressBarValue.setText(String.valueOf(fourthWordWins));
        }

        binding.pbFifthWordWinsCount.setProgress(fifthWordWins);
        if (fifthWordWins != 0){
            binding.tvFifthProgressBarValue.setText(String.valueOf(fifthWordWins));
        }

        binding.pbSixthWordWinsCount.setProgress(sixthWordWins);
        if (sixthWordWins != 0){
            binding.tvSixthProgressBarValue.setText(String.valueOf(sixthWordWins));
        }
    }

    public void onClickBackToMain(View view) {
        Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickGoToAchievementsScreen(View view) {
        Intent intent = new Intent(StatisticsActivity.this, AchievementsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
        startActivity(intent);
    }


}