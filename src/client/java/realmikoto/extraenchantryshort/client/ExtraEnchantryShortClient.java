package realmikoto.extraenchantryshort.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

/**
 * 客户端入口：仅保留配饰栏客户端状态管理。
 */
public class ExtraEnchantryShortClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// 离开世界时重置配饰栏动画/展开状态——静态量跨世界残留，
		// 会导致重进后物品画在偏移位置而 isActive 仍为 false
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
				AccessoryHudState.reset());
	}
}
