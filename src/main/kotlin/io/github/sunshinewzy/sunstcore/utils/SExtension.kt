package io.github.sunshinewzy.sunstcore.utils

import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import io.github.sunshinewzy.sunstcore.modules.task.TaskProject
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


//region 快速创建边框

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

//endregion

//region 对象转化

/**
 * Object转Map
 */
fun <K, V> Any.castMap(kClazz: Class<K>, vClazz: Class<V>): MutableMap<K, V>? {
    val result = HashMap<K, V>()
    if (this is Map<*, *>) {
        for ((key, value) in this) {
            result[kClazz.cast(key)] = vClazz.cast(value)
        }
        return result
    }
    return null
}

fun <K, V> Any.castMap(kClazz: Class<K>, vClazz: Class<V>, targetMap: MutableMap<K, V>): Boolean {
    if (this is Map<*, *>) {
        for ((key, value) in this) {
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

//endregion

//region 任务模块

fun Player.hasCompleteTask(task: TaskBase?): Boolean {
    if(task == null) return true
    
    val taskProject = task.taskStage.taskProject
    val progress = taskProject.getProgress(this) ?: return false
    
    return progress.hasCompleteTask(this, task)
}

fun Player.hasCompleteStage(stage: TaskStage?): Boolean {
    if(stage == null) return true
    if(stage.finalTask == null) return true
    
    val progress = stage.taskProject.getProgress(this) ?: return false
    return progress.hasCompleteStage(this, stage)
}

//endregion

//region 玩家 Player

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
fun Player.finishTask(task: TaskBase) {
    val progress = getProgress(task)
    
}

/**
 * 使用特定前缀发送消息
 */
fun Player.sendMsg(msg: String) {
    sendMessage(msg.replace('&', '§'))
}

//endregion

//region Inventory

/**
 * 判断物品栏中是否含有 [amount] 数量的物品 [item]
 */
fun Inventory.containsItem(item: ItemStack, amount: Int = 1): Boolean {
    if(amount <= 0) return true
    
    var cnt = amount
    storageContents.forEach {
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

fun InventoryView.getSPlayer(): Player = player as Player

//endregion

//region File

fun File.getDataPath(plugin: JavaPlugin) 
    = absolutePath.split("(?<=${plugin.dataFolder.absolutePath}/)[\\s\\S]*(?=/$name.yml)".toRegex()).last()

//endregion