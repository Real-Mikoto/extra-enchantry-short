package realmikoto.extraenchantryshort.combat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import realmikoto.extraenchantryshort.advancement.Advancements;
import realmikoto.extraenchantryshort.fx.FxHelper;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

import java.util.Locale;

/**
 * 壁垒（Bulwark）伤害上限规则。
 *
 * <p>把<strong>防御减免之后</strong>的实际承伤钳制到胸甲壁垒等级对应的上限。
 * 钳制发生在护甲 / 魔抗减免之后、吸收盾结算之前——约束的是实际承伤（吸收+掉血合计），
 * 不会被护甲二次削减（若钳在 hurtServer 入口，30 伤害先钳 2 再被护甲减 80% 只剩 0.4，出现过强 bug）。</p>
 *
 * <p>即死 / 系统性伤害源不受壁垒保护（与汲取组合会形成近战永动机的「绝对免死」）。</p>
 *
 * <p>两个调用点共用本方法：{@code LivingEntity#actuallyHurt}（非玩家生物）与
 * {@code Player#actuallyHurt}（玩家专用，Player 重写了 actuallyHurt 且不调 super，
 * 必须单独拦截）。</p>
 */
public final class BulwarkManager {

	/** 壁垒各等级的单次伤害上限（HP，下标 = 等级 - 1）：5.5/5/4.5/4/3.5/3/2.5/2/1.5/1 颗心（每级递减 0.5 心） */
	private static final float[] BULWARK_CAP =
			{11.0F, 10.0F, 9.0F, 8.0F, 7.0F, 6.0F, 5.0F, 4.0F, 3.0F, 2.0F, 1.5F};

	private BulwarkManager() {
	}

	/**
	 * 承伤上限钳制：返回钳制后的伤害值。
	 * 壁垒格挡确认：实际钳制 ≥4 点或减免 ≥20% 时给粒子 / 音效 / 悬浮文本反馈，
	 * 并授予实战成就「铜墙铁壁」（壁垒挡下 ≥4 点伤害）。
	 */
	public static float capDamage(LivingEntity entity, DamageSource source, float reduced) {
		if (reduced <= 0.0F) {
			return reduced;
		}
		// 即死与系统性伤害不受壁垒保护
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
				|| source.is(DamageTypes.GENERIC_KILL)
				|| source.is(DamageTypes.FELL_OUT_OF_WORLD)
				|| source.is(DamageTypes.STARVE)) {
			return reduced;
		}
		// 玩家看胸甲（BODY 槽恒空）；马匹看 BODY 槽的马铠（CHEST 槽恒空），二者取其一
		int level = Math.max(
				ModEnchantments.getBulwarkLevel(entity.getItemBySlot(EquipmentSlot.CHEST)),
				ModEnchantments.getBulwarkLevel(entity.getItemBySlot(EquipmentSlot.BODY)));
		if (level <= 0 || level > BULWARK_CAP.length) {
			return reduced;
		}
		float capped = Math.min(reduced, BULWARK_CAP[level - 1]);
		// 阈值设计：旧实现固定 4 点让 VII+ 级（上限 ≤6 HP）几乎永不触发反馈，设计目标落空
		float blocked = reduced - capped;
		if (blocked >= 4.0F || blocked >= reduced * 0.2F) {
			if (blocked >= 4.0F && entity instanceof ServerPlayer player
					&& entity.level() instanceof ServerLevel serverLevel) {
				FxHelper.burst(serverLevel, entity, ParticleTypes.ENCHANTED_HIT, 6, 0.3D);
				FxHelper.play(serverLevel, entity, SoundEvents.SHIELD_BLOCK, 1.0F, 0.6F);
				player.sendOverlayMessage(Component.translatable(
						"message.extra-enchantry-short.bulwark_blocked",
						String.format(Locale.ROOT, "%.1f", blocked)).withStyle(
						ChatFormatting.GRAY, ChatFormatting.ITALIC));
				// 实战成就「铜墙铁壁」：壁垒挡下 ≥4 点伤害
				Advancements.award(player, Advancements.BULWARK_SAVE);
			}
		}
		return capped;
	}
}
