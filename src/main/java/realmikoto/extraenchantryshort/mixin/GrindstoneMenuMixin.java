package realmikoto.extraenchantryshort.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import realmikoto.extraenchantryshort.registry.ModEnchantments;

/**
 * 破限（Limit Break）砂轮保护：
 * 砂轮 removeNonCursesFrom 移除全部非诅咒附魔（单物品与双物品合并路径都汇于此）。
 * 装备上的破限附魔无法被砂轮去除——HEAD 时记录破限的 Holder 与等级，
 * RETURN 时塞回结果物品。
 * 附魔书不做保护：书去附魔后本就转换为普通书（原版机制），是自愿清除的合理途径。
 */
@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

	private record LimitBreakData(Holder<Enchantment> holder, int level) {
	}

	private static final ThreadLocal<LimitBreakData> extraenchantryshort$capturedLimitBreak = new ThreadLocal<>();

	@Inject(method = "removeNonCursesFrom", at = @At("HEAD"))
	private void extraenchantryshort$captureLimitBreak(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		extraenchantryshort$capturedLimitBreak.set(null);
		// 仅捕获装备 ENCHANTMENTS 组件中的破限（附魔书的 STORED 不保护）
		ItemEnchantments enchantments = stack.getEnchantments();
		for (Holder<Enchantment> holder : enchantments.keySet()) {
			if (holder.is(ModEnchantments.LIMIT_BREAK)) {
				extraenchantryshort$capturedLimitBreak.set(new LimitBreakData(holder, enchantments.getLevel(holder)));
				break;
			}
		}
	}

	@Inject(method = "removeNonCursesFrom", at = @At("RETURN"))
	private void extraenchantryshort$restoreLimitBreak(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		LimitBreakData data = extraenchantryshort$capturedLimitBreak.get();
		extraenchantryshort$capturedLimitBreak.remove();
		if (data == null) {
			return;
		}
		ItemStack result = cir.getReturnValue();
		// 防御：附魔书去附魔后转换为普通书，无法承载附魔
		if (result.is(Items.BOOK)) {
			return;
		}
		ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(result.getEnchantments());
		mutable.set(data.holder(), data.level());
		result.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
	}
}
