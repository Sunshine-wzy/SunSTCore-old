package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import java.io.File
import java.util.*
import kotlin.collections.HashMap

object DataManager {
    val sPlayerData = HashMap<String, SPlayerData>()
    
    
    fun init() {
        val dir = SunSTCore.getPlugin().dataFolder
        val dataDir = File(dir, "data")
        
        val dirSPlayerData = File(dataDir, "sPlayerData")
        if(dirSPlayerData.exists()) {
            dirSPlayerData.listFiles()?.forEach {
                if(it.extension == "yml"){
                    val nameSPlayerData = it.nameWithoutExtension
                    sPlayerData[nameSPlayerData] = SPlayerData(nameSPlayerData, "data")
                }
            }
        }
    }
    
    private fun loadDirs(dir: File) {
        if(!dir.exists()) return
        val files = dir.listFiles() ?: return
        files.forEach { 
            if(it.isFile){
                val extensionName = it.extension
                if(extensionName == "yml"){
                    val fileName = it.nameWithoutExtension
                    
                }
            }
            else if(it.isDirectory){
                loadDirs(it)
            }
        }
    }
    
}