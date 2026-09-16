package realmikoto.extraenchantryshort.client.mixin;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * 空跃（Skyward）空中跳跃——客户端侧：
 *
 * <p>26.2 反编译确认：玩家的跳跃输入是客户端权威（原版玩家跳跃也由客户端
 * aiStep 发起，服务端仅按位置包接受结果），服务端看不到跳跃键状态，
 * 多段跳必须在客户端注入。挂在 {@code LocalPlayer#aiStep} 的 HEAD：
 * 原版本体稍后会读取 input.keyPresses 并执行地面跳跃，时序天然对齐。</p>
 *
 * <ul>
 *   <li>按键沿检测（本 tick 按下、上 tick 未按）才触发，按住空格不会连烧次数；</li>
 *   <li>触地 / 进水 / 攀爬立即重置次数；骑乘、鞘翅滑翔、创造飞行时不触发；</li>
 *   <li>跳跃本体直接复用 {@code jumpFromGround()}（含跳跃力度与疾跑加跳），手感与原版跳一致；</li>
 *   <li>次数上限 = 靴子空跃等级（I → 1 次，II → 2 次）。</li>
 * </ul>
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

	/** 剩余空中跳跃次数 */
	@Unique
	private int extraenchantryshort$skywardJumpsLeft = 0;

	/** 上一 tick 是否按住跳跃键（沿检测用） */
	@Unique
	private boolean extraenchantryshort$prevJumpPressed = false;

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void extraenchantryshort$skywardAirJump(CallbackInfo ci) {
		LocalPlayer self = (LocalPlayer) (Object) this;
		ClientInput input = self.input;
		boolean jumpPressed = input != null && input.keyPresses.jump();
		int maxJumps = ModEnchantments.getSkywardLevel(self.getItemBySlot(EquipmentSlot.FEET));
		if (maxJumps <= 0) {
			extraenchantryshort$skywardJumpsLeft = 0;
			extraenchantryshort$prevJumpPressed = jumpPressed;
			return;
		}
		if (self.onGround() || self.isInWater() || self.onClimbable()) {
			// 落地 / 入水 / 攀爬即重置次数
			extraenchantryshort$skywardJumpsLeft = maxJumps;
		} else if (jumpPressed && !extraenchantryshort$prevJumpPressed
				&& extraenchantryshort$skywardJumpsLeft > 0
				&& !self.isPassenger()
				&& !self.isFallFlying()
				&& !self.getAbilities().flying) {
			extraenchantryshort$skywardJumpsLeft--;
			self.jumpFromGround();
			extraenchantryshort$airJumpEffects(self, maxJumps - extraenchantryshort$skywardJumpsLeft);
		}
		// 装备等级变化（脱靴 / 换靴）时收敛剩余次数
		if (extraenchantryshort$skywardJumpsLeft > maxJumps) {
			extraenchantryshort$skywardJumpsLeft = maxJumps;
		}
		extraenchantryshort$prevJumpPressed = jumpPressed;
	}

	/**
	 * 空跃起跳反馈：脚下云雾下喷 + 短促振翅音。
	 * 多段跳音高阶梯递减（第 1/2/3 跳 → 1.6/1.5/1.4），制造"阶梯感"。
	 */
	@Unique
	private void extraenchantryshort$airJumpEffects(LocalPlayer self, int jumpIndex) {
		for (int i = 0; i < 6; i++) {
			self.level().addParticle(ParticleTypes.CLOUD,
					self.getX() + (self.getRandom().nextDouble() - 0.5D) * 0.4D, self.getY(),
					self.getZ() + (self.getRandom().nextDouble() - 0.5D) * 0.4D,
					0.0D, -0.08D, 0.0D);
		}
		for (int i = 0; i < 3; i++) {
			self.level().addParticle(ParticleTypes.POOF,
					self.getX(), self.getY() + 0.1D, self.getZ(), 0.0D, -0.05D, 0.0D);
		}
		self.playSound(SoundEvents.ENDER_DRAGON_FLAP, 0.4F, 1.7F - jumpIndex * 0.1F);
	}
}
