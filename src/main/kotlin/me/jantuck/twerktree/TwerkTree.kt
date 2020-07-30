package me.jantuck.twerktree

import me.jantuck.twerktree.compatibility.ModernProvider
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.java.JavaPlugin

class TwerkTree : JavaPlugin(), Listener {

    private val regexAllowedBlock by lazy {
        config.getStringList("allowedBlocks.regex").map { it.toRegex() }
    }
    private val allowedBlockMaterials by lazy {
        config.getStringList("allowedBlocks.absolute").map { Material.getMaterial(it) }
    }

    private val yRange by lazy { config.getInt("yRange") }
    private val flatRange by lazy { config.getInt("flatRange") }

    private val provider = ModernProvider()

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        saveDefaultConfig()
    }

    @EventHandler
    fun PlayerToggleSneakEvent.onSneak(){
        val player = this.player
        if (!player.isSneaking)
            return
        val location = player.location
        val world = player.world
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        for (xOff in -flatRange..flatRange)
            for (yOff in -yRange..yRange)
                for (zOff in -flatRange..flatRange){
                    val block = world.getBlockAt(x + xOff, y + yOff, z + zOff)
                    val blockType = block.type
                    val blockName = blockType.name
                    if (allowedBlockMaterials.any { it == blockType } || regexAllowedBlock.any { it.matches(blockName) }){
                        provider.boneMeal(block)
                    }
                }
    }
}