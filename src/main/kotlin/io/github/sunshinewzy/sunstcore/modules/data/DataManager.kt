package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SunSTPlayerData
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.LineStickData
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.RangeStickData
import io.github.sunshinewzy.sunstcore.utils.giveItem
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import java.io.File

object DataManager : Initable {
    private val dir = SunSTCore.getPlugin().dataFolder
    private val allReloadData = ArrayList<SAutoSaveData>()
    
    val allAutoSaveData = ArrayList<SAutoSaveData>()
    
    val sPlayerData = HashMap<String, SunSTPlayerData>()
    val sTaskData = HashMap<String, STaskData>()
    
    val firstJoinGiveOpenItems = HashMap<String, ItemStack>()
    
    
    override fun init() {
//        loadFolderData("SPlayer"){
//            file, fileName ->
//            SunSTPlayerData(SunSTCore.getPlugin(), fileName, file).load()
//        }
        
        subscribeEvent<PlayerJoinEvent> { 
            val uid = player.uniqueId.toString()
            
            val data = player.getSunSTData()
            val isFirstJoinGive = data.isFirstJoinGive
            firstJoinGiveOpenItems.forEach { (projectName, openItem) -> 
                if(!isFirstJoinGive.containsKey(projectName) || isFirstJoinGive[projectName] != true){
                    player.giveItem(openItem)
                    isFirstJoinGive[projectName] = true
                }
            }
        }

        LineStickData.init()
        RangeStickData.init()
    }
    
    fun saveData() {
        allAutoSaveData.forEach { 
            it.save()
        }
        
//        sPlayerData.values.forEach { 
//            it.save()
//        }
    }
    
    fun reloadData() {
        allReloadData.forEach { 
            it.save()
            it.load()
        }
    }
    
    fun addReloadData(data: SAutoSaveData) {
        allReloadData.add(data)
    }
    
    private fun loadFolderData(
        folderName: String,
        dirFolder: File = File(dir, folderName),
        block: (file: File, fileName: String) -> Unit
    ) {
        if(!dirFolder.exists()) return
        val files = dirFolder.listFiles() ?: return
        
        files.forEach {
            if(it.isFile){
                val extensionName = it.extension
                if(extensionName == "yml"){
                    val fileName = it.nameWithoutExtension
                    block(it, fileName)
                }
            }
            
//            else if(it.isDirectory){
//                loadFolderData()
//            }
        }
    }
    
    
    fun Player.getSunSTData(): SunSTPlayerData {
        val uid = uniqueId.toString()
        
        if(sPlayerData.containsKey(uid))
            return sPlayerData[uid]!!
        
        val data = SunSTPlayerData(SunSTCore.getPlugin(), uid)
        data.load()
        
        sPlayerData[uid] = data
        return data
    }
    
    inline fun <reified V> YamlConfiguration.getMap(
        key: String,
        map: MutableMap<String, V>
    ): Boolean {
        if(!contains(key))
            return false
        
        val root = getConfigurationSection(key) ?: return false
        val keys = root.getKeys(false)
        keys.forEach { 
            val value = root.get(it) ?: return@forEach
            if(value is V){
                map[it] = value
            }
        }

        return true
    }
    
}