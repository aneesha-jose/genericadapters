package com.aneeshajose.genericadapters;

import com.aneeshajose.genericadapters.model.ItemEncapsulator;
import com.aneeshajose.genericadapters.utils.GenericAdapterUtilsKt;

import java.util.List;


public class SimpleGenericRecyclerViewAdapter<T> extends GenericRecyclerViewAdapter<ItemEncapsulator> {

    private Class<T> type;

    SimpleGenericRecyclerViewAdapter(Class<T> cls, SimpleGenericAdapterBuilder.SimpleGenericAdapterListener listener) {
        super(listener);
        type = cls;
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

    public T get(int position) {
        return (T) mItems.get(position).getItem();
    }

    public List<T> getAll() {
        return GenericAdapterUtilsKt.getAllItemsFromEncapsulatorsOfClassType(mItems, type);
    }

    public void publishItems(List<T> filteredList) {
        super.publishResults(GenericAdapterUtilsKt.getItemEncapsulatorsFromObjects(filteredList));
    }

    public void resetAllItems(List<T> items) {
        resetItems(GenericAdapterUtilsKt.getItemEncapsulatorsFromObjects(items));
    }

}
