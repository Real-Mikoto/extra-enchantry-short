package realmikoto.extraenchantryshort.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import realmikoto.extraenchantryshort.access.HomingPlumeAccess;
import realmikoto.extraenchantryshort.advancement.Advancements;
import realmikoto.extraenchantryshort.combat.HomingPlumeManager;
import realmikoto.extraenchantryshort.fx.FxHelper;

/**
 * 归羽（Homing Plume）：发射时快照的等级挂在箭实体上，
 * 命中实体置标记（命中不返还），插地方（未命中）时按等级概率
 * 登记延迟返还（I 级 50% / II 级 100%）。
 *
 * <p>26.2 反编译确认：AbstractArrow 已移至 projectile.arrow 分包；
 * onHitEntity / onHitBlock / pickup 字段（ALLOWED/DISALLOWED/CREATIVE_ONLY）
 * 为本实现提供的全部钩子。</p>
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements HomingPlumeAccess {

	@Shadow
	public AbstractArrow.Pickup pickup;

	/** 发射时写入的归羽等级快照（0 = 无归羽） */
	@Unique
	private int extraenchantryshort$homingLevel;

	/** 是否命中过实体（命中的箭不返还） */
	@Unique
	private boolean extraenchantryshort$hitEntity;

	/** 发射点坐标（百步穿杨挑战测距用；null = 未记录） */
	@Unique
	private double[] extraenchantryshort$launchPos;

	@Override
	public void extraenchantryshort$setHomingLevel(int level) {
		this.extraenchantryshort$homingLevel = level;
	}

	@Override
	public int extraenchantryshort$getHomingLevel() {
		return this.extraenchantryshort$homingLevel;
	}

	@Override
	public void extraenchantryshort$setLaunchPos(double x, double y, double z) {
		this.extraenchantryshort$launchPos = new double[]{x, y, z};
	}

	@Override
	public double[] extraenchantryshort$getLaunchPos() {
		return this.extraenchantryshort$launchPos;
	}

	/** 透传 protected 的 getPickupItem（@Invoker 标准做法） */
	@Invoker("getPickupItem")
	@Override
	public abstract ItemStack extraenchantryshort$getPickupItem();

	@Inject(method = "onHitEntity", at = @At("HEAD"))
	private void extraenchantryshort$markEntityHit(EntityHitResult hitResult, CallbackInfo ci) {
		this.extraenchantryshort$hitEntity = true;
		// 隐秘挑战「百步穿杨」：归羽箭命中距发射点 ≥40 格的生物
		if (this.extraenchantryshort$homingLevel > 0 && this.extraenchantryshort$launchPos != null) {
			AbstractArrow self = (AbstractArrow) (Object) this;
			double dx = self.getX() - this.extraenchantryshort$launchPos[0];
			double dy = self.getY() - this.extraenchantryshort$launchPos[1];
			double dz = self.getZ() - this.extraenchantryshort$launchPos[2];
			if (dx * dx + dy * dy + dz * dz >= 40.0D * 40.0D
					&& self.getOwner() instanceof net.minecraft.server.level.ServerPlayer owner) {
				Advancements.awardHidden(owner, "homing_snipe");
			}
		}
	}

	@Inject(method = "onHitBlock", at = @At("RETURN"))
	private void extraenchantryshort$scheduleReturn(BlockHitResult hitResult, CallbackInfo ci) {
		if (this.extraenchantryshort$homingLevel <= 0 || this.extraenchantryshort$hitEntity
				|| this.pickup == AbstractArrow.Pickup.DISALLOWED) {
			return;
		}
		AbstractArrow self = (AbstractArrow) (Object) this;
		if (!(self.getOwner() instanceof ServerPlayer owner)) {
			return;
		}
		double chance = this.extraenchantryshort$homingLevel >= 2 ? 1.0D : 0.5D;
		if (owner.getRandom().nextDouble() < chance) {
			HomingPlumeManager.schedule(self, owner);
			// 归羽起飞反馈（P1）：声音提前到起飞（1 秒飞回过程，声音先行更有期待感）+ 末地烛轨迹
			if (self.level() instanceof ServerLevel serverLevel) {
				FxHelper.burst(serverLevel, self, net.minecraft.core.particles.ParticleTypes.END_ROD, 3, 0.1D);
				FxHelper.play(serverLevel, self, SoundEvents.ITEM_PICKUP, 0.3F, 1.4F);
			}
		}
	}
}
