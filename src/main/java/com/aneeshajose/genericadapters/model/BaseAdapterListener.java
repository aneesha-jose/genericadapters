package com.aneeshajose.genericadapters.model;

import java.util.List;

public interface BaseAdapterListener<T> {

    List<T> getItems();

    int getItemCount();

    void onItemClicked(int position, T item);

}
