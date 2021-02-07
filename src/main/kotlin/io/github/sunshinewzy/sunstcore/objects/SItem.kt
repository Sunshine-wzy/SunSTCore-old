package io.github.sunshinewzy.sunstcore.objects

import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin

open class SItem(item: ItemStack) : ItemStack(item) {
    
    constructor(item: ItemStack, name: String) : this(item) {
        setName(name)
    }
    constructor(item: ItemStack, name: String, vararg lore: String) : this(item) {
        setNameAndLore(name, lore.toList())
    }
    constructor(item: ItemStack, lore: List<String>) : this(item) {
        setLore(lore)
    }
    
    constructor(type: Material) : this(ItemStack(type))
    constructor(type: Material, name: String) : this(type) {
        setName(name)
    }
    constructor(type: Material, name: String, vararg lore: String) : this(type) {
        setNameAndLore(name, lore.toList())
    }
    constructor(type: Material, name: String, lore: List<String>) : this(type) {
        setNameAndLore(name, lore)
    }
    constructor(type: Material, lore: List<String>) : this(type) {
        setLore(lore)
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


    /**
     * 添加物品行为 - 当玩家手持物品交互时调用添加的行为
     * 此函数已经帮您判断好了 [PlayerInteractEvent] 事件的物品(确保为你的 [SItem])
     * 无需重复判断
     */
    fun addAction(block: PlayerInteractEvent.() -> Unit): SItem {
        val actions = itemActions[this] ?: kotlin.run {
            itemActions[this] = arrayListOf(block)
            return this
        }
        
        actions.add(block)
        return this
    }


    override fun equals(other: Any?): Boolean =
        when {
            other == null -> false
            this === other -> true
            other !is ItemStack -> false
            else -> isItemSimilar(other) 
        }

    override fun hashCode(): Int {
        var hash = 1
        hash = hash * 31 + type.hashCode()
        hash = hash * 31 + amount
        hash = hash * 31 + if(hasItemMeta()) itemMeta.hashCode() else 0
        return hash
    }

    companion object {
        private val itemActions = HashMap<SItem, ArrayList<PlayerInteractEvent.() -> Unit>>()
        
        
        internal fun initAction() {
            subscribeEvent<PlayerInteractEvent> { 
                if(item == null || item.type == Material.AIR) return@subscribeEvent
                
                itemActions.forEach { (sItem, blocks) -> 
                    if(item == sItem){
                        blocks.forEach { it(this) }
                    }
                }
            }
        }
        
        
        fun ItemStack.setName(name: String): ItemStack {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta.displayName = name
            itemMeta = meta
            return this
        }

        fun ItemStack.setLore(vararg lore: String): ItemStack {
            setLore(lore.toList())
            return this
        }

        fun ItemStack.setLore(lore: List<String>): ItemStack {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta.lore = lore
            itemMeta = meta
            return this
        }

        fun ItemStack.setNameAndLore(name: String, vararg lore: String): ItemStack {
            setNameAndLore(name, lore.toList())
            return this
        }

        fun ItemStack.setNameAndLore(name: String, lore: List<String>): ItemStack {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta.lore = lore
            meta.displayName = name
            itemMeta = meta
            return this
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
        
        fun ItemStack.addRecipe(plugin: JavaPlugin, recipe: Recipe): ItemStack {
            plugin.server.addRecipe(recipe)
            return this
        }
        
        fun ItemStack.addRecipe(plugin: JavaPlugin, vararg recipes: Recipe): ItemStack {
            recipes.forEach { 
                plugin.server.addRecipe(it)
            }
            
            return this
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