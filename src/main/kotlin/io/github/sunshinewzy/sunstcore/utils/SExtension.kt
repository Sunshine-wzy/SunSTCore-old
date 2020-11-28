package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


//快速创建 5*9 边框
fun Inventory.createEdge(invSize: Int, edgeItem: ItemStack) {
    val meta = if (edgeItem.hasItemMeta()) edgeItem.itemMeta else Bukkit.getItemFactory().getItemMeta(edgeItem.type)
    meta.displayName = "§f边框"
    edgeItem.itemMeta = meta
    
    for(i in 0..8) {
        setItem(i, edgeItem)
        setItem(i + 9 * (invSize - 1), edgeItem)
    }
    for(i in 9..9*(invSize - 2) step 9) {
        setItem(i, edgeItem)
        setItem(i + 8, edgeItem)
    }
}