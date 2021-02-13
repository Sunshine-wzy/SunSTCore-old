package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.exceptions.MachineStructureException
import io.github.sunshinewzy.sunstcore.exceptions.NoIngredientException
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.utils.addClone
import io.github.sunshinewzy.sunstcore.utils.setItem
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.Inventory

/**
 * 机器结构
 * 
 * @param shape 描述机器空间结构 以\n\n作为每层之间的分隔符 以\n作为每层中每行的分隔符 (推荐使用原生字符串构造)
 * @param ingredients [shape] 中每个字符指代的方块 [SBlock] (推荐使用 mapOf('x' to SBlock(Material.XXX), 'y' to SBlock(Material.XXX)) 的形式构造)
 * @param center 机器构造(和使用)的中心坐标, 请确保该坐标对应的方块不为空气
 */
abstract class SMachineStructure(val size: SMachineSize, val shape: String, val ingredients: Map<Char, SBlock>, val center: Triple<Int, Int, Int>) {
    val structure = HashMap<Triple<Int, Int, Int>, SBlock>()
    val centerBlock: SBlock
    
    
    init {
        shapeStructure()

        centerBlock = if(structure.containsKey(center)){
            val theCenterBlock = structure[center] ?: throw MachineStructureException(
                shape,
                "The center is not a block"
            )

            if(theCenterBlock.material == Material.AIR) throw  MachineStructureException(
                shape,
                "The center block cannot be AIR."
            )
            theCenterBlock
        }
        else throw MachineStructureException(
            shape,
            "The center is not a block."
        )
    }

    abstract fun specialStructure(x: Int, y: Int, z: Int, sBlock: SBlock)

    abstract fun judge(loc: Location): Boolean
    
    /**
     * 中心对称
     *
     * 自底层向上
     * 每层之间空一行
     * 每层第一行只能有一个字符
     * 其余行 为中心对称的一个角
     * 
     * """
     * a
     * 
     * a
     * ba
     * cba
     * 
     * a
     * """
     */
    class CentralSymmetry(
        size: SMachineSize,
        shape: String,
        ingredients: Map<Char, SBlock>,
        center: Triple<Int, Int, Int>
    ) : SMachineStructure(size, shape, ingredients, center) {
        
        override fun specialStructure(x: Int, y: Int, z: Int, sBlock: SBlock) {
            if(z == 0){
                if(x > 0) throw MachineStructureException(
                    shape,
                    "The first line of CentralSymmetry's layer is the central block, which cannot have more than one char."
                )
                
                structure[Triple(0, y, 0)] = sBlock
            }
            else{
                structure[Triple(x, y, z)] = sBlock
                structure[Triple(-z, y, x)] = sBlock
                structure[Triple(z, y, -x)] = sBlock
                structure[Triple(-x, y, -z)] = sBlock
            }
        }

        override fun judge(loc: Location): Boolean {
            var theLoc: Location
            structure.forEach { (coord, sBlock) -> 
                theLoc = loc.addClone(coord)
                if(!sBlock.isSimilar(theLoc))
                    return false
            }
            
            return true
        }
    }

    /**
     * 特定朝向
     */
    class Orientation(
        size: SMachineSize,
        shape: String,
        ingredients: Map<Char, SBlock>,
        center: Triple<Int, Int, Int>
    ) : SMachineStructure(size, shape, ingredients, center) {
        
        override fun specialStructure(x: Int, y: Int, z: Int, sBlock: SBlock) {
            
        }

        override fun judge(loc: Location): Boolean {
            
            return false
        }
    }

    
    fun displayInInventory(inv: Inventory, layer: Int) {
        structure.forEach { (coord, sBlock) -> 
            val (x, y, z) = coord
            if(y != layer - 1) return@forEach
            
            when(size) {
                SMachineSize.SIZE3 -> {
                    if(size.isCoordInSize(coord))
                        inv.setItem(invBaseX + x, invBaseY + z, sBlock.getDisplayItem())
                }
                
                SMachineSize.SIZE5 -> {
                    if(size.isCoordInSize(coord))
                        inv.setItem(invBaseX + x, invBaseY + z, sBlock.getDisplayItem())
                }
            }
        }
    }
    

    private fun shapeStructure() {
        val layers = shape.split("\n\n")
        
        layers.forEachIndexed forEachY@{ y, layer ->
            
            val lines = layer.split("\n")
            lines.forEachIndexed forEachZ@{ z, line ->
                
                line.forEachIndexed forEachX@{ x, char ->
                    if(char == ' ') return@forEachX
                    
                    if(ingredients.containsKey(char)){
                        val sBlock = ingredients[char] ?: throw NoIngredientException(shape, char)
                        
                        specialStructure(x, y, z, sBlock)
                    }
                    else throw NoIngredientException(shape, char)
                }
                
            }
            
        }
    }
    
    
    companion object {
        const val invBaseX = 5
        const val invBaseY = 3
    }
    
}