package com.vechona.com.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vechona.app.R;

public class DiscardCartDialog extends DialogFragment {

    private static final String CURRENT_SUPPLIER_NAME = "current_supplier_name";
    private static final String NEW_SUPPLIER_NAME = "new_supplier_name";

    private DiscardCartDialog.DiscardCartDialogListener callback;

    public static DiscardCartDialog newInstance(String currentSupplier, String newSupplier) {
        Bundle args = new Bundle();
        args.putString(CURRENT_SUPPLIER_NAME, currentSupplier);
        args.putString(NEW_SUPPLIER_NAME, newSupplier);

        DiscardCartDialog dialog = new DiscardCartDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DiscardCartDialog.DiscardCartDialogListener) context;
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
        String currentSupplier = args.getString(CURRENT_SUPPLIER_NAME);
        String newSupplier = args.getString(NEW_SUPPLIER_NAME);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_cart_discard, null);
        TextView tvCurrentSupplier = view.findViewById(R.id.tvCurrentSupplier);
        TextView tvNewSupplier = view.findViewById(R.id.tvNewSupplier);

        tvCurrentSupplier.setText(context.getResources().getString(R.string.title_discard_cart_one, currentSupplier));
        tvNewSupplier.setText(context.getResources().getString(R.string.title_discard_cart_two, newSupplier));

        builder.setView(view)
                .setPositiveButton(R.string.title_cart_dialog_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDiscardCartDialogDoneClick();
                    }
                })
                .setNegativeButton(R.string.title_cart_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    public interface DiscardCartDialogListener {
        void onDiscardCartDialogDoneClick();
    }

}
