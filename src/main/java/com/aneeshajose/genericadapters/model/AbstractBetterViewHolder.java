package com.aneeshajose.genericadapters.model;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sujittayade on 18/01/17.
 */

public abstract class AbstractBetterViewHolder<T> extends RecyclerView.ViewHolder {

    int currentPosition;

    protected AbstractBetterViewHolder(View view) {
        super(view);
    }

    public abstract void bind(T element, int position);

    @CallSuper
    public void bind(T element, int size, int position) {
        bind(element, position);
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

}
