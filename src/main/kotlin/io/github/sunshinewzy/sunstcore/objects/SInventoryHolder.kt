package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class SInventoryHolder<T>(var data: T) : InventoryHolder {
    private val inventory: Inventory? = null
    
    
    override fun getInventory(): Inventory? {
        return inventory
    }
    
    
    companion object {
        fun Inventory.getSHolder(): SInventoryHolder<*>? {
            if (holder !is SInventoryHolder<*>)
                return null
            
            return holder as SInventoryHolder<*>
        }
    }
    
}