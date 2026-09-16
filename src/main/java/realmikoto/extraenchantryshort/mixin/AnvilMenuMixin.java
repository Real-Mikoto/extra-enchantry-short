package realmikoto.extraenchantryshort.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * 破限（Limit Break）铁砧逻辑：
 * <ol>
 *   <li>任一输入（主槽 / 附加槽）带破限时，无视「过于昂贵」——
 *       26.2 的门禁在 {@code AnvilMenu#createResult} 内部：
 *       {@code cost.get() >= 40 && !player.hasInfiniteMaterials()} → 产出清空。
 *       重定向其中第二处（门禁处，ordinal 1）的 {@code hasInfiniteMaterials()} 调用，
 *       带破限时视为无限材料，产出不再被清空。
 *       第一处调用是附魔书适用性判定（{@code canEnchant} 旁路），语义不同，不可一并重定向；
 *       且 fabric-item-api 已 Redirect 同方法内的 {@code canEnchant} 调用点，
 *       因此互斥无视走 EnchantmentEvents.ALLOW_ENCHANTING（见入口类）。</li>
 *   <li>任一输入带破限时，铁砧合成费用固定为 10 级经验（TAIL 覆写 cost DataSlot）。</li>
 * </ol>
 *
 * <p>26.2 反编译确认：{@code cost} DataSlot 声明于 AnvilMenu 本类（可 @Shadow）；
 * createResult 内 hasInfiniteMaterials 共两处调用，ordinal 1 为门禁处。</p>
 */
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

	/** 破限融合的固定费用（级） */
	private static final int EXTRAENCHANTRYSHORT$LIMIT_BREAK_FIXED_COST = 10;

	@Shadow
	@Final
	private DataSlot cost;

	/** 判断铁砧两侧输入中是否有带破限的物品（已附魔物品或破限附魔书均算） */
	private boolean extraenchantryshort$hasLimitBreakInput() {
		AnvilMenu menu = (AnvilMenu) (Object) this;
		return ModEnchantments.carriesLimitBreak(menu.getSlot(0).getItem())
				|| ModEnchantments.carriesLimitBreak(menu.getSlot(1).getItem());
	}

	/**
	 * 无视过于昂贵：仅重定向「过于昂贵」门禁处（第二处）的 hasInfiniteMaterials 调用——
	 * 带破限时视为无限材料，门禁 {@code cost >= 40} 不再清空产出
	 * （费用随后在 TAIL 固定为 10 级；创造模式的无限材料豁免保持原样）。
	 */
	@Redirect(
			method = "createResult",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z",
					ordinal = 1
			)
	)
	private boolean extraenchantryshort$ignoreTooExpensive(Player player) {
		return extraenchantryshort$hasLimitBreakInput() || player.hasInfiniteMaterials();
	}

	/** 破限融合费用固定 10 级（仅在产出非空时覆写——早退路径产出为空，无需处理） */
	@Inject(method = "createResult", at = @At("TAIL"))
	private void extraenchantryshort$fixedAnvilCost(CallbackInfo ci) {
		AnvilMenu menu = (AnvilMenu) (Object) this;
		if (!menu.getSlot(2).getItem().isEmpty() && extraenchantryshort$hasLimitBreakInput()) {
			this.cost.set(EXTRAENCHANTRYSHORT$LIMIT_BREAK_FIXED_COST);
		}
	}
}
