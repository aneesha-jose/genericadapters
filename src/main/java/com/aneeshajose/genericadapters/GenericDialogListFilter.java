package com.aneeshajose.genericadapters;

import android.widget.Filter;

import androidx.annotation.NonNull;

import com.aneeshajose.miscellaneous.utils.Optional;

import java.util.ArrayList;
import java.util.List;


public class GenericDialogListFilter<T> extends Filter {

    private List<T> masterList;
    private GenericDialogListFilterListener<T> listener;

    public GenericDialogListFilter(GenericDialogListFilterListener<T> listener) {
        this.listener = listener;
    }

    public void setMasterList(List<T> masterList) {
        this.masterList = masterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return getFilterResultsFormat(Optional.orElse(listener.filterResults(new ArrayList<>(masterList), constraint.toString()), new ArrayList<T>()).get());
    }

    private FilterResults getFilterResultsFormat(@NonNull List<T> result) {
        FilterResults filterResults = new FilterResults();
        filterResults.count = result.size();
        filterResults.values = result;

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        //noinspection unchecked
        listener.publishResults((List<T>) results.values);
    }

    public interface GenericDialogListFilterListener<T> {

        /**
         * This method is called only if @isImplementingFilter return true. A method to implement custom searching/ filtering logic.
         *
         * @param masterList       the list with all the elements of the adapter
         * @param searchConstraint the query for search
         * @return filtered list for the search
         */
        List<T> filterResults(List<T> masterList, String searchConstraint);

        void publishResults(List<T> filteredList);

    }
}
