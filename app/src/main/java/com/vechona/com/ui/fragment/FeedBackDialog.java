package com.vechona.com.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.vechona.app.R;
import com.vechona.com.ui.utils.BundleParams;

public class FeedBackDialog extends DialogFragment {

    private FeedBackDialogListner callback;
    private RatingBar rating;
    private EditText etComment;

    public static FeedBackDialog newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(BundleParams.POSITION, position);
        FeedBackDialog dialog = new FeedBackDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (FeedBackDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CartItemAddDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_send_feedback, null);

        Bundle args = getArguments();
        final int position = args.getInt(BundleParams.POSITION);
        rating = view.findViewById(R.id.ratingBar);
        etComment = view.findViewById(R.id.etComment);
        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onSubmitButtonClick(position, (int) rating.getRating(), etComment.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    public interface FeedBackDialogListner {
        void onSubmitButtonClick(int position, int rating, String comments);
    }
}
