package io.github.sunshinewzy.sunstcore.utils

import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import io.github.sunshinewzy.sunstcore.modules.task.TaskProject
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.orderWith
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.*
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


//region Any 对象

/**
 * Object转Map
 */
inline fun <reified K, reified V> Any.castMap(kClazz: Class<K>, vClazz: Class<V>): MutableMap<K, V>? {
    val result = HashMap<K, V>()
    if (this is Map<*, *>) {
        for ((key, value) in this) {
            if(key is K && value is V){
                result[kClazz.cast(key)] = vClazz.cast(value)
            }
        }
        return result
    }
    return null
}

inline fun <reified K, reified V> Any.castMap(kClazz: Class<K>, vClazz: Class<V>, targetMap: MutableMap<K, V>): Boolean {
    if (this is Map<*, *>) {
        for ((key, value) in this) {
            if(key is K && value is V)
                targetMap[kClazz.cast(key)] = vClazz.cast(value)
        }
        return true
    }
    return false
}

inline fun <reified K, reified V> Any.castMap(targetMap: MutableMap<K, V>): Boolean {
    if(castMap(K::class.java, V::class.java, targetMap))
        return true
    return false
}

fun Any.castMapBoolean(): MutableMap<String, Boolean> {
    val map = HashMap<String, Boolean>()
    
    if(this is Map<*, *>){
        forEach { key, value -> 
            if(key is String && value is Boolean){
                map[key] = value
            }
        }
    }
    
    return map
}

//endregion

//region TaskModule 任务模块

fun Player.hasCompleteTask(task: TaskBase?): Boolean {
    if(task == null) return true
    
    val taskProject = task.taskStage.taskProject
    val progress = taskProject.getProgress(this)
    
    return progress.hasCompleteTask(task)
}

fun Player.hasCompleteStage(stage: TaskStage?): Boolean {
    if(stage == null) return true
    if(stage.finalTask == null) return true
    
    val progress = stage.taskProject.getProgress(this)
    return progress.hasCompleteStage(stage)
}

//endregion

//region Player 玩家

fun Player.openInvWithSound(inv: Inventory, openSound: Sound, volume: Float, pitch: Float) {
    world.playSound(location, openSound, volume, pitch)
    openInventory(inv)
}

/**
 * 给予玩家物品时检测玩家背包是否已满
 * 如果未满则直接添加到玩家背包
 * 否则以掉落物的形式生成到玩家附近
 */
fun Player.giveItem(item: ItemStack) {
    if(inventory.isFull()){
        player.world.dropItem(player.location, item)
    }
    else {
        player.inventory.addItem(item)
    }
}

fun Player.giveItem(items: Array<ItemStack>) {
    items.forEach { 
        giveItem(it)
    }
}

fun Player.giveItem(item: Itemable) {
    giveItem(item.getTheItem())
}

/**
 * 获取 [taskProject] 的任务进度
 */
fun Player.getProgress(taskProject: TaskProject): TaskProgress = taskProject.getProgress(player)

/**
 * 获取 [task] 所在 [TaskProject] 的任务进度
 */
fun Player.getProgress(task: TaskBase): TaskProgress = getProgress(task.taskStage.taskProject)

/**
 * 完成某项任务
 * @param task 任务
 */
fun Player.completeTask(task: TaskBase, isCompleted: Boolean = true) {
    val progress = getProgress(task)
    progress.completeTask(task, isCompleted)
}

/**
 * 使用特定前缀发送消息
 */
fun Player.sendMsg(msg: String) {
    sendMessage(msg.replace('&', '§'))
}

/**
 * 生成粒子效果
 */
private fun Player.spawnParticle(particle: Particle, listLoc: List<Location>, count: Int) {
    listLoc.forEach {
        spawnParticle(particle, it, count)
    }
}

private fun Player.spawnParticle(particle: Particle, listLoc: List<Location>, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double) {
    listLoc.forEach {
        spawnParticle(particle, it, count, offsetX, offsetY, offsetZ)
    }
}

//endregion

//region Inventory 物品栏

/**
 * 判断物品栏中是否含有 [amount] 数量的物品 [item]
 */
fun Inventory.containsItem(item: ItemStack, amount: Int = 1): Boolean {
    if(amount <= 0) return true
    
    var cnt = amount
    storageContents.forEach {
        if(it == null) return@forEach
        
        if (it.isItemSimilar(item)) {
            cnt -= it.amount
            if (cnt <= 0) return true
        }
    }
    
    return false
}

fun Inventory.containsItem(items: Array<ItemStack>): Boolean {
    items.forEach { 
        if(!containsItem(it)) return false
    }
    return true
}

fun Inventory.isFull(): Boolean = firstEmpty() > size

fun Inventory.setItem(x: Int, y: Int, item: ItemStack) {
    setItem(x orderWith y, item)
}

/**
 * 快速创建 5*9 边框
 */
fun Inventory.createEdge(invSize: Int, edgeItem: ItemStack) {
    val meta = if (edgeItem.hasItemMeta()) edgeItem.itemMeta else Bukkit.getItemFactory().getItemMeta(edgeItem.type)
    meta.displayName = "§f"
    edgeItem.itemMeta = meta

    for(i in 0..8) {
        setItem(i, edgeItem)
        setItem(i + 9 * (invSize - 1), edgeItem)
    }
    for(i in 9..9*(invSize - 2) step 9) {
        setItem(i, edgeItem)
        setItem(i + 8, edgeItem)
    }
}


fun InventoryView.getSPlayer(): Player = player as Player

//endregion

//region File 文件

fun File.getDataPath(plugin: JavaPlugin) 
    = absolutePath.split("(?<=${plugin.dataFolder.absolutePath.replace('\\', '/')}/)[\\s\\S]*(?=/$name)".toRegex()).last()

//endregion

//region Recipe 配方

fun ShapedRecipe.getRecipe(): Array<ItemStack> {
    val recipe = Array(9) { ItemStack(Material.AIR) }
    val rows = shape
    val ingredients = ingredientMap
    
    for(i in rows.indices){
        val base = i * 3
        val str = rows[i]
        
        for(j in str.indices){
            if(str[j] != ' '){
                val item = ingredients[str[j]] ?: continue
                recipe[base + j] = ItemStack(item.type)
            }
        }
    }
    
    return recipe
}

//endregion

//region Block 方块

fun Block.getDurability() = state.data.toItemStack(1).durability

fun Block.getFaceLocation(face: BlockFace): Location = location.getFaceLocation(face)

//endregion

//region Location 坐标

fun Location.getFaceLocation(face: BlockFace): Location = clone().add(face.modX.toDouble(), face.modY.toDouble(), face.modZ.toDouble())

//endregion