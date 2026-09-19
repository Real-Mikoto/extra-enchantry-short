package realmikoto.extraenchantryshort;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import realmikoto.extraenchantryshort.accessory.Accessories;
import realmikoto.extraenchantryshort.accessory.AccessoryAttachments;
import realmikoto.extraenchantryshort.accessory.AccessoryManager;
import realmikoto.extraenchantryshort.advancement.Advancements;
import realmikoto.extraenchantryshort.combat.AfterglowManager;
import realmikoto.extraenchantryshort.combat.DecoyManager;
import realmikoto.extraenchantryshort.combat.HomingPlumeManager;
import realmikoto.extraenchantryshort.combat.JudgementManager;
import realmikoto.extraenchantryshort.combat.LimitBreakManager;
import realmikoto.extraenchantryshort.combat.LoamManager;
import realmikoto.extraenchantryshort.combat.SanctuaryManager;
import realmikoto.extraenchantryshort.combat.SheathedEdgeManager;
import realmikoto.extraenchantryshort.combat.ShieldChargeManager;
import realmikoto.extraenchantryshort.fx.FxHelper;
import realmikoto.extraenchantryshort.registry.ModCreativeTabs;
import realmikoto.extraenchantryshort.registry.ModDataComponents;
import realmikoto.extraenchantryshort.registry.ModEffects;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * Extra Enchantry Short 主入口。
 *
 * <p>职责收窄为三件事：modid 常量与 ID 工厂、静态注册装配（效果 / 创造页签 /
 * 数据组件 / 配饰与宝石）、事件装配（tick / 死亡 / 连接 / 战利品 / 附魔门禁）。
 * 附魔注册键与查询规则见 {@link ModEnchantments}，具体机制逻辑分属
 * {@code combat} 包内的各管理器。</p>
 */
public class ExtraEnchantryShort implements ModInitializer {

	public static final String MOD_ID = "extra-enchantry-short";

	/** 按最佳实践以 modid 命名 logger，便于在日志中区分来源 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Attachment 持有类显式触发静态注册（必须早于任何玩家数据读取，
		// 否则存量配饰会被当作未知 attachment 丢弃——背包清空级事故）
		AccessoryAttachments.register();

		ModCreativeTabs.register();
		ModEffects.register();

		// 破限附魔书来源标记组件（区分隐藏进度「雷霆之礼 / 极限之证」）
		ModDataComponents.register();

		// 环佩与獠牙：16 配饰 + 8 宝石 + socketed_gem 组件
		Accessories.register();

		// 配饰结算：属性类 + 自然恢复 tick（每玩家）
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				AccessoryManager.tick(player);
			}
		});

		// 配饰击杀类被动（魂铃回复饥饿 / 魂珀额外经验）
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (source.getEntity() instanceof ServerPlayer killer) {
				AccessoryManager.onKillMob(killer, entity);
			}
		});

		// 破限附魔书的掉落（监守者 0.05% / 闪电苦力怕代杀 0.5%）
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) ->
				LimitBreakManager.onWardenDeath(entity, source));

		// 冲阵：击杀结算（以彼之道隐秘挑战）
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) ->
				ShieldChargeManager.onLivingDeath(entity, source));

		// 深渊回响（隐秘挑战）：携带渊息在水下击杀远古守卫者
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (source.getEntity() instanceof ServerPlayer killer
					&& entity.getType() == EntityTypes.ELDER_GUARDIAN
					&& killer.isEyeInFluid(FluidTags.WATER)
					&& ModEnchantments.getTideheartLevel(killer.getItemBySlot(EquipmentSlot.HEAD)) > 0) {
				Advancements.awardHidden(killer, "abyssal_echo");
			}
		});

		// 死而不僵（隐秘挑战）：劫后余辉锁血期间击杀攻击者
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (source.getEntity() instanceof ServerPlayer killer
					&& AfterglowManager.isLocked(killer)) {
				Advancements.awardHidden(killer, "afterglow_revenge");
			}
		});

		// 重登缓存重置（重登是新实体，瞬态修改器已丢；清缓存强制下一 tick 全量重写属性）
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
				AccessoryManager.onPlayerRestore(handler.player));

		// 玩家离线清理（避免计数 / 冷却 / 缓存残留）
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayer player = handler.player;
			AccessoryManager.onDisconnect(player);
			DecoyManager.onDisconnect(player.getUUID());
			SanctuaryManager.onDisconnect(player.getUUID());
			LoamManager.onDisconnect(player.getUUID());
			JudgementManager.onDisconnect(player.getUUID());
			SheathedEdgeManager.onDisconnect(player.getUUID());
			ShieldChargeManager.onDisconnect(player.getUUID());
			// 注意：EmberfallManager 的金胸甲冷却刻意不随登出清理——
			// 冷却需跨重登保留（重登重置冷却会重新打开「修补耐久→反复免死」循环）
		});

		// 服务器停止全清（单 JVM 内跨存档残留的静态运行时状态）
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			DecoyManager.onServerStopped();
			FxHelper.clearThrottle();
		});

		// 破限腿甲跨部位解锁：可附魔原版摔落保护（走 fabric-item-api 官方事件，
		// 避免与 fabric 自身对 AnvilMenu 的 Mixin 冲突）
		EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, stack, context) -> {
			if (enchantment.is(ModEnchantments.FEATHER_FALLING)
					&& ModEnchantments.hasLimitBreak(stack)
					&& stack.is(itemHolder -> itemHolder.is(ModEnchantments.LEG_ARMOR_TAG))) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		});

		// 庇护（Sanctuary）：追加进试炼密室基础 / 稀有奖励箱
		// （26.2 盾牌机制为 BlocksAttacks 数据组件；庇护走 fabric-loot-api-v3 的
		// MODIFY 事件追加战利品池，避免整表覆盖丢失原版内容）
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()
					|| (!key.equals(BuiltInLootTables.TRIAL_CHAMBERS_REWARD)
							&& !key.equals(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE))) {
				return;
			}
			Holder<Enchantment> sanctuary =
					registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.SANCTUARY);
			tableBuilder.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(EmptyLootItem.emptyItem().setWeight(90))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(7)
							.apply(new SetEnchantmentsFunction.Builder()
									.withEnchantment(sanctuary, ConstantValue.exactly(1.0F))))
					.add(LootItem.lootTableItem(Items.SHIELD).setWeight(3)
							.apply(new SetEnchantmentsFunction.Builder()
									.withEnchantment(sanctuary, UniformGenerator.between(1.0F, 3.0F)))));
		});

		// 渊息（Tideheart）：追加进海洋系宝箱（沉船三类 / 埋藏的宝藏 / 海底废墟大小）
		// 与钓鱼宝藏池——主题绑定海洋，不进 on_random_loot 通用随机池
		// （treasure 标签已将其挡在附魔台之外，同庇护的事件追加法）
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()
					|| (!key.equals(BuiltInLootTables.SHIPWRECK_SUPPLY)
							&& !key.equals(BuiltInLootTables.SHIPWRECK_MAP)
							&& !key.equals(BuiltInLootTables.SHIPWRECK_TREASURE)
							&& !key.equals(BuiltInLootTables.BURIED_TREASURE)
							&& !key.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG)
							&& !key.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL)
							&& !key.equals(BuiltInLootTables.FISHING_TREASURE))) {
				return;
			}
			Holder<Enchantment> tideheart =
					registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.TIDEHEART);
			tableBuilder.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(EmptyLootItem.emptyItem().setWeight(85))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(15)
							.apply(new SetEnchantmentsFunction.Builder()
									.withEnchantment(tideheart, UniformGenerator.between(1.0F, 3.0F)))));
		});

		// 归羽（Homing Plume）：落空箭矢的延迟返还节拍（1 秒飞回动画窗口）
		ServerTickEvents.END_SERVER_TICK.register(HomingPlumeManager::tick);

		LOGGER.info("Extra Enchantry Short initialized: {} enchantments + accessory & gem system", 41);
	}

	/** 构造本模命名空间下的资源 ID */
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
