package me.jantuck.twerktree.compatibility

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class ModernProvider {

    private val boneMealItemStack by lazy {
        @Suppress("DEPRECATION")
        when (ReflectionSupplier.getLegacy().new) {
            true -> ReflectionSupplier
                .CRAFT_ITEM_STACK_METHOD_ACCESS
                .invoke(
                    null,
                    ReflectionSupplier.CRAFT_ITEM_STACK_AS_NMS_COPY,
                    ItemStack(Material.BONE_MEAL)
                )
            else -> ReflectionSupplier
                .CRAFT_ITEM_STACK_METHOD_ACCESS
                .invoke(
                    null,
                    ReflectionSupplier.CRAFT_ITEM_STACK_AS_NMS_COPY,
                    ItemStack(Material.getMaterial("INK_SACK")!!, 1, 15)
                )
        }
    }

    private fun getBlockPosition(block: Block): Array<out Any> {
        return when (ReflectionSupplier.getLegacy()) {
            ReflectionSupplier.LegacyType.OLD_OLD -> arrayOf(block.x, block.y, block.z)
            ReflectionSupplier.LegacyType.OLD -> arrayOf(
                ReflectionSupplier.NMS_BLOCK_POSITION_CONSTRUCTOR.invokeWithArguments(
                    block.x,
                    block.y,
                    block.z
                )
            )
            else -> arrayOf(
                ReflectionSupplier.CRAFT_BLOCK_METHOD_ACCESS.invoke(
                    block,
                    ReflectionSupplier.GET_POSITION_METHOD_INDEX
                )
            )
        }
    }

    fun boneMeal(block: Block) {
        ReflectionSupplier
            .NMS_BONE_MEAL_METHOD_ACCESS
            .invoke(
                null,
                ReflectionSupplier.NMS_BONE_MEAL_APPLY_INDEX,
                boneMealItemStack,
                ReflectionSupplier.CRAFT_WORLD_METHOD_ACCESS.invoke(
                    block.world,
                    ReflectionSupplier.CRAFT_WORLD_HANDLE_METHOD_INDEX
                ),
                *getBlockPosition(block)
            )
        applyEffect(block)
    }

    private fun applyEffect(block: Block) {
        when (ReflectionSupplier.getLegacy()) {
            ReflectionSupplier.LegacyType.NEWER -> { // Nicer
                ReflectionSupplier.GENERATOR_ACCESS_METHOD_ACCESS.invoke(
                    ReflectionSupplier.CRAFT_WORLD_METHOD_ACCESS.invoke(
                        block.world,
                        ReflectionSupplier.CRAFT_WORLD_HANDLE_METHOD_INDEX
                    ),
                    ReflectionSupplier.TRIGGER_EFFECT_INDEX,
                    2005,
                    *getBlockPosition(block),
                    0
                )
            }
            else -> { // Ewie
                ReflectionSupplier.NMS_WORLD_METHOD_ACCESS.invoke(
                    ReflectionSupplier.CRAFT_WORLD_METHOD_ACCESS.invoke(
                        block.world,
                        ReflectionSupplier.CRAFT_WORLD_HANDLE_METHOD_INDEX
                    ),
                    ReflectionSupplier.TRIGGER_EFFECT_WORLD_INDEX,
                    2005,
                    *getBlockPosition(block),
                    0
                )
            }
        }
    }

}