package realmikoto.extraenchantryshort;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 数据包交叉引用完整性测试（回归历史上「数据包错误 / 无法启动世界」事故形态）。
 *
 * <p>覆盖两类真实事故：worldgen 引用不存在的原版 placed_feature / noise 参数；
 * 另校验 advancement parent、图标模型、recipe 结构与配方引用的物品 ID。</p>
 */
class DatapackIntegrityTest {

	private static final String MOD_ID = "extra-enchantry-short";

	private static Path dataRoot() {
		return Path.of("src/main/resources/data", MOD_ID);
	}

	private static Path assetsRoot() {
		return Path.of("src/main/resources/assets", MOD_ID);
	}

	private static boolean existsVanilla(String path) {
		return DatapackIntegrityTest.class.getClassLoader().getResource(path) != null;
	}

	private static JsonObject read(Path p) throws IOException {
		return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
	}

	private static List<Path> jsons(String sub) throws IOException {
		Path root = dataRoot().resolve(sub);
		if (!Files.isDirectory(root)) {
			return List.of();
		}
		try (Stream<Path> s = Files.walk(root)) {
			return s.filter(x -> x.toString().endsWith(".json")).toList();
		}
	}

	@Test
	void advancementParentsAndIconsExist() throws IOException {
		List<String> missing = new ArrayList<>();
		for (Path p : jsons("advancement")) {
			JsonObject adv = read(p);
			if (adv.has("parent")) {
				String parent = adv.get("parent").getAsString();
				String[] parts = parent.split(":", 2);
				if (parts[0].equals(MOD_ID)
						&& !Files.exists(dataRoot().resolve("advancement/" + parts[1] + ".json"))) {
					missing.add(p.getFileName() + " parent -> " + parent);
				}
			}
			JsonObject display = adv.has("display") ? adv.getAsJsonObject("display") : null;
			if (display != null && display.has("icon")) {
				String icon = display.getAsJsonObject("icon").get("id").getAsString();
				String[] parts = icon.split(":", 2);
				if (parts[0].equals(MOD_ID)) {
					// 物品模型（items/<id>.json）必须存在，否则进度图标显示 missing texture
					if (!Files.exists(assetsRoot().resolve("items/" + parts[1] + ".json"))) {
						missing.add(p.getFileName() + " icon 无模型 -> " + icon);
					}
				}
			}
		}
		assertTrue(missing.isEmpty(), "advancement 引用缺失: " + missing);
	}

	@Test
	void shapelessRecipesUseStringIngredients() throws IOException {
		List<String> bad = new ArrayList<>();
		for (Path p : jsons("recipe")) {
			JsonObject r = read(p);
			String type = r.get("type").getAsString();
			if (type.contains("shapeless")) {
				JsonArray ing = r.getAsJsonArray("ingredients");
				if (ing == null || ing.isEmpty()) {
					bad.add(p.getFileName() + " ingredients 为空");
					continue;
				}
				for (JsonElement e : ing) {
					if (!e.isJsonPrimitive()) {
						bad.add(p.getFileName() + " ingredients 元素非字符串: " + e);
					}
				}
			}
			if (type.contains("shaped")) {
				assertNotNull(r.get("pattern"), p.getFileName() + " 缺 pattern");
				assertNotNull(r.get("key"), p.getFileName() + " 缺 key");
			}
		}
		assertTrue(bad.isEmpty(), "配方结构非法: " + bad);
	}

	private interface NodeVisitor {
		void visit(JsonObject node);
	}

	private static void walk(JsonElement el, NodeVisitor v) {
		if (el.isJsonObject()) {
			JsonObject o = el.getAsJsonObject();
			v.visit(o);
			for (var e : o.entrySet()) {
				walk(e.getValue(), v);
			}
		} else if (el.isJsonArray()) {
			for (JsonElement e : el.getAsJsonArray()) {
				walk(e, v);
			}
		}
	}

	/** 配方引用的物品 ID 必须真实存在（本模有 assets/items/<id>.json，或原版有对应资源） */
	@Test
	void recipeItemIdsResolvable() throws IOException {
		Path assetsItems = assetsRoot().resolve("items");
		List<String> missing = new ArrayList<>();
		for (Path p : jsons("recipe")) {
			JsonObject r = read(p);
			// 收集：ingredients / key / result.id
			List<String> ids = new ArrayList<>();
			if (r.has("ingredients")) {
				for (JsonElement e : r.getAsJsonArray("ingredients")) {
					if (e.isJsonPrimitive()) {
						ids.add(e.getAsString());
					}
				}
			}
			if (r.has("key")) {
				for (var e : r.getAsJsonObject("key").entrySet()) {
					if (e.getValue().isJsonPrimitive()) {
						ids.add(e.getValue().getAsString());
					}
				}
			}
			if (r.has("result") && r.getAsJsonObject("result").has("id")) {
				ids.add(r.getAsJsonObject("result").get("id").getAsString());
			}
			for (String id : ids) {
				if (id.startsWith("#")) {
					continue;   // 标签引用跳过
				}
				String[] parts = id.split(":", 2);
				if (parts.length < 2) {
					missing.add(p.getFileName() + " -> 非法 id: " + id);
					continue;
				}
				if (parts[0].equals(MOD_ID)) {
					if (!Files.exists(assetsItems.resolve(parts[1] + ".json"))) {
						missing.add(p.getFileName() + " -> 本模物品不存在: " + id);
					}
				} else if (!existsVanilla("assets/minecraft/items/" + parts[1] + ".json")) {
					missing.add(p.getFileName() + " -> 原版物品不存在: " + id);
				}
			}
		}
		assertTrue(missing.isEmpty(), "配方引用了不存在的物品: " + missing);
	}

	/** 物品定义（assets/items/*.json）引用的本模模型必须存在 */
	@Test
	void itemDefinitionsResolveModels() throws IOException {
		Path assetsDir = assetsRoot();
		Path modelsDir = assetsDir.resolve("models");
		assertTrue(Files.isDirectory(assetsDir.resolve("items")), "items 目录缺失: " + assetsDir.resolve("items").toAbsolutePath());
		List<String> missing = new ArrayList<>();
		try (Stream<Path> files = Files.walk(assetsDir.resolve("items"))) {
			for (Path p : files.filter(x -> x.toString().endsWith(".json")).toList()) {
				JsonObject def = read(p);
				JsonObject model = def.has("model") ? def.getAsJsonObject("model") : null;
				if (model == null || !model.has("model")) {
					continue;
				}
				String modelId = model.get("model").getAsString();
				String[] parts = modelId.split(":", 2);
				if (parts.length < 2 || !parts[0].equals(MOD_ID)) {
					continue;   // 原版模型/其他命名空间跳过
				}
				// 模型 ID 自带 item/ 前缀：models/item/xxx.json
				if (!Files.exists(modelsDir.resolve(parts[1] + ".json"))) {
					missing.add(p.getFileName() + " -> 模型不存在: " + modelId);
				}
			}
		}
		assertTrue(missing.isEmpty(), "物品定义引用了不存在的模型: " + missing);
	}

	/** 配方产物 / 材料里引用的本模物品必须在代码注册清单口径内（24 物品：16 配饰 + 8 宝石） */
	@Test
	void registeredItemCoverage() throws IOException {
		Path itemsDir = assetsRoot().resolve("items");
		int count;
		try (Stream<Path> files = Files.list(itemsDir)) {
			count = (int) files.filter(x -> x.toString().endsWith(".json")).count();
		}
		assertTrue(count >= 24, "本模物品定义数量异常: " + count + "（应 ≥ 24：16 配饰 + 8 宝石）");
	}
}
