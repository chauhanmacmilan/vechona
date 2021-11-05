package com.vechona.com.ui.interfaces;

import com.vechona.com.ui.model.ProductStock;

public interface OnCartItemClickListener {
    void onRemoveItemClick(int position);

    void onSizeSpinnerClick(ProductStock productStock, int position);

    void onQntyMinusClick(int position);

    void onQntyPlusClick(int position);
}
