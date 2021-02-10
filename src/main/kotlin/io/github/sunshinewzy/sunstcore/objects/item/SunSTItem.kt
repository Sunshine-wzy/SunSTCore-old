package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.addRecipe
import io.github.sunshinewzy.sunstcore.objects.SShapedRecipe
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.LineStick
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.RangeStick
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

enum class SunSTItem(val item: ItemStack) : Itemable {
    CONSTRUCTIONSTICK_IRON(
        LineStick(SItem(
            Material.IRON_SPADE,
            "§f铁制建筑手杖",
            "§e将我拿在副手",
            "§e用主手放方块",
            "§a[建造长度: 3]"
        ), 3)
    ),
    CONSTRUCTIONSTICK_DIAMOND(
        RangeStick(SItem(
            Material.DIAMOND_SPADE,
            "§b钻石建筑手杖",
            "§e将我拿在主手并对准方块",
            "§e(请确保背包中有该方块)",
            "§a[最大建造半径: 5]"
        ), 5)
    )
    
    
    ;


    constructor(
        item: SItem,
        key: String,
        ingredient: Map<Char, Material>,
        line1: String = "",
        line2: String = "",
        line3: String = ""
    ) : this(item) {
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