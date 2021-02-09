package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

class SBlock(val material: Material, val data: MaterialData) {
    var name = ""
    
    
    constructor(material: Material, durability: Byte) : this(material, MaterialData(material, durability))
    
    constructor(material: Material, durability: Byte, name: String) : this(material, durability) {
        this.name = name
    }
    
    constructor(loc: Location) : this(loc.block.type, loc.block.state.data)
    
    constructor(item: ItemStack) : this(item.type, item.data)
    
    
    fun setLocation(loc: Location) {
        val block = loc.block ?: return
        block.type = material
        val blockState = block.state ?: return
        blockState.data = data
    }
    
    fun isSimilar(block: BlockState): Boolean
        = if(material == block.type){
            data == block.data
        } else false
    
    fun isSimilar(block: Block): Boolean = isSimilar(block.state)
    
    fun isSimilar(loc: Location): Boolean = isSimilar(loc.block)
    
    fun hasName(): Boolean = name != ""


    override fun equals(other: Any?): Boolean =
        when {
            other == null -> false
            this === other -> true
            other !is SBlock -> false
            
            else -> if(name != other.name) false
            else material == other.material && data == other.data
        }

    override fun hashCode(): Int {
        var hash = 1
        hash = hash * 31 + material.hashCode()
        hash = hash * 31 + data.hashCode()
        hash = hash * 31 + if(hasName()) name.hashCode() else 0
        return hash
    }

    override fun toString(): String = "SBlock{material=$material,data=$data}"
    
}