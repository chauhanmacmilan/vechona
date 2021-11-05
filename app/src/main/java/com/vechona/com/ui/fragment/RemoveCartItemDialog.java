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

public class RemoveCartItemDialog extends DialogFragment {

    private static final String POSITION = "position";
    private static final String PRODUCT_NAME = "productName";
    private static final String SIZE = "size";

    private RemoveCartItemDialog.RemoveCartItemDialogListener callback;

    public static RemoveCartItemDialog newInstance(int position, String productName, String size) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(PRODUCT_NAME, productName);
        args.putString(SIZE, size);

        RemoveCartItemDialog dialog = new RemoveCartItemDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (RemoveCartItemDialog.RemoveCartItemDialogListener) context;
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
        final int position = args.getInt(POSITION);
        final String productName = args.getString(PRODUCT_NAME);
        final String size = args.getString(SIZE);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_cart_item, null);
        final TextView tvProductNameSize = view.findViewById(R.id.tvProductNameSize);
        tvProductNameSize.setText(context.getResources().getString(R.string.title_cart_remove_product_name, productName, size));

        builder.setView(view)
                .setPositiveButton(R.string.title_cart_dialog_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onRemoveCartItemDialogDoneClick(position);
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

    private void setQnty(int qnty, TextView tvQnty) {
        tvQnty.setText(String.valueOf(qnty));
    }

    public interface RemoveCartItemDialogListener {
        void onRemoveCartItemDialogDoneClick(int position);
    }

}
