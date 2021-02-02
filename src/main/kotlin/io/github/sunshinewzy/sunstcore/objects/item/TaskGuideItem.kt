package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.objects.SItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class TaskGuideItem(val item: ItemStack) {
    BACK(SItem(Material.BARRIER, "§c返回")),
    
    ;
}