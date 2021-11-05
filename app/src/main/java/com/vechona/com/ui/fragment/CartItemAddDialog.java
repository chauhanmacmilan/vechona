package com.vechona.com.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vechona.app.R;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.model.ProductStock;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CartItemAddDialog extends DialogFragment {

    private CartItemAddDialogListener callback;

    private Map<String, Map<String, ProductStock>> stockMap;
    private List<String> sizes;
    private List<String> colors;
    private int stock;

    public static CartItemAddDialog newInstance(Product product) {
        Bundle args = new Bundle();
        args.putParcelable(BundleParams.PRODUCT, product);
        CartItemAddDialog dialog = new CartItemAddDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (CartItemAddDialogListener) context;
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
        Product product = args.getParcelable(BundleParams.PRODUCT);
        stockMap = product.getProductStocks();
        sizes = new ArrayList<>(stockMap.keySet());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_cart_item_add, null);
        final Spinner spinnerSize = view.findViewById(R.id.spinnerSize);
        final Spinner spinnerColor = view.findViewById(R.id.spinnerColor);
        final TextView tvQnty = view.findViewById(R.id.tvQnty);
        setQnty(1, tvQnty);
        AppCompatImageView ivMinusQnty = view.findViewById(R.id.ivMinusQnty);
        AppCompatImageView ivPlusQnty = view.findViewById(R.id.ivPlusQnty);

        ivMinusQnty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qnty = Integer.parseInt(tvQnty.getText().toString().trim());
                if (qnty == 1)
                    ViewUtils.showToast(context, context.getString(R.string.msg_qnty_must_be_one));
                else setQnty(--qnty, tvQnty);
            }
        });

        ivPlusQnty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qnty = Integer.parseInt(tvQnty.getText().toString().trim());
                if (qnty == stock)
                    ViewUtils.showToast(context, context.getString(R.string.msg_qnty_out_of_stock));
                else setQnty(++qnty, tvQnty);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_size_spinner_layout, sizes);
        spinnerSize.setAdapter(adapter);

        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colors = new ArrayList<>(stockMap.get(sizes.get(i)).keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_size_spinner_layout, colors);
                spinnerColor.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stock = stockMap
                        .get(sizes.get(spinnerSize.getSelectedItemPosition()))
                        .get(colors.get(spinnerColor.getSelectedItemPosition()))
                        .getQuantity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view)
                .setPositiveButton(R.string.title_cart_dialog_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCartItemAddDialogDoneClick(
                                stockMap.get(sizes.get(spinnerSize.getSelectedItemPosition()))
                                        .get(colors.get(spinnerColor.getSelectedItemPosition()))
                                , Integer.parseInt(tvQnty.getText().toString()));
                    }
                });

        return builder.create();
    }

    private void setQnty(int qnty, TextView tvQnty) {
        tvQnty.setText(String.valueOf(qnty));
    }

    public interface CartItemAddDialogListener {
        void onCartItemAddDialogDoneClick(ProductStock productStock, int qnty);
    }
}
