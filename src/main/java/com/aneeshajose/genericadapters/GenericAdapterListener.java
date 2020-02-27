package com.aneeshajose.genericadapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.genericadapters.model.BaseAdapterListener;
import com.aneeshajose.genericadapters.model.ItemEncapsulator;
import com.aneeshajose.genericadapters.utils.GenericAdapterUtilsKt;

import java.util.ArrayList;
import java.util.List;


public abstract class GenericAdapterListener<T> implements BaseAdapterListener<T> {

    @Override
    public abstract List<T> getItems();

    /**
     * Called when a selectable type item is clicked
     *
     * @param position   the position of the item
     * @param isSelected if the item is selected or deselected
     */
    public void onItemSelectionChanged(int position, boolean isSelected, T item) {
    }

    /**
     * Called when a non-selectable but clickable type item is clicked
     *
     * @param position the position of the item
     */
    @Override
    public void onItemClicked(int position, T item) {
    }

    @Override
    public int getItemCount() {
        return getItems() == null ? 0 : getItems().size();
    }

    /**
     * to manipulate the visual indicator
     *
     * @param viewHolder the view holder corresponding
     * @param isSelected if the item is selected or deselected
     */
    public <V extends AbstractBetterViewHolder<T>> void setSelectedIndicator(V viewHolder, boolean isSelected) {
    }

    /**
     * the maximum number of items that can be selected
     *
     * @return the maximum number of items that can be selected
     */
    public abstract int maxSelectableItems();

    /**
     * if only a single item can be selected at one time
     *
     * @return false to enable multiselection. true to permit only single selection. If not overridden, by default returns false.
     */
    public boolean isSingleSelection() {
        return false;
    }

    /**
     * if the type specified in the {@link ItemEncapsulator} can be selected
     *
     * @param type the type of {@link ItemEncapsulator}
     * @return true if selectable else false. To handle selection on your own, return false.
     */
    public boolean isTypeSelectable(int type) {
        return false;
    }

    /**
     * if the type specified in the {@link ItemEncapsulator} can be clicked
     *
     * @param type the type of {@link ItemEncapsulator}
     * @return true if clickable else false.To handle click on your own, return false.
     */
    public boolean isTypeClickable(int type) {
        return false;
    }

    public abstract View getView(@NonNull ViewGroup parent, int viewType);

    public abstract <V extends AbstractBetterViewHolder<T>> V getViewHolder(View convertView, int viewType);

    /**
     * A method to implement searching/filtering logic. The default implementation works on calling toString method of the item object.
     *
     * @param masterList       the list with all the elements of the adapter
     * @param searchConstraint the query for search
     * @return filtered list for the search
     */
    public List<T> filterResults(List<T> masterList, String searchConstraint) {
        if (TextUtils.isEmpty(searchConstraint) || searchConstraint.trim().isEmpty())
            return new ArrayList<>(masterList);

        return GenericAdapterUtilsKt.getFilteredItemsOnSearchConstraint(masterList, searchConstraint);
    }
}
