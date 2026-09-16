package realmikoto.extraenchantryshort.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import realmikoto.extraenchantryshort.advancement.Advancements;
import realmikoto.extraenchantryshort.combat.LoamManager;
import realmikoto.extraenchantryshort.fx.FxHelper;
import realmikoto.extraenchantryshort.registry.ModEnchantments;
import realmikoto.extraenchantryshort.rules.TierBreakRules;

/**
 * 丰壤（Loam）作物收获入口 + 拓阶挖掘特效：
 * 26.2 反编译确认，玩家破坏方块的掉落结算集中在
 * {@code Block#playerDestroy(Level, Player, BlockPos, BlockState, BlockEntity, ItemStack)}。
 * RETURN 处交由 LoamManager 判定成熟作物 + 丰壤锄头，做双倍掉落与范围收获；
 * 并处理拓阶特效与「基石崩解」隐藏挑战。
 */
@Mixin(Block.class)
public abstract class BlockMixin {

	@Inject(method = "playerDestroy", at = @At("RETURN"))
	private void extraenchantryshort$loamHarvest(Level level, Player player, BlockPos pos, BlockState state,
			BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
		LoamManager.onCropHarvest(level, player, pos, state, blockEntity, tool);
		extraenchantryshort$tierBreakFx(level, player, pos, state, tool);
		// 隐藏挑战「基石崩解」：拓阶镐真正挖掉基岩（不受 tierBreakFx 节流影响）
		if (state.is(Blocks.BEDROCK)
				&& TierBreakRules.canMineBedrock(tool, ModEnchantments.getTierBreakLevel(tool))
				&& player instanceof ServerPlayer serverPlayer) {
			Advancements.award(serverPlayer, Advancements.BEDROCK_BREAKER);
		}
	}

	/**
	 * 拓阶特效（P2）：仅当挖掉的是"没有拓阶就本不可挖"的方块时触发——
	 * 附魔打击粒子 ×3 + 极轻铁砧音（"这镐子不对劲，是附魔在干活"）；
	 * 正常挖掘不触发（防刷屏的关键），10 tick 节流。
	 */
	private void extraenchantryshort$tierBreakFx(Level level, Player player, BlockPos pos, BlockState state,
			ItemStack tool) {
		int tierBreakLevel = ModEnchantments.getTierBreakLevel(tool);
		if (tierBreakLevel <= 0 || !(level instanceof ServerLevel serverLevel)
				|| !FxHelper.throttle(player, "tier_break", 10)) {
			return;
		}
		boolean boosted = TierBreakRules.isBoostedHarvestable(tool, state, tierBreakLevel)
				|| (state.is(Blocks.BEDROCK) && TierBreakRules.canMineBedrock(tool, tierBreakLevel));
		if (!boosted || TierBreakRules.isBoostedHarvestable(tool, state, 0)) {
			return;
		}
		FxHelper.burstAt(serverLevel,
				pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
				ParticleTypes.ENCHANTED_HIT, 3, 0.3D);
		FxHelper.play(serverLevel, player, SoundEvents.ANVIL_USE, 0.15F, 1.2F);
	}
}
