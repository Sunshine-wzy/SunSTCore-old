package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import java.io.File

object DataManager {
    private val dir = SunSTCore.getPlugin().dataFolder
    private val dataDir = File(dir, "data")
    
    val sPlayerData = HashMap<String, SPlayerData>()
    val sTaskData = HashMap<String, STaskData>()
    
    
    fun init() {
        loadFolderData("SPlayer"){
            file, fileName ->
            sPlayerData[fileName] = SPlayerData(fileName)
        }
    }
    
    
    private fun loadFolderData(
        folderName: String,
        dirFolder: File = File(dataDir, folderName),
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
    
}