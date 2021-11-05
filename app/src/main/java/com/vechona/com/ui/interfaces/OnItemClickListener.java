package com.vechona.com.ui.interfaces;

import android.view.View;

public interface OnItemClickListener {
    void onClickAtPosition(View view, int position);
    void onClickWishList(View view,int position);
    void onClickShareNow(View view, int position);
}
