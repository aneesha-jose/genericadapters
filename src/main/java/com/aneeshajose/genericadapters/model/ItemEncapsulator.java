package com.aneeshajose.genericadapters.model;

public class ItemEncapsulator {

    private int itemType;
    private Object item;
    private boolean selectable = true;

    public ItemEncapsulator(int itemType, Object item) {
        this.itemType = itemType;
        this.item = item;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public String toString() {
        return item != null ? item.toString() : super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && item != null) {
            if (obj instanceof ItemEncapsulator) {
                if (((ItemEncapsulator) obj).getItem() != null) {
                    return ((ItemEncapsulator) obj).getItem().equals(item);
                }
            } else if (obj.getClass().equals(item.getClass())) {
                return obj.equals(item);
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 31 * itemType + (item != null ? item.hashCode() : 0);
    }
}
