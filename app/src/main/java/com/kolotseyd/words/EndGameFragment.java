package com.kolotseyd.words;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EndGameFragment extends DialogFragment implements View.OnClickListener {

    TextView tvFragmentWordIsGuessed, tvFragmentSearchWordValue, tvFragmentAttemptsCount;

    public interface onCloseEndGameFragmentEventListener {
        public void closeEndGameFragmentEvent();
    }

    onCloseEndGameFragmentEventListener closeEndGameFragmentEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            closeEndGameFragmentEventListener = (onCloseEndGameFragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.fragment_end_game, null);

        tvFragmentWordIsGuessed = v.findViewById(R.id.tvFragmentWordIsGuessed);
        tvFragmentSearchWordValue = v.findViewById(R.id.tvFragmentSearchWordValue);
        tvFragmentAttemptsCount = v.findViewById(R.id.tvFragmentAttemptsCount);
        v.findViewById(R.id.bFragmentNextWord).setOnClickListener(this);

        tvFragmentSearchWordValue.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvFragmentSearchWordValue.setText(getArguments().getString("search_word"));
        tvFragmentSearchWordValue.setOnClickListener(view -> tvFragmentSearchWordValue.setTransformationMethod(HideReturnsTransformationMethod.getInstance()));
        tvFragmentAttemptsCount.setText(getResources().getString(R.string.attempts_count) + " " + String.valueOf(getArguments().getInt("attempts_count"))
        + " " + getResources().getString(R.string.from_six));

        if (getArguments().getBoolean("is_word_guessed")){
            tvFragmentWordIsGuessed.setText(getResources().getString(R.string.word_is_guessed));

        } else {
            tvFragmentWordIsGuessed.setText(getResources().getString(R.string.word_is_not_guessed));
        }

        return v;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        closeEndGameFragmentEventListener.closeEndGameFragmentEvent();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        closeEndGameFragmentEventListener.closeEndGameFragmentEvent();
    }
}
