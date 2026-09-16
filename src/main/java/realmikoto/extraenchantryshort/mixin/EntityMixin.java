package realmikoto.extraenchantryshort.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import realmikoto.extraenchantryshort.access.EquipmentReady;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * 无踪（Unseen）I 级——声音与震动屏蔽：
 * 26.2 反编译确认，行走的脚步声与 STEP 震动事件都由
 * {@code Entity#vibrationAndSoundEffectsFromBlock} 一个方法负责
 * （param3 控制播放脚步音、param4 控制发送 GameEvent.STEP）；
 * 落地的 HIT_GROUND 震动则在 {@code Entity#checkFallDamage} 内经 Level#gameEvent 发出。
 * 两处屏蔽后，幽匿感测体与监守者都收不到穿戴者的行走/落地震动。
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

	/**
	 * 脚步声 + STEP 震动一并屏蔽（该方法返回 false 即"未产生声音与震动"，
	 * 与原版在空气中/游泳时的返回一致，调用方无副作用）。
	 */
	@Inject(method = "vibrationAndSoundEffectsFromBlock", at = @At("HEAD"), cancellable = true)
	private void extraenchantryshort$unseenSilenceStep(BlockPos pos, BlockState state, boolean playSound,
			boolean sendEvent, Vec3 movement, CallbackInfoReturnable<Boolean> cir) {
		if (extraenchantryshort$wearsUnseen()) {
			cir.setReturnValue(false);
		}
	}

	/**
	 * 落地震动屏蔽：仅拦下 HIT_GROUND 的 gameEvent 发送，
	 * 摔落伤害、落地粒子与 Block#fallOn 行为全部保持原样。
	 */
	@Redirect(
			method = "checkFallDamage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"
			)
	)
	private void extraenchantryshort$unseenSilenceLanding(Level level, Holder<GameEvent> event, Vec3 pos,
			GameEvent.Context context) {
		if (extraenchantryshort$wearsUnseen()) {
			return;
		}
		level.gameEvent(event, pos, context);
	}

	/** 穿戴者（须为生物）靴子上是否带无踪 */
	private boolean extraenchantryshort$wearsUnseen() {
		return (Object) this instanceof LivingEntity living
				&& ModEnchantments.getUnseenLevelOnFeet(living) > 0;
	}

	/**
	 * 渊息（Tideheart）I~III 级——氧气上限提升：
	 * 26.2 反编译确认，氧气上限就是 {@code Entity#getMaxAirSupply()} 硬编码返回 300（15 秒），
	 * 且 increaseAirSupply 以它为钳制上限——单点 HEAD 注入放大后，
	 * 消耗、水面换气回满、客户端气泡 HUD 全部自动跟随。
	 * 每级 +300 tick（+15 秒）：I/II/III 级 → 30/45/60 秒。
	 *
	 * <p>构造时序陷阱（26.2 实测堆栈）：{@code Entity#<init>} 的 defineSyncker
	 * 在第 322 行就回调 getMaxAirSupply()（定义 DATA_AIR_SUPPLY_ID 的初值），
	 * 而 {@code LivingEntity#equipment} 字段要到子类构造体才初始化——
	 * 此时 instanceof LivingEntity 已为真但 getItemBySlot 必然 NPE，
	 * 实体构造直接失败（新世界/登录时 "Couldn't place player in world"）。
	 * 防御：装备未就绪视为无渊息，走原版上限；实体构造完成后调用路径全部正常。</p>
	 */
	@Inject(method = "getMaxAirSupply", at = @At("HEAD"), cancellable = true)
	private void extraenchantryshort$tideheartMaxAir(CallbackInfoReturnable<Integer> cir) {
		if ((Object) this instanceof LivingEntity living
				&& living instanceof EquipmentReady ready
				&& ready.extraenchantryshort$isEquipmentReady()) {
			// 性能：改用构造 TAIL 置位的实例标记——旧实现靠 catch NullPointerException
			// 兜底，每个实体构造都抛一次带 fillInStackTrace 的异常，世界生成期每秒数千次。
			int level = ModEnchantments.getTideheartLevel(living.getItemBySlot(EquipmentSlot.HEAD));
			if (level > 0) {
				// 每级 +300 tick（+15 秒）：I/II/III 级 → 30/45/60 秒（原版上限 300）
				cir.setReturnValue(300 + 300 * level);
			}
		}
	}
}
