package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.modules.task.TaskProject
import org.bukkit.configuration.file.YamlConfiguration

class STaskData(val taskProject: TaskProject) : SAutoSaveData(taskProject.projectName) {
    
    
    override fun modifyConfig(config: YamlConfiguration) {
        
    }

    override fun loadConfig(config: YamlConfiguration) {
        
    }
    
}