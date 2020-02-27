package com.aneeshajose.genericadapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.genericadapters.model.ItemEncapsulator;

import java.util.List;


public class GenericAdapterBuilder<T extends ItemEncapsulator> {

    private static final String CONTEXT_NOT_INITIALIZED = "Context Not Initialized";
    private static final String VIEW_HOLDER_NOT_INITIALIZED = "View Holder Provider Not Initialized";

    private boolean isSelectable;
    private boolean isClickable;
    private boolean isSingleSelection;
    private SimpleGenericAdapterBuilder.OnItemSelectionChanged<T> onItemSelectionChanged;
    private SimpleGenericAdapterBuilder.OnItemClicked<T> onItemClicked;
    private SimpleGenericAdapterBuilder.SetSelectionIndicator setSelectionIndicator;
    private Context context;
    private int maxSelection = 1;
    private GetViewHolder<T> viewHolder;
    private List<T> items;

    public GenericAdapterBuilder(Context context, GetViewHolder<T> viewHolder) {
        this.context = context;
        this.viewHolder = viewHolder;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public GenericAdapterBuilder setSelectable(boolean selectable) {
        isSelectable = selectable;
        return this;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public GenericAdapterBuilder setClickable(boolean clickable) {
        isClickable = clickable;
        return this;
    }

    public boolean isSingleSelection() {
        return isSingleSelection;
    }

    public GenericAdapterBuilder setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
        return this;
    }

    public SimpleGenericAdapterBuilder.OnItemSelectionChanged<T> getOnItemSelectionChanged() {
        return onItemSelectionChanged;
    }

    public GenericAdapterBuilder setOnItemSelectionChanged(SimpleGenericAdapterBuilder.OnItemSelectionChanged<T> onItemSelectionChanged) {
        this.onItemSelectionChanged = onItemSelectionChanged;
        return this;
    }

    public SimpleGenericAdapterBuilder.OnItemClicked<T> getOnItemClicked() {
        return onItemClicked;
    }

    public GenericAdapterBuilder setOnItemClicked(SimpleGenericAdapterBuilder.OnItemClicked<T> onItemClicked) {
        this.onItemClicked = onItemClicked;
        return this;
    }

    public SimpleGenericAdapterBuilder.SetSelectionIndicator getSetSelectionIndicator() {
        return setSelectionIndicator;
    }

    public GenericAdapterBuilder setSetSelectionIndicator(SimpleGenericAdapterBuilder.SetSelectionIndicator setSelectionIndicator) {
        this.setSelectionIndicator = setSelectionIndicator;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public GenericAdapterBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public GenericAdapterBuilder setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
        return this;
    }

    public GetViewHolder<T> getViewHolder() {
        return viewHolder;
    }

    public GenericAdapterBuilder setViewHolder(GetViewHolder<T> viewHolder) {
        this.viewHolder = viewHolder;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public GenericAdapterBuilder setItems(List<T> items) {
        this.items = items;
        return this;
    }

    /*public SimpleGenericListAdapter<T> buildListAdapter() {
        checkPreliminaries();
        GeneralGenericAdapterListener listener = createListenerInstance();
        return new GenericListAdapter<>(listener);
    }*/

    private void checkPreliminaries() {
        if (context == null)
            throw new RuntimeException(CONTEXT_NOT_INITIALIZED);
        if (viewHolder == null)
            throw new RuntimeException(VIEW_HOLDER_NOT_INITIALIZED);
    }

    public GenericRecyclerViewAdapter<T> buildRecyclerViewAdapter() {
        checkPreliminaries();
        return new GenericRecyclerViewAdapter<>(createListenerInstance());
    }

    public interface GetViewHolder<T> {
        View getView(@NonNull ViewGroup parent, int viewType);

        <V extends AbstractBetterViewHolder<T>> V getViewHolder(View view, int viewType);
    }

    private GeneralGenericAdapterListener createListenerInstance() {
        return new GeneralGenericAdapterListener(items);
    }

    class GeneralGenericAdapterListener extends GenericAdapterListener<T> {
        private List<T> listItems;

        GeneralGenericAdapterListener(List<T> items) {
            this.listItems = items;
        }

        @Override
        public List<T> getItems() {
            return listItems;
        }

        public void setListItems(List<T> listItems) {
            this.listItems = listItems;
        }

        @Override
        public int maxSelectableItems() {
            return maxSelection;
        }

        @Override
        public View getView(@NonNull ViewGroup parent, int viewType) {
            return viewHolder.getView(parent, viewType);
        }

        @Override
        public <V extends AbstractBetterViewHolder<T>> V getViewHolder(View convertView, int viewType) {
            return viewHolder.getViewHolder(convertView, viewType);
        }

        @Override
        public void onItemClicked(int position, T item) {
            if (onItemClicked != null)
                onItemClicked.onItemClicked(position, item);
        }

        @Override
        public boolean isTypeClickable(int type) {
            return isClickable;
        }

        @Override
        public void onItemSelectionChanged(int position, boolean isSelected, T item) {
            if (onItemSelectionChanged != null)
                onItemSelectionChanged.onItemSelectionChanged(position, item, isSelected);
        }

        @Override
        public boolean isTypeSelectable(int type) {
            return isSelectable;
        }

        @Override
        public <V extends AbstractBetterViewHolder<T>> void setSelectedIndicator(V viewHolder, boolean isSelected) {
            if (setSelectionIndicator != null)
                setSelectionIndicator.setSelectedIndicator(viewHolder, isSelected);
        }


        @Override
        public boolean isSingleSelection() {
            return isSingleSelection;
        }
    }
}
