package com.busilinq.casepocket.widget.pixelate;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.busilinq.casepocket.adapter.CaseImageAdapter;

public interface OnPixelateListener {
    void onPixelated(final Bitmap bitmap, int density , CaseImageAdapter.ViewHolder holder);
}
