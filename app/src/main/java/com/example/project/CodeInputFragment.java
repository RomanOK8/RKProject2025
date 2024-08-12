package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

public class CodeInputFragment extends DialogFragment {
    private EditText codeEditText;
    private Button submitCodeButton;

    public interface CodeInputListener {
        void onCodeInputComplete(String code);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_input, container, false);

        codeEditText = view.findViewById(R.id.codeEditText);
        submitCodeButton = view.findViewById(R.id.submitCodeButton);

        submitCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString().trim();
                if (code.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter the code", Toast.LENGTH_SHORT).show();
                } else {
                    CodeInputListener listener = (CodeInputListener) getActivity();
                    listener.onCodeInputComplete(code);
                    dismiss();
                }
            }
        });

        return view;
    }
}