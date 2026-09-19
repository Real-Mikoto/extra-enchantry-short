package realmikoto.extraenchantryshort.registry;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import realmikoto.extraenchantryshort.ExtraEnchantryShort;
import realmikoto.extraenchantryshort.accessory.Accessories;

import java.util.List;

/**
 * 创造模式物品栏：8 宝石 + 16 配饰 + 本模全部附魔的附魔书单列一页。
 * displayItems 回调在打开物品栏时执行，届时数据包已加载，
 * 可通过 ItemDisplayParameters.holders() 查询附魔注册表获取 Holder。
 */
public final class ModCreativeTabs {

	private record EnchantmentEntry(ResourceKey<Enchantment> key, int maxLevel) {
	}

	/** 本模全部附魔及其最大等级 */
	private static final List<EnchantmentEntry> ENCHANTMENTS = List.of(
			new EnchantmentEntry(ModEnchantments.WITHER_PROTECTION, 4),
			new EnchantmentEntry(ModEnchantments.BLAZING_WALKER, 2),
			new EnchantmentEntry(ModEnchantments.LIMIT_BREAK, 1),
			new EnchantmentEntry(ModEnchantments.TIER_BREAK, 3),
			new EnchantmentEntry(ModEnchantments.REACH, 10),
			new EnchantmentEntry(ModEnchantments.DECOY, 3),
			new EnchantmentEntry(ModEnchantments.SIPHON, 3),
			new EnchantmentEntry(ModEnchantments.LIFE_EROSION, 3),
			new EnchantmentEntry(ModEnchantments.VITALITY, 5),
			new EnchantmentEntry(ModEnchantments.BULWARK, 10),
			new EnchantmentEntry(ModEnchantments.AFTERGLOW, 1),
			new EnchantmentEntry(ModEnchantments.OATHBOUND, 1),
			new EnchantmentEntry(ModEnchantments.SKYWARD, 2),
			new EnchantmentEntry(ModEnchantments.CLEAVE, 3),
			new EnchantmentEntry(ModEnchantments.WINDRIDER, 3),
			new EnchantmentEntry(ModEnchantments.UNSEEN, 2),
			new EnchantmentEntry(ModEnchantments.JUDGEMENT, 2),
			new EnchantmentEntry(ModEnchantments.GALE, 3),
			new EnchantmentEntry(ModEnchantments.EMBERFALL, 1),
			new EnchantmentEntry(ModEnchantments.SHIELD_CHARGE, 3),
			new EnchantmentEntry(ModEnchantments.DEFIANCE, 2),
			new EnchantmentEntry(ModEnchantments.SANCTUARY, 3),
			new EnchantmentEntry(ModEnchantments.AEGIS, 3),
			new EnchantmentEntry(ModEnchantments.HOMING_PLUME, 2),
			new EnchantmentEntry(ModEnchantments.STARFALL, 1),
			new EnchantmentEntry(ModEnchantments.STORMSURGE, 2),
			new EnchantmentEntry(ModEnchantments.SHEATHED_EDGE, 3),
			new EnchantmentEntry(ModEnchantments.TIDEHEART, 3),
			new EnchantmentEntry(ModEnchantments.LOAM, 3),
			// 环佩与獠牙：8 配饰附魔 + 3 狼铠附魔
			new EnchantmentEntry(ModEnchantments.SOUL_CHIME, 2),
			new EnchantmentEntry(ModEnchantments.SHIELD_PENDANT, 2),
			new EnchantmentEntry(ModEnchantments.THUNDER_CLASP, 2),
			new EnchantmentEntry(ModEnchantments.VERDANT_DROP, 2),
			new EnchantmentEntry(ModEnchantments.BLADE_RING, 2),
			new EnchantmentEntry(ModEnchantments.PLUME_RING, 2),
			new EnchantmentEntry(ModEnchantments.EMBER_BRACELET, 2),
			new EnchantmentEntry(ModEnchantments.TIDE_BRACELET, 2),
			new EnchantmentEntry(ModEnchantments.REACH_BRACELET, 2),
			new EnchantmentEntry(ModEnchantments.SHARP_FANG, 3),
			new EnchantmentEntry(ModEnchantments.VIGIL, 2),
			new EnchantmentEntry(ModEnchantments.RENEWAL, 2)
	);

	public static final ResourceKey<CreativeModeTab> TAB_KEY =
			ResourceKey.create(Registries.CREATIVE_MODE_TAB, ExtraEnchantryShort.id("enchantments"));

	public static void register() {
		CreativeModeTab tab = FabricCreativeModeTab.builder()
				.title(Component.translatable("itemGroup.extra-enchantry-short.enchantments"))
				.icon(() -> new ItemStack(Items.ENCHANTED_BOOK))
				.displayItems((parameters, output) -> {
					// 环佩与獠牙：8 宝石 + 16 配饰（宝石被动展示由配方产物体现）
					for (net.minecraft.world.item.Item gem : Accessories.GEM_ITEMS) {
						output.accept(new ItemStack(gem));
					}
					for (Accessories.Material material : Accessories.Material.values()) {
						for (Accessories.SlotType slot : Accessories.SlotType.values()) {
							output.accept(new ItemStack(
									Accessories.ACCESSORY_ITEMS[material.ordinal()][slot.ordinal()]));
						}
					}
					for (EnchantmentEntry entry : ENCHANTMENTS) {
						Holder<Enchantment> holder = parameters.holders()
								.lookupOrThrow(Registries.ENCHANTMENT)
								.getOrThrow(entry.key());
						for (int level = 1; level <= entry.maxLevel(); level++) {
							output.accept(createEnchantedBook(holder, level));
						}
					}
				})
				.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, tab);
	}

	/** 构造指定等级的附魔书（STORED_ENCHANTMENTS 组件） */
	private static ItemStack createEnchantedBook(Holder<Enchantment> enchantment, int level) {
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		ItemEnchantments.Mutable stored = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		stored.set(enchantment, level);
		book.set(DataComponents.STORED_ENCHANTMENTS, stored.toImmutable());
		return book;
	}

	private ModCreativeTabs() {
	}
}
