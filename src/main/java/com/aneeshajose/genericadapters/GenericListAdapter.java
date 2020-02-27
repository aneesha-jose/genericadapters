package com.aneeshajose.genericadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.genericadapters.model.ItemEncapsulator;
import com.aneeshajose.genericadapters.utils.GenericAdapterUtilsKt;
import com.aneeshajose.miscellaneous.utils.Optional;

import java.util.ArrayList;
import java.util.List;


public class GenericListAdapter<T extends ItemEncapsulator> extends ArrayAdapter<T> implements GenericDialogListFilter.GenericDialogListFilterListener<T> {
    private List<T> selectedItems = new ArrayList<>();
    protected List<T> mItems = new ArrayList<>();
    protected GenericAdapterListener<T> listener;
    private GenericDialogListFilter<T> filter;

    public GenericListAdapter(@NonNull Context context, int resource, GenericAdapterListener<T> listener) {
        super(context, resource);
        this.listener = listener;
        if (listener != null && listener.getItems() != null)
            mItems.addAll(listener.getItems());
    }

    public GenericListAdapter(@NonNull Context context, int resource, int textViewResourceId, GenericAdapterListener<T> listener) {
        super(context, resource, textViewResourceId);
        this.listener = listener;
        if (listener != null && listener.getItems() != null)
            mItems.addAll(listener.getItems());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AbstractBetterViewHolder<T> viewHolder;
        T item = getItem(position);
        if (convertView == null) {
            convertView = listener.getView(parent, getItemViewType(position));
            if (convertView == null)
                convertView = super.getView(position, convertView, parent);
            if (item == null)
                return convertView;
            viewHolder = listener.getViewHolder(convertView,getItemViewType(position));
            if (listener.isTypeClickable(item.getItemType()) || listener.isTypeSelectable(item.getItemType())) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int pos = ((AbstractBetterViewHolder) v.getTag()).getCurrentPosition();
                    T currentItem = getItem(pos);
                    if (listener.isTypeSelectable(currentItem.getItemType())) {
                        if (listener.isSingleSelection() && listener.maxSelectableItems() != 1) {
                            selectedItems.clear();
                        }
                        boolean isSelected = !selectedItems.contains(item);
                        if (selectedItems.size() < listener.maxSelectableItems() || !isSelected) {
                            if (listener != null) {
                                listener.onItemSelectionChanged(pos, isSelected, currentItem);
                            }
                            if (isSelected)
                                addToSelectedItems(currentItem);
                            else
                                removeFromSelectedItem(currentItem);
                        }
                        return;
                    }
                    listener.onItemClicked(pos, currentItem);
                });
            }
            convertView.setTag(viewHolder);
        } else
            //noinspection unchecked
            viewHolder = (AbstractBetterViewHolder<T>) convertView.getTag();

        listener.setSelectedIndicator(viewHolder, selectedItems.contains(item));
        viewHolder.bind(item, position);
        viewHolder.setCurrentPosition(position);

        return convertView;
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new GenericDialogListFilter<>(this);
        filter.setMasterList(listener.getItems());
        return filter;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? -1 : Optional.orElse(getItem(position).getItemType(), -1).get();
    }

    public void setListener(GenericAdapterListener<T> listener) {
        this.listener = listener;
        if (listener != null && listener.getItems() != null) {
            mItems.clear();
            mItems.addAll(listener.getItems());
            notifyDataSetChanged();
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

    public void removeFromSelectedItem(T item) {
        if (selectedItems == null)
            selectedItems = new ArrayList<>();
        selectedItems.remove(item);
        notifyDataSetChanged();
    }


    @Override
    public List<T> filterResults(List<T> masterList, String searchConstraint) {
        return listener == null ? masterList : listener.filterResults(masterList, searchConstraint);
    }

    @Override
    public void publishResults(List<T> filteredList) {
        mItems.clear();
        mItems.addAll(filteredList);
        notifyDataSetChanged();
    }
}
