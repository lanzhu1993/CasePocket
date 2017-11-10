package com.busilinq.casepocket.widget.pixelate;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;


import com.busilinq.casepocket.adapter.CaseImageAdapter;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class Pixelate implements OnPixelateListener {

    private int density = 12;
    private OnPixelateListener onPixelateListener;
    private PixelateThread pixelateThread;
    private int x, y, width, height = -1;

    private Bitmap destBitmap;
    private ImageView targetImageView;
    private boolean loadIntoImageView = true;
    private CaseImageAdapter.ViewHolder holder;

    public Pixelate(@NonNull Bitmap bitmap) {
        pixelateThread = new PixelateThread();
        setBitmap(bitmap);
    }

    public Pixelate(@NonNull final ImageView imageView) {
        pixelateThread = new PixelateThread();
        this.targetImageView = imageView;
        imageView.setDrawingCacheEnabled(true);
        setBitmap(imageView.getDrawingCache());
    }

    public Pixelate setShouldHandleImageView(boolean loadIntoImageView) {
        this.loadIntoImageView = loadIntoImageView;
        return this;
    }

    public Pixelate setDensity(int density) {
        this.density = density;
        return this;
    }

    public Pixelate setListener(OnPixelateListener listener, CaseImageAdapter.ViewHolder holder) {
        this.onPixelateListener = listener;
        this.holder = holder;
        return this;
    }

    public Pixelate setArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public Pixelate setBitmap(Bitmap bitmap) {
        // TODO instead of copying whole bitmap, draw on new one
        this.destBitmap = bitmap.copy(ARGB_8888, true);
        return this;
    }

    public void make() {
        if(pixelateThread == null || pixelateThread.isRendering()) {
            // Create a new thread when the old one is busy
            pixelateThread = new PixelateThread();
        }
        pixelateThread.setBitmap(this.destBitmap);
        pixelateThread.setHolder(holder);
        pixelateThread.pixelate(density);
        if(x != -1 && y != -1) {
            pixelateThread.setArea(x, y, width, height);
        }
        // Let this class handle the callback and not the thread
        pixelateThread.setPixelateListener(this);
        pixelateThread.start();
    }

    @Override
    public void onPixelated(final Bitmap bitmap, final int density,CaseImageAdapter.ViewHolder holder) {
        if(loadIntoImageView && targetImageView != null){
            targetImageView.setImageBitmap(bitmap);
        }
        if(onPixelateListener != null) onPixelateListener.onPixelated(bitmap, density,holder );
    }

}
