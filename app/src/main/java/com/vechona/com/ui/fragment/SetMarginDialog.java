package com.vechona.com.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vechona.app.R;

public class SetMarginDialog extends DialogFragment {

    private SetMarginDialogListener callback;
    private EditText etMargin;
    private TextInputLayout input_layout_Margin;

    public static SetMarginDialog newInstance() {
        Bundle args = new Bundle();
        SetMarginDialog dialog = new SetMarginDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_margin, null);

        etMargin = view.findViewById(R.id.etMargin);
        input_layout_Margin = view.findViewById(R.id.input_layout_Margin);


        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String margin = etMargin.getText().toString();
                    if (margin.isEmpty())
                        input_layout_Margin.setError(getString(R.string.error_required));
                    else {
                        callback.onSetMarginDialogSubmitClick(Integer.valueOf(etMargin.getText().toString()));
                        dismiss();
                    }
                }
            });
        }
    }

    public void setMarginListener(SetMarginDialogListener setMarginDialogListener) {
        callback = setMarginDialogListener;
    }

    public interface SetMarginDialogListener {
        void onSetMarginDialogSubmitClick(int margin);
    }
}
