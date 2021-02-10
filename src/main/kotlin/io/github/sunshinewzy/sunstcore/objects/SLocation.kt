package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block

class SLocation(val world: String, val x: Int, val y: Int, val z: Int) {
    
    constructor(world: World, x: Int, y: Int, z: Int) : this(world.name, x, y, z)
    
    constructor(loc: Location) : this(loc.world, loc.blockX, loc.blockY, loc.blockZ)

    
    fun isSimilar(loc: Location): Boolean =
        world == loc.world.name && x == loc.blockX && y == loc.blockY && z == loc.blockZ
    
    fun setSBlock(sBlock: SBlock) {
        locations[this] = sBlock
    }
    
    fun getSBlock(): SBlock {
        if (locations.containsKey(this)) {
            return locations[this] ?: kotlin.run {
                val block = getBlock() ?: return SBlock(Material.AIR)
                return SBlock(block)
            }
        } else {
            val block = getBlock() ?: return SBlock(Material.AIR)
            return SBlock(block)
        }
    }
    
    fun getBlock(): Block? {
        val world = Bukkit.getServer().getWorld(world) ?: return null
        return world.getBlockAt(x, y, z)
    }
    

    override fun equals(other: Any?): Boolean =
        when {
            other == null -> false
            this === other -> true
            other !is SLocation -> false
            
            else -> world == other.world && x == other.x && y == other.y && z == other.z
        }

    override fun hashCode(): Int {
        var hash = 1
        hash = hash * 31 + world.hashCode()
        hash = hash * 31 + x
        hash = hash * 31 + y
        hash = hash * 31 + z
        return hash
    }

    override fun toString(): String =
        "$world;$x,$y,$z"
    
    
    companion object {
        private val locations = HashMap<SLocation, SBlock>()
        
        
        fun Location.toSLocation(): SLocation = SLocation(this)
    }
}