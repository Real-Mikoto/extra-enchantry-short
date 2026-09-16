package realmikoto.extraenchantryshort.combat;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import realmikoto.extraenchantryshort.ExtraEnchantryShort;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * 狼铠附魔：锐牙 / 哨戒 / 回春——BODY 槽狼铠结算。
 * 挂 {@code LivingEntityMixin} tick HEAD 的 instanceof Wolf 分支，活力同款属性修改器模式。
 * 活力 / 壁垒对狼的复用走 isArmor() 求和与 BULWARK_CAP 既有路径（纯标签变更，零代码）。
 * 狼铠附魔不计入玩家共鸣（计件只扫玩家自身槽位）。
 */
public final class WolfArmorManager {

	/** 锐牙：近战伤害 +10%/级 */
	private static final double SHARP_FANG_PER_LEVEL = 0.10D;
	/** 哨戒：索敌/跟随范围 +25%/级 */
	private static final double VIGIL_PER_LEVEL = 0.25D;
	/** 回春：每 4 秒（80 tick）恢复 level HP */
	private static final int RENEWAL_INTERVAL_TICKS = 80;

	private WolfArmorManager() {
	}

	/** LivingEntityMixin tick HEAD 调用（内部仅处理 Wolf） */
	public static void tick(LivingEntity entity) {
		if (!(entity instanceof Wolf wolf)) {
			return;
		}
		ItemStack armor = wolf.getItemBySlot(EquipmentSlot.BODY);
		if (armor.isEmpty()) {
			// 无甲快路径：仅在仍有残留修改器时才清理（避免每狼每 tick 空转三连getAttribute）
			if (hasWolfModifiers(wolf)) {
				removeAllModifiers(wolf);
			}
			return;
		}
		int sharpFang = ModEnchantments.level(armor, ModEnchantments.SHARP_FANG);
		int vigil = ModEnchantments.level(armor, ModEnchantments.VIGIL);
		int renewal = ModEnchantments.level(armor, ModEnchantments.RENEWAL);

		applyModifier(wolf, Attributes.ATTACK_DAMAGE, "wolf_sharp_fang",
				sharpFang * SHARP_FANG_PER_LEVEL, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
		applyModifier(wolf, Attributes.FOLLOW_RANGE, "wolf_vigil",
				vigil * VIGIL_PER_LEVEL, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

		// 回春：每 80 tick 一跳（服务器游戏时间全局节拍，无需每狼计时器）
		if (renewal > 0 && wolf.isAlive() && wolf.getHealth() < wolf.getMaxHealth()
				&& wolf.level().getGameTime() % RENEWAL_INTERVAL_TICKS == 0L) {
			wolf.heal(renewal);
		}
	}

	/** 无甲快路径：任一槽仍有本模修改器才值得清理 */
	private static boolean hasWolfModifiers(Wolf wolf) {
		AttributeInstance fang = wolf.getAttribute(Attributes.ATTACK_DAMAGE);
		AttributeInstance vigil = wolf.getAttribute(Attributes.FOLLOW_RANGE);
		return (fang != null && fang.getModifier(ExtraEnchantryShort.id("wolf_sharp_fang")) != null)
				|| (vigil != null && vigil.getModifier(ExtraEnchantryShort.id("wolf_vigil")) != null);
	}

	private static void applyModifier(LivingEntity entity,
			Holder<Attribute> attribute, String name, double value,
			AttributeModifier.Operation operation) {
		AttributeInstance instance = entity.getAttribute(attribute);
		if (instance == null) {
			return;
		}
		Identifier id = ExtraEnchantryShort.id(name);
		if (value <= 0.0D) {
			instance.removeModifier(id);
		} else {
			instance.addOrUpdateTransientModifier(new AttributeModifier(id, value, operation));
		}
	}

	private static void removeAllModifiers(Wolf wolf) {
		removeModifier(wolf, Attributes.ATTACK_DAMAGE, "wolf_sharp_fang");
		removeModifier(wolf, Attributes.FOLLOW_RANGE, "wolf_vigil");
	}

	private static void removeModifier(LivingEntity entity,
			Holder<Attribute> attribute, String name) {
		AttributeInstance instance = entity.getAttribute(attribute);
		if (instance != null) {
			instance.removeModifier(ExtraEnchantryShort.id(name));
		}
	}
}
