package com.aneeshajose.genericadapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aneeshajose.genericadapters.model.ItemEncapsulator;
import com.aneeshajose.genericadapters.utils.GenericAdapterUtilsKt;

import java.util.List;


public class SimpleGenericListAdapter<T> extends GenericListAdapter<ItemEncapsulator> {

    private Class<T> type;

    SimpleGenericListAdapter(Class<T> cls, @NonNull Context context, int resource, SimpleGenericAdapterBuilder.SimpleGenericAdapterListener listener) {
        super(context, resource, listener);
        type = cls;
    }

    public SimpleGenericListAdapter(Class<T> cls, @NonNull Context context, int resource, int textViewResourceId, SimpleGenericAdapterBuilder.SimpleGenericAdapterListener listener) {
        super(context, resource, textViewResourceId, listener);
    }

    public void setSelected(List<T> selectedItems) {
        super.setSelectedItems(GenericAdapterUtilsKt.getItemEncapsulatorsFromObjects(selectedItems));
    }


    @SuppressWarnings("unchecked")
    public List<T> getSelected() {
        return GenericAdapterUtilsKt.getAllItemsFromEncapsulatorsOfClassType(getSelectedItemsOfCertainClass(type), type);
    }

    public void addToSelectedItems(T item) {
        super.addToSelectedItems(GenericAdapterUtilsKt.getItemEncapsulatorFromObject(item));
    }

    public void removeFromSelectedItem(T item) {
        super.removeFromSelectedItem(GenericAdapterUtilsKt.getItemEncapsulatorFromObject(item));
    }

    public void publishItems(List<T> filteredList) {
        super.publishResults(GenericAdapterUtilsKt.getItemEncapsulatorsFromObjects(filteredList));
    }

    public T get(int position) {
        return (T) getItem(position).getItem();
    }

    public List<T> getAll() {
        return GenericAdapterUtilsKt.getAllItemsFromEncapsulatorsOfClassType(mItems, type);
    }

    public void setItems(List<T> items) {
        ((SimpleGenericAdapterBuilder.SimpleGenericAdapterListener) listener).setListItems(GenericAdapterUtilsKt.getItemEncapsulatorsFromObjects(items));

    }
}
