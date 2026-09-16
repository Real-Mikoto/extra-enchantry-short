package realmikoto.extraenchantryshort.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import realmikoto.extraenchantryshort.access.StarfallAccess;
import realmikoto.extraenchantryshort.advancement.Advancements;
import realmikoto.extraenchantryshort.fx.FxHelper;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * 御风（Windrider）II/III 级——烟花推进强化：
 * 26.2 反编译确认，烟花火箭在 {@code FireworkRocketEntity#tick} 中对**正在滑翔的实体**
 * 做一次朝视线方向的插值加速，随后调用 {@code LivingEntity#setDeltaMovement}
 * （该方法内仅此一处以 LivingEntity 为 owner 的调用，其余是火箭自身的移动）。
 * 这里重定向该调用：取出"本次推进增量"（新速度 - 旧速度）按倍率放大后写回，
 * 不改动原版的插值公式本身。
 *
 * <p>倍率：I 级 1.0（原版）、II 级 1.5、III 级 1.75。</p>
 *
 * <p>坠星（Starfall）——烟花爆炸增强：
 * 26.2 反编译确认，爆炸结算在 private {@code dealExplosionDamage(ServerLevel)}：
 * 基础伤害 = 5.0f + 2×爆炸星数，伤害半径 5.0d（AABB 外扩 + 距离平方阈值 25.0d，
 * 并按 (5.0-距离)/5.0 衰减）。坠星把三组常量同步放大为 9.0f / 6.0d / 36.0d
 * （伤害 +4、半径 +1，衰减公式随半径同构缩放），爆炸粒子升级为星形散射。
 * 标记来源：CrossbowItemMixin 在发射时快照。</p>
 */
@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin implements StarfallAccess {

	/** 坠星标记：发射时由 CrossbowItemMixin 快照写入 */
	@Unique
	private boolean extraenchantryshort$starfall;

	@Override
	public void extraenchantryshort$setStarfall() {
		this.extraenchantryshort$starfall = true;
	}

	@Override
	public boolean extraenchantryshort$isStarfall() {
		return this.extraenchantryshort$starfall;
	}

	@Redirect(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
			)
	)
	private void extraenchantryshort$windriderBoost(LivingEntity glider, Vec3 boosted) {
		double factor = ModEnchantments.getWindriderBoostFactor(glider);
		if (factor == 1.0D) {
			glider.setDeltaMovement(boosted);
			return;
		}
		Vec3 previous = glider.getDeltaMovement();
		Vec3 impulse = boosted.subtract(previous);
		glider.setDeltaMovement(previous.add(
				impulse.x * factor, impulse.y * factor, impulse.z * factor));
		// 御风加速气流尾迹（P2）：II 级白云，III 级掺末地烛（末地主题呼应），10 tick 节流
		if (glider.level() instanceof ServerLevel serverLevel
				&& FxHelper.throttle(glider, "windrider", 10)) {
			FxHelper.burst(serverLevel, glider, ParticleTypes.CLOUD, 3, 0.3D);
			if (ModEnchantments.getWindriderLevel(glider.getItemBySlot(EquipmentSlot.CHEST)) >= 3) {
				FxHelper.burst(serverLevel, glider, ParticleTypes.END_ROD, 1, 0.2D);
			}
		}
	}

	/** 坠星：爆炸基础伤害 5.0f → 9.0f（+4，命中挂载实体的那一份同样增强） */
	@ModifyConstant(method = "dealExplosionDamage", constant = @Constant(floatValue = 5.0F))
	private float extraenchantryshort$starfallBaseDamage(float original) {
		return this.extraenchantryshort$starfall ? 9.0F : original;
	}

	/** 坠星：爆炸半径 5.0d → 6.0d（AABB 外扩与衰减基数同步缩放） */
	@ModifyConstant(method = "dealExplosionDamage", constant = @Constant(doubleValue = 5.0D))
	private double extraenchantryshort$starfallRadius(double original) {
		return this.extraenchantryshort$starfall ? 6.0D : original;
	}

	/** 坠星：距离平方阈值 25.0d → 36.0d（与半径 6.0d 匹配） */
	@ModifyConstant(method = "dealExplosionDamage", constant = @Constant(doubleValue = 25.0D))
	private double extraenchantryshort$starfallRadiusSqr(double original) {
		return this.extraenchantryshort$starfall ? 36.0D : original;
	}

	/** 坠星：爆炸粒子升级为星形散射（末地烛粒子沿球面均布 + 中心烟花粒子） */
	@Inject(method = "explode", at = @At("TAIL"))
	private void extraenchantryshort$starfallParticles(ServerLevel level, CallbackInfo ci) {
		if (!this.extraenchantryshort$starfall) {
			return;
		}
		FireworkRocketEntity self = (FireworkRocketEntity) (Object) this;
		// 星形散射：三条正交轴 ± 方向 + 四条体对角线方向，共 14 束
		double[][] directions = {
				{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1},
				{0.577, 0.577, 0.577}, {-0.577, 0.577, 0.577},
				{0.577, -0.577, 0.577}, {0.577, 0.577, -0.577}
		};
		for (double[] dir : directions) {
			level.sendParticles(ParticleTypes.END_ROD,
					self.getX(), self.getY(), self.getZ(),
					4, dir[0] * 1.5D, dir[1] * 1.5D, dir[2] * 1.5D, 0.15D);
		}
		level.sendParticles(ParticleTypes.FIREWORK,
				self.getX(), self.getY(), self.getZ(),
				24, 0.8D, 0.8D, 0.8D, 0.2D);
		// 实战成就「火树银花」：坠星强化烟花首次爆炸
		if (self.getOwner() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			Advancements.award(serverPlayer, Advancements.FIREWORKS);
		}
	}
}
