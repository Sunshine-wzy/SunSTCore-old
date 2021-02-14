package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.addRecipe
import io.github.sunshinewzy.sunstcore.objects.SShapedRecipe
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

enum class SunSTItem(val sName: String, val item: ItemStack) : Itemable {
    
    
    
    ;

    
    init {
        SItem.items[sName] = item
    }

    constructor(
        sName: String,
        item: SItem,
        key: String,
        ingredient: Map<Char, Material>,
        line1: String = "",
        line2: String = "",
        line3: String = ""
    ) : this(sName, item) {
        item.addRecipe(
            SunSTCore.getPlugin(),
            SShapedRecipe(
                NamespacedKey(SunSTCore.getPlugin(), key),
                item,
                ingredient,
                line1, line2, line3
            )
        )
    }


    override fun getSItem(): ItemStack = item

    companion object : Initable {
        override fun init() {}
    }
}