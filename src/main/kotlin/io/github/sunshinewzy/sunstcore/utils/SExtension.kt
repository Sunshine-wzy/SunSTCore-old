package io.github.sunshinewzy.sunstcore.utils

import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


//快速创建 5*9 边框
fun Inventory.createEdge(invSize: Int, edgeItem: ItemStack) {
    val meta = if (edgeItem.hasItemMeta()) edgeItem.itemMeta else Bukkit.getItemFactory().getItemMeta(edgeItem.type)
    meta.displayName = "§f边框"
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

// Object转Map
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

fun Player.hasCompleteTask(task: TaskBase): Boolean {
    val uid = uniqueId.toString()
    val progress = (if(DataManager.sPlayerData.containsKey(uid)) DataManager.sPlayerData[uid] else return false) ?: return false

    val projectName = task.taskStage.taskProject.projectName
    if(!progress.taskProgress.containsKey(projectName))
        return false

    val projects = progress.taskProgress[projectName] ?: return false
    val stageName = task.taskStage.stageName
    if(!projects.containsKey(stageName))
        return false

    val stages = projects[stageName] ?: return false
    if(!stages.containsKey(task.taskName))
        return false

    return stages[task.taskName] ?: return false
}