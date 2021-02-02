package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin

class SItem(itemStack: ItemStack) : ItemStack(itemStack) {
    
    constructor(type: Material) : this(ItemStack(type))
    constructor(type: Material, name: String) : this(type) {
        setName(name)
    }
    constructor(type: Material, name: String, vararg lore: String) : this(type) {
        setNameAndLore(name, lore.toList())
    }
    
    constructor(type: Material, amount: Int) : this(ItemStack(type, amount))
    constructor(type: Material, amount: Int, name: String) : this(type, amount) {
        setName(name)
    }
    constructor(type: Material, amount: Int, name: String, vararg lore: String) : this(type, amount) {
        setNameAndLore(name, lore.toList())
    }
    
    constructor(type: Material, damage: Short, amount: Int) : this(ItemStack(type, amount, damage))
    constructor(type: Material, damage: Short, amount: Int, name: String) : this(type, damage, amount) {
        setName(name)
    }
    constructor(type: Material, damage: Short, amount: Int, name: String, vararg lore: String) : this(type, damage, amount) {
        setNameAndLore(name, lore.toList())
    }
    
    
    fun addRecipe(plugin: JavaPlugin, recipe: Recipe) {
        plugin.server.addRecipe(recipe)
    }
    


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

        fun ItemStack.isItemSimilar(item: ItemStack, checkLore: Boolean = true, checkAmount: Boolean = true, checkDurability: Boolean = false): Boolean {
            return if (type != item.type) {
                false
            } else if (checkAmount && amount < item.amount) {
                false
            } else if (checkDurability && durability != item.durability) {
                false
            } else if (hasItemMeta()) {
                val itemMeta = itemMeta

                if (item.hasItemMeta())
                    itemMeta.isMetaEqual(item.itemMeta, checkLore)
                else false
            } else !item.hasItemMeta()
        }

        fun ItemMeta.isMetaEqual(itemMeta: ItemMeta, checkLore: Boolean = true): Boolean {
            return if (itemMeta.hasDisplayName() != hasDisplayName()) {
                false
            } else if (itemMeta.hasDisplayName() && hasDisplayName() && itemMeta.displayName != displayName) {
                false
            } else if (!checkLore) {
                true
            } else if (itemMeta.hasLore() && hasLore()) {
                lore.isLoreEqual(itemMeta.lore)
            } else !itemMeta.hasLore() && !hasLore()
        }

        fun List<String>.isLoreEqual(lore: List<String>): Boolean {
            if(isEmpty() && lore.isEmpty()) return true

            return toString() == lore.toString()
        }
    }
}