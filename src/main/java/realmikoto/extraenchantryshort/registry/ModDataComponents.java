package realmikoto.extraenchantryshort.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import realmikoto.extraenchantryshort.ExtraEnchantryShort;

/**
 * 自定义数据组件注册中心。
 *
 * <p>{@link #LIMIT_BREAK_SOURCE}：破限附魔书的来源标记（布尔）。由闪电苦力怕
 * 代杀监守者掉落的破限书带此标记，拾取时据此区分「雷霆之礼」与「极限之证」
 * 两个隐藏进度；普通来源不打标记。</p>
 */
public final class ModDataComponents {

	public static final DataComponentType<Boolean> LIMIT_BREAK_SOURCE =
			DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build();

	private ModDataComponents() {
	}

	public static void register() {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
				ExtraEnchantryShort.id("limit_break_source"), LIMIT_BREAK_SOURCE);
	}
}
