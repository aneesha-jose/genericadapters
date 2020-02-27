package com.aneeshajose.genericadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.genericadapters.model.ItemEncapsulator;
import com.aneeshajose.genericadapters.utils.GenericAdapterUtilsKt;
import com.aneeshajose.miscellaneous.utils.GeneralUtilsKt;

import java.util.List;


public class SimpleGenericAdapterBuilder<T, V extends AbstractBetterViewHolder<T>> {

    private static final String CONTEXT_NOT_INITIALIZED = "Context Not Initialized";
    private static final String LAYOUT_NOT_INITIALIZED = "Layout Resource Not Initialized";
    private static final String VIEW_HOLDER_NOT_INITIALIZED = "View Holder Provider Not Initialized";

    private boolean isSelectable;
    private boolean isClickable;
    private boolean isSingleSelection;
    private OnItemSelectionChanged<T> onItemSelectionChanged;
    private OnItemClicked<T> onItemClicked;
    private SetSelectionIndicator<V> setSelectionIndicator;
    private Context context;
    private int maxSelection = 1;
    @LayoutRes
    private int layoutRes = -1;
    @IdRes
    private int textViewResourceId = -1;
    private GetViewHolder<T, V> viewHolder;
    private List<T> items;
    private Class<T> type;

    public SimpleGenericAdapterBuilder(Context context, Class<T> type, @LayoutRes int layoutRes, GetViewHolder<T, V> viewHolder) {
        this.context = context;
        this.layoutRes = layoutRes;
        this.viewHolder = viewHolder;
        this.type = type;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public SimpleGenericAdapterBuilder setSelectable(boolean selectable) {
        isSelectable = selectable;
        return this;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public SimpleGenericAdapterBuilder setClickable(boolean clickable) {
        isClickable = clickable;
        return this;
    }

    public boolean isSingleSelection() {
        return isSingleSelection;
    }

    public SimpleGenericAdapterBuilder setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
        return this;
    }

    public OnItemSelectionChanged<T> getOnItemSelectionChanged() {
        return onItemSelectionChanged;
    }

    public SimpleGenericAdapterBuilder setOnItemSelectionChanged(OnItemSelectionChanged<T> onItemSelectionChanged) {
        this.onItemSelectionChanged = onItemSelectionChanged;
        return this;
    }

    public OnItemClicked<T> getOnItemClicked() {
        return onItemClicked;
    }

    public SimpleGenericAdapterBuilder setOnItemClicked(OnItemClicked<T> onItemClicked) {
        this.onItemClicked = onItemClicked;
        return this;
    }

    public SetSelectionIndicator<V> getSetSelectionIndicator() {
        return setSelectionIndicator;
    }

    public SimpleGenericAdapterBuilder setSetSelectionIndicator(SetSelectionIndicator<V> setSelectionIndicator) {
        this.setSelectionIndicator = setSelectionIndicator;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public SimpleGenericAdapterBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public SimpleGenericAdapterBuilder setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
        return this;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public SimpleGenericAdapterBuilder setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    public GetViewHolder<T, V> getViewHolder() {
        return viewHolder;
    }

    public SimpleGenericAdapterBuilder setViewHolder(GetViewHolder<T, V> viewHolder) {
        this.viewHolder = viewHolder;
        return this;
    }

    public int getTextViewResourceId() {
        return textViewResourceId;
    }

    public SimpleGenericAdapterBuilder setTextViewResourceId(int textViewResourceId) {
        this.textViewResourceId = textViewResourceId;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public SimpleGenericAdapterBuilder setItems(List<T> items) {
        this.items = items;
        return this;
    }

    public SimpleGenericListAdapter<T> buildListAdapter() {
        checkPreliminaries();
        SimpleGenericAdapterListener listener = createListenerInstance();
        if (textViewResourceId == -1)
            return new SimpleGenericListAdapter<>(type, context, layoutRes, listener);
        return new SimpleGenericListAdapter<>(type, context, layoutRes, textViewResourceId, listener);
    }

    private void checkPreliminaries() {
        if (context == null)
            throw new RuntimeException(CONTEXT_NOT_INITIALIZED);
        if (layoutRes == -1)
            throw new RuntimeException(LAYOUT_NOT_INITIALIZED);
        if (viewHolder == null)
            throw new RuntimeException(VIEW_HOLDER_NOT_INITIALIZED);
    }

    public SimpleGenericRecyclerViewAdapter<T> buildRecyclerViewAdapter() {
        checkPreliminaries();
        return new SimpleGenericRecyclerViewAdapter<>(type, createListenerInstance());
    }

    public interface GetViewHolder<T, V extends AbstractBetterViewHolder<T>> {
        V getViewHolder(View view);
    }

    public interface OnItemSelectionChanged<T> {
        /**
         * Called when a selectable type item is clicked
         *
         * @param position   the position of the item
         * @param isSelected if the item is selected or deselected
         * @param item       The item at the position selected
         */
        void onItemSelectionChanged(int position, T item, boolean isSelected);
    }

    public interface OnItemClicked<T> {
        /**
         * Called when a non-selectable but clickable type item is clicked
         *
         * @param position the position of the item
         * @param item     The item at the position selected
         */
        void onItemClicked(int position, T item);
    }

    public interface SetSelectionIndicator<V extends AbstractBetterViewHolder> {
        /**
         * to manipulate the visual indicator
         *
         * @param viewHolder the view holder corresponding
         * @param isSelected if the item is selected or deselected
         */
        void setSelectedIndicator(AbstractBetterViewHolder viewHolder, boolean isSelected);
    }

    private SimpleGenericAdapterListener createListenerInstance() {
        return new SimpleGenericAdapterListener();
    }

    public class SimpleGenericAdapterListener extends GenericAdapterListener<ItemEncapsulator> {
        private List<ItemEncapsulator> listItems;

        @Override
        public List<ItemEncapsulator> getItems() {
            if (listItems == null)
                listItems = GenericAdapterUtilsKt.getItemEncapsulatorsFromObjects(items);
            return listItems;
        }

        public void setListItems(List<ItemEncapsulator> listItems) {
            this.listItems = listItems;
        }

        @Override
        public int maxSelectableItems() {
            return maxSelection;
        }

        @Override
        public View getView(@NonNull ViewGroup parent, int viewType) {
            return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        }

        @Override
        public ConverterAbstractBetterViewHolder getViewHolder(View convertView, int viewType) {
            return new ConverterAbstractBetterViewHolder(convertView, viewHolder);
        }

        @Override
        public void onItemClicked(int position, ItemEncapsulator item) {
            if (onItemClicked != null)
                onItemClicked.onItemClicked(position, (T) item.getItem());
        }

        @Override
        public boolean isTypeClickable(int type) {
            return isClickable;
        }

        @Override
        public void onItemSelectionChanged(int position, boolean isSelected, ItemEncapsulator item) {
            if (onItemSelectionChanged != null)
                onItemSelectionChanged.onItemSelectionChanged(position, (T) item.getItem(), isSelected);
        }

        @Override
        public boolean isTypeSelectable(int type) {
            return isSelectable;
        }

        @Override
        public <VH extends AbstractBetterViewHolder<ItemEncapsulator>> void setSelectedIndicator(VH viewHolder, boolean isSelected) {
            if (setSelectionIndicator != null)
                setSelectionIndicator.setSelectedIndicator(((ConverterAbstractBetterViewHolder) viewHolder).getViewHolder(), isSelected);
        }

        @Override
        public boolean isSingleSelection() {
            return isSingleSelection;
        }
    }

    class ConverterAbstractBetterViewHolder extends AbstractBetterViewHolder<ItemEncapsulator> {

        AbstractBetterViewHolder<T> viewHolder;

        protected ConverterAbstractBetterViewHolder(View view, GetViewHolder<T, V> viewHolder) {
            super(view);
            this.viewHolder = viewHolder.getViewHolder(view);
        }

        @Override
        public void bind(ItemEncapsulator element, int position) {
            viewHolder.bind((T) element.getItem(), position);
        }

        @Override
        public void bind(ItemEncapsulator element, int size, int position) {
            viewHolder.bind((T) element.getItem(), size, position);
            super.bind(element, size, position);
        }

        public AbstractBetterViewHolder<T> getViewHolder() {
            return viewHolder;
        }
    }
}
