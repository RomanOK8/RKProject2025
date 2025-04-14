package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WarningFragment extends DialogFragment {

    public interface WarningListener {
        void onContinue();
        void onContinueAsGuest();
        void onCancel();
    }

    private WarningListener listener;

    public void setWarningListener(WarningListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warning, container, false);

        TextView warningText = view.findViewById(R.id.warningText);
        Button yesButton = view.findViewById(R.id.yesButton);
        Button noButton = view.findViewById(R.id.noButton);

        warningText.setText("Warning: Online play will not be available. Continue?");

        yesButton.setOnClickListener(v -> {
            // Переход в MainMenu при нажатии Yes
            startActivity(new Intent(getActivity(), MainMenu.class));
            dismiss();

            // Если нужно также уведомить listener
            if (listener != null) {
                listener.onContinueAsGuest();
            }
        });

        noButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
    }
}