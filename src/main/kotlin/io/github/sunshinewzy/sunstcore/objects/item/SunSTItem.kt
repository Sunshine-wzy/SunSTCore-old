package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.addRecipe
import io.github.sunshinewzy.sunstcore.objects.SShapedRecipe
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.block.BlockFace

enum class SunSTItem(val item: ItemStack) : Itemable {
    CONSTRUCTIONSTICK_IRON(
        SItem(Material.IRON_SPADE, "§f铁制建筑手杖", "§e将我拿在副手", "§e用主手放方块", "§a就能一次放置3个方块")
    ),
    CONSTRUCTIONSTICK_GOLD(
        SItem(Material.GOLD_SPADE, "§e金制建筑手杖", "§a将我拿在主手并对准方块", "§a(请确保背包中有该方块)")
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


    override fun getTheItem(): ItemStack = item
}