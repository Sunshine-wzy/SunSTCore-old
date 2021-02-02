package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class SShapedRecipe(key: NamespacedKey, result: ItemStack) : ShapedRecipe(key, result) {
    constructor(
        key: NamespacedKey,
        result: ItemStack,
        ingredient: Map<Char, Material>,
        line1: String = "",
        line2: String = "",
        line3: String = ""
    ) : this(key, result) {
        shape(line1, line2, line3)
        ingredient.forEach { (char, value) -> 
            setIngredient(char, value)
        }
    }
    
    
}