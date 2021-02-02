package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.metadata.MetadataValueAdapter
import org.bukkit.plugin.java.JavaPlugin

class SMetadataValue(plugin: JavaPlugin, var data: Any) : MetadataValueAdapter(plugin) {
    override fun value(): Any {
        return data
    }

    override fun invalidate() {
        
    }
}