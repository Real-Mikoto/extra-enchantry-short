package realmikoto.extraenchantryshort.advancement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import realmikoto.extraenchantryshort.ExtraEnchantryShort;

/**
 * 实战成就（usage 树）统一授予入口。
 *
 * <p>14 个实战成就均为数据驱动 JSON（{@code data/extra-enchantry-short/advancement/usage/*.json}），
 * 触发器统一为 {@code minecraft:impossible}（无法被任何游戏事件自然触发），
 * 由各附魔逻辑在效果实际触发处调用 {@link #award(ServerPlayer, ResourceKey)} 授予。</p>
 *
 * <p>注意：26.2 的成就 ID 包含 advancement/ 下的子目录路径
 * （{@code ServerAdvancementManager} 按文件相对路径建键），
 * 因此这里的键必须是 {@code usage/xxx} 而非裸 {@code xxx}。</p>
 */
public final class Advancements {

	/** usage 树所有 JSON 的判定准则名（与 JSON criteria 键一致） */
	private static final String CRITERION = "triggered";

	// ========== 战斗类 ==========

	/** 处刑者：断罪首次触发处决 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> EXECUTOR = key("executor");

	/** 铜墙铁壁：壁垒挡下 ≥4 点伤害 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> BULWARK_SAVE = key("bulwark_save");

	/** 拔刀斩：藏锋首次拔刀一击 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> DRAW_STRIKE = key("draw_strike");

	/** 冲锋陷阵：冲阵首次撞击命中 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> CHARGE = key("charge");

	/** 金蝉脱壳：假象首次替身逃脱 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> DECOY_ESCAPE = key("decoy_escape");

	/** 余烬不灭：余烬首次免于坠落死亡 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> EMBER_SAVE = key("ember_save");

	/** 劫后余辉：余辉首次复活触发 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> AFTERGLOW_TRIGGER = key("afterglow_trigger");

	// ========== 功能类 ==========

	/** 丰收时刻：丰壤首次双倍收获 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> HARVEST = key("harvest");

	/** 倦鸟归林：归羽首次自动返手 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> RETURN_HOME = key("return_home");

	/** 深海呼吸：渊息首次水下换气 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> DEEP_BREATH = key("deep_breath");

	/** 雷霆万钧：霆霓首次感电连锁 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> THUNDER_CHAIN = key("thunder_chain");

	/** 火树银花：坠星首次强化烟花爆炸 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> FIREWORKS = key("fireworks");

	/** 基石崩解（隐藏挑战）：拓阶挖掘基岩 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> BEDROCK_BREAKER = key("bedrock_breaker");

	// ========== 环佩与獠牙 ==========

	/** 环佩琳琅：首次穿戴任意配饰 */
	public static final ResourceKey<net.minecraft.advancements.Advancement> ACCESSORY_ATTIRE = key("accessory_attire");

	private Advancements() {
	}

	private static ResourceKey<net.minecraft.advancements.Advancement> key(String name) {
		return ResourceKey.create(Registries.ADVANCEMENT, ExtraEnchantryShort.id("usage/" + name));
	}

	/**
	 * 授予隐秘挑战进度（幂等），键含 hidden_challenges/ 子目录前缀。
	 * （挑战奖励改由各附魔逻辑直接调用，不走统一管理器）
	 */
	public static void awardHidden(ServerPlayer player, String name) {
		MinecraftServer server = player.level().getServer();
		if (server == null) {
			return;
		}
		AdvancementHolder holder = server.getAdvancements()
				.get(ExtraEnchantryShort.id("hidden_challenges/" + name));
		if (holder != null) {
			player.getAdvancements().award(holder, CRITERION);
		}
	}

	/**
	 * 按 ResourceKey 授予进度（幂等：原版 award 已获得时不会重复触发）。
	 * 26.2 的进度不是注册表，须走 {@code server.getAdvancements().get(Identifier)}。
	 */
	public static void award(ServerPlayer player, ResourceKey<net.minecraft.advancements.Advancement> key) {
		MinecraftServer server = player.level().getServer();
		if (server == null) {
			return;
		}
		AdvancementHolder holder = server.getAdvancements().get(key.identifier());
		if (holder != null) {
			player.getAdvancements().award(holder, CRITERION);
		} else {
			ExtraEnchantryShort.LOGGER.warn("进度未找到: {}", key.identifier());
		}
	}
}
