package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.exceptions.MachineStructureException
import io.github.sunshinewzy.sunstcore.exceptions.NoIngredientException
import io.github.sunshinewzy.sunstcore.objects.SBlock

/**
 * 机器结构
 */
sealed class MachineStructure(val shape: String, val ingredients: Map<Char, SBlock>) {
    val structure = HashMap<Triple<Int, Int, Int>, SBlock>()
    
    init {
        shapeStructure()
    }
    
    
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
     * 
     * a
     * """
     */
    class CentralSymmetry(shape: String, ingredients: Map<Char, SBlock>) : MachineStructure(shape, ingredients) {
        
        override fun specialStructure(x: Int, y: Int, z: Int, sBlock: SBlock) {
            if(z == 0){
                if(x > 0) throw MachineStructureException(
                    shape,
                    "The first line of CentralSymmetry's layer is the central block, which cannot have more than one char"
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
        
    }

    /**
     * 特定朝向
     */
    class Orientation(shape: String, ingredients: Map<Char, SBlock>) : MachineStructure(shape, ingredients) {
        
        override fun specialStructure(x: Int, y: Int, z: Int, sBlock: SBlock) {
            
        }
        
    }


    private fun shapeStructure() {
        val layers = shape.split("\n\n")
        layers.forEachIndexed { y, layer ->
            
            val lines = layer.split("\n")
            lines.forEachIndexed { z, line ->
                
                line.forEachIndexed { x, char ->
                    if(ingredients.containsKey(char)){
                        val sBlock = ingredients[char] ?: throw NoIngredientException(shape, char)
                        specialStructure(x, y, z, sBlock)
                    }
                    else throw NoIngredientException(shape, char)
                }
                
            }
            
        }
    }
    
    abstract fun specialStructure(x: Int, y: Int, z: Int, sBlock: SBlock)
    
}