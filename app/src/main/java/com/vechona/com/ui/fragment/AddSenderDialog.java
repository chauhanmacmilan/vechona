package com.vechona.com.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vechona.app.R;
import com.vechona.com.ui.model.SenderDetailsItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ValidationUtils;

public class AddSenderDialog extends DialogFragment {

    private AddSenderDialog.AddSenderDialogListener callback;
    private EditText etName, etPhoneNo;
    private TextInputLayout input_layout_name, input_layout_phone_number;
    private boolean isEdit;
    private int position;


    public static AddSenderDialog newInstance(boolean isEdit, int position, SenderDetailsItem senderDetailsItem) {
        Bundle args = new Bundle();
        args.putBoolean(BundleParams.IS_EDIT, isEdit);
        args.putInt(BundleParams.POSITION, position);
        args.putParcelable(BundleParams.SENDER_DETAILS, senderDetailsItem);
        AddSenderDialog dialog = new AddSenderDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (AddSenderDialog.AddSenderDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CartItemAddDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        Bundle args = getArguments();
        isEdit = args.getBoolean(BundleParams.IS_EDIT);
        position = args.getInt(BundleParams.POSITION);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_sender, null);

        etName = view.findViewById(R.id.etName);
        etPhoneNo = view.findViewById(R.id.etPhoneNumber);
        input_layout_name = view.findViewById(R.id.input_layout_name);
        input_layout_phone_number = view.findViewById(R.id.input_layout_phone_number);
        if (isEdit) {
            SenderDetailsItem senderDetailsItem = args.getParcelable(BundleParams.SENDER_DETAILS);
            if (senderDetailsItem != null) {
                etName.setText(senderDetailsItem.getName());
                etPhoneNo.setText(senderDetailsItem.getPhoneNumber());
            }
        }

        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                    String name = etName.getText().toString().trim();
                    String phoneNo = etPhoneNo.getText().toString().trim();
                    if (name.isEmpty())
                        input_layout_name.setError(getString(R.string.error_required));
                    else if (phoneNo.isEmpty())
                        input_layout_phone_number.setError(getString(R.string.error_required));
                    else if (!ValidationUtils.isValidPhoneNumber(phoneNo))
                        input_layout_phone_number.setError(getString(R.string.error_phone_number_length));
                    else {
                        SenderDetailsItem senderDetailsItem = new SenderDetailsItem(
                                etName.getText().toString().trim(),
                                etPhoneNo.getText().toString().trim());

                        callback.onAddSenderDialogSubmitClick(isEdit, position, senderDetailsItem);
                        dismiss();
                    }
                }
            });
        }
    }

    private void setQnty(int qnty, TextView tvQnty) {
        tvQnty.setText(String.valueOf(qnty));
    }

    public interface AddSenderDialogListener {
        void onAddSenderDialogSubmitClick(boolean isEdit, int position, SenderDetailsItem senderDetailsItem);
    }
}
