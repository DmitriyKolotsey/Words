package com.kolotseyd.words;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.kolotseyd.words.Data.DataBaseHelper;
import com.kolotseyd.words.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements EndGameFragment.onCloseEndGameFragmentEventListener, NoPromptsFragment.onCloseNoPromptsFragmentEvent {

    TextView tvToastText;
    View toastLayout;

    DialogFragment endGameFragment, noPromptsFragment;
    Bundle bundleDataForFragment;

    private boolean isFirstWordEntered = false, isSecondWordEntered  = false, isThirdWordEntered  = false, isFourthWordEntered  = false,
                isFifthWordEntered  = false, isSixthWordEntered  = false,
            isFirstLetterEntered = false, isSecondLetterEntered  = false, isThirdLetterEntered  = false, isFourthLetterEntered  = false,
                isFifthLetterEntered = false, isFirstWord = false, isSixthWord = false;
    public boolean isLoading = false;

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase wordsDataBase;
    Cursor wordsCursor;

    ActivityMainBinding binding;

    Animation animationButtonsScale;
    Animation animationWordLettersScale;
    Animation animationWordLettersRotate;

    HashMap<String, TextView> keyboardLettersHashMap;
    ArrayList<String> correctLettersList;
    ArrayList<String> anotherPositionLettersList;
    ArrayList<String> incorrectLettersList;

    private final int wordsInDataBaseCount = 1147;

    private String rewardedAdUnitId = "ca-app-pub-8542657706295420/1989141342";
    private RewardedAd rewardedAd;

    public static final String APP_PREFERENCES = "preferences";
    public static final String APP_PREFERENCES_SEARCH_WORD = "search_word";
    public static final String APP_PREFERENCES_IS_WORD_GUESSED = "is_word_guessed";
    public static final String APP_PREFERENCES_IS_GAME_ENDED = "is_game_ended";
    public static final String APP_PREFERENCES_ENTERED_WORDS_COUNT = "entered_words_count";
    public static final String APP_PREFERENCES_FIRST_ENTERED_WORD = "first_entered_word";
    public static final String APP_PREFERENCES_SECOND_ENTERED_WORD = "second_entered_word";
    public static final String APP_PREFERENCES_THIRD_ENTERED_WORD = "third_entered_word";
    public static final String APP_PREFERENCES_FOURTH_ENTERED_WORD = "fourth_entered_word";
    public static final String APP_PREFERENCES_FIFTH_ENTERED_WORD = "fifth_entered_word";
    public static final String APP_PREFERENCES_PROMPTS_COUNT = "prompts_count";
    public static final String APP_PREFERENCES_PROMPTS_USED = "prompts_used";
    public static final String APP_PREFERENCES_IS_FIRST_TIME = "is_first_time";
    public static final String APP_PREFERENCES_GAMES_PLAYED = "games_played";
    public static final String APP_PREFERENCES_GAMES_WON = "games_won";
    public static final String APP_PREFERENCES_FIRST_WORD_WINS = "first_word_wins";
    public static final String APP_PREFERENCES_SECOND_WORD_WINS = "second_word_wins";
    public static final String APP_PREFERENCES_THIRD_WORD_WINS = "third_word_wins";
    public static final String APP_PREFERENCES_FOURTH_WORD_WINS = "fourth_word_wins";
    public static final String APP_PREFERENCES_FIFTH_WORD_WINS = "fifth_word_wins";
    public static final String APP_PREFERENCES_SIXTH_WORD_WINS = "sixth_word_wins";

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

    SharedPreferences sharedPrefWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.d("TAG", "onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        dataBaseHelper.create_db();
        wordsDataBase = dataBaseHelper.open();

        animationButtonsScale = AnimationUtils.loadAnimation(this, R.anim.buttons_scale);
        animationWordLettersScale = AnimationUtils.loadAnimation(this, R.anim.word_letters_scale);
        animationWordLettersRotate = AnimationUtils.loadAnimation(this, R.anim.word_letters_alpha);

        endGameFragment = new EndGameFragment();
        noPromptsFragment = new NoPromptsFragment();
        bundleDataForFragment = new Bundle();
        endGameFragment.setArguments(bundleDataForFragment);

        keyboardLettersHashMap = new HashMap<>();

        correctLettersList = new ArrayList<>();
        anotherPositionLettersList = new ArrayList<>();
        incorrectLettersList = new ArrayList<>();

        sharedPrefWords = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        setAchievementsGoals();

        if (sharedPrefWords.getBoolean(APP_PREFERENCES_IS_FIRST_TIME, true)){
            SharedPreferences.Editor editor = sharedPrefWords.edit();
            editor.putBoolean(APP_PREFERENCES_IS_FIRST_TIME, false);
            editor.putInt(APP_PREFERENCES_PROMPTS_COUNT, 1);
            editor.putInt(APP_PREFERENCES_PROMPTS_USED, 0);
            editor.apply();
        }

        if (sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0) != 0){
            binding.tvPromptsCount.setText(String.valueOf(sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0)));
        } else binding.tvPromptsCount.setVisibility(View.INVISIBLE);

        if (sharedPrefWords.getString(APP_PREFERENCES_FIRST_ENTERED_WORD, " ").equals(" ")){
            startNewGame();
            Log.d("TAG", sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD, ""));
        } else {
            resumeGame();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobileAds.initialize(this, initializationStatus -> {
        });
        loadRewardedAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFifthWordEntered){
            SharedPreferences.Editor editor = sharedPrefWords.edit();
            editor.putBoolean(APP_PREFERENCES_IS_GAME_ENDED, true);
            editor.apply();
        }
        Log.d("TAG", "onDestroy");
        //wordsCursor.close();
        //wordsDataBase.close();
    }

    public void onClickEnterLetterInWord(View view) {
        view.startAnimation(animationButtonsScale);
        if (!isFirstWordEntered){
            addLetters(view, binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter,
                    binding.tvFirstWordFourthLetter, binding.tvFirstWordFifthLetter);
        } else if (!isSecondWordEntered) {
            addLetters(view, binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter,
                    binding.tvSecondWordFourthLetter, binding.tvSecondWordFifthLetter);
        } else if (!isThirdWordEntered) {
            addLetters(view, binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter,
                    binding.tvThirdWordFourthLetter, binding.tvThirdWordFifthLetter);
        } else if (!isFourthWordEntered) {
            addLetters(view, binding.tvFourthWordFirstLetter, binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter,
                    binding.tvFourthWordFourthLetter, binding.tvFourthWordFifthLetter);
        } else if (!isFifthWordEntered) {
            addLetters(view, binding.tvFifthWordFirstLetter, binding.tvFifthWordSecondLetter, binding.tvFifthWordThirdLetter,
                    binding.tvFifthWordFourthLetter, binding.tvFifthWordFifthLetter);
        } else if (!isSixthWordEntered) {
            addLetters(view, binding.tvSixthWordFirstLetter, binding.tvSixthWordSecondLetter, binding.tvSixthWordThirdLetter,
                    binding.tvSixthWordFourthLetter, binding.tvSixthWordFifthLetter);
        }

    }

    public void onClickDeleteLetterInWord(View view) {
        view.startAnimation(animationButtonsScale);
        if (!isFirstWordEntered) {
            deleteLetter(binding.tvFirstWordFifthLetter, binding.tvFirstWordFourthLetter, binding.tvFirstWordThirdLetter,
                    binding.tvFirstWordSecondLetter, binding.tvFirstWordFirstLetter);
        } else if (!isSecondWordEntered){
            deleteLetter(binding.tvSecondWordFifthLetter, binding.tvSecondWordFourthLetter, binding.tvSecondWordThirdLetter,
                    binding.tvSecondWordSecondLetter, binding.tvSecondWordFirstLetter);
        } else if (!isThirdWordEntered){
            deleteLetter(binding.tvThirdWordFifthLetter, binding.tvThirdWordFourthLetter, binding.tvThirdWordThirdLetter,
                    binding.tvThirdWordSecondLetter, binding.tvThirdWordFirstLetter);
        } else if (!isFourthWordEntered){
            deleteLetter(binding.tvFourthWordFifthLetter, binding.tvFourthWordFourthLetter, binding.tvFourthWordThirdLetter,
                    binding.tvFourthWordSecondLetter, binding.tvFourthWordFirstLetter);
        } else if (!isFifthWordEntered){
            deleteLetter(binding.tvFifthWordFifthLetter, binding.tvFifthWordFourthLetter, binding.tvFifthWordThirdLetter,
                    binding.tvFifthWordSecondLetter, binding.tvFifthWordFirstLetter);
        } else if (!isSixthWordEntered){
            deleteLetter(binding.tvSixthWordFifthLetter, binding.tvSixthWordFourthLetter, binding.tvSixthWordThirdLetter,
                    binding.tvSixthWordSecondLetter, binding.tvSixthWordFirstLetter);
        }
    }

    public void onClickCheckWord(View view) {
        view.startAnimation(animationButtonsScale);
        if (!isFirstWordEntered){
            checkWord(binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter,
                    binding.tvFirstWordFourthLetter, binding.tvFirstWordFifthLetter);
        } else if (!isSecondWordEntered){
            checkWord(binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter,
                    binding.tvSecondWordFourthLetter, binding.tvSecondWordFifthLetter);

        } else if (!isThirdWordEntered){
            checkWord(binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter,
                    binding.tvThirdWordFourthLetter, binding.tvThirdWordFifthLetter);

        } else if (!isFourthWordEntered){
            checkWord(binding.tvFourthWordFirstLetter, binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter,
                    binding.tvFourthWordFourthLetter, binding.tvFourthWordFifthLetter);

        } else if (!isFifthWordEntered){
            checkWord(binding.tvFifthWordFirstLetter, binding.tvFifthWordSecondLetter, binding.tvFifthWordThirdLetter,
                    binding.tvFifthWordFourthLetter, binding.tvFifthWordFifthLetter);

        } else if (!isSixthWordEntered){
            checkWord(binding.tvSixthWordFirstLetter, binding.tvSixthWordSecondLetter, binding.tvSixthWordThirdLetter,
                    binding.tvSixthWordFourthLetter, binding.tvSixthWordFifthLetter);
        }
    }

    public void onClickGetPrompt(View view) {
        if (sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0) > 0){
            String searchWord = sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD, "");
            ArrayList<String> searchWordArrayList = new ArrayList<>();
            searchWordArrayList.add(searchWord.substring(0,1));
            searchWordArrayList.add(searchWord.substring(1,2));
            searchWordArrayList.add(searchWord.substring(2,3));
            searchWordArrayList.add(searchWord.substring(3,4));
            searchWordArrayList.add(searchWord.substring(4,5));

            for (int i = 0; i < searchWordArrayList.size(); i++){
                String letterForPrompt = searchWordArrayList.get(i);
                if (!correctLettersList.contains(letterForPrompt.toUpperCase(Locale.ROOT))
                        && !anotherPositionLettersList.contains(letterForPrompt.toUpperCase(Locale.ROOT))){
                    showCustomToast(getResources().getString(R.string.prompt_letter_in_word) +
                            " '" + letterForPrompt.toUpperCase(Locale.ROOT) + "'");
                    SharedPreferences.Editor editor = sharedPrefWords.edit();
                    editor.putInt(APP_PREFERENCES_PROMPTS_COUNT, sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0) - 1);
                    editor.putInt(APP_PREFERENCES_PROMPTS_USED, sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_USED, 0) + 1);
                    editor.apply();
                    break;
                } else if (!correctLettersList.contains(letterForPrompt.toUpperCase(Locale.ROOT))
                    && anotherPositionLettersList.contains(searchWordArrayList.get(0).toUpperCase(Locale.ROOT))
                    && anotherPositionLettersList.contains(searchWordArrayList.get(1).toUpperCase(Locale.ROOT))
                    && anotherPositionLettersList.contains(searchWordArrayList.get(2).toUpperCase(Locale.ROOT))
                    && anotherPositionLettersList.contains(searchWordArrayList.get(3).toUpperCase(Locale.ROOT))
                    && anotherPositionLettersList.contains(searchWordArrayList.get(4).toUpperCase(Locale.ROOT))){

                    showCustomToast(getResources().getString(R.string.all_needed_letters_guessed));
                    break;
                }
            }
            if (sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0) != 0){
                binding.tvPromptsCount.setText(String.valueOf(sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0)));
                binding.tvPromptsCount.setVisibility(View.VISIBLE);
            } else binding.tvPromptsCount.setVisibility(View.INVISIBLE);

        } else getMorePrompts();
    }

    public void onClickGoToStatisticScreen(View view) {
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void getMorePrompts(){
          noPromptsFragment.show(getSupportFragmentManager(), "noPromptsFragment");
    }

    public void startNewGame(){
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        editor.putBoolean(APP_PREFERENCES_IS_GAME_ENDED, false);
        editor.apply();
        clearAllForNewGame();
        getWordFromDataBase();
    }

    @Override
    public void closeEndGameFragmentEvent() {
        startNewGame();
    }

    @Override
    public void closeNoPromptsFragmentEvent() {
        binding.tvPromptsCount.setText(String.valueOf(sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0)));
        if (sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0) != 0){
            binding.tvPromptsCount.setVisibility(View.VISIBLE);
        }

    }

    public void resumeGame(){
        int enteredWordsCount = sharedPrefWords.getInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 0);
        Log.d("TAG", String.valueOf(sharedPrefWords.getInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 0)) + " "
        + isFirstWordEntered + " "  + isSecondWordEntered + " "  + isThirdWordEntered + " "  + isFourthWordEntered + " "
                + isFifthWordEntered + " "  + isSixthWordEntered);

        String firstWord = sharedPrefWords.getString(APP_PREFERENCES_FIRST_ENTERED_WORD, "");
        String secondWord = sharedPrefWords.getString(APP_PREFERENCES_SECOND_ENTERED_WORD, "");
        String thirdWord = sharedPrefWords.getString(APP_PREFERENCES_THIRD_ENTERED_WORD, "");
        String fourthWord = sharedPrefWords.getString(APP_PREFERENCES_FOURTH_ENTERED_WORD, "");
        String fifthWord = sharedPrefWords.getString(APP_PREFERENCES_FIFTH_ENTERED_WORD, "");

        switch (enteredWordsCount){
            case 1:
                restoreWordFromSharedPreferences(firstWord,
                        binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);

                checkWordAfterResume(firstWord, binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                break;
            case 2:
                restoreWordFromSharedPreferences(firstWord,
                        binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                restoreWordFromSharedPreferences(secondWord,
                        binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);

                checkWordAfterResume(firstWord, binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                checkWordAfterResume(secondWord, binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);

                break;
            case 3:
                restoreWordFromSharedPreferences(firstWord,
                        binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                restoreWordFromSharedPreferences(secondWord,
                        binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);
                restoreWordFromSharedPreferences(thirdWord,
                        binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter,
                        binding.tvThirdWordFifthLetter);

                checkWordAfterResume(firstWord, binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                checkWordAfterResume(secondWord, binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);
                checkWordAfterResume(thirdWord, binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter,
                        binding.tvThirdWordFifthLetter);

                break;
            case 4:
                restoreWordFromSharedPreferences(firstWord,
                        binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                restoreWordFromSharedPreferences(secondWord,
                        binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);
                restoreWordFromSharedPreferences(thirdWord,
                        binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter,
                        binding.tvThirdWordFifthLetter);
                restoreWordFromSharedPreferences(fourthWord,
                        binding.tvFourthWordFirstLetter, binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter, binding.tvFourthWordFourthLetter,
                        binding.tvFourthWordFifthLetter);

                checkWordAfterResume(firstWord, binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                checkWordAfterResume(secondWord, binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);
                checkWordAfterResume(thirdWord, binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter,
                        binding.tvThirdWordFifthLetter);
                checkWordAfterResume(fourthWord, binding.tvFourthWordFirstLetter, binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter, binding.tvFourthWordFourthLetter,
                        binding.tvFourthWordFifthLetter);

                break;
            case 5:
                restoreWordFromSharedPreferences(firstWord,
                        binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                restoreWordFromSharedPreferences(secondWord,
                        binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);
                restoreWordFromSharedPreferences(thirdWord,
                        binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter,
                        binding.tvThirdWordFifthLetter);
                restoreWordFromSharedPreferences(fourthWord,
                        binding.tvFourthWordFirstLetter, binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter, binding.tvFourthWordFourthLetter,
                        binding.tvFourthWordFifthLetter);
                restoreWordFromSharedPreferences(fifthWord,
                        binding.tvFifthWordFirstLetter, binding.tvFifthWordSecondLetter, binding.tvFifthWordThirdLetter, binding.tvFifthWordFourthLetter,
                        binding.tvFifthWordFifthLetter);

                checkWordAfterResume(firstWord, binding.tvFirstWordFirstLetter, binding.tvFirstWordSecondLetter, binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                        binding.tvFirstWordFifthLetter);
                checkWordAfterResume(secondWord, binding.tvSecondWordFirstLetter, binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter,
                        binding.tvSecondWordFifthLetter);
                checkWordAfterResume(thirdWord, binding.tvThirdWordFirstLetter, binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter,
                        binding.tvThirdWordFifthLetter);
                checkWordAfterResume(fourthWord, binding.tvFourthWordFirstLetter, binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter, binding.tvFourthWordFourthLetter,
                        binding.tvFourthWordFifthLetter);
                checkWordAfterResume(fifthWord, binding.tvFifthWordFirstLetter, binding.tvFifthWordSecondLetter, binding.tvFifthWordThirdLetter, binding.tvFifthWordFourthLetter,
                        binding.tvFifthWordFifthLetter);
                break;
        }
        showCustomToast(getResources().getString(R.string.game_resumed));
    }

    public void endGame(){
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        editor.putBoolean(APP_PREFERENCES_IS_GAME_ENDED, true);
        editor.putInt(APP_PREFERENCES_GAMES_PLAYED, sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0) + 1);
        editor.putString(APP_PREFERENCES_FIRST_ENTERED_WORD, " ");
        editor.apply();
        Log.d("TAG", String.valueOf(sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0)));
        correctLettersList.clear();
        anotherPositionLettersList.clear();
        incorrectLettersList.clear();

        clearAllForNewGame();
        checkAchievementsComplete();

        endGameFragment.show(getSupportFragmentManager(), "endGameFragment");
    }

    public void checkWord(TextView firstLetter, TextView secondLetter, TextView thirdLetter, TextView fourthLetter, TextView fifthLetter){
        String enteredWord = "'" + firstLetter.getText().toString().toLowerCase(Locale.ROOT) + secondLetter.getText().toString().toLowerCase(Locale.ROOT)
                + thirdLetter.getText().toString().toLowerCase(Locale.ROOT)
                + fourthLetter.getText().toString().toLowerCase(Locale.ROOT) + fifthLetter.getText().toString().toLowerCase(Locale.ROOT) + "'";

        wordsCursor = wordsDataBase.rawQuery("SELECT word FROM words WHERE word =" + enteredWord, null);
        wordsCursor.moveToFirst();

        SharedPreferences.Editor editor = sharedPrefWords.edit();

        if (enteredWord.length() != 7){
            showCustomToast(getResources().getString(R.string.not_all_letters_entered));
        } else {
            if (wordsCursor.getCount() == 0){
                showCustomToast(getResources().getString(R.string.no_such_word));
            } else {
                wordEntered();
                if (enteredWord.substring(1,6).equals(sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD,""))){
                    if (!isFirstWordEntered){
                        isFirstWord = true;
                        wordIsGuessed(firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        editor.putInt(APP_PREFERENCES_FIRST_WORD_WINS, sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0) + 1);
                        isFirstWordEntered = true;
                        isFirstWord = false;
                        bundleDataForFragment.putInt("attempts_count", 1);
                    } else if (!isSecondWordEntered){
                        wordIsGuessed(firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        editor.putInt(APP_PREFERENCES_SECOND_WORD_WINS, sharedPrefWords.getInt(APP_PREFERENCES_SECOND_WORD_WINS, 0) + 1);
                        isSecondWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 2);
                    } else if (!isThirdWordEntered){
                        wordIsGuessed(firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        editor.putInt(APP_PREFERENCES_THIRD_WORD_WINS, sharedPrefWords.getInt(APP_PREFERENCES_THIRD_WORD_WINS, 0) + 1);
                        isThirdWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 3);
                    } else if (!isFourthWordEntered){
                        wordIsGuessed(firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        editor.putInt(APP_PREFERENCES_FOURTH_WORD_WINS, sharedPrefWords.getInt(APP_PREFERENCES_FOURTH_WORD_WINS, 0) + 1);
                        isFourthWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 4);
                    } else if (!isFifthWordEntered){
                        wordIsGuessed(firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        editor.putInt(APP_PREFERENCES_FIFTH_WORD_WINS, sharedPrefWords.getInt(APP_PREFERENCES_FIFTH_WORD_WINS, 0) + 1);
                        isFifthWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 5);
                    } else if (!isSixthWordEntered){
                        isSixthWord = true;
                        wordIsGuessed(firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        editor.putInt(APP_PREFERENCES_SIXTH_WORD_WINS, sharedPrefWords.getInt(APP_PREFERENCES_SIXTH_WORD_WINS, 0) + 1);
                        isSixthWordEntered = true;
                        isSixthWord = false;
                        bundleDataForFragment.putInt("attempts_count", 6);
                    }

                    editor.putBoolean(APP_PREFERENCES_IS_WORD_GUESSED, true);
                    editor.putInt(APP_PREFERENCES_GAMES_WON, sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0) +1);
                    editor.apply();

                    bundleDataForFragment.putBoolean("is_word_guessed", sharedPrefWords.getBoolean(APP_PREFERENCES_IS_WORD_GUESSED,true));
                    bundleDataForFragment.putString("search_word", sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD, ""));

                    endGame();

                } else {
                    if (!isFirstWordEntered){
                        isFirstWord = true;
                        checkWordsByLetters(enteredWord.substring(1,6), firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        isFirstWordEntered = true;
                        isFirstWord = false;
                        bundleDataForFragment.putInt("attempts_count", 1);
                        editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT,1);
                        editor.putString(APP_PREFERENCES_FIRST_ENTERED_WORD, enteredWord.substring(1,6));
                        editor.apply();
                    } else if (!isSecondWordEntered){
                        checkWordsByLetters(enteredWord.substring(1,6), firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        isSecondWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 2);
                        editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT,2);
                        editor.putString(APP_PREFERENCES_SECOND_ENTERED_WORD, enteredWord.substring(1,6));
                        editor.apply();
                    } else if (!isThirdWordEntered){
                        checkWordsByLetters(enteredWord.substring(1,6), firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        isThirdWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 3);
                        editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT,3);
                        editor.putString(APP_PREFERENCES_THIRD_ENTERED_WORD, enteredWord.substring(1,6));
                        editor.apply();
                    } else if (!isFourthWordEntered){
                        checkWordsByLetters(enteredWord.substring(1,6), firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        isFourthWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 4);
                        editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT,4);
                        editor.putString(APP_PREFERENCES_FOURTH_ENTERED_WORD, enteredWord.substring(1,6));
                        editor.apply();
                    } else if (!isFifthWordEntered){
                        checkWordsByLetters(enteredWord.substring(1,6), firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        isFifthWordEntered = true;
                        bundleDataForFragment.putInt("attempts_count", 5);
                        editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT,5);
                        editor.putString(APP_PREFERENCES_FIFTH_ENTERED_WORD, enteredWord.substring(1,6));
                        editor.apply();
                    } else if (!isSixthWordEntered){
                        isSixthWord = true;
                        checkWordsByLetters(enteredWord.substring(1,6), firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
                        isSixthWordEntered = true;
                        isSixthWord = false;

                        editor.putBoolean(APP_PREFERENCES_IS_WORD_GUESSED, false);
                        editor.apply();

                        bundleDataForFragment.putInt("attempts_count", 6);
                        bundleDataForFragment.putBoolean("is_word_guessed", sharedPrefWords.getBoolean(APP_PREFERENCES_IS_WORD_GUESSED, false));
                        bundleDataForFragment.putString("search_word", sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD, ""));

                        endGame();
                    }
                }
            }
        }
    }

    public void checkWordAfterResume(String word,TextView firstLetter, TextView secondLetter, TextView thirdLetter, TextView fourthLetter, TextView fifthLetter) {
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        if (!isFirstWordEntered) {
            isFirstWord = true;
            checkWordsByLetters(word, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
            isFirstWordEntered = true;
            isFirstWord = false;
            bundleDataForFragment.putInt("attempts_count", 1);
            editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 1);
            editor.putString(APP_PREFERENCES_FIRST_ENTERED_WORD, word);
            editor.apply();
        } else if (!isSecondWordEntered) {
            checkWordsByLetters(word, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
            isSecondWordEntered = true;
            bundleDataForFragment.putInt("attempts_count", 2);
            editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 2);
            editor.putString(APP_PREFERENCES_SECOND_ENTERED_WORD, word);
            editor.apply();
        } else if (!isThirdWordEntered) {
            checkWordsByLetters(word, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
            isThirdWordEntered = true;
            bundleDataForFragment.putInt("attempts_count", 3);
            editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 3);
            editor.putString(APP_PREFERENCES_THIRD_ENTERED_WORD, word);
            editor.apply();
        } else if (!isFourthWordEntered) {
            checkWordsByLetters(word, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
            isFourthWordEntered = true;
            bundleDataForFragment.putInt("attempts_count", 4);
            editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 4);
            editor.putString(APP_PREFERENCES_FOURTH_ENTERED_WORD, word);
            editor.apply();
        } else if (!isFifthWordEntered) {
            checkWordsByLetters(word, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter);
            isFifthWordEntered = true;
            bundleDataForFragment.putInt("attempts_count", 5);
            editor.putInt(APP_PREFERENCES_ENTERED_WORDS_COUNT, 5);
            editor.putString(APP_PREFERENCES_FIFTH_ENTERED_WORD, word);
            editor.apply();
        }
        bundleDataForFragment.putString("search_word", sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD, ""));
    }

    public void checkWordsByLetters(String enteredWord, TextView firstLetter, TextView secondLetter,
                                    TextView thirdLetter, TextView fourthLetter, TextView fifthLetter){
        String searchWord = sharedPrefWords.getString(APP_PREFERENCES_SEARCH_WORD, "");

        ArrayList<String> enteredWordArrayList = new ArrayList<>();
        ArrayList<String> searchWordArrayList = new ArrayList<>();

        for (int i = 0; i < searchWord.length(); i++){
            searchWordArrayList.add(String.valueOf(searchWord.charAt(i)).toUpperCase(Locale.ROOT));
            enteredWordArrayList.add(String.valueOf(enteredWord.charAt(i)).toUpperCase(Locale.ROOT));
        }

        for (int i = 0; i < enteredWordArrayList.size(); i++){
            if (searchWordArrayList.contains(enteredWordArrayList.get(i))){
                if (enteredWordArrayList.get(i).equals(searchWordArrayList.get(i))) {
                    Log.d("TAG", enteredWordArrayList.get(i) + " " +
                            searchWordArrayList.get(i) + " " + enteredWordArrayList.get(i).equals(searchWordArrayList.get(i)));
                    if (!correctLettersList.contains(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT))){
                        correctLettersList.add(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT));
                        if (correctLettersList.size() != 0){
                            Log.d("TAG", correctLettersList.get(0));
                        }
                    }

                    wordIsNotGuessed(i, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter,
                            getDrawable(R.drawable.textview_correct), getDrawable(R.drawable.textview_topright_correct_round_corner),
                            getDrawable(R.drawable.textview_topleft_correct_round_corner), getDrawable(R.drawable.textview_bottomright_correct_round_corner),
                            getDrawable(R.drawable.textview_bottomleft_correct_round_corner), getColor(R.color.white));
                } else {
                    if (!correctLettersList.contains(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT))
                            && !anotherPositionLettersList.contains(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT))){
                        anotherPositionLettersList.add(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT));
                    }

                    wordIsNotGuessed(i, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter,
                            getDrawable(R.drawable.textview_another_position), getDrawable(R.drawable.textview_topright_another_position_round_corner),
                            getDrawable(R.drawable.textview_topleft_another_position_round_corner), getDrawable(R.drawable.textview_bottomright_another_position_round_corner),
                            getDrawable(R.drawable.textview_bottomleft_another_position_round_corner), getColor(R.color.white));
                }
            } else {
                if (!correctLettersList.contains(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT))
                        && !anotherPositionLettersList.contains(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT))
                        && !incorrectLettersList.contains(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT))){
                    incorrectLettersList.add(enteredWordArrayList.get(i).toUpperCase(Locale.ROOT));

                }

                wordIsNotGuessed(i, firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter,
                        getDrawable(R.drawable.textview_incorrect), getDrawable(R.drawable.textview_topright_incorrect_round_corner),
                        getDrawable(R.drawable.textview_topleft_incorrect_round_corner), getDrawable(R.drawable.textview_bottomright_incorrect_round_corner),
                        getDrawable(R.drawable.textview_bottomleft_incorrect_round_corner), getColor(R.color.grey));
            }
            if (i < correctLettersList.size()){
                Log.d("TAG", "correct " + correctLettersList.get(i));
            }

            if (i < anotherPositionLettersList.size()){
                Log.d("TAG", "another_pos " + anotherPositionLettersList.get(i));
            }

            if (i < incorrectLettersList.size()){
                Log.d("TAG", "incorrect " + incorrectLettersList.get(i));
            }
        }
    }

    public void getWordFromDataBase(){
        Random random = new Random();
        int random_id = random.nextInt(wordsInDataBaseCount-1);
        random_id++;
        Log.d("TAG", "getWord");
        wordsCursor = wordsDataBase.rawQuery("SELECT word FROM words WHERE id=" + random_id, null);
        wordsCursor.moveToFirst();
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        editor.putString(APP_PREFERENCES_SEARCH_WORD, wordsCursor.getString(0));
        bundleDataForFragment.putString("search_word", wordsCursor.getString(0));
        editor.apply();
        showCustomToast(getResources().getString(R.string.word_is_selected));
    }

    public void showCustomToast(String textForToast){
        LayoutInflater inflater = getLayoutInflater();
        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toastLayout));

        tvToastText = (TextView) toastLayout.findViewById(R.id.tvToastText);
        tvToastText.setText(textForToast);
        tvToastText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvToastText.getText().subSequence(0, 1) == "Д"){
                    Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
                    startActivity(intent);
                }
            }
        });

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();

    }

    public void addLetters(View v, TextView firstLetter, TextView secondLetter, TextView thirdLetter, TextView fourthLetter, TextView fifthLetter){
        if(!isFirstLetterEntered){
            firstLetter.startAnimation(animationWordLettersScale);
            firstLetter.setText(((TextView) v).getText());
            isFirstLetterEntered = true;
        } else if (!isSecondLetterEntered){
            secondLetter.startAnimation(animationWordLettersScale);
            secondLetter.setText(((TextView) v).getText());
            isSecondLetterEntered = true;
        } else if (!isThirdLetterEntered){
            thirdLetter.startAnimation(animationWordLettersScale);
            thirdLetter.setText(((TextView) v).getText());
            isThirdLetterEntered = true;
        } else if (!isFourthLetterEntered){
            fourthLetter.startAnimation(animationWordLettersScale);
            fourthLetter.setText(((TextView) v).getText());
            isFourthLetterEntered = true;
        } else if (!isFifthLetterEntered){
            fifthLetter.startAnimation(animationWordLettersScale);
            fifthLetter.setText(((TextView) v).getText());
            isFifthLetterEntered = true;
        }
    }

    public void deleteLetter(TextView fifthLetter, TextView fourthLetter, TextView thirdLetter, TextView secondsLetter, TextView firstLetter){
        if (isFifthLetterEntered) {
            fifthLetter.startAnimation(animationWordLettersScale);
            fifthLetter.setText("");
            isFifthLetterEntered = false;
        } else if (isFourthLetterEntered) {
            fourthLetter.startAnimation(animationWordLettersScale);
            fourthLetter.setText("");
            isFourthLetterEntered = false;
        } else if (isThirdLetterEntered) {
            thirdLetter.startAnimation(animationWordLettersScale);
            thirdLetter.setText("");
            isThirdLetterEntered = false;
        } else if (isSecondLetterEntered) {
            secondsLetter.startAnimation(animationWordLettersScale);
            secondsLetter.setText("");
            isSecondLetterEntered = false;
        } else if (isFirstLetterEntered) {
            firstLetter.startAnimation(animationWordLettersScale);
            firstLetter.setText("");
            isFirstLetterEntered = false;
        }
    }

    public void wordEntered(){
        isFirstLetterEntered = false;
        isSecondLetterEntered = false;
        isThirdLetterEntered = false;
        isFourthLetterEntered = false;
        isFifthLetterEntered = false;
    }

    public void restoreWordFromSharedPreferences(String word, TextView firstLetter, TextView secondLetter, TextView thirdLetter, TextView fourthLetter, TextView fifthLetter){
        firstLetter.setText(word.substring(0,1).toUpperCase(Locale.ROOT));
        secondLetter.setText(word.substring(1,2).toUpperCase(Locale.ROOT));
        thirdLetter.setText(word.substring(2,3).toUpperCase(Locale.ROOT));
        fourthLetter.setText(word.substring(3,4).toUpperCase(Locale.ROOT));
        fifthLetter.setText(word.substring(4,5).toUpperCase(Locale.ROOT));
    }

    public void setLetterBackground(TextView letterTextView, Drawable drawable, int color){
        animationWordLettersRotate(letterTextView);
        letterTextView.setBackground(drawable);
        letterTextView.setTextColor(color);
        setKeyboardLettersBackground(letterTextView);
    }

    public void wordIsGuessed(TextView firstLetter, TextView secondLetter, TextView thirdLetter, TextView fourthLetter, TextView fifthLetter){
        if (isFirstWord){
            setLetterBackground(firstLetter, getDrawable(R.drawable.textview_topleft_correct_round_corner), getColor(R.color.white));
        } else if (isSixthWord) {
            setLetterBackground(firstLetter, getDrawable(R.drawable.textview_bottomleft_correct_round_corner), getColor(R.color.white));
        } else {
            setLetterBackground(firstLetter, getDrawable(R.drawable.textview_correct), getColor(R.color.white));
        }
        setLetterBackground(secondLetter, getDrawable(R.drawable.textview_correct), getColor(R.color.white));
        setLetterBackground(thirdLetter, getDrawable(R.drawable.textview_correct), getColor(R.color.white));
        setLetterBackground(fourthLetter, getDrawable(R.drawable.textview_correct), getColor(R.color.white));
        if (isFirstWord){
            setLetterBackground(fifthLetter, getDrawable(R.drawable.textview_topright_correct_round_corner), getColor(R.color.white));
        } else if (isSixthWord) {
            setLetterBackground(fifthLetter, getDrawable(R.drawable.textview_bottomright_correct_round_corner), getColor(R.color.white));
        } else {
            setLetterBackground(fifthLetter, getDrawable(R.drawable.textview_correct), getColor(R.color.white));
        }
    }

    public void wordIsNotGuessed(int i, TextView firstLetter, TextView secondLetter, TextView thirdLetter, TextView fourthLetter, TextView fifthLetter,
                                 Drawable drawableCenter, Drawable drawableTopRight, Drawable drawableTopLeft, Drawable drawableBottomRight,
                                 Drawable drawableBottomLeft, int color){
        switch (i) {
            case 0:
                if (isFirstWord){
                    setLetterBackground(firstLetter, drawableTopLeft, color);
                } else if (isSixthWord) {
                    setLetterBackground(firstLetter, drawableBottomLeft, color);
                } else {
                    setLetterBackground(firstLetter, drawableCenter, color);
                }
            case 1:
                setLetterBackground(secondLetter, drawableCenter, color);
            case 2:
                setLetterBackground(thirdLetter, drawableCenter, color);
            case 3:
                setLetterBackground(fourthLetter, drawableCenter, color);
            case 4:
                if (isFirstWord){
                    setLetterBackground(fifthLetter, drawableTopRight, color);
                } else if (isSixthWord) {
                    setLetterBackground(fifthLetter, drawableBottomRight, color);
                } else {
                    setLetterBackground(fifthLetter, drawableCenter, color);
                }
        }
    }

    public void setKeyboardLettersBackground(TextView lettersTextView){
        keyboardLettersHashMap.put(String.valueOf(binding.b1Letter.getText()), binding.b1Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b2Letter.getText()), binding.b2Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b3Letter.getText()), binding.b3Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b4Letter.getText()), binding.b4Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b5Letter.getText()), binding.b5Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b6Letter.getText()), binding.b6Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b7Letter.getText()), binding.b7Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b8Letter.getText()), binding.b8Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b9Letter.getText()), binding.b9Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b10Letter.getText()), binding.b10Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b11Letter.getText()), binding.b11Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b12Letter.getText()), binding.b12Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b13Letter.getText()), binding.b13Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b14Letter.getText()), binding.b14Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b15Letter.getText()), binding.b15Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b16Letter.getText()), binding.b16Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b17Letter.getText()), binding.b17Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b18Letter.getText()), binding.b18Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b19Letter.getText()), binding.b19Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b20Letter.getText()), binding.b20Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b21Letter.getText()), binding.b21Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b22Letter.getText()), binding.b22Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b23Letter.getText()), binding.b23Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b24Letter.getText()), binding.b24Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b25Letter.getText()), binding.b25Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b26Letter.getText()), binding.b26Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b27Letter.getText()), binding.b27Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b28Letter.getText()), binding.b28Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b29Letter.getText()), binding.b29Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b30Letter.getText()), binding.b30Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b31Letter.getText()), binding.b31Letter);
        keyboardLettersHashMap.put(String.valueOf(binding.b32Letter.getText()), binding.b32Letter);

        for (int i = 0; i <= keyboardLettersHashMap.size(); i ++){
            if (correctLettersList.size() != 0){
                for (int j = 0; j < correctLettersList.size(); j++){
                    keyboardLettersHashMap.get(correctLettersList.get(j)).setBackground(getDrawable(R.drawable.button_correct_round_corners));
                }
            }
            if (anotherPositionLettersList.size() != 0){
                for (int k = 0; k < anotherPositionLettersList.size(); k++){
                    if (!correctLettersList.contains(anotherPositionLettersList.get(k))){
                        keyboardLettersHashMap.get(anotherPositionLettersList.get(k)).setBackground(getDrawable(R.drawable.button_another_position_round_corners));
                    }
                }
            }
            if (incorrectLettersList.size() != 0){
                for (int m = 0; m < incorrectLettersList.size(); m++){
                    keyboardLettersHashMap.get(incorrectLettersList.get(m)).setBackground(getDrawable(R.drawable.button_incorrect_round_corners));
                }
            }
        }
    }

    public void animationWordLettersRotate(View rotationView){
        YoYo.with(Techniques.FlipInX).duration(500).playOn(rotationView);
    }

    public void loadRewardedAd(){
        if (rewardedAd == null){
            Log.d("TAG", "loadRewardedAd");
            isLoading = true;
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            RewardedAd.load(this, rewardedAdUnitId, adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    MainActivity.this.rewardedAd = rewardedAd;
                    MainActivity.this.isLoading = false;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.d("TAG", loadAdError.getMessage());
                    rewardedAd = null;
                    MainActivity.this.isLoading = false;
                }
            });
        }
    }

    public void showRewardedVideo(){
        Log.d("TAG", "showRewardedVideo");
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                Log.d("TAG", "onAdShowedFullScreenContent");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                rewardedAd = null;
                Log.d("TAG", "onAdFailedToShowFullScreenContent");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d("TAG", "onAdDismissedFullScreenContent");
                rewardedAd = null;
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                //MainActivity.this.loadRewardedAd();
            }
        });

        Activity activityContext = MainActivity.this;
        rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Log.d("TAG", "The user earned the reward.");
                SharedPreferences.Editor editor = sharedPrefWords.edit();
                editor.putInt(APP_PREFERENCES_PROMPTS_COUNT, sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0) + 1);
                editor.apply();
                binding.tvPromptsCount.setText(String.valueOf(sharedPrefWords.getInt(APP_PREFERENCES_PROMPTS_COUNT, 0)));
                binding.tvPromptsCount.setVisibility(View.VISIBLE);
            }
        });
    }

    public void clearAllForNewGame(){
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        editor.putBoolean(APP_PREFERENCES_IS_WORD_GUESSED, false);
        editor.apply();

        isFirstWordEntered = false;
        isSecondWordEntered  = false;
        isThirdWordEntered  = false;
        isFourthWordEntered  = false;
        isFifthWordEntered  = false;
        isSixthWordEntered  = false;

        isFirstLetterEntered = false;
        isSecondLetterEntered  = false;
        isThirdLetterEntered  = false;
        isFourthLetterEntered  = false;
        isFifthLetterEntered = false;

        isFirstWord = false;
        isSixthWord = false;

        clearWordsTextView(getDrawable(R.drawable.textview_default),binding.tvFirstWordFirstLetter, getDrawable(R.drawable.textview_topleft_default_round_corner)
                ,binding.tvFirstWordSecondLetter ,binding.tvFirstWordThirdLetter, binding.tvFirstWordFourthLetter,
                binding.tvFirstWordFifthLetter, getDrawable(R.drawable.textview_topright_default_round_corner));
        clearWordsTextView(getDrawable(R.drawable.textview_default),binding.tvSecondWordFirstLetter, getDrawable(R.drawable.textview_default),
                binding.tvSecondWordSecondLetter, binding.tvSecondWordThirdLetter, binding.tvSecondWordFourthLetter
                , binding.tvSecondWordFifthLetter, getDrawable(R.drawable.textview_default));
        clearWordsTextView(getDrawable(R.drawable.textview_default), binding.tvThirdWordFirstLetter, getDrawable(R.drawable.textview_default),
                binding.tvThirdWordSecondLetter, binding.tvThirdWordThirdLetter, binding.tvThirdWordFourthLetter
                , binding.tvThirdWordFifthLetter, getDrawable(R.drawable.textview_default));
        clearWordsTextView(getDrawable(R.drawable.textview_default), binding.tvFourthWordFirstLetter, getDrawable(R.drawable.textview_default),
                binding.tvFourthWordSecondLetter, binding.tvFourthWordThirdLetter, binding.tvFourthWordFourthLetter
                , binding.tvFourthWordFifthLetter, getDrawable(R.drawable.textview_default));
        clearWordsTextView(getDrawable(R.drawable.textview_default), binding.tvFifthWordFirstLetter, getDrawable(R.drawable.textview_default),
                binding.tvFifthWordSecondLetter, binding.tvFifthWordThirdLetter, binding.tvFifthWordFourthLetter
                , binding.tvFifthWordFifthLetter, getDrawable(R.drawable.textview_default));
        clearWordsTextView(getDrawable(R.drawable.textview_default), binding.tvSixthWordFirstLetter, getDrawable(R.drawable.textview_bottomleft_default_round_corner),
                binding.tvSixthWordSecondLetter, binding.tvSixthWordThirdLetter, binding.tvSixthWordFourthLetter
                , binding.tvSixthWordFifthLetter, getDrawable(R.drawable.textview_bottomright_default_round_corner));

        Iterator it = keyboardLettersHashMap.entrySet().iterator();
        TextView keyboardLetter;
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            keyboardLetter = (TextView) pair.getValue();
            keyboardLetter.setBackground(getResources().getDrawable(R.drawable.button_default_round_corners));
        }

    }

    public void clearWordsTextView(Drawable lettersDrawable,TextView firstLetter, Drawable firstLetterDrawable,TextView secondLetter, TextView thirdLetter, TextView fourthLetter,
                                   TextView fifthLetter, Drawable fifthLetterDrawable){
        firstLetter.setText("");
        firstLetter.setTextColor(getResources().getColor(R.color.black));
        firstLetter.setBackground(firstLetterDrawable);
        secondLetter.setText("");
        secondLetter.setTextColor(getResources().getColor(R.color.black));
        secondLetter.setBackground(lettersDrawable);
        thirdLetter.setText("");
        thirdLetter.setTextColor(getResources().getColor(R.color.black));
        thirdLetter.setBackground(lettersDrawable);
        fourthLetter.setText("");
        fourthLetter.setTextColor(getResources().getColor(R.color.black));
        fourthLetter.setBackground(lettersDrawable);
        fifthLetter.setText("");
        fifthLetter.setTextColor(getResources().getColor(R.color.black));
        fifthLetter.setBackground(fifthLetterDrawable);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void setAchievementsGoals(){
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        editor.putInt(APP_PREFERENCES_ACHIEVE_5_GAMES_PLAYED, 5);
        editor.putInt(APP_PREFERENCES_ACHIEVE_10_GAMES_PLAYED, 10);
        editor.putInt(APP_PREFERENCES_ACHIEVE_25_GAMES_PLAYED, 25);
        editor.putInt(APP_PREFERENCES_ACHIEVE_50_GAMES_PLAYED, 50);
        editor.putInt(APP_PREFERENCES_ACHIEVE_100_GAMES_PLAYED, 100);

        editor.putInt(APP_PREFERENCES_ACHIEVE_5_GAMES_WON, 5);
        editor.putInt(APP_PREFERENCES_ACHIEVE_10_GAMES_WON, 10);
        editor.putInt(APP_PREFERENCES_ACHIEVE_25_GAMES_WON, 25);
        editor.putInt(APP_PREFERENCES_ACHIEVE_50_GAMES_WON, 50);
        editor.putInt(APP_PREFERENCES_ACHIEVE_100_GAMES_WON, 100);

        editor.putInt(APP_PREFERENCES_ACHIEVE_1_FIRST_WORD_WINS, 1);
        editor.putInt(APP_PREFERENCES_ACHIEVE_3_FIRST_WORD_WINS, 3);
        editor.putInt(APP_PREFERENCES_ACHIEVE_5_FIRST_WORD_WINS, 5);
        editor.putInt(APP_PREFERENCES_ACHIEVE_10_FIRST_WORD_WINS, 10);
        editor.apply();
    }

    public void checkAchievementsComplete() {
        if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_1_FIRST_WORD_WINS, 0) == sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0)) {
            showCustomToast("Достижение 'Экстасенс' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_3_FIRST_WORD_WINS, 0) == sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0)) {
            showCustomToast("Достижение 'Колдун' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_FIRST_WORD_WINS, 0) == sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0)) {
            showCustomToast("Достижение 'ПолуБог' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_FIRST_WORD_WINS, 0) == sharedPrefWords.getInt(APP_PREFERENCES_FIRST_WORD_WINS, 0)) {
            showCustomToast("Достижение 'Бог' получено!");
        }

        if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_GAMES_PLAYED, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0)) {
            showCustomToast("Достижение 'Новичок' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_GAMES_PLAYED, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0)) {
            showCustomToast("Достижение 'Игрок' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_25_GAMES_PLAYED, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0)) {
            showCustomToast("Достижение 'Опытный игрок' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_50_GAMES_PLAYED, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0)) {
            showCustomToast("Достижение 'Ветеран' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_100_GAMES_PLAYED, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_PLAYED, 0)) {
            showCustomToast("Достижение 'Игроман' получено!");
        }

        if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_5_GAMES_WON, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0)) {
            showCustomToast("Достижение 'Умник' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_10_GAMES_WON, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0)) {
            showCustomToast("Достижение 'Интеллектуал' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_25_GAMES_WON, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0)) {
            showCustomToast("Достижение 'Гений' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_50_GAMES_WON, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0)) {
            showCustomToast("Достижение 'Мегамозг' получено!");
        } else if (sharedPrefWords.getInt(APP_PREFERENCES_ACHIEVE_100_GAMES_WON, 0) == sharedPrefWords.getInt(APP_PREFERENCES_GAMES_WON, 0)) {
            showCustomToast("Достижение 'Сверхразум' получено!");
        }
    }
}