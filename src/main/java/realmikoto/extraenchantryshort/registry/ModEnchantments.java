package realmikoto.extraenchantryshort.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import realmikoto.extraenchantryshort.ExtraEnchantryShort;
import realmikoto.extraenchantryshort.accessory.Accessories;

import java.util.Optional;

/**
 * 附魔注册键与查询规则中心。
 *
 * <p>全部 41 个附魔均为数据驱动定义（{@code data/extra-enchantry-short/enchantment/*.json}），
 * 代码侧只持有 {@link ResourceKey} 常量供逻辑层查询；本类同时承载各附魔的等级查询、
 * 数值规则表与共享判定（火焰家族、boss 豁免等），是 Mixin 与管理器之间的公共查询层。</p>
 */
public final class ModEnchantments {

	// ============ 基础战斗 / 防御附魔 ============

	/** 凋零保护（缩短凋零效果时长） */
	public static final ResourceKey<Enchantment> WITHER_PROTECTION = key("wither_protection");

	/** 炽焰行者（岩浆块行走 / 足下火迹） */
	public static final ResourceKey<Enchantment> BLAZING_WALKER = key("blazing_walker");

	/** 破限（互斥无视 / 保护上限突破 / 跨部位解锁的钥匙） */
	public static final ResourceKey<Enchantment> LIMIT_BREAK = key("limit_break");

	/** 拓阶（挖掘等级提升 / 基岩崩解） */
	public static final ResourceKey<Enchantment> TIER_BREAK = key("tier_break");

	/** 触及（近战攻击距离延长） */
	public static final ResourceKey<Enchantment> REACH = key("reach");

	/** 假象（PvP 受击生成诱饵） */
	public static final ResourceKey<Enchantment> DECOY = key("decoy");

	/** 汲取（造成伤害回复生命） */
	public static final ResourceKey<Enchantment> SIPHON = key("siphon");

	/** 蚀命（命中附加最大生命百分比伤害） */
	public static final ResourceKey<Enchantment> LIFE_EROSION = key("life_erosion");

	/** 活力（护甲附魔等级换算额外生命上限） */
	public static final ResourceKey<Enchantment> VITALITY = key("vitality");

	/** 壁垒（单次承伤上限钳制） */
	public static final ResourceKey<Enchantment> BULWARK = key("bulwark");

	/** 劫后余辉（不死图腾触发后的锁血窗口） */
	public static final ResourceKey<Enchantment> AFTERGLOW = key("afterglow");

	/** 誓约（随身 keepInventory） */
	public static final ResourceKey<Enchantment> OATHBOUND = key("oathbound");

	/** 空跃（靴子多段空中跳） */
	public static final ResourceKey<Enchantment> SKYWARD = key("skyward");

	/** 破阵（近战溅射） */
	public static final ResourceKey<Enchantment> CLEAVE = key("cleave");

	/** 御风（滑翔耐久减免 / 烟花推进强化） */
	public static final ResourceKey<Enchantment> WINDRIDER = key("windrider");

	/** 无踪（脚步声与震动屏蔽 / 索敌可见度降低） */
	public static final ResourceKey<Enchantment> UNSEEN = key("unseen");

	/** 断罪（低生命目标斩杀 / boss 增伤） */
	public static final ResourceKey<Enchantment> JUDGEMENT = key("judgement");

	/** 疾风（护腿移速加成） */
	public static final ResourceKey<Enchantment> GALE = key("gale");

	/** 余烬（金胸甲 / 金马铠免死） */
	public static final ResourceKey<Enchantment> EMBERFALL = key("emberfall");

	/** 冲阵（疾跑持盾撞击） */
	public static final ResourceKey<Enchantment> SHIELD_CHARGE = key("shield_charge");

	/** 不屈（盾牌破盾减免 / 免疫） */
	public static final ResourceKey<Enchantment> DEFIANCE = key("defiance");

	/** 庇护（格挡团队治疗光环） */
	public static final ResourceKey<Enchantment> SANCTUARY = key("sanctuary");

	/** 坚壁（不可格挡类伤害减免） */
	public static final ResourceKey<Enchantment> AEGIS = key("aegis");

	/** 归羽（落空箭矢延迟返手） */
	public static final ResourceKey<Enchantment> HOMING_PLUME = key("homing_plume");

	/** 坠星（弩烟花爆炸强化） */
	public static final ResourceKey<Enchantment> STARFALL = key("starfall");

	/** 霆霓（雨天 / 水中三叉戟增伤与连锁） */
	public static final ResourceKey<Enchantment> STORMSURGE = key("stormsurge");

	/** 藏锋（脱战后的拔刀一击） */
	public static final ResourceKey<Enchantment> SHEATHED_EDGE = key("sheathed_edge");

	/** 渊息（氧气上限提升 / 水下挖掘不减速） */
	public static final ResourceKey<Enchantment> TIDEHEART = key("tideheart");

	/** 丰壤（作物双倍掉落 / 范围收获） */
	public static final ResourceKey<Enchantment> LOAM = key("loam");

	// ============ 环佩与獠牙：配饰附魔 ×8（计入八系共鸣计件口径的旧称谓） ============

	/** 魂铃（击杀敌对回复饥饿） */
	public static final ResourceKey<Enchantment> SOUL_CHIME = key("soul_chime");

	/** 盾坠（全伤害减免） */
	public static final ResourceKey<Enchantment> SHIELD_PENDANT = key("shield_pendant");

	/** 雷鸣扣（雷雨天气造成伤害加成） */
	public static final ResourceKey<Enchantment> THUNDER_CLASP = key("thunder_clasp");

	/** 翠滴（自然恢复加速） */
	public static final ResourceKey<Enchantment> VERDANT_DROP = key("verdant_drop");

	/** 刃戒（攻击速度加成） */
	public static final ResourceKey<Enchantment> BLADE_RING = key("blade_ring");

	/** 羽环（弹射物伤害减免） */
	public static final ResourceKey<Enchantment> PLUME_RING = key("plume_ring");

	/** 烬镯（火焰伤害减免） */
	public static final ResourceKey<Enchantment> EMBER_BRACELET = key("ember_bracelet");

	/** 潮镯（游泳效率加成） */
	public static final ResourceKey<Enchantment> TIDE_BRACELET = key("tide_bracelet");

	/** 远镯（手镯触及距离加成，可与触及叠加） */
	public static final ResourceKey<Enchantment> REACH_BRACELET = key("reach_bracelet");

	// ============ 环佩与獠牙：狼铠附魔 ×3（铁砧上书，不计玩家共鸣） ============

	/** 锐牙（狼近战伤害加成） */
	public static final ResourceKey<Enchantment> SHARP_FANG = key("sharp_fang");

	/** 哨戒（狼索敌 / 跟随范围加成） */
	public static final ResourceKey<Enchantment> VIGIL = key("vigil");

	/** 回春（狼周期性自愈） */
	public static final ResourceKey<Enchantment> RENEWAL = key("renewal");

	// ============ 共享键与标签 ============

	/** 原版摔落保护（破限腿甲解锁跨部位附魔的钥匙） */
	public static final ResourceKey<Enchantment> FEATHER_FALLING =
			ResourceKey.create(Registries.ENCHANTMENT, Identifier.withDefaultNamespace("feather_falling"));

	/** 腿甲标签（破限解锁摔落保护的适用部位） */
	public static final TagKey<net.minecraft.world.item.Item> LEG_ARMOR_TAG = TagKey.create(Registries.ITEM,
			Identifier.withDefaultNamespace("enchantable/leg_armor"));

	/** 火焰家族附魔标签（family_fire）：防火判定依据（带任一火焰系附魔的物品免烧毁 / 免火损耐久） */
	public static final TagKey<Enchantment> FIRE_FAMILY_TAG =
			TagKey.create(Registries.ENCHANTMENT, ExtraEnchantryShort.id("family_fire"));

	private static ResourceKey<Enchantment> key(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, ExtraEnchantryShort.id(path));
	}

	private ModEnchantments() {
	}

	// ============ 查询 API ============

	/** 在物品的附魔里找指定附魔的 Holder（无则 Optional.empty） */
	public static Optional<Holder<Enchantment>> findHolder(ItemStack stack, ResourceKey<Enchantment> key) {
		for (Holder<Enchantment> holder : stack.getEnchantments().keySet()) {
			if (holder.is(key)) {
				return Optional.of(holder);
			}
		}
		return Optional.empty();
	}

	/**
	 * 火焰家族物品判定（「烬火不侵」设定）：
	 * 1) 附魔命中 {@code #extra-enchantry-short:family_fire}（炽焰行者 / 余烬 / 烬镯 / 劫后余辉，
	 *    含附魔书 STORED_ENCHANTMENTS）；
	 * 2) 配饰镶嵌烬心石（宝石即火焰家族凭证，未附魔也防火）。
	 * 动态判定即时生效，砂轮磨掉附魔即失效——用于 ItemEntity 烧毁免疫与穿戴耐久过滤。
	 */
	public static boolean isFireFamilyItem(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return false;
		}
		for (Holder<Enchantment> enchantment : stack.getEnchantments().keySet()) {
			if (enchantment.is(FIRE_FAMILY_TAG)) {
				return true;
			}
		}
		ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);
		if (stored != null) {
			for (Holder<Enchantment> enchantment : stored.keySet()) {
				if (enchantment.is(FIRE_FAMILY_TAG)) {
					return true;
				}
			}
		}
		return "ember_heart".equals(Accessories.socketedGem(stack));
	}

	/** 通用：读取物品上指定附魔的等级（无则返回 0，stack 可为 null）——管理器层的公开查询入口 */
	public static int level(ItemStack stack, ResourceKey<Enchantment> key) {
		if (stack == null || stack.isEmpty()) {
			return 0;
		}
		ItemEnchantments enchantments = stack.getEnchantments();
		for (Holder<Enchantment> enchantment : enchantments.keySet()) {
			if (enchantment.is(key)) {
				return enchantments.getLevel(enchantment);
			}
		}
		return 0;
	}

	// ============ 各附魔等级查询 ============

	/** 劫后余辉等级 */
	public static int getAfterglowLevel(ItemStack stack) {
		return level(stack, AFTERGLOW);
	}

	/** 誓约等级 */
	public static int getOathboundLevel(ItemStack stack) {
		return level(stack, OATHBOUND);
	}

	/** 空跃等级 */
	public static int getSkywardLevel(ItemStack stack) {
		return level(stack, SKYWARD);
	}

	/** 破阵等级 */
	public static int getCleaveLevel(ItemStack stack) {
		return level(stack, CLEAVE);
	}

	/** 拓阶等级 */
	public static int getTierBreakLevel(ItemStack stack) {
		return level(stack, TIER_BREAK);
	}

	/** 壁垒等级 */
	public static int getBulwarkLevel(ItemStack stack) {
		return level(stack, BULWARK);
	}

	/** 活力等级 */
	public static int getVitalityLevel(ItemStack stack) {
		return level(stack, VITALITY);
	}

	/** 蚀命等级 */
	public static int getLifeErosionLevel(ItemStack stack) {
		return level(stack, LIFE_EROSION);
	}

	/** 汲取等级 */
	public static int getSiphonLevel(ItemStack stack) {
		return level(stack, SIPHON);
	}

	/** 御风等级 */
	public static int getWindriderLevel(ItemStack stack) {
		return level(stack, WINDRIDER);
	}

	/** 无踪等级 */
	public static int getUnseenLevel(ItemStack stack) {
		return level(stack, UNSEEN);
	}

	/** 断罪等级 */
	public static int getJudgementLevel(ItemStack stack) {
		return level(stack, JUDGEMENT);
	}

	/** 疾风等级 */
	public static int getGaleLevel(ItemStack stack) {
		return level(stack, GALE);
	}

	/** 余烬等级 */
	public static int getEmberfallLevel(ItemStack stack) {
		return level(stack, EMBERFALL);
	}

	/** 冲阵等级 */
	public static int getShieldChargeLevel(ItemStack stack) {
		return level(stack, SHIELD_CHARGE);
	}

	/** 不屈等级 */
	public static int getDefianceLevel(ItemStack stack) {
		return level(stack, DEFIANCE);
	}

	/** 庇护等级 */
	public static int getSanctuaryLevel(ItemStack stack) {
		return level(stack, SANCTUARY);
	}

	/** 坚壁等级 */
	public static int getAegisLevel(ItemStack stack) {
		return level(stack, AEGIS);
	}

	/** 归羽等级 */
	public static int getHomingPlumeLevel(ItemStack stack) {
		return level(stack, HOMING_PLUME);
	}

	/** 坠星等级 */
	public static int getStarfallLevel(ItemStack stack) {
		return level(stack, STARFALL);
	}

	/** 霆霓等级 */
	public static int getStormsurgeLevel(ItemStack stack) {
		return level(stack, STORMSURGE);
	}

	/** 藏锋等级 */
	public static int getSheathedEdgeLevel(ItemStack stack) {
		return level(stack, SHEATHED_EDGE);
	}

	/** 渊息等级 */
	public static int getTideheartLevel(ItemStack stack) {
		return level(stack, TIDEHEART);
	}

	/** 丰壤等级 */
	public static int getLoamLevel(ItemStack stack) {
		return level(stack, LOAM);
	}

	/** 触及等级 */
	public static int getReachLevel(ItemStack stack) {
		return level(stack, REACH);
	}

	/** 远镯等级 */
	public static int getReachBraceletLevel(ItemStack stack) {
		return level(stack, REACH_BRACELET);
	}

	/** 炽焰行者等级 */
	public static int getBlazingWalkerLevel(ItemStack stack) {
		return level(stack, BLAZING_WALKER);
	}

	/** 靴子上的无踪等级（Entity / LivingEntity 注入点快速判定用） */
	public static int getUnseenLevelOnFeet(LivingEntity entity) {
		return getUnseenLevel(entity.getItemBySlot(EquipmentSlot.FEET));
	}

	// ============ 破限（Limit Break）辅助判定 ============

	/** 判断物品是否带有破限附魔 */
	public static boolean hasLimitBreak(ItemStack stack) {
		return level(stack, LIMIT_BREAK) > 0;
	}

	/** 判断实体的任意护甲槽位是否带有破限附魔 */
	public static boolean hasLimitBreakOnArmor(LivingEntity entity) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.isArmor() && hasLimitBreak(entity.getItemBySlot(slot))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断物品是否携带破限：已附魔物品，或破限附魔书
	 * （附魔书上的破限存于 STORED_ENCHANTMENTS，不经 getEnchantments()）。
	 * 铁砧融合的「带破限输入」判定依据（AnvilMenuMixin）。
	 */
	public static boolean carriesLimitBreak(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if (hasLimitBreak(stack)) {
			return true;
		}
		ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);
		if (stored == null) {
			return false;
		}
		for (Holder<Enchantment> enchantment : stored.keySet()) {
			if (enchantment.is(LIMIT_BREAK)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 活力：护甲总附魔等级 × 4 点生命；多件叠加，上限 +50。
	 * 任意护甲带破限时上限失效（破限可生效）。
	 * 客户端 HUD 同样调用此方法计算显示值。
	 */
	public static int getVitalityBonus(LivingEntity entity) {
		// 性能：本方法在 LivingEntity#tick HEAD 对所有生物每 tick 调用；
		// 补一个廉价的「全槽为空」快速路径（绝大多数无甲生物命中）。
		// BODY 槽（马铠 / 狼铠）也要查——isArmor() 覆盖 BODY，漏查会让马铠与狼铠活力失效。
		if (entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
				&& entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
				&& entity.getItemBySlot(EquipmentSlot.LEGS).isEmpty()
				&& entity.getItemBySlot(EquipmentSlot.FEET).isEmpty()
				&& entity.getItemBySlot(EquipmentSlot.BODY).isEmpty()) {
			return 0;
		}
		int totalLevels = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.isArmor()) {
				totalLevels += getVitalityLevel(entity.getItemBySlot(slot));
			}
		}
		if (totalLevels <= 0) {
			return 0;
		}
		int bonus = totalLevels * 4;
		if (!hasLimitBreakOnArmor(entity)) {
			bonus = Math.min(bonus, 50);
		}
		return bonus;
	}

	// ============ 数值规则表 ============

	/** 御风各等级的滑翔耐久消耗跳过概率（下标 = 等级 - 1）：I/II 级 -50%，III 级再强化 50% → -75% */
	private static final float[] WINDRIDER_DURABILITY_SKIP = {0.5F, 0.5F, 0.75F};

	/** 御风各等级的烟花推进增量倍率：I 级原版，II 级 +50%，III 级再强化 50% → +75% */
	private static final double[] WINDRIDER_BOOST_FACTOR = {1.0D, 1.5D, 1.75D};

	/** 疾风每级提供的移动速度加成（5%/级） */
	private static final double GALE_SPEED_PER_LEVEL = 0.05D;

	/** 御风：本次滑翔耐久消耗是否被跳过（概率 = 50%/50%/75%） */
	public static boolean shouldSkipGlideDurability(ItemStack elytra) {
		int level = getWindriderLevel(elytra);
		if (level <= 0 || level > WINDRIDER_DURABILITY_SKIP.length) {
			return false;
		}
		return Math.random() < WINDRIDER_DURABILITY_SKIP[level - 1];
	}

	/** 御风：烟花推进增量倍率（1.0 / 1.5 / 1.75，无附魔返回 1.0） */
	public static double getWindriderBoostFactor(LivingEntity glider) {
		int level = getWindriderLevel(glider.getItemBySlot(EquipmentSlot.CHEST));
		if (level <= 0 || level > WINDRIDER_BOOST_FACTOR.length) {
			return 1.0D;
		}
		return WINDRIDER_BOOST_FACTOR[level - 1];
	}

	/**
	 * 疾风：护腿提供的移动速度加成（+5%/级），潜行时为 0（保留潜行的战术价值）。
	 * 疾风 III 级只能由两个 II 级在带破限的铁砧上融合得到（见 AnvilMenuMixin）。
	 */
	public static double getGaleSpeedBonus(LivingEntity entity) {
		if (entity.isCrouching()) {
			return 0.0D;
		}
		return getGaleLevel(entity.getItemBySlot(EquipmentSlot.LEGS)) * GALE_SPEED_PER_LEVEL;
	}

	/**
	 * 断罪的 boss 豁免判定：26.2 无统一的 isBoss()/boss 标签，
	 * 故按带 boss 血条的三个原版 boss 显式判定（末影龙 / 凋灵 / 监守者）。
	 */
	public static boolean isBossLike(LivingEntity entity) {
		return entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon
				|| entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss
				|| entity instanceof net.minecraft.world.entity.monster.warden.Warden;
	}
}
