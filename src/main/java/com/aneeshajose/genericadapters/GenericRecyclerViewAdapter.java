package com.aneeshajose.genericadapters;

import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.genericadapters.model.ItemEncapsulator;
import com.aneeshajose.genericadapters.utils.GenericAdapterUtilsKt;
import com.aneeshajose.miscellaneous.utils.Optional;

import java.util.ArrayList;
import java.util.List;


public class GenericRecyclerViewAdapter<T extends ItemEncapsulator> extends RecyclerView.Adapter<AbstractBetterViewHolder<T>> implements Filterable, GenericDialogListFilter.GenericDialogListFilterListener<T> {
    private List<T> selectedItems = new ArrayList<>();
    protected List<T> mItems = new ArrayList<>();
    private GenericAdapterListener<T> listener;
    private GenericDialogListFilter<T> filter;

    public GenericRecyclerViewAdapter(GenericAdapterListener<T> listener) {
        this.listener = listener;
        if (listener != null && listener.getItems() != null)
            mItems.addAll(listener.getItems());
    }

    public void setListener(GenericAdapterListener<T> listener) {
        this.listener = listener;
        if (listener != null && listener.getItems() != null) {
            mItems.clear();
            mItems.addAll(listener.getItems());
        }
    }

    public void setSelectedItems(List<T> selectedItems) {
        if (selectedItems != null)
            this.selectedItems = new ArrayList<>(selectedItems);
        notifyDataSetChanged();
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    public List<T> getSelectedItemsOfCertainClass(final Class cls) {
        return GenericAdapterUtilsKt.getItemEncapsulatorsWithItemClassType(selectedItems,cls);
    }

    public void addToSelectedItems(T item) {
        if (selectedItems == null)
            selectedItems = new ArrayList<>();
        selectedItems.add(item);
        notifyDataSetChanged();
    }

    public void removeFromSelectedItem(ItemEncapsulator item) {
        if (selectedItems == null)
            selectedItems = new ArrayList<>();
        selectedItems.remove(item);
        notifyDataSetChanged();
    }

    public void clearSelectedItems() {
        if (selectedItems == null)
            selectedItems = new ArrayList<>();
        selectedItems.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AbstractBetterViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return listener.getViewHolder(listener.getView(parent, viewType), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractBetterViewHolder<T> holder, int position) {
        T item = mItems.get(position);
        if (listener.isTypeClickable(item.getItemType()) || listener.isTypeSelectable(item.getItemType())) {
            holder.itemView.setOnClickListener(v -> {
                if (listener.isTypeSelectable(item.getItemType())) {
                    boolean isSelected = !selectedItems.contains(item);
                    if (listener.isSingleSelection() && listener.maxSelectableItems() != 1) {
                        selectedItems.clear();
                    }
                    if (selectedItems.size() < listener.maxSelectableItems() || !isSelected) {
                        if (listener != null) {
                            listener.onItemSelectionChanged(position, isSelected, item);
                        }
                        if (isSelected)
                            addToSelectedItems(item);
                        else
                            removeFromSelectedItem(item);
                    }
                    return;
                }
                listener.onItemClicked(position, item);
            });
        }
        listener.setSelectedIndicator(holder, selectedItems.contains(item));
        holder.bind(item, getItemCount(), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Optional.orElse(mItems.get(position).getItemType(), super.getItemViewType(position)).get();
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position) == null ? super.getItemId(position) : mItems.get(position).hashCode();
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new GenericDialogListFilter<>(this);
        filter.setMasterList(listener.getItems());
        return filter;
    }

    @Override
    public List<T> filterResults(List<T> masterList, String searchConstraint) {
        return listener == null ? masterList : listener.filterResults(masterList, searchConstraint);
    }

    @Override
    public void publishResults(List<T> filteredList) {
        if (mItems == null)
            mItems = new ArrayList<>();
        else mItems.clear();
        mItems.addAll(Optional.orElse(filteredList, new ArrayList<T>()).get());
        notifyDataSetChanged();
    }

    public void resetItems(List<T> items) {
        if (selectedItems == null)
            selectedItems = new ArrayList<>();
        else selectedItems.clear();
        publishResults(items);
    }

}
