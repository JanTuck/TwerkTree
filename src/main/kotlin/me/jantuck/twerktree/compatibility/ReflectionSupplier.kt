package me.jantuck.twerktree.compatibility

import com.esotericsoftware.reflectasm.MethodAccess
import org.bukkit.Bukkit
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

object ReflectionSupplier {

    private val version = Bukkit.getServer().javaClass.packageName.split(".")[3]

    val CRAFT_BLOCK_CLASS: Class<*> by lazy { getNMSClass("block.CraftBlock", false) }
    val CRAFT_BLOCK_METHOD_ACCESS: MethodAccess by lazy {
        MethodAccess.get(CRAFT_BLOCK_CLASS)
    }

    /**
     * This is only available on 1.13->1.16
     */
    val GET_POSITION_METHOD_INDEX: Int by lazy {
        CRAFT_BLOCK_METHOD_ACCESS.getIndex("getPosition")
    }

    val CRAFT_ITEM_STACK_CLASS: Class<*> by lazy {
        getNMSClass("inventory.CraftItemStack", false)
    }
    val CRAFT_ITEM_STACK_METHOD_ACCESS: MethodAccess by lazy {
        MethodAccess.get(CRAFT_ITEM_STACK_CLASS)
    }
    val CRAFT_ITEM_STACK_AS_NMS_COPY: Int by lazy {
        CRAFT_ITEM_STACK_METHOD_ACCESS.getIndex("asNMSCopy")
    }

    /**
     * Craft world is the same on all versions // Atleast for my usages.
     */
    val CRAFT_WORLD_CLASS: Class<*> by lazy { getNMSClass("CraftWorld", false)}
    val CRAFT_WORLD_METHOD_ACCESS: MethodAccess by lazy {
        MethodAccess.get(CRAFT_WORLD_CLASS)
    }

    val CRAFT_WORLD_HANDLE_METHOD_INDEX: Int by lazy {
        CRAFT_WORLD_METHOD_ACCESS.getIndex("getHandle")
    }

    val NMS_WORLD_CLASS: Class<*> by lazy {
        getNMSClass("World")
    }
    val NMS_WORLD_METHOD_ACCESS: MethodAccess by lazy {
        MethodAccess.get(NMS_WORLD_CLASS)
    }
    val TRIGGER_EFFECT_WORLD_INDEX: Int by lazy {
        NMS_WORLD_METHOD_ACCESS.getIndex("triggerEffect")
    }

    /**
     * BlockPosition is present in 1.8->1.16, in 1.8.8->1.12 you gotta use reflection to make a new instance.
     */
    val NMS_BLOCK_POSITION_CLASS: Class<*> by lazy {
        getNMSClass("BlockPosition")
    }

    /**
     * Only used in 1.8->1.12
     */
    val NMS_BLOCK_POSITION_CONSTRUCTOR: MethodHandle by lazy {
        MethodHandles.lookup().findConstructor(NMS_BLOCK_POSITION_CLASS, MethodType.methodType(Void.TYPE, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType))
    }

    /**
     * GENERATOR access has the trigger effect method in newer version. I'm gonna assume it is only on 1.13->1.16+
     */
    val GENERATOR_ACCESS_CLASS: Class<*> by lazy { getNMSClass("GeneratorAccess")}
    val GENERATOR_ACCESS_METHOD_ACCESS: MethodAccess by lazy {
        MethodAccess.get(GENERATOR_ACCESS_CLASS)
    }
    val TRIGGER_EFFECT_INDEX: Int by lazy {
        GENERATOR_ACCESS_METHOD_ACCESS.getIndex("triggerEffect")
    }

    val NMS_ITEM_STACK_CLASS: Class<*> by lazy {
        getNMSClass("ItemStack")
    }


    /**
     * Differs per version
     */
    val NMS_BONE_MEAL_CLASS: Class<*> by lazy {
        getNMSClass(if (getLegacy().old) "ItemDye" else "ItemBoneMeal")
    }

    val NMS_BONE_MEAL_METHOD_ACCCESS: MethodAccess by lazy {
        MethodAccess.get(NMS_BONE_MEAL_CLASS)
    }

    val NMS_BONE_MEAL_APPLY_INDEX: Int by lazy {
        when (getLegacy()){
            LegacyType.OLD_OLD -> NMS_BONE_MEAL_METHOD_ACCCESS.getIndex("a",
                NMS_ITEM_STACK_CLASS,
                NMS_WORLD_CLASS,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType)
            else -> NMS_BONE_MEAL_METHOD_ACCCESS.getIndex("a", NMS_ITEM_STACK_CLASS, NMS_WORLD_CLASS, NMS_BLOCK_POSITION_CLASS)
        }
    }



    enum class LegacyType(val old: Boolean) {
        OLD_OLD(true), OLD(true), NEWER(false)
    }

    fun getLegacy() : LegacyType{
        return when {
            version.startsWith("v1_7") -> LegacyType.OLD_OLD
            version.startsWith("v1_8") -> LegacyType.OLD
            version.startsWith("v1_9") -> LegacyType.OLD
            version.startsWith("v1_10") -> LegacyType.OLD
            version.startsWith("v1_11") -> LegacyType.OLD
            version.startsWith("v1_12") -> LegacyType.OLD
            version.startsWith("v1_13") -> LegacyType.NEWER
            version.startsWith("v1_14") -> LegacyType.NEWER
            version.startsWith("v1_15") -> LegacyType.NEWER
            version.startsWith("v1_16") -> LegacyType.NEWER
            else -> LegacyType.NEWER
        }
    }

    private fun getNMSClass(str: String, isNMS: Boolean = true) : Class<*> {
        return if (isNMS)
            Class.forName("net.minecraft.server.$version.$str")
        else
            Class.forName("org.bukkit.craftbukkit.$version.$str")
    }
}