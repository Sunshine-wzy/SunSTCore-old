package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.inventory.ItemStack
import java.util.*

object SManager {

    // 判断两物品是否相同
    fun ItemStack.isSimilar(theItem: ItemStack?, lore: Boolean): Boolean {
        if(theItem == null) {
            return false
        }
        if(type == theItem.type && amount >= theItem.amount) {
            if(hasItemMeta() && theItem.hasItemMeta()) {
                if(itemMeta.hasDisplayName() && theItem.itemMeta.hasDisplayName()) {
                    return if(itemMeta.displayName == theItem.itemMeta.displayName) {
                        if(lore) {
                            if(itemMeta.hasLore() && theItem.itemMeta.hasLore()) {
                                (itemMeta.lore.toString()
                                        == theItem.itemMeta.lore.toString())
                            } else !itemMeta.hasLore() && !theItem.itemMeta.hasLore()
                        } else true
                    } else false
                }
                return if(!itemMeta.hasDisplayName() && !theItem.itemMeta.hasDisplayName()) {
                    if(lore) {
                        if(itemMeta.hasLore() && theItem.itemMeta.hasLore()) {
                            (itemMeta.lore.toString()
                                    == theItem.itemMeta.lore.toString())
                        } else !itemMeta.hasLore() && !theItem.itemMeta.hasLore()
                    } else true
                } else false
            }
            return !hasItemMeta() && !theItem.hasItemMeta()
        }
        return false
    }

    // Object转List
    fun <T> castList(obj: Any, clazz: Class<T>): List<T>? {
        val result: MutableList<T> = ArrayList()
        if(obj is List<*>) {
            for(o in obj) {
                result.add(clazz.cast(o))
            }
            return result
        }
        return null
    }
    
}