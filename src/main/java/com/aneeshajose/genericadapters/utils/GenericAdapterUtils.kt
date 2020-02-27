package com.aneeshajose.genericadapters.utils

import com.aneeshajose.genericadapters.model.ItemEncapsulator
import java.math.BigInteger
import java.util.*

fun <T> getItemEncapsulatorsFromObjects(items: List<T?>?): List<ItemEncapsulator> {
    return items?.map { getItemEncapsulatorFromObject(it) } ?: emptyList()
}

fun encapsulateItems(type: Int, arrayList: List<*>?): List<ItemEncapsulator> {
    return arrayList?.filterNotNull()?.map { ItemEncapsulator(type, it) } ?: Collections.emptyList()
}

fun <T> encapsulateItem(type: Int, obj: T): ItemEncapsulator {
    return ItemEncapsulator(type, obj)
}

fun <T> getItemEncapsulatorFromObject(obj: T?): ItemEncapsulator {
    return ItemEncapsulator(BigInteger((obj
            ?: 0)::class.java.simpleName.toByteArray()).toInt(), obj)
}

fun <T : ItemEncapsulator> getItemEncapsulatorsWithItemClassType(items: List<T?>?, type: Class<T>): List<T> {
    return items?.asSequence()?.filterNotNull()?.filter { type.isInstance(it.item) }?.toList()
            ?: emptyList()
}

fun <T, I : ItemEncapsulator> getAllItemsFromEncapsulatorsOfClassType(items: List<I?>?, type: Class<T>): List<T> {
    return items?.asSequence()?.filterNotNull()?.map { it.item }?.filterIsInstance(type)?.toList()
            ?: emptyList()
}

fun <T> getFilteredItemsOnSearchConstraint(items: List<T?>?, searchString: String): List<T?> {
    return items?.filter { it != null && searchString.equals(it.toString(), true) }
            ?: emptyList<T>()
}
