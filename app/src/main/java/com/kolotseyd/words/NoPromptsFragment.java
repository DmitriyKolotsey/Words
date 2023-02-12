package com.kolotseyd.words;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NoPromptsFragment extends DialogFragment {

    public interface onCloseNoPromptsFragmentEvent {
        public void closeNoPromptsFragmentEvent();
    }

    NoPromptsFragment.onCloseNoPromptsFragmentEvent closeNoPromptsFragmentEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            closeNoPromptsFragmentEventListener = (NoPromptsFragment.onCloseNoPromptsFragmentEvent) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement interface");
        }
    }

    Button bShowAd, bCancelAd;
    TextView tvNoPrompts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.fragment_no_prompts, null);

        tvNoPrompts = v.findViewById(R.id.tvNoPrompts);
        tvNoPrompts.setText(getResources().getString(R.string.no_prompts));

        bShowAd = v.findViewById(R.id.bShowAd);
        bShowAd.setOnClickListener(view -> { 
            showAd();
            dismiss();
        });
        bCancelAd = v.findViewById(R.id.bCancelAd);
        bCancelAd.setOnClickListener(view -> {
            dismiss();
        });
        return v;
    }

    public void showAd(){
        //заменить на показ рекламы

        ((MainActivity) getActivity()).showRewardedVideo();
        /*sharedPrefWords = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefWords.edit();
        editor.putInt("prompts_count", sharedPrefWords.getInt("prompts_count", 0) + 1);
        editor.apply();
        Toast.makeText(this.getActivity(), String.valueOf(sharedPrefWords.getInt("prompts_count", 0) + 1), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        closeNoPromptsFragmentEventListener.closeNoPromptsFragmentEvent();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        closeNoPromptsFragmentEventListener.closeNoPromptsFragmentEvent();
    }
}
