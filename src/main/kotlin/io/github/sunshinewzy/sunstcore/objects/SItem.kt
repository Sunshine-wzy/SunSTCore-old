package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SItem(itemStack: ItemStack) : ItemStack(itemStack) {
    companion object {
        fun ItemStack.setName(name: String) {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta.displayName = name
            itemMeta = meta
        }
        
        fun ItemStack.setLore(vararg lore: String) {
            setLore(lore.toList())
        }
        
        fun ItemStack.setLore(lore: List<String>) {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta.lore = lore
            itemMeta = meta
        }
        
        fun ItemStack.setNameAndLore(name: String, vararg lore: String) {
            setNameAndLore(name, lore.toList())
        }

        fun ItemStack.setNameAndLore(name: String, lore: List<String>) {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta.lore = lore
            meta.displayName = name
            itemMeta = meta
        }
    }
    
    constructor(type: Material) : this(ItemStack(type))
    
    constructor(type: Material, amount: Int) : this(ItemStack(type, amount))
    
    constructor(type: Material, damage: Short, amount: Int) : this(ItemStack(type, amount, damage))
    
    
    constructor(type: Material, name: String) : this(type) {
        setName(name)
    }
    
    constructor(type: Material, name: String, vararg lore: String) : this(type) {
        setNameAndLore(name, lore.toList())
    }
}