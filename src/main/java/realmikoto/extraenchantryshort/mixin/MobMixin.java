package realmikoto.extraenchantryshort.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import realmikoto.extraenchantryshort.combat.DecoyManager;
import realmikoto.extraenchantryshort.entity.DecoyEntity;

import java.util.List;

/**
 * 所有生物对目标的 {@code Mob#setTarget} 统一拦截，假象（Decoy）仇恨重定向与触发：
 * 玩家已有存活诱饵：目标改为最近诱饵；无诱饵：执行假象触发判定。
 *
 * <p>重定向候选严格验证（修复诱饵死光后玩家无法被锁定的 bug）：
 * isAlive()：真正活着（未移除且血量 > 0），剔除任何残留失效引用；
 * 同维度 + 距离 ≤ 32 格（跟随索敌范围）：诱饵过远时怪物追不到，
 * 表现为"对着空气发呆"而不再锁定玩家，此时回落到玩家本体。</p>
 */
@Mixin(Mob.class)
public abstract class MobMixin {

	/** 重定向距离上限（格）：超出则视为诱饵无效，怪物直接锁定玩家 */
	private static final double EXTRAENCHANTRYSHORT$REDIRECT_MAX_DIST = 32.0;

	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity extraenchantryshort$resolveTarget(LivingEntity target) {
		Mob self = (Mob) (Object) this;
		if (self.level().isClientSide()) {
			return target;
		}

		// 假象：仅对"锁定玩家"的目标做诱饵重定向
		if (!(target instanceof ServerPlayer player)) {
			return target;
		}

		List<DecoyEntity> decoys = DecoyManager.getAliveDecoys(player);
		if (decoys.isEmpty()) {
			DecoyManager.tryTrigger(player);
			decoys = DecoyManager.getAliveDecoys(player);
			if (decoys.isEmpty()) {
				return target;
			}
		}

		// 重定向至最近的同维度存活诱饵（isAlive + 距离双重验证）
		DecoyEntity nearest = null;
		double best = EXTRAENCHANTRYSHORT$REDIRECT_MAX_DIST * EXTRAENCHANTRYSHORT$REDIRECT_MAX_DIST;
		for (DecoyEntity decoy : decoys) {
			if (!decoy.isAlive() || decoy.level() != self.level()) {
				continue;
			}
			double dist = decoy.distanceToSqr(self);
			if (dist < best) {
				best = dist;
				nearest = decoy;
			}
		}
		return nearest != null ? nearest : target;
	}
}
