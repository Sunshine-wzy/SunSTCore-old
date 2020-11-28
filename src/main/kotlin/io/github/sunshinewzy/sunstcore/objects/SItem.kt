package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SItem(type: Material) : ItemStack(type) {
    constructor(type: Material, amount: Int) : this(type)
}