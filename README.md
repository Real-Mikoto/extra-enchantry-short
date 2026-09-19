# Extra Enchantry Short

Minecraft 26.2 (Fabric) 自定义附魔模组——**只新增附魔，并保留饰品与饰品栏**。

- 许可证：AGPL-3.0-or-later（GNU Affero 通用公共许可证 v3），详见 [LICENSE](LICENSE)

## 功能介绍

Extra Enchantry Short 为 Minecraft 26.2 (Fabric) 增加 **41 个自定义附魔**，并保留耳环 / 项链 / 戒指 / 手镯四槽位的**配饰与宝石体系**。全部内容数据驱动，可用数据包覆盖。

* **41 附魔，全部可用且可自然获取**：全部由数据包定义（`data/extra-enchantry-short/enchantment/*.json`），可用数据包覆盖；全部带专属行为（破限 / 拓阶、断罪、蚀命、汲取、假象、归羽、空跃、劫后余辉、丰壤、冲阵、誓约、盾牌四附魔、配饰附魔、狼铠三附魔等）
* **配饰与宝石**：耳环 / 项链 / 戒指 / 手镯四槽位（内嵌原版背包与创造界面，展开按钮 + 动画）+ 八家族宝石（合成时镶嵌入配饰，材质决定被动传导率）+ 16 配饰与 8 宝石共 40 个合成配方
* **附魔获取**：附魔台 / 铁砧打书 + 原版战利品池覆盖与事件追加（远古城市 / 末地城 / 试炼密室 / 凋零 / 海洋系宝箱与钓鱼）
* **附魔成就**：14 个实战成就（如「处刑者」「金蝉脱壳」「铜墙铁壁」）+ 7 个隐藏挑战（「深渊回响」「以彼之道」「丰收之神的赞许」等），代码授予
* **数据驱动**：附魔 / 标签 / 战利品表 / 成就 / 配方均为标准数据包内容，原版 `/reload` 生效；模组侧无额外规则文件

## 环境

- Minecraft 26.2 / Fabric Loader 0.19.5 / Fabric API 0.160.0+26.2 / Java 25 / Loom 1.17-SNAPSHOT
- 26.2 的关键 API 变化：`ResourceLocation` → `Identifier`；镐 / 斧等工具由**数据驱动**（Tool 组件规则）；附魔全部走 JSON 数据包定义；`tradeable` / `on_random_loot` 为**显式列表**（不引用 `#non_treasure`，本 mod 仅炽焰行者进入这两个标签）

## 安装与使用

### 安装

* **前置**：Fabric Loader `0.19.5+`、Fabric API `0.160.0+26.2`、Java 25
* **客户端 / 服务端**：均需安装（附魔结算与成就授予在服务端；配饰栏渲染、活力 HUD、空跃多段跳在客户端）
* **步骤**：安装 Fabric Loader → 将 `fabric-api` 与本 mod 的 jar（`build/libs/extra-enchantry-short-1.0.2.jar`）放入 `.minecraft/mods/` → 启动

### 使用

* **获取附魔**：按[附魔总览](#附魔总览)表，经附魔台 / 铁砧打书 / 原版战利品池获得；创造模式物品栏「额外附魔书」页签提供全部 41 个附魔的整级附魔书与 24 件饰品
* **配饰栏**：背包界面护甲左侧一列为耳环 / 项链 / 戒指 / 手镯四槽位，点击护甲行上方的展开按钮滑出；右键手持配饰可快捷穿戴（占用则交换回主手）；配饰上可附魔专属附魔（见[配饰八附魔](#配饰八附魔-accessory-enchantments3037)）
* **宝石**：8 种宝石按槽位配对（耳环 = 魂珀 / 盾纹玉，项链 = 雷光石 / 萌芽晶，戒指 = 刃晶 / 风羽晶，手镯 = 烬心石 / 潮汐珠），**合成时镶嵌、不可更换**；配饰材质（铜 / 铁 / 金 / 钻）决定宝石被动传导率（见[配饰与宝石](#配饰与宝石)）
* **成就**：实战成就随效果首次触发授予；隐藏挑战达成即弹出（challenge 帧），清单见[成就体系](#成就体系)

### 数据包自定义

* `data/extra-enchantry-short/enchantment/`：41 个附魔定义（等级 / 费用曲线 / 可附物品 / 互斥）
* `data/minecraft/tags/enchantment/`：获取途径标签（`non_treasure` / `tradeable` / `on_random_loot` / `treasure` / `exclusive_set/*`）
* `data/minecraft/loot_table/`：4 处原版战利品表覆盖（远古城市 / 末地城 / 不祥试炼唯一箱 / 凋零）；庇护与渊息经 `LootTableEvents.MODIFY` 事件追加，不在数据包内
* `data/extra-enchantry-short/recipe/`：8 宝石 + 32 配饰配方（每件配饰有 2 个宝石变体）
* `data/extra-enchantry-short/advancement/recipes/`：40 个配方解锁成就（获得任一合成材料即解锁对应配方书条目）
* 附魔 JSON 单文件损坏仅该附魔不可用（日志报错），其余照常；`/reload` 后数据包内容即时生效（模组无额外监听逻辑）

## 附魔总览

| #  | 附魔                     | 等级  | 可附魔物品                              | 获取方式                                              |
| -- | ---------------------- | --- | ---------------------------------- | ------------------------------------------------- |
| 1  | 凋零保护 Wither Protection | IV  | 四件护甲                               | 非宝藏：附魔台 / 铁砧打书（与保护系互斥）                            |
| 2  | 炽焰行者 Blazing Walker    | II  | 靴子                                 | 宝藏：图书管理员 / 宝箱 / 钓鱼（无附魔台）                          |
| 3  | 破限 Limit Break         | I   | 武器 / 工具 / 护甲 / 弓弩 / 三叉戟 / 钓鱼竿 / 马铠 / 配饰 | 监守者死亡 0.05%；被闪电苦力怕击杀 0.5%                    |
| 4  | 拓阶 Tier Break          | III | 镐 / 斧 / 锹 / 锄                      | 击杀凋零 20%（1-3 级随机）                                 |
| 5  | 触及 Reach               | X   | 近战武器 + 工具                          | 非宝藏：随机来源仅 1 级，铁砧融合升级                           |
| 6  | 假象 Decoy               | III | 头盔                                 | 非宝藏：weight 1（与荆棘互斥）                                   |
| 7  | 汲取 Siphon              | III | 武器 / 工具 / 弓弩 / 三叉戟                 | 非宝藏：very_rare，附魔台 / 铁砧打书                                    |
| 8  | 蚀命 Life Erosion        | III | 武器 / 工具 / 弓弩 / 三叉戟                 | 仅远古城市宝箱（附魔书 / 带附魔的武器工具）                           |
| 9  | 活力 Vitality            | V   | 四件盔甲 / 马铠 / 狼铠 / 配饰                | 非宝藏：附魔台 / 铁砧打书                                    |
| 10 | 壁垒 Bulwark             | X   | 胸甲 / 马铠 / 狼铠 / 配饰                  | 非宝藏：随机来源仅 1 级，铁砧融合升级                                |
| 11 | 劫后余辉 Afterglow         | I   | 不死图腾                               | 非宝藏：附魔台出书 → 铁砧上书（图腾无附魔台途径）                        |
| 12 | 誓约 Oathbound           | I   | 全装备                                | 非宝藏：附魔台 / 铁砧打书                                    |
| 13 | 空跃 Skyward             | II  | 靴子                                 | 非宝藏：附魔台 / 铁砧打书（与冰霜行者 / 深海探索者 / 炽焰行者互斥）            |
| 14 | 破阵 Cleave              | III | 近战武器 / 工具                          | 非宝藏：附魔台 / 铁砧打书（与横扫之刃互斥）                           |
| 15 | 御风 Windrider           | III | 鞘翅                                 | 仅末地城宝箱（附魔书 / 带附魔的鞘翅）                              |
| 16 | 无踪 Unseen              | II  | 靴子                                 | 仅远古城市宝箱（与靴子系附魔互斥）                                 |
| 17 | 断罪 Judgement           | II  | 近战武器 / 工具                          | 非宝藏：very_rare，附魔台 / 铁砧打书                          |
| 18 | 疾风 Gale                | III | 护腿                                 | 非宝藏：随机最高 II 级，III 级两本 II 铁砧融合                       |
| 19 | 余烬 Emberfall           | I   | 金胸甲 / 金马铠                          | 仅不祥试炼唯一奖励箱（附魔书 / 带附魔的金胸甲）                         |
| 20 | 冲阵 Shield Charge       | III | 盾牌                                 | 非宝藏：uncommon，附魔台 / 铁砧打书                           |
| 21 | 不屈 Defiance            | II  | 盾牌                                 | 非宝藏：rare，附魔台 / 铁砧打书                               |
| 22 | 庇护 Sanctuary           | III | 盾牌                                 | 试炼密室基础 / 稀有奖励箱（附魔书 / 带附魔的盾牌）                      |
| 23 | 坚壁 Aegis               | III | 盾牌                                 | 非宝藏：rare，附魔台 / 铁砧打书                               |
| 24 | 归羽 Homing Plume        | II  | 弓 / 弩                              | 非宝藏：附魔台 / 铁砧打书（与无限互斥）                             |
| 25 | 坠星 Starfall            | I   | 弩                                  | 仅末地城宝箱（附魔书 / 带附魔的弩，与多重射击互斥）                       |
| 26 | 霆霓 Stormsurge          | II  | 三叉戟                                | 非宝藏：附魔台 / 铁砧打书（与引雷互斥）                             |
| 27 | 藏锋 Sheathed Edge       | III | 剑 / 斧                              | 非宝藏：rare，附魔台 / 铁砧打书                               |
| 28 | 渊息 Tideheart           | III | 头盔                                 | 宝藏（无附魔台）：海洋系宝箱（沉船 / 宝藏 / 海底废墟）+ 钓鱼；与水下呼吸 / 水下速掘互斥 |
| 29 | 丰壤 Loam                | III | 锄                                  | 非宝藏：附魔台 / 铁砧打书                                    |
| 30 | 魂铃 Soul Chime          | II  | 耳环                                 | 非宝藏：击杀敌对生物回复饥饿与饱和                                 |
| 31 | 盾坠 Shield Pendant      | II  | 耳环                                 | 非宝藏：受到的伤害 -3%/级                                   |
| 32 | 雷鸣扣 Thunder Clasp      | II  | 项链                                 | 非宝藏：雷雨时造成的伤害 +4%/级                                |
| 33 | 翠滴 Verdant Drop        | II  | 项链                                 | 非宝藏：自然恢复间隔 80 tick → 最低 40 tick                   |
| 34 | 刃戒 Blade Ring          | II  | 戒指                                 | 非宝藏：攻击速度 +5%/级                                    |
| 35 | 羽环 Plume Ring          | II  | 戒指                                 | 非宝藏：弹射物伤害 -6%/级                                   |
| 36 | 烬镯 Ember Bracelet      | II  | 手镯                                 | 非宝藏：火焰伤害 -10%/级                                   |
| 37 | 潮镯 Tide Bracelet       | II  | 手镯                                 | 非宝藏：游泳效率 +8%/级                                    |
| 38 | 锐牙 Sharp Fang          | III | 狼铠                                 | 非宝藏：附魔台出书 → 铁砧上书                              |
| 39 | 哨戒 Vigil               | II  | 狼铠                                 | 非宝藏：附魔台出书 → 铁砧上书                              |
| 40 | 回春 Renewal             | II  | 狼铠                                 | 非宝藏：附魔台出书 → 铁砧上书                              |
| 41 | 远镯 Reach Bracelet      | II  | 手镯                                 | 非宝藏：触及距离 +0.5/级（可与触及叠加）                                |

## 目录

- [功能介绍](#功能介绍)
- [安装与使用](#安装与使用)
- [附魔](#附魔)
  - [1. 凋零保护 (Wither Protection)](#1-凋零保护-wither-protection)
  - [2. 炽焰行者 (Blazing Walker)](#2-炽焰行者-blazing-walker)
  - [3. 破限 (Limit Break)](#3-破限-limit-break)
  - [4. 拓阶 (Tier Break)](#4-拓阶-tier-break)
  - [5. 触及 (Reach)](#5-触及-reach)
  - [6. 假象 (Decoy)](#6-假象-decoy)
  - [7. 汲取 (Siphon)](#7-汲取-siphon)
  - [8. 蚀命 (Life Erosion)](#8-蚀命-life-erosion)
  - [9. 活力 (Vitality)](#9-活力-vitality)
  - [10. 壁垒 (Bulwark)](#10-壁垒-bulwark)
  - [11. 劫后余辉 (Afterglow)](#11-劫后余辉-afterglow)
  - [12. 誓约 (Oathbound)](#12-誓约-oathbound)
  - [13. 空跃 (Skyward)](#13-空跃-skyward)
  - [14. 破阵 (Cleave)](#14-破阵-cleave)
  - [15. 御风 (Windrider)](#15-御风-windrider)
  - [16. 无踪 (Unseen)](#16-无踪-unseen)
  - [17. 断罪 (Judgement)](#17-断罪-judgement)
  - [18. 疾风 (Gale)](#18-疾风-gale)
  - [19. 余烬 (Emberfall)](#19-余烬-emberfall)
  - [盾牌四附魔 (Shield Enchantments)（20~23）](#盾牌四附魔-shield-enchantments2023)
  - [24. 归羽 (Homing Plume)](#24-归羽-homing-plume)
  - [25. 坠星 (Starfall)](#25-坠星-starfall)
  - [26. 霆霓 (Stormsurge)](#26-霆霓-stormsurge)
  - [27. 藏锋 (Sheathed Edge)](#27-藏锋-sheathed-edge)
  - [28. 渊息 (Tideheart)](#28-渊息-tideheart)
  - [29. 丰壤 (Loam)](#29-丰壤-loam)
  - [配饰八附魔 (Accessory Enchantments)（30~37）](#配饰八附魔-accessory-enchantments3037)
  - [狼铠三附魔 (Wolf Armor Enchantments)（38~40）](#狼铠三附魔-wolf-armor-enchantments3840)
  - [41. 远镯 (Reach Bracelet)](#41-远镯-reach-bracelet)
- [版本主题](#版本主题)
  - [1.0.2「远镯与配方书」 (Reach Bracelet & Recipe Book)](#102远镯与配方书-reach-bracelet--recipe-book)
  - [1.0.1「独立移植」 (Independent Port)](#101独立移植-independent-port)
- [更新日志](#更新日志)
- [通用技术模式](#通用技术模式)
- [记录规范](#记录规范)
  - [附魔记录规范](#附魔记录规范)
  - [排版规则](#排版规则)

---

## 附魔

### 1. 凋零保护 (Wither Protection)

**功能**：受到凋零效果时，按装备上的附魔总等级缩短凋零持续时间。每级减少 15%，多件叠加，总减少上限 90%。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/wither_protection.json`：

- `max_level: 4`，权重 / 费用曲线复制自火焰保护（获取概率与四类原版保护一致）
- `supported_items/primary_items: #minecraft:enchantable/armor`（四件护甲），`slots: ["armor"]`
- `exclusive_set: "#minecraft:exclusive_set/armor"` + 追加进原版 `exclusive_set/armor` 标签（与保护 / 火焰 / 爆炸 / 弹射物保护互斥，双向）
- `effects: {}` —— 效果逻辑无对应数据组件，纯 Mixin 实现

**核心逻辑 Mixin** `mixin/LivingEntityMixin.java`：

`@ModifyVariable` 拦截 `LivingEntity.addEffect(MobEffectInstance, Entity)` 的入参：

- 判定 `effect.is(MobEffects.WITHER)` 且非无限时长
- 遍历护甲槽位（`EquipmentSlot.isArmor()`，含动物 / 狼的 BODY 槽），累加各装备上的凋零保护等级
- `reduction = min(0.9, totalLevels × 0.15)`，返回 `effect.withScaledDuration(1.0F - reduction)`

**获取途径**：追加进 `minecraft:non_treasure` → 附魔台 / 铁砧打书（26.2 的 `tradeable` / `on_random_loot` 为显式列表且不引用 `#non_treasure`，本 mod 未把凋零保护加进去，故图书管理员 / 宝箱 / 钓鱼不出）。

### 2. 炽焰行者 (Blazing Walker)

**功能**：与冰霜行者同构 —— 行走时脚下半径内岩浆凝固成岩浆块（1 级半径 3 格、2 级 4 格），同时免疫踩踏热方块的伤害（含岩浆块）。与冰霜行者、深海探索者互斥。

#### 实现方法

**纯数据驱动**，无 Mixin。`data/extra-enchantry-short/enchantment/blazing_walker.json` 复制冰霜行者结构改两个效果目标：

- `minecraft:location_changed` 效果 → `minecraft:replace_disk`：`block_state` 由 `frosted_ice` 改为 `magma_block`，predicate 中 `matching_blocks` / `matching_fluids` 由 `water` 改为 `lava`（其余不变：上方空气、unobstructed、on_ground 且非载具时触发，半径 `linear 3 + 1/级`、clamp 0~16）
- `minecraft:damage_immunity` 效果：免疫 `burn_from_stepping` 伤害标签（冰霜行者原样保留，恰好覆盖岩浆块踩踏伤害）
- `exclusive_set: "#minecraft:exclusive_set/boots"` + 追加进原版 boots 互斥标签
- 等级 / 权重 / 费用 / 铁砧成本与冰霜行者完全一致；足下火迹粒子为纯视觉分支（`LivingEntityMixin` tick 分发），与凝固逻辑无关

**获取途径**：宝藏附魔 —— 追加进 `treasure`、`tradeable`、`on_random_loot` 三个标签（图书管理员 / 宝箱 / 钓鱼，无附魔台）。

### 3. 破限 (Limit Break)

**功能**（单级，作用于带此附魔的物品 / 护甲）：

1. 无视全部附魔互斥（附魔台与铁砧两路：保护四系、精准 / 时运、修补 / 无限、锋利三系、多重 / 穿透等）
2. 无视铁砧「过于昂贵」—— 合成费用固定为 10 级经验
3. 保护类附魔伤害减免上限从 80% 提升到 100%（EPF clamp 20 → 25）
4. 跨部位附魔解锁：带破限的腿甲可在铁砧附魔原版摔落保护（原版仅限靴子）
5. 装备上的破限无法被砂轮去除
6. 带破限护甲上的**活力上限失效**（+50 上限不再生效，每级 ×4 倍率照常）

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/limit_break.json`：

- 1 级，`supported_items: #extra-enchantry-short:limit_break_supported`（自定义物品标签：10 个原版 enchantable 标签 + 6 种马铠 + 16 件配饰），**无 primary_items** → 附魔台不可出
- 费用 `min_cost = max_cost = 25`；不加入任何获取标签（仅监守者掉落）

**获取途径** `combat/LimitBreakManager.java`：订阅 `ServerLivingEntityEvents.AFTER_DEATH` —— 死亡实体是监守者时：普通击杀 `0.05%`（0.0005）掉 1 级破限附魔书；**闪电苦力怕**（`Creeper#isPowered`）击杀 `0.5%`（0.005）。附魔书经 `Items.ENCHANTED_BOOK` + `STORED_ENCHANTMENTS` 组件构造，闪电苦力怕来源额外打自定义布尔组件 `LIMIT_BREAK_SOURCE`（区分隐藏挑战来源）。

**隐藏挑战**：`mixin/PlayerMixin.java` 挂 `Player#addItem`（拾取 / 漏斗 / 合成统一入口）+ `mixin/InventoryMixin.java` 挂 `Inventory#add`（/give 直调 add 不经 addItem 的兜底）→ `LimitBreakManager.onItemObtained` 授予「极限之证」（任意来源破限书）与「雷霆之礼」（闪电苦力怕代杀来源）。

**Mixin 五处**：

1. `mixin/EnchantmentHelperMixin.java`（附魔台路径）—— `selectEnchantment` HEAD/RETURN 间用 **ThreadLocal** 传递当前物品，`@Inject`（HEAD, cancellable）拦截 `filterCompatibleEnchantments`：物品带破限 → 跳过互斥过滤
2. `mixin/AnvilMenuMixin.java` —— `@Redirect` 拦截 `createResult` 中的 `hasInfiniteMaterials`：任一输入带破限 → 视为无限材料跳过「过于昂贵」；TAIL 将费用固定为 10 级
3. `mixin/LivingEntityMixin.java`（保护上限）—— `@Redirect` 拦截 `getDamageAfterMagicAbsorb` 中的 `CombatRules.getDamageAfterMagicAbsorb(damage, protection)`：护甲带破限时改用 `damage × (1 - clamp(EPF, 0, 25)/25)`（原版 clamp 上限 20 = 80% 减免，改 25 = 100%）
4. `mixin/GrindstoneMenuMixin.java` —— 砂轮 `removeNonCursesFrom`（单物品与双物品合并路径都汇于此）HEAD 记录破限 Holder 与等级（ThreadLocal）、RETURN 塞回结果物品；附魔书不保护：书去附魔后本就转换为普通书（原版机制）
5. **跨部位摔落保护** —— 主类订阅 fabric-item-api 的 `EnchantmentEvents.ALLOW_ENCHANTING`：附魔为原版 `feather_falling` 且目标是带破限的腿甲（`#minecraft:enchantable/leg_armor`）时返回 `TriState.TRUE`。**不能**用 Mixin Redirect 拦 `Enchantment.canEnchant`：fabric-item-api 自身的 AnvilMenuMixin 已 Redirect 该调用点（Redirect 独占注入，冲突即 Critical injection failure）

**活力上限失效**：`ModEnchantments.getVitalityBonus` 判定 `hasLimitBreakOnArmor` —— 任一护甲带破限即不再钳制 +50 上限（见[活力](#9-活力-vitality)）。

### 4. 拓阶 (Tier Break)

**功能**：3 级，每级 +1 挖掘等级（低级工具可采集原本不可掉落的方块，如木镐 + 1 挖铁矿）。下界合金镐 + III 级拓阶可挖掘基岩，挖掘时间为黑曜石的两倍，受效率影响，破坏后掉落基岩。

#### 实现方法

**等级体系** `rules/TierBreakRules.java`（双 Mixin 共用，反编译 `ToolMaterial.applyToolProperties` 确认）：工具由 Tool 组件两条规则构成 —— `deniesDrops(#incorrect_for_X_tool)` + `minesAndDrops(#mineable/X, speed)`。标签嵌套推导基础等级：木 / 金 = 0，石 / 铜 = 1，铁 = 2，钻石 / 下界合金 = 3；有效等级 = 基础 + 拓阶，≥3 无限制；基岩特判 = 下界合金镐 + 3 级。

**Mixin 两处**：

1. `mixin/ItemStackMixin.java` —— `@Inject`（HEAD, cancellable）拦截 `isCorrectToolForDrops`：按提升后等级换算限制标签，范围内放行（基岩分支直接 true）；同样拦截 `getDestroySpeed`：等级提升后以工具正常速度挖掘（否则退化为手速），基岩返回镐材质速度（使效率属性生效）
2. `mixin/BlockBehaviourMixin.java` —— `@Redirect` 拦截 `getDestroyProgress` 中的 `BlockState.getDestroySpeed`：基岩原版 destroyTime = -1（进度恒 0 不可破坏），持下界合金镐 + 3 级拓阶时返回 100（黑曜石 50 的两倍）

**数据**：附魔 JSON（3 级，`#minecraft:enchantable/mining_loot`，无 primary_items，仅主手）；覆盖 `data/minecraft/loot_table/entities/wither.json`（整表替换为单池：`random_chance 0.2` 掉 1~3 级拓阶附魔书）—— 26.2 原版凋灵掉落表本身无池，**下界之星由 `WitherBoss#dropCustomDeathLoot` 硬编码掉落**（反汇编确认），此覆盖不影响原版掉落。挖穿基岩授予隐藏挑战「基石崩解」（`mixin/BlockMixin.java`）。

### 5. 触及 (Reach)

**功能**：10 级，每级 +0.5 实体交互范围与方块交互范围。非宝藏附魔，随机来源仅能获得 1 级，更高等级只能通过铁砧融合升级（I+I→II…）。命中超出基础交互范围（3 格）的目标时附带剑气粒子反馈。

#### 实现方法

**效果（纯数据驱动）** `data/extra-enchantry-short/enchantment/reach.json` 的 `minecraft:attributes` 效果：

- `entity_interaction_range` / `block_interaction_range` 各 `linear 0.5 + 0.5/级`（`add_value`，修改器 id `extra-enchantry-short:enchantment.reach/entity|block`）
- `supported_items: #extra-enchantry-short:reach_supported`（`#minecraft:enchantable/weapon` + trident + mining_loot），`slots: ["mainhand"]`
- `max_level: 10`，`anvil_cost: 2`；`min_cost = {base: 5, per_level_above_first: 100}` —— II 级起需 cost ≥ 105，附魔台 / 钓鱼永远只出 1 级（纯数据限级）
- `mixin/EnchantRandomlyFunctionMixin.java` 对触及钳制宝箱书 / 交易的 `enchant_randomly` 到 minLevel（与壁垒共用同一拦截）

**剑气反馈** `mixin/LivingEntityMixin.java`：`hurtServer` 注入 —— 命中距离超出基础交互范围时在目标处播放剑气粒子（纯视觉）。

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书；随机来源仅 1 级）。

### 6. 假象 (Decoy)

**功能**：3 级，头盔专属，与荆棘互斥。被玩家攻击时概率生成复刻玩家外观的诱饵盔甲架，仇恨立即转移：

| 等级  | 触发概率 | 诱饵数 | 生命值 | 持续   | 冷却   |
| --- | ---- | --- | --- | ---- | ---- |
| I   | 25%  | 1   | 8   | 12 秒 | 25 秒 |
| II  | 40%  | 1   | 16  | 18 秒 | 20 秒 |
| III | 50%  | 2   | 20  | 25 秒 | 18 秒 |

判定间隔 10 秒（成功后进入全局冷却）；诱饵存在期间玩家处于「仇恨脱战」—— 即使主动攻击敌人，敌人也优先追击诱饵，直至全部诱饵被摧毁。含两个彩蛋：0.5% 阿西莫夫级联（诱饵攻击主人及其他诱饵）、受击诱饵 5% 嵌套生成更弱的诱饵（生命 / 持续减半，递归减弱，深度上限 5）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/decoy.json`：

- 费用曲线与 `anvil_cost: 8` 复制荆棘，`weight: 1`（出现率极低），`supported_items/primary_items: #minecraft:enchantable/head_armor`，`slots: ["head"]`
- `exclusive_set: ["minecraft:thorns"]` —— 直接列表格式（互斥是双向的：`areCompatible` 检查双方的 exclusiveSet，单侧声明即完全互斥）
- `effects: {}` —— 逻辑纯 Mixin 实现

**诱饵实体** `entity/DecoyEntity.java`：

- **复用原版盔甲架实体类型**（`extends ArmorStand`）—— 客户端零注册自动渲染，服务器侧保留子类行为；`shouldBeSaved() = false` 永不持久化（防重启残留为普通盔甲架），`interact` 返回 PASS 禁止取放装备
- 外观复刻：头部 = 带主人档案的玩家头颅（`DataComponents.PROFILE`，客户端自动解析皮肤），盔甲与主副手物品原样复制
- 行为：无重力缓慢游走（离主人 8 格外自动靠拢）、`hurtServer` 重写为纯血量扣减、超时 / 摧毁时散发淡蓝色全息粒子
- 彩蛋：`asimov` 标记的诱饵每秒攻击 2.5 格内主人和其他诱饵；首次受击一次性 5% 嵌套判定

**状态管理** `combat/DecoyManager.java`：每玩家状态 = `lastJudgeMs`（10 秒判定间隔）+ `cooldownUntilMs`（全局冷却，期间无论受多少次攻击不再触发）+ 存活诱饵列表；`tryTrigger` 按 间隔→冷却→等级→概率 四重校验生成诱饵；生成时 `retargetNearbyMobs` 把 32 格内正在仇恨主人的生物立即 `setTarget(诱饵)`。

**Mixin 两处**：

1. `mixin/MobMixin.java` —— `@ModifyVariable` 拦截 `Mob.setTarget` 的目标参数：目标玩家带存活诱饵 → 改为最近同维度诱饵（无诱饵则先执行触发判定）。**全部仇恨路径**（索敌 / 受击反击 / 仇恨传递）都汇于 setTarget，单点拦截全覆盖
2. `mixin/LivingEntityMixin.java` —— `@Inject` 拦截 `hurtServer` HEAD：ServerPlayer 被**其他玩家**伤害时执行 `tryTrigger`（PvP 触发路径）

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。以假象替身逃脱授予「金蝉脱壳」成就。

### 7. 汲取 (Siphon)

**功能**：3 级，武器 / 工具 / 弓弩 / 三叉戟。每次攻击成功造成伤害时回复自身生命：近战每级回复 1 颗心（I/II/III → 2/4/6 HP）；投射物命中获得的回复减半（→ 1/2/3 HP）。非宝藏附魔，`weight: 1`（very_rare）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/siphon.json`：

- `max_level: 3`，`weight: 1`
- `supported_items/primary_items: #extra-enchantry-short:siphon_supported`（自定义物品标签聚合 weapon / mining / bow / crossbow / trident / mace 六个 enchantable 标签，覆盖近远程）
- `slots: ["mainhand"]`（仅主手生效），`effects: {}` 纯 Mixin 实现

**核心逻辑 Mixin** `mixin/LivingEntityMixin.java`：`@Inject` 在 `hurtServer` RETURN（成功造成伤害返回 true 才触发）：

- 攻击者取 `source.getEntity()`（需为 LivingEntity 且非自己）；武器取 `source.getWeaponItem()` —— 近战为攻击者主手物品，箭矢命中时返回发射时的弓 / 弩，掷出的三叉戟返回三叉戟本身（一处覆盖全部武器来源）
- 远近判定用 `source.isDirect()`（直接伤害 = 近战；投射物等非直接伤害 = 远程减半）
- 回复量 `heal(等级 × 2.0F × (isDirect ? 1.0 : 0.5))`

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书，`weight: 1` 出现率极低）。

### 8. 蚀命 (Life Erosion)

**功能**：3 级，武器 / 工具 / 弓弩 / 三叉戟。造成伤害时额外追加百分比**目标最大生命值**伤害：I/II/III → 14%/15%/17%；投射物效果 ×0.75。仅远古城市宝箱可获得（附魔书或自带蚀命的武器 / 工具）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/life_erosion.json`：

- `max_level: 3`，`weight: 1`（very_rare），`anvil_cost: 4`
- `supported_items/primary_items: #extra-enchantry-short:life_erosion_supported`（同汲取的六标签聚合），`slots: ["mainhand"]`，`effects: {}` 纯 Mixin 实现

**核心逻辑 Mixin** `mixin/LivingEntityMixin.java`：`@ModifyVariable`（HEAD, argsOnly）拦截 `hurtServer` 的伤害入参 `amount`：

- 武器取 `source.getWeaponItem()`（同汲取）
- `amount += 目标 getMaxHealth() × {0.14, 0.15, 0.17}[等级-1]`；`!source.isDirect()`（投射物）时比率 ×0.75；加成随原始伤害一起走后续护甲 / 魔抗结算（并参与断罪斩杀阈值结算）

**获取途径（专属掉落表）**：覆盖 `data/minecraft/loot_table/chests/ancient_city.json` —— 保留原版池，追加第三池（roll 1）：70% 空 / 16% 蚀命附魔书（等级 `uniform 1-3`）/ 14% 带蚀命的武器工具（铁剑 / 铁斧 / 铁镐 / 钻石剑 / 钻石镐 / 钻石斧，均 `set_damage 0.8-1.0`）。

**排他性保障**：不加入 `non_treasure` / `tradeable` / `on_random_loot` 任何获取标签 —— `enchant_randomly`（宝箱书 / 交易 / 钓鱼随机附魔）从显式标签筛选候选，不在标签内则全途径隔离，仅远古城市专属池掉落。

### 9. 活力 (Vitality)

**功能**：5 级，四件盔甲 / 马铠 / 狼铠 / 配饰可附魔。每级 +4 点最大生命值，多件叠加，叠加上限 +50；任意护甲带破限时上限失效（最高 4 件 × 5 级 × 4 HP = +80）。稀有度普通（`weight: 10`），非宝藏。额外生命不在心形栏中展开，而是在原生命值条上方左对齐以「❤×n/N」紧凑显示（n = 当前剩余，N = 盔甲提供的最大颗数）。马铠 / 狼铠（BODY 槽）活力对动物与狼生效（26.2 `isArmor()` 覆盖 BODY 槽，天然参与同一套求和）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/vitality.json`：

- `max_level: 5`，`weight: 10`（原版 common 稀有度权重档），`anvil_cost: 2`
- `primary_items: #minecraft:enchantable/armor`，`supported_items: #extra-enchantry-short:vitality_supported`（自定义标签：`#minecraft:enchantable/armor` + 六种马铠 + 狼铠 + 16 件配饰），`slots: ["armor"]`
- `effects: {}` —— 加成与 HUD 均为 Mixin 实现

**属性加成 Mixin** `mixin/LivingEntityMixin.java`（tick HEAD 统一分发）：

- `ModEnchantments.getVitalityBonus(entity)`：护甲槽（`isArmor()` 含人形四件套与动物 / 狼 BODY 槽）活力总等级 × 4，无破限则钳制 +50；客户端同样调用（计算 HUD 显示值）
- 与上次值不同时向 `Attributes.MAX_HEALTH` 写入 / 移除瞬态修改器（`addOrUpdateTransientModifier`，id `extra-enchantry-short:vitality`，ADD_VALUE）；tick 双端运行，客户端最大生命值同步正确
- 加成减少时钳制当前生命值，避免血量残留超上限

**HUD 显示 Mixin（客户端）** `client/mixin/HudMixin.java`，26.2 HUD 为渲染状态提取架构（`Hud.extractPlayerHealth` 计算心形行数并提取贴图 / 文本元素）：

1. `@Redirect` ×2 拦截 `extractPlayerHealth` 中的 `Player.getAttributeValue(Attributes.MAX_HEALTH)` 与 `Player.getHealth()` —— 心形栏行数与红心数均按基础生命计算，保持原版单行 10 颗心（高血量不会把活力血量画成多行红心，伤害吸收黄心也只出现在基础行之上）
2. `@Inject` 在 `extractPlayerHealth` TAIL：盔甲图标行上方左对齐绘制 `hud/heart/container` + `hud/heart/full` 双层心形图标 + 白色文本 `×n/N`（`extractArmor` HEAD 另一 `@Inject` 捕获盔甲行实际 y，活力行锚定其上 10 像素；n 按原版半心粒度取整；文本颜色必须写带 alpha 的 ARGB，alpha = 0 直接不渲染）

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书，普通稀有度出现率）。

### 10. 壁垒 (Bulwark)

**功能**：10 级，胸甲 / 马铠 / 狼铠 / 配饰可附魔。锁定受到单次伤害的上限：I 级 5.5 颗心（11 HP），每级递减 0.5 颗心，至 X 级 1 颗心（2 HP）。非宝藏，随机来源仅能获得 1 级（同触及），更高等级只能铁砧融合升级。胸甲与马铠 / 狼铠（BODY 槽）同时结算取较大者。即死 / 虚空 / 饥饿伤害豁免。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/bulwark.json`：

- `max_level: 10`，`weight: 5`，`anvil_cost: 2`，`primary_items: #minecraft:enchantable/chest_armor`，`supported_items: #extra-enchantry-short:bulwark_supported`（`#minecraft:enchantable/chest_armor` + 六种马铠 + 狼铠 + 16 件配饰），`slots: ["chest"]`
- 「仅 1 级」双路拦截完全复用触及方案：`min_cost = {base: 5, per_level_above_first: 100}`（纯数据）+ `EnchantRandomlyFunctionMixin` 钳制宝箱书 / 交易

**核心逻辑** `combat/BulwarkManager.java` + `mixin/LivingEntityMixin.java`：

- `@Inject`（RETURN, cancellable）注入 `getDamageAfterMagicAbsorb` 方法本身（护甲 / 魔抗减免链的终点）：壁垒等级查表 `BULWARK_CAP = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2}` HP，`cir.setReturnValue(applyBulwarkCap(self, 原返回值))` —— **钳制发生在防御减免之后**、吸收盾结算之前，约束的是实际承伤（吸收 + 掉血合计），不会被护甲二次削减（若钳在 hurtServer 入口，30 伤害先钳 2 再被护甲减 80% 只剩 0.4，出现过强 bug）
- **单点覆盖玩家与非玩家**：`Player` 重写了 `actuallyHurt` 不调 super，但其内部对 `getDamageAfterMagicAbsorb` 是虚调用、派发到 `LivingEntity` 的唯一实现 —— 注入方法本身即可全覆盖，**且不破坏本 Mixin 对其内部 `CombatRules` 调用的 Redirect（破限 100% 保护）**
- 单次挡下 ≥4 点伤害授予「铜墙铁壁」成就

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书，随机途径仅 1 级）。

### 11. 劫后余辉 (Afterglow)

**功能**：单级，**只能附魔在不死图腾上**。图腾触发时在原版复活效果之外额外给予「余辉」：生命值立即回满、10 秒锁血（期间生命值不会因任何伤害降低）、5 颗伤害吸收黄心（10 HP）。非宝藏附魔，`weight: 2`（rare）。效果不做任何持久化 —— 图腾触发时即被消耗，附魔随之消失。锁血期间反杀攻击者授予隐藏挑战「死而不僵」。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/afterglow.json`：

- `max_level: 1`，`weight: 2`，`anvil_cost: 4`
- `supported_items/primary_items: "minecraft:totem_of_undying"` —— **直接写单个物品 ID**。不死图腾没有 `enchantable` 组件、也不在任何 `#minecraft:enchantable/*` 标签内；`HolderSet` 编解码支持单 ID 字符串，`#tag` 只是可选项
- `slots: ["hand"]`（主手 / 副手持图腾均生效），`effects: {}` 纯 Mixin 实现

**触发点 Mixin** `mixin/DeathProtectionMixin.java`：`@Inject` 在 `DeathProtection.applyEffects(ItemStack, LivingEntity)` 的 RETURN，参数即触发用的图腾栈副本：

- 原版链路：copy 一份图腾栈 → 消耗原栈 `shrink(1)` → `setHealth(1.0F)` → `deathProtection.applyEffects(副本, this)`。必须挂在这里（而非 `checkTotemDeathProtection` 自身）的理由：① 参数是图腾栈的**副本**（附魔信息完整）；② 死亡效果列表的第一项是 `ClearAllStatusEffectsConsumeEffect`，注入点必须**晚于**它，否则施加的效果会被立刻清空
- 消耗发生在注入点之前 → 「触发后随图腾一起消失」由原版机制天然保证

**效果实现** `combat/AfterglowManager.java` + `registry/ModEffects.java`：

- `setHealth(getMaxHealth())` 回满
- **余辉状态栏效果**：自定义 `extra-enchantry-short:afterglow` MobEffect（BENEFICIAL、暖金色调 `0xFFC850`、图腾粒子），触发时施加 10 秒实例 —— 状态栏自动显示余辉图标与倒计时，`isLocked` 直接 `hasEffect` 查询该实例（锁血计时与状态栏共用同一来源，效果到期锁血自动结束）；图标纹理 `assets/extra-enchantry-short/textures/mob_effect/afterglow.png`
- 5 颗吸收心：原版 `AbsorptionMobEffect#onEffectStarted` 的公式 `max(当前, 4 × (1 + amplifier))` 给不出 10 HP —— 先挂 `MobEffects.ABSORPTION`（0 级放大、2400 tick 与金苹果一致），再 `setAbsorptionAmount(10.0F)` 精确覆盖（放在 `addEffect` 之后，顺序不可颠倒）

**锁血 Mixin** `mixin/PlayerMixin.java`：`@Inject`（HEAD, cancellable）拦截 `Player#actuallyHurt` —— 锁血期间取消调用，生命值与吸收心都不减少，但击退、受伤音效、盔甲耐久等反馈照常。**必须挂在 `Player` 而不是 `LivingEntity` 上**：`Player` 重写了 `actuallyHurt` 且不调用 super，拦截 `LivingEntity` 版本对玩家完全无效。

**获取途径**：追加进 `minecraft:non_treasure` → 附魔台出书。注意：附魔台槽位的准入检查是 `ItemStack#isEnchantable()`（要求 `DataComponents.ENCHANTABLE` 组件），不死图腾没有该组件，**实际放不进附魔台**；`AnvilMenu` 不做该检查（只看 `supported_items`）—— 生存中的正常途径是**铁砧把劫后余辉附魔书打到不死图腾上**。

### 12. 誓约 (Oathbound)

**功能**：单级。带誓约的物品在玩家死亡时不掉落、不消失（消失诅咒同样无效），重生后回到原槽位（配饰四槽同样受保护）；四件护甲（头 / 胸 / 腿 / 脚）全部带誓约时，死亡时经验值与分数也全额保留（随身 keepInventory）。`keepInventory` 开启时本附魔逻辑自然短路。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/oathbound.json`：

- `max_level: 1`，`weight: 2`（rare），`anvil_cost: 4`
- `supported_items/primary_items: "#extra-enchantry-short:limit_break_supported"`（复用全装备聚合标签，含马铠与配饰），`slots: ["any"]`
- `effects: {}` 纯 Mixin

**核心逻辑**（26.2 死亡链路：`ServerPlayer#die` → `dropAllDeathLoot` → `Player#dropEquipment` 是玩家全部物品掉落的唯一入口）：

- **物品保留**：`mixin/PlayerMixin.java` 在 `Player#dropEquipment` HEAD 提取全部带誓约物品、TAIL 原槽位放回 —— `destroyVanishingCursedItems`（消失诅咒销毁）与 `inventory.dropAll` 都碰不到它们；配饰的死亡掉落 / 回插同点位处理（`AccessoryManager`）
- **重生搬运**：`mixin/ServerPlayerMixin.java` 在 `ServerPlayer#restoreFrom` TAIL 补搬运（原版仅 keepInventory / 旁观者时搬运）：部分誓约 → `Inventory#replaceWith` 仅搬背包，经验照常掉落；四件套 → `transferInventoryXpAndScore` 全量搬运（背包 + 经验 + 分数）
- **经验防重复**：`mixin/LivingEntityMixin.java` `@Redirect` `dropAllDeathLoot` 内的 `dropExperience` 调用 —— 四件套时跳过经验球生成（否则「保留 + 掉落」重复），非四件套路径透传原版

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

### 13. 空跃 (Skyward)

**功能**：2 级，靴子专属。空中可再跳 I 级 1 次 / II 级 2 次。次数在触地 / 进水 / 攀爬时重置；按住空格不连烧次数（按键沿触发）；骑乘、鞘翅滑翔、创造飞行时不触发。与冰霜行者 / 深海探索者 / 炽焰行者 / 无踪互斥。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/skyward.json`：

- `max_level: 2`，`weight: 5`（uncommon），`anvil_cost: 4`
- `supported_items/primary_items: "#minecraft:enchantable/foot_armor"`，`slots: ["feet"]`
- 互斥：追加进原版 `minecraft:exclusive_set/boots` 标签（同炽焰行者的做法）

**核心逻辑（客户端权威）** `client/mixin/LocalPlayerMixin.java`：26.2 反编译确认玩家跳跃输入是客户端权威（服务端看不到跳跃键状态，`jumping` 字段对玩家不生效），多段跳类效果必须做在客户端 —— `@Inject` `LocalPlayer#aiStep` HEAD：直接读公开字段 `input.keyPresses.jump()` 做按键沿检测，触发时调用 `jumpFromGround()`（26.2 为 public，含跳跃力度与疾跑加跳，手感与原版一致）；连跳音效音高逐次递减（`1.7 - jumpIndex × 0.1`）。

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

### 14. 破阵 (Cleave)

**功能**：3 级，近战武器 / 工具。命中主目标后，对 2 格内最多 1/2/3 个额外目标造成主目标伤害的 45%/55%/65%（走完整护甲结算），按与主目标的距离由近到远选取。溅射**不触发**汲取与蚀命（防滚雪球），也**不触发**破阵自身（防递归）。与横扫之刃互斥。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/cleave.json`：

- `max_level: 3`，`weight: 5`（uncommon），`anvil_cost: 2`
- `supported_items/primary_items: "#extra-enchantry-short:cleave_supported"`（自定义物品标签：`#minecraft:enchantable/weapon` + `#minecraft:enchantable/mining`，仅近战，不含 trident / bow）
- `exclusive_set: ["minecraft:sweeping_edge"]`（列表写法），`slots: ["mainhand"]`，`effects: {}` 纯 Mixin

**核心逻辑** `combat/CleaveManager.java` + `mixin/LivingEntityMixin.java`：

- `@Inject` `hurtServer` RETURN：主目标伤害实际生效后，以主目标碰撞箱外扩 2 格选目标（排除主目标 / 攻击者 / 友方 / 死者），共用同一 `DamageSource` 逐个 `hurt`（伤害 = 主目标入参 × `{0.45, 0.55, 0.65}`）
- ThreadLocal 标记位统一短路三处 hurtServer 注入：破阵 RETURN 自身（防递归）、汲取 RETURN、蚀命 ModifyVariable（防滚雪球）
- **范围视觉**：溅射目标身体中心各一个 `SWEEP_ATTACK` 粒子（原版横扫之刃同款），主目标脚下以 2 格为半径沿圆周 `CRIT` 粒子画范围指示圈

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

### 15. 御风 (Windrider)

**功能**：3 级，**鞘翅专属**。I 级滑翔耐久消耗 50% 概率跳过；II 级在此基础上烟花火箭推进 ×1.5；III 级耐久跳过 75%、推进 ×1.75。仅末地城宝箱可获得。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/windrider.json`：

- `max_level: 3`，`weight: 2`（rare），`anvil_cost: 4`
- `supported_items/primary_items: "minecraft:elytra"`（单物品 ID 写法，同劫后余辉的图腾），`slots: ["chest"]`（鞘翅占胸甲槽）
- 不加入任何获取标签 → 附魔台 / 交易 / 随机宝箱全隔离，仅专属掉落池

**核心逻辑两处**：

1. **滑翔耐久** `mixin/LivingEntityMixin.java` —— 26.2 反编译确认鞘翅耐久在 `LivingEntity#updateFallFlying`：每 10 tick 计一次、每 2 次（即 20 tick）对可滑翔装备槽中随机一个 `hurtAndBreak(1, this, slot)`。`@Redirect` 该调用，按 `WINDRIDER_DURABILITY_SKIP = {0.5, 0.5, 0.75}` 概率跳过（概率跳过比改 tick 计数更不易出错）
2. **烟花推进** `mixin/FireworkRocketEntityMixin.java` —— `FireworkRocketEntity#tick` 对正在滑翔的附着实体做朝视线方向的插值加速后调 `LivingEntity#setDeltaMovement`（该方法内仅此一处以 LivingEntity 为 owner 的调用，其余是火箭自身移动）。`@Redirect` 后取出「本次推进增量」（新速度 - 旧速度）按 `WINDRIDER_BOOST_FACTOR = {1.0, 1.5, 1.75}` 放大写回，不改动原版插值公式

**获取途径（专属掉落表）**：覆盖 `data/minecraft/loot_table/chests/end_city_treasure.json` —— 保留原版池，追加第三池（roll 1）：70% 空 / 18% 御风附魔书（等级 `uniform 1-3`）/ 12% 带御风的鞘翅（`set_damage 0.9-1.0`）。

### 16. 无踪 (Unseen)

**功能**：2 级，靴子专属。I 级：消除脚步声与落地声，**且不发出 STEP / HIT_GROUND 震动事件** —— 幽匿感测体与监守者无法通过行走 / 落地侦测到穿戴者（摔落伤害、落地粒子、`Block#fallOn` 行为全部保留）；II 级：额外使怪物索敌可见度 ×0.7。与靴子系附魔互斥。仅远古城市宝箱可获得。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/unseen.json`：

- `max_level: 2`，`weight: 1`（very_rare），`anvil_cost: 4`，`supported_items/primary_items: #minecraft:enchantable/foot_armor`，`slots: ["feet"]`
- `exclusive_set: "#minecraft:exclusive_set/boots"` + 追加进该标签（同炽焰行者 / 空跃的做法，双向互斥）

**核心逻辑两处**：

1. **声音与震动屏蔽** `mixin/EntityMixin.java` —— 26.2 反编译确认：脚步声与 `GameEvent.STEP` 震动由**同一个方法**负责：`Entity#vibrationAndSoundEffectsFromBlock(pos, state, playSound, sendEvent, movement)`（private boolean，param3 控声音、param4 控事件），`@Inject` HEAD cancellable 返回 false 即同时屏蔽两者；落地的 `GameEvent.HIT_GROUND` 在 `Entity#checkFallDamage` 内经 `Level#gameEvent(...)` 发出（LivingEntity 重写了 checkFallDamage 但末尾调 super，注入 Entity 版本即全覆盖），`@Redirect` 该调用，带无踪则不转发
2. **索敌可见度降低** `mixin/LivingEntityMixin.java` —— `@Inject` 在 `LivingEntity#getVisibilityPercent(Entity)` 的 RETURN 乘 0.7。该方法是原版潜行（×0.8）/ 隐身影响索敌距离的**唯一系数**，自动作用于全部 `TargetingConditions` 索敌路径

**获取途径（专属掉落表）**：`chests/ancient_city.json` 追加第四池（roll 1）：70% 空 / 16% 无踪附魔书（等级 `uniform 1-2`）/ 14% 带无踪的靴子（铁 5 / 锁链 5 / 钻石 4，均 `set_damage 0.8-1.0`）。不加入任何获取标签，与监守者（声音侦测）主题绑定。

### 17. 断罪 (Judgement)

**功能**：2 级，近战武器 / 工具（复用破阵的 `cleave_supported` 标签）。目标当前生命（含伤害吸收）≤ 最大生命的 10%/20% 时**直接斩杀**；对 boss（末影龙 / 凋灵 / 监守者）不斩杀，改为该次伤害 ×1.75；斩杀触发后攻击者 5 秒冷却。非宝藏，very_rare。破阵溅射期间不触发（避免一次挥砍连环斩杀）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/judgement.json`：`max_level: 2`，`weight: 1`，`anvil_cost: 4`，`supported_items/primary_items: #extra-enchantry-short:cleave_supported`，`slots: ["mainhand"]`。

**核心逻辑** `combat/JudgementManager.java` + `mixin/LivingEntityMixin.java`：

- `@ModifyVariable`（HEAD, argsOnly）拦截 `hurtServer` 的 `amount`，**定义在蚀命之后** → 同方法内按定义顺序链式执行，蚀命追加的百分比伤害也参与斩杀结算
- 阈值判定用 `getHealth() + getAbsorptionAmount()`（吸收心不绕过斩杀）；斩杀实现为把伤害放大到 `maxHealth × 4 + 100`（足以穿透吸收与护甲减免）；冷却按攻击者 UUID 记录毫秒时间戳
- **boss 判定**：26.2 无统一的 `isBoss()`、也无 boss 实体标签，按带 boss 血条的三个原版 boss 显式 `instanceof`（`ModEnchantments.isBossLike`：EnderDragon / WitherBoss / Warden）
- **与壁垒的克制关系**：斩杀仍会经过壁垒在 `getDamageAfterMagicAbsorb` 的上限钳制 —— 壁垒可以挡下断罪（有意的攻防克制，非 bug）

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书，`weight: 1` 出现率极低）。首次处决授予「处刑者」成就。

### 18. 疾风 (Gale)

**功能**：护腿专属。每级 +5% 移动速度（I/II → 5%/10%），**潜行时不生效**（保留潜行的战术价值）。常规途径最高 II 级；**III 级（+15%）只能由两个 II 级铁砧融合得到**。非宝藏，uncommon。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/gale.json`：

- `max_level: 3`，`weight: 5`，`anvil_cost: 4`，`supported_items/primary_items: #minecraft:enchantable/leg_armor`，`slots: ["legs"]`
- **III 级两路封锁**（阈值改为 2 的触及方案）：
  1. 附魔台 / 钓鱼 / 宝箱 `enchant_with_levels`：`min_cost = {base: 5, per_level_above_first: 25}` → II 级需 cost ≥ 30（附魔台满书架恰好可达）、**III 级需 cost ≥ 55**。注意 `non_treasure` 会流入 `on_random_loot`，而原版宝箱装备的 `enchant_with_levels` 最高给到 **cost 50**（远古城市 / 末地城 30-50）—— III 级阈值必须 > 50，不能只管附魔台的 30
  2. 宝箱书 / 图书管理员交易：`mixin/EnchantRandomlyFunctionMixin.java` 对疾风钳制最高 II 级

**速度加成 Mixin** `mixin/LivingEntityMixin.java`：tick HEAD 分发 —— 护腿等级 × 0.05，**潜行时返回 0**（`isCrouching()`）；值变化时向 `Attributes.MOVEMENT_SPEED` 写入 / 移除瞬态修改器（`ADD_MULTIPLIED_BASE`，id `extra-enchantry-short:gale`）。潜行 / 起身、穿脱护腿都会触发重算。

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书，随机最高 II 级；III 级两本 II 铁砧融合）。

### 19. 余烬 (Emberfall)

**功能**：单级，**金胸甲与金马铠可附魔**。受到致命伤害时免死一次：保留 1 颗心（2 HP）+ 5 秒锁血，随后进入 60 秒「余烬」虚弱（缓慢 I + 挖掘疲劳 I，状态栏显示自定义图标与倒计时）；与不死图腾共存时**图腾优先**。仅不祥试炼（试炼密室）唯一奖励箱可获得。

- **金胸甲（玩家，CHEST 槽）**：每次触发消耗 **50% 最大耐久**，剩余耐久不足时不触发（不会把胸甲打碎）
- **金马铠（马匹，BODY 槽）**：马铠不可掉耐久，改用**每实体 60 秒冷却**（跨重连保留，防「修补耐久 → 反复免死」式刷取）；触发时机体免死（1 颗心 + 5 秒锁血 + 同款虚弱），并把 5 秒锁血窗口**同步给骑士**（不继承缓慢 / 挖掘疲劳）。马铠死亡照常掉落

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/emberfall.json`：`max_level: 1`，`weight: 2`（rare），`anvil_cost: 4`，`supported_items/primary_items: ["minecraft:golden_chestplate", "minecraft:golden_horse_armor"]`（数组写法），`slots: ["chest", "body"]`。

**免死触发** `mixin/LivingEntityMixin.java` + `combat/EmberfallManager.java`：

- `@Inject`（RETURN, cancellable）注入 `LivingEntity#checkTotemDeathProtection`（private）：26.2 链路为 `hurtServer → isDeadOrDying() → if (!checkTotemDeathProtection(source)) die(source)`，返回 true 即跳过死亡。选 RETURN 而非 HEAD → 图腾先走，图腾没救命才轮到余烬
- `trySave` 分两路：CHEST 槽金胸甲带余烬且 `isDamageableItem()` → 扣 50% 耐久；BODY 槽金马铠 → 查 60 秒每实体冷却。两路共用 `setHealth(2)`、清空吸收、施加余烬 / 缓慢 / 挖掘疲劳三个 60 秒效果、灵魂火升腾视觉；马铠路额外给 `getFirstPassenger()`（骑士）施加锁血窗口

**锁血窗口**：不用独立计时器 —— 余烬效果总时长 1200 tick，**剩余时长 > 1100 tick（即前 5 秒）即为锁血期**（「效果实例即计时器」思路，同劫后余辉）。锁血拦截两处：`PlayerMixin#actuallyHurt`（玩家，Player 不调 super）与 `LivingEntityMixin#actuallyHurt`（非玩家生物），与余辉锁血共用同一取消点

**自定义状态效果** `registry/ModEffects.java`：`extra-enchantry-short:emberfall`（HARMFUL、暗红色调 `0x8B2500`、灵魂火粒子），图标纹理 `assets/extra-enchantry-short/textures/mob_effect/emberfall.png`

**获取途径（专属掉落表）**：覆盖 `data/minecraft/loot_table/chests/trial_chambers/reward_ominous_unique.json`（不祥宝库唯一奖励）—— 保留原版 5 个唯一奖励，追加第二池（roll 1）：75% 空 / 15% 余烬附魔书 / 10% 带余烬的金胸甲（`set_damage 0.9-1.0`）。不加入任何获取标签。

### 盾牌四附魔 (Shield Enchantments)（20~23）

26.2 盾牌机制已数据组件化（`DataComponents.BLOCKS_ATTACKS`，破盾 = 物品冷却、耐久 = `hurtBlockingItem`），四个盾牌附魔（编号 20~23）分别挂组件方法与伤害入口。附魔定义的 `supported_items/primary_items` 均为单物品 ID `minecraft:shield`，`slots: ["hand"]`（主 / 副手任一持盾生效）。

#### 20. 冲阵 Shield Charge（III，uncommon）

疾跑持盾近战命中：额外 **4/6/8 伤害** + **强力击退**（击退强度 0.9/1.1/1.3，方向 = 攻击者→受击者，实体被推离），攻击者 1 秒冷却。挂 `hurtServer` HEAD `@ModifyVariable`（蚀命之后、断罪之前 —— 冲阵加伤参与断罪斩杀阈值结算），逻辑在 `combat/ShieldChargeManager.java`。疾跑持盾冲阵撞飞敌人授予「冲锋陷阵」、撞倒劫掠兽授予隐藏挑战「以彼之道」。追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

#### 21. 不屈 Defiance（II，rare）

- **I**：盾牌被斧类破盾（26.2 = 盾牌上物品冷却，时长来自攻击者武器组件 `WEAPON.disableBlockingForSeconds`）→ 时长**减半** + 等长的**抗性提升 I** 补偿
- **II**：**完全免疫破盾**，代价是**格挡耐久消耗 ×2**

实现：`mixin/BlocksAttacksMixin.java` 注入组件方法 —— `disable` HEAD 取消（II 免疫）/ `@ModifyVariable` 时长减半 + 抗性（I）；`hurtBlockingItem` 的 damage 参数 ×2（II）。追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

#### 22. 庇护 Sanctuary（III，试炼密室奖励箱）

格挡时每 **2 秒**对 **8 格内所有玩家**（含持有者）回复 **1/2/4 HP**，每次脉冲消耗盾牌 **1 点耐久**（耗尽后格挡中断光环即停）；有视线检测。视觉：受疗者头顶心形粒子 + 持有者周身环绕光点。逻辑在 `combat/SanctuaryManager.java`（脉冲计时按维度 gameTime，首次举盾起算 2 秒；`LivingEntityMixin` tick 分发）。

**获取**：主类订阅 `LootTableEvents.MODIFY`（fabric-loot-api-v3）向 `chests/trial_chambers/reward` 与 `reward_rare` **事件追加而非整表覆盖**：90% 空 / 7% 庇护书（固定 1 级）/ 3% 带 1~3 级庇护的盾牌 —— 不丢原版奖励。不加入任何获取标签。

#### 23. 坚壁 Aegis（III，rare）

格挡中受到**不可格挡类伤害**（`#minecraft:bypasses_shield` 标签：音波 / 魔法等 —— 原版格挡对这类伤害完全无效，`BlocksAttacks#bypassedBy` 直接放行）时按等级减免 **30/45/60%**（`AEGIS_REDUCTION` 查表）。挂 `hurtServer` HEAD（`isBlocking` + `getItemBlockingWith` 读等级 + 伤害标签判定）。追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

### 24. 归羽 (Homing Plume)

**功能**：射出的箭 / 弩箭未命中任何实体（插地方或落空）时，按 50% / 100% 概率在 1 秒后自动飞回背包（药箭同样返还）；命中实体不返还。**与无限互斥**（无限 = 不耗箭但禁药箭，归羽 = 省箭 + 保留药箭的经济取舍）。命中 40 格外目标授予隐藏挑战「百步穿杨」。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/homing_plume.json`：

- `max_level: 2`，`weight: 5`（uncommon），`anvil_cost: 4`
- `supported_items/primary_items: "#extra-enchantry-short:homing_plume_supported"`（自定义标签：`#minecraft:enchantable/bow` + `#minecraft:enchantable/crossbow`），`slots: ["mainhand"]`
- `exclusive_set: ["minecraft:infinity"]`（列表写法），`effects: {}` 纯 Mixin

**核心逻辑** `mixin/ProjectileWeaponItemMixin.java` + `mixin/AbstractArrowMixin.java` + `combat/HomingPlumeManager.java`：

- 发射快照：26.2 反编译确认弓与弩的箭矢都经 `ProjectileWeaponItem#createProjectile(Level, LivingEntity, ItemStack weapon, ItemStack projectile, boolean)` 生成（弩的重写仅处理烟花，箭矢走 super），RETURN 处把武器归羽等级写入箭实体（`access/HomingPlumeAccess` duck 接口）—— 爆炸时射手可能已换武器，必须快照
- 返还判定：`AbstractArrow#onHitEntity` HEAD 置命中标记（并做 40 格测距挑战）；`onHitBlock` RETURN 处 —— 无命中标记、拾取态非 DISALLOWED、发射者为 ServerPlayer 时按概率（I 50% / II 100%）登记延迟任务
- 延迟返还：`HomingPlumeManager` 走 `ServerTickEvents.END_SERVER_TICK`，20 tick 后箭仍存在（未被手动捡走）→ `getPickupItem()`（protected，`@Invoker` 透传）进背包（满了掉脚下）+ 拾取音 + `discard()`

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。落空箭矢飞回授予「倦鸟归林」成就。

### 25. 坠星 (Starfall)

**功能**：装填烟花火箭发射时，爆炸基础伤害 5→9、爆炸半径 5→6 格（距离平方阈值 25→36 同步缩放，衰减公式同构），爆炸粒子升级为星形散射（末地烛 + 烟花混合）。**与多重射击互斥**（烟花流 vs 散弹流二选一）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/starfall.json`：

- `max_level: 1`，`weight: 2`（rare），`anvil_cost: 4`
- `supported_items/primary_items: "#minecraft:enchantable/crossbow"`，`slots: ["mainhand"]`
- `exclusive_set: ["minecraft:multishot"]`，不加入任何获取标签 → 全途径隔离，仅末地城专属掉落池（与御风同方案）

**核心逻辑** `mixin/CrossbowItemMixin.java` + `mixin/FireworkRocketEntityMixin.java`：

- 发射快照：26.2 反编译确认弩发射烟花走 `CrossbowItem#createProjectile` 烟花分支（`new FireworkRocketEntity(level, 烟花栈, 射手, ...)`），RETURN 处给火箭打坠星标记（`access/StarfallAccess` duck 接口）—— 爆炸时射手可能已换武器，必须快照
- 爆炸增强：`dealExplosionDamage(ServerLevel)`（private）内三组常量 `@ModifyConstant` **同步**放大 —— 基础伤害 5.0f→9.0f、半径 5.0d→6.0d、距离平方阈值 25.0d→36.0d（衰减公式随半径同构缩放）
- 星形粒子：`explode` TAIL 按三正交轴 + 体对角线 14 束 `END_ROD` + 中心 `FIREWORK` 散射

**获取途径（专属掉落表）**：`chests/end_city_treasure.json` 第四池（roll 1）：75% 空 / 15% 坠星附魔书 / 10% 带坠星的弩（`set_damage 0.8-1.0`）。坠星强化烟花炸出星火授予「火树银花」成就。

### 26. 霆霓 (Stormsurge)

**功能**：雨天 / 雷雨天 / 目标在水中时，掷出的三叉戟命中额外 +2 / +4 伤害，并连锁至 2 格内最近 1 个其他实体（连锁伤害减半）；命中点播放引雷同款雷声与电弧粒子（无真实闪电、不引燃）。仅投掷触发，近战戳刺不生效。**与引雷互斥**。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/stormsurge.json`：

- `max_level: 2`，`weight: 5`（uncommon），`anvil_cost: 4`
- `supported_items/primary_items: "#minecraft:enchantable/trident"`，`slots: ["mainhand"]`
- `exclusive_set: ["minecraft:channeling"]`，`effects: {}` 纯 Mixin

**核心逻辑** `combat/StormsurgeManager.java`（挂 `LivingEntityMixin` hurtServer HEAD `@ModifyVariable`，定义在冲阵之后、断罪之前 —— 加伤参与断罪斩杀结算）：

- 武器取 `source.getWeaponItem()`（掷出三叉戟返回三叉戟本身，同蚀命），`!source.isDirect()` 限定投掷
- 环境门：`level.isRaining()` 或目标 `isInWater()`（26.2 已无 `isInWaterRainOrBubble`，拆分判定）
- 连锁：主目标碰撞箱外扩 2 格取最近 1 个（排除攻击者 / 主目标 / 友方 / 死者），共用主目标 DamageSource + ThreadLocal 短路防递归（破阵同款模式）。闪电链命中第二个目标授予「雷霆万钧」成就

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。

### 27. 藏锋 (Sheathed Edge)

**功能**：脱离战斗（未造成且未承受任何伤害）满 5 秒后，首次近战命中额外 +2 / +4 / +6 伤害，伴随拔刀音效与刀光粒子；触发后重新计时。与断罪不互斥 —— 藏锋加伤参与断罪斩杀阈值结算（与冲阵同一设计逻辑），受壁垒上限克制。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/sheathed_edge.json`：

- `max_level: 3`，`weight: 2`（rare），`anvil_cost: 4`
- `supported_items/primary_items: "#minecraft:enchantable/sharp_weapon"`（原版标签 = 近战武器 + 斧，与锋利同适用范围），`slots: ["mainhand"]`，`effects: {}` 纯 Mixin

**核心逻辑** `combat/SheathedEdgeManager.java`（挂 `LivingEntityMixin` hurtServer）：

- 计时：每生物 UUID 记录最近参与战斗时间戳（wall-clock，与断罪冷却同风格）；`hurtServer` RETURN 伤害生效后受害者与攻击者双记账；无记录视为就绪（开局第一刀即拔刀斩）
- 加伤：HEAD `@ModifyVariable`（argsOnly），定义在霆霓之后、断罪之前，就绪则 +2/+4/+6 并立刻重新计时

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。脱战五秒后首刀授予「拔刀斩」成就。

### 28. 渊息 (Tideheart)

**功能**：水下呼吸时间每级 +15 秒（I/II/III 级 → 总氧气 30/45/60 秒），III 级时水下挖掘不再减速（等效水下速掘）。**与水下呼吸、水下速掘互斥**。宝藏附魔（无附魔台）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/tideheart.json`：

- `max_level: 3`，`weight: 2`（rare），`anvil_cost: 4`
- `supported_items/primary_items: "#minecraft:enchantable/head_armor"`，`slots: ["head"]`
- `exclusive_set: ["minecraft:respiration", "minecraft:aqua_affinity"]`（列表写法）

**核心逻辑** `mixin/EntityMixin.java` + `mixin/LivingEntityMixin.java`：

- 氧气上限：26.2 反编译确认 `Entity#getMaxAirSupply()` 硬编码返回 300（15 秒），且 `increaseAirSupply` 以它为钳制上限 —— HEAD 注入按头盔渊息等级返回 `300 + 300×level`，消耗 / 换气回满 / 客户端气泡 HUD 全部自动跟随。**构造期防护**：`Entity#<init>` 的 defineSyncker 在定义 `DATA_AIR_SUPPLY_ID` 初值时就会回调本方法（此时装备字段尚未初始化，`getItemBySlot` 必 NPE）—— 经 `access/EquipmentReady` duck 标记（`LivingEntity#<init>` TAIL 置位）判空放行原版上限
- III 级水下免减速：26.2 的水下挖掘惩罚是 `Player#getDestroySpeed` 里的 `Attributes.SUBMERGED_MINING_SPEED` 属性乘算（基础值 0.2），tick 内写入 +0.8 瞬态修改器即恢复 1.0（与活力 / 疾风同一模式，双端执行）

**获取途径**：追加进 `minecraft:treasure`（挡附魔台）；另订阅 `LootTableEvents.MODIFY` 向沉船三类 / 埋藏的宝藏 / 海底废墟 / 钓鱼宝藏**事件追加**专属池（85% 空 / 15% I~III 级附魔书），庇护同款事件追加法，不进 `on_random_loot` 通用随机池。佩戴渊息在水中呼吸授予「深海呼吸」成就；头戴渊息讨伐远古守卫者授予隐藏挑战「深渊回响」。

### 29. 丰壤 (Loam)

**功能**：收获完全成熟的作物时 20% / 35% / 50% 概率双倍掉落（复制一份含时运等加成后的完整掉落，含种子）；III 级额外 3×3 范围收获 —— 仅破坏同种且已成熟的作物，未成熟不动，每格消耗 1 点耐久，各格独立走双倍判定。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/loam.json`：

- `max_level: 3`，`weight: 5`（uncommon），`anvil_cost: 2`
- `supported_items/primary_items: "#minecraft:hoes"`，`slots: ["mainhand"]`，`effects: {}` 纯 Mixin

**核心逻辑** `mixin/BlockMixin.java` + `combat/LoamManager.java`：

- 注入点：26.2 反编译确认玩家破坏结算集中在 `Block#playerDestroy(Level, Player, BlockPos, BlockState, BlockEntity, ItemStack)`，RETURN 处判定 `CropBlock.isMaxAge(state)`（public final）+ 锄头丰壤等级
- 双倍掉落：`Block.getDrops(...)` 重算一份掉落弹出（第六参 26.2 已改为 `ItemInstance` 新接口，`ItemStack` 直接实现之，原样传入）
- 3×3 范围：邻格走 `ServerLevel#destroyBlock` 原版流程（各自触发双倍判定），ThreadLocal 深度标记防连锁扩散；隐藏挑战「丰收之神的赞许」= 3 秒窗口内连锁收获累计 64 株

**获取途径**：追加进 `minecraft:non_treasure`（附魔台 / 铁砧打书）。丰壤触发双倍收获授予「丰收时刻」成就。

### 配饰八附魔 (Accessory Enchantments)（30~37）

全部 `max_level: 2`、非宝藏（附魔台 / 铁砧打书），`slots: ["any"]`，`supported_items` 为各槽位自定义物品标签（`#extra-enchantry-short:accessory/{earring|necklace|ring|bracelet}`）。仅对玩家生效，行为集中在 `accessory/AccessoryManager`：属性类 tick 值变化才写瞬态修改器（活力同模式）；受伤 / 造成伤害由 `LivingEntityMixin` hurtServer 侧调静态入口（victim 侧 `@ModifyVariable`）；击杀类挂 `ServerLivingEntityEvents.AFTER_DEATH`；自然恢复用自有计时器（零注入原版回血分支）。多件减伤类线性叠加，总减免统一钳制（至少保留 5% 伤害）。

#### 30. 魂铃 Soul Chime（II，耳环）

击杀**敌对生物**（`Enemy` 接口，`AFTER_DEATH` 事件）回复 **2 饥饿 + level 点饱和**（`FoodData#eat(2×level, level)`）。

#### 31. 盾坠 Shield Pendant（II，耳环）

受到的**全部伤害 -3%/级**（多件线性叠加；总减免统一钳制，至少保留 5% 伤害）。

#### 32. 雷鸣扣 Thunder Clasp（II，项链）

**雷雨天气**（`isThundering`）造成的伤害 **+4%/级**（多件线性叠加）。实现为 `LivingEntityMixin` hurtServer HEAD 的 `@ModifyVariable`，从 `source.getEntity()` 解析攻击者并排除自伤 —— **`hurtServer` 的 `this` 永远是受害者**，不能把受害者当攻击者。

#### 33. 翠滴 Verdant Drop（II，项链）

自然恢复 **+10%/级** —— 不注入原版回血分支：自有计时器把 80 tick 基线间隔缩短为 `80/(1+加成)`（下限 40 tick），`food ≥ 18` 且未满血时每跳 heal 1 HP。

#### 34. 刃戒 Blade Ring（II，戒指）

攻击速度 **+5%/级**（`ATTACK_SPEED` 瞬态修改器 `ADD_MULTIPLIED_BASE`，值变化才写）。

#### 35. 羽环 Plume Ring（II，戒指）

受到的**弹射物伤害**（`#minecraft:is_projectile`）**-6%/级**（多件线性叠加）。

#### 36. 烬镯 Ember Bracelet（II，手镯）

受到的**火焰伤害**（`#minecraft:is_fire`）**-10%/级**。烬镯属火焰家族（`#extra-enchantry-short:family_fire`，成员还有炽焰行者 / 余烬 / 劫后余辉）：带火焰家族附魔的物品与镶嵌烬心石的配饰作为掉落物**免疫火焰 / 岩浆烧毁**（「烬火不侵」，`mixin/ItemEntityMixin.java` hurtServer HEAD 取消，与下界合金同款；动态判定，砂轮磨掉附魔即失效）。

#### 37. 潮镯 Tide Bracelet（II，手镯）

游泳效率 **+8%/级**（`WATER_MOVEMENT_EFFICIENCY` 瞬态修改器）。

### 狼铠三附魔 (Wolf Armor Enchantments)（38~40）

`slots: ["body"]`，`supported_items: #extra-enchantry-short:wolf_armor_supported`（仅 `minecraft:wolf_armor`）。行为集中在 `combat/WolfArmorManager`（挂 `LivingEntityMixin` tick HEAD 的 `instanceof Wolf` 分支，活力同款瞬态属性修改器，卸甲自动移除全部修改器）。狼铠无 `ENCHANTABLE` 组件（同马铠），**附魔台必然不可用，铁砧上书是生存唯一获取途径**；三枚均追加进 `minecraft:non_treasure`（附魔台对书出书 → 铁砧上书）。

#### 38. 锐牙 Sharp Fang（III，狼铠）

狼近战伤害 **+10%/级**（`ATTACK_DAMAGE` 瞬态修改器）。

#### 39. 哨戒 Vigil（II，狼铠）

狼索敌 / 跟随范围 **+25%/级**（`FOLLOW_RANGE` 瞬态修改器）。

#### 40. 回春 Renewal（II，狼铠）

狼每 **4 秒**（80 tick，服务器全局 gameTime 节拍，无需每狼计时器）回复 **1 HP/级**，仅存活且未满血时生效。

### 41. 远镯 (Reach Bracelet)

**功能**：2 级，手镯专属。实体 / 方块交互距离各 +0.5 格/级（多只叠加）；修改器与武器「触及」相互独立，**两者天然叠加**。非宝藏（附魔台对配饰或书 / 铁砧上书）。

#### 实现方法

**数据定义** `data/extra-enchantry-short/enchantment/reach_bracelet.json`：

- `max_level: 2`，`weight: 5`（uncommon），`anvil_cost: 4`
- `supported_items/primary_items: #extra-enchantry-short:accessory/bracelet`，`slots: ["any"]`
- `effects: {}` —— 手镯不在原版装备槽，数据驱动 `attributes` 效果不适用，由 `AccessoryManager` 结算

**核心逻辑** `accessory/AccessoryManager.java`（tick 结算，活力同模式）：

- 汇总 4 个配饰槽手镯的远镯总等级 × 0.5，向 `Attributes.ENTITY_INTERACTION_RANGE` 与 `Attributes.BLOCK_INTERACTION_RANGE` 写入 / 移除瞬态修改器（`ADD_VALUE`，id `extra-enchantry-short:accessory_reach_entity|block`）
- 修改器 id 独立于触及的数据驱动效果（`extra-enchantry-short:enchantment.reach/*`）→ 同属性不同 id 加法叠加，互不覆盖；值变化才写，卸下自动移除

**获取途径**：追加进 `minecraft:non_treasure`（附魔台对配饰或书 / 铁砧上书）。

## 版本主题

### 1.0.2「远镯与配方书」 (Reach Bracelet & Recipe Book)

内容扩展与体验修复版本：新增第 41 个附魔「远镯」，补齐全部配方的配方书解锁链路，并核实凋灵掉落文档。

#### 远镯（手镯触及，编号 41）

2 级手镯专属附魔：实体 / 方块交互距离各 +0.5 格/级（多只叠加）。修改器 id（`accessory_reach_entity|block`）与武器「触及」的数据驱动效果（`enchantment.reach/*`）相互独立 —— **同属性加法叠加、互不覆盖**。手镯不在原版装备槽，数据驱动 `attributes` 效果不适用，由 `AccessoryManager` tick 结算（活力同模式）；非宝藏（附魔台对配饰或书 / 铁砧上书）。实现细节见[远镯](#41-远镯-reach-bracelet)。

#### 配方书解锁（40 个配方解锁成就）

修复「模组物品不出现在配方书」：原版配方书只显示**已解锁**的配方，本模 40 个配方（8 宝石 + 32 配饰）此前均无解锁成就 —— 物品可合成却永不入书。按原版 `RecipeProvider` 同构补齐 `advancement/recipes/`：材料 `inventory_changed` + `recipe_unlocked` 双 criteria、单组 OR `requirements`（与 criteria 键完全一致，`AdvancementSchemaTest` 门禁覆盖）—— 获得任一合成材料即解锁对应条目。

#### 凋灵掉落核实（下界之星无忧）

反汇编 26.2 原版 jar 确认：原版凋灵掉落表（`entities/wither.json`）本身**无任何池**，下界之星由 `WitherBoss#dropCustomDeathLoot` **硬编码掉落** —— 本模对该表的整表覆盖不影响下界之星，此前「覆盖可能丢失下界之星」的疑虑为误判（见[拓阶](#4-拓阶-tier-break)）。

### 1.0.1「独立移植」 (Independent Port)

从完整版 `extra-enchantry` 独立移植的「只附魔」版本：剥离八元素家族 / 共鸣 / 试炼 / 领主 / 幽渊等玩法线，**完整保留 40 个附魔与配饰宝石体系**，代码按分层结构重写。modid 由 `extra-enchantry` 迁移为 `extra-enchantry-short`，Java 包名迁移为 `realmikoto.extraenchantryshort`，Mixin 注入前缀迁移为 `extraenchantryshort$`，数据包 / 资源文件按新命名空间迁移。

#### 配饰栏（耳环 / 项链 / 戒指 / 手镯，集成原版背包）

- **数据层**：`accessory/AccessoryAttachments` —— Fabric Data Attachment（`extra-enchantry-short:accessories`，persistent，**刻意非 copyOnDeath**）：4 槽 ItemStack 快照，死亡掉落 / 誓约保留由 `AccessoryManager` 显式处理。**注册时序陷阱**：Attachment 注册在静态块时，方法引用不会触发 `<clinit>`（JLS 惰性解析）—— 玩家登录反序列化 NBT 时类型未注册会被**静默丢弃**（背包清空级事故），故主入口 `onInitialize` 最顶部显式调用 `AccessoryAttachments.register()`
- **菜单层**：`mixin/InventoryMenuMixin` 在 `InventoryMenu` 构造尾部追加 4 个 `AccessorySlot`（下标 46~49），内容由 `AccessoryContainer` 承接，走 containerId 0 原生同步 —— **生存背包与创造玩家页签直接可用，无需独立 GUI / 按键 / 网络包**；`quickMoveStack` HEAD 拦截实现 shift 智能移动（Mixin 继承 `AbstractContainerMenu` 以访问 protected 成员）
- **存储语义**：Fabric Data Attachment 是快照语义 —— `getAttached` 返回落盘快照，原地修改其中的 ItemStack 不会自动保存，所有写路径收敛到 `AccessoryContainer.setItem → setAttached` 回写
- **生存界面**：`client/mixin/InventoryScreenMixin` 在护甲左侧绘制配饰列（x=-11，y=8/26/44/62），护甲行上方展开按钮（盾牌列 x=77、头盔行 y=8，与盾牌同列、与头盔平齐）；点击滑出 / 收起，动画由 `AccessoryHudState` 状态机驱动（约 170ms ease-out，收起 ×1.4 略快），断线重置防跨世界残留。自绘元素左缘必须 ≥76（生存背包纸娃娃渲染区 x=26~75，压入即遮挡玩家模型 1~2px；且纯色衬底与面板纹理有色阶差，能不用就不用）
- **创造界面**：`client/mixin/CreativeModeInventoryScreenMixin` + `client/mixin/SlotMixin` —— 26.2 创造页签的槽位列表不可替换（点击槽被硬转 `SlotWrapper`，插入自定义条目会 ClassCastException），重定位只能**原地改坐标**：向 `Slot` 基类织入 `@Shadow @Final @Mutable x/y` 改写接口（`SlotReposition`），副手槽下移与胸甲平齐、配饰 2×2 出现在装备栏左侧、按钮与盾牌列让位滑到面板左缘
- **右键快捷穿戴**：`AccessoryItem` 右键穿戴（占用则交换回主手）+ 材质对应装备音效 + 首穿成就「环佩琳琅」

#### 配饰与宝石

**16 件配饰** = 4 材质 × 4 槽位（`{copper,iron,gold,diamond}_{earring,necklace,ring,bracelet}`）。**材质只影响宝石被动传导率**：铜 ×1.0 / 铁 ×1.25 / 金 ×1.5 / 钻 ×2.0。

**8 种宝石**（槽位配对固定，`socketed_gem` 组件**合成时决定、不可事后更换**；每槽至多 1 条、全身至多 4 条被动）：

| 宝石                | 槽位 | 基数被动              | 铜 | 铁 | 金 | 钻 |
| ------------------- | ---- | --------------------- | ---- | ----- | ----- | ----- |
| 刃晶 blade_shard      | 戒指 | 攻速 +2%             | 2%   | 2.5%  | 3%    | 4%    |
| 风羽晶 wind_feather    | 戒指 | 弹射物伤害 -3%          | 3%   | 3.75% | 4.5%  | 6%    |
| 魂珀 soul_amber       | 耳环 | 击杀敌对 +5 经验          | 5    | 6     | 8     | 10    |
| 盾纹玉 guard_jade      | 耳环 | 受到伤害 -2%           | 2%   | 2.5%  | 3%    | 4%    |
| 雷光石 storm_stone     | 项链 | 雷雨时移速 +3%          | 3%   | 3.75% | 4.5%  | 6%    |
| 萌芽晶 sprout_crystal  | 项链 | 自然恢复 +5%           | 5%   | 6.25% | 7.5%  | 10%   |
| 烬心石 ember_heart     | 手镯 | 火焰伤害 -4%           | 4%   | 5%    | 6%    | 8%    |
| 潮汐珠 tide_pearl      | 手镯 | 游泳效率 +5%           | 5%   | 6.25% | 7.5%  | 10%   |

- 镶宝石配饰触发 `fx/GemFx`：穿戴 L2 爆发 + 常驻 L1 环境粒子（每 40 tick 一粒，逐宝石节流）；音高按材质分档（铜铁 1.0 / 金 1.1 / 钻 1.2）
- 烬心石兼任「火焰家族凭证」：镶嵌它的配饰免火烧毁（见[配饰八附魔](#配饰八附魔-accessory-enchantments3037)）

#### 配方

`data/extra-enchantry-short/recipe/` 共 **40** 个，全部为无序合成：

- **8 个宝石配方**（基础材料 + 盔甲纹饰锻造模板）：刃晶 = 石英 + 恼鬼 / 烬心石 = 石英 + 原初 / 盾纹玉 = 铁锭 + 幽寂 / 魂珀 = 紫水晶碎片 + 沉静 / 萌芽晶 = 绿宝石 + 野性 / 雷光石 = 青金石 + 闪电 / 潮汐珠 = 海晶碎片 + 潮汐 / 风羽晶 = 钻石 + 流光
- **32 个配饰配方**：材质底材 ×2 + 线 + 对应槽位宝石，result 携带 `extra-enchantry-short:socketed_gem` 组件（每件配饰 2 个宝石变体）
- 全部配方配有解锁成就（`advancement/recipes/`，原版 `RecipeProvider` 同构：材料 `inventory_changed` + `recipe_unlocked` 双 criteria、单组 OR `requirements`）—— 获得任一合成材料即解锁配方书条目，可在合成书 / 配方书中检索

## 更新日志

完整版本主题（含设计动机与实现细节）见[版本主题](#版本主题)。

| 版本 | 主题 | 摘要 |
| ----- | ----------- | ---------------------------------------- |
| 1.0.2 | 远镯与配方书 | 新增远镯（手镯触及，可与触及叠加）；40 个配方补齐配方书解锁成就；核实凋灵掉落（下界之星为代码硬编码掉落） |
| 1.0.1 | 独立移植 | modid / 包名 / Mixin 前缀迁移；代码分层重写；40 附魔 + 配饰与宝石 + 成就体系全量保留；AGPL-3.0-or-later |

## 通用技术模式

### 项目结构

```
src/main/java/realmikoto/extraenchantryshort/
├── ExtraEnchantryShort.java       主入口：MOD_ID / id() / 事件装配（Attachment 早注册 / 服务端 tick / AFTER_DEATH / 连接与停服清理 / ALLOW_ENCHANTING / LootTableEvents.MODIFY）
├── registry/                      注册中心
│   ├── ModEnchantments.java       41 附魔 ResourceKey + 等级查询 + 数值规则表
│   ├── ModEffects.java            自定义状态效果（余辉 / 余烬，纯计时视觉载体）
│   ├── ModDataComponents.java     自定义数据组件（limit_break_source 破限书来源标记）
│   └── ModCreativeTabs.java       创造物品栏页签（41 附魔整级书 + 24 饰品）
├── accessory/                     饰品与宝石体系
│   ├── Accessories.java           槽位 / 材质 / 宝石注册中心与查询 API（socketed_gem 组件在此注册）
│   ├── AccessoryItem / GemItem    配饰与宝石物品
│   ├── AccessorySlot / AccessoryContainer / AccessoryAttachments   槽位 / 容器 / Attachment 持久化
│   └── AccessoryManager.java      配饰结算中心（属性 / 伤免 / 击杀 / 死亡链路）
├── combat/                        机制管理器（每附魔一个）
│   ├── AfterglowManager           BulwarkManager      CleaveManager
│   ├── DecoyManager               EmberfallManager    HomingPlumeManager
│   ├── JudgementManager           LimitBreakManager   LoamManager
│   ├── OathboundManager           SanctuaryManager    SheathedEdgeManager
│   ├── ShieldChargeManager        StormsurgeManager   WolfArmorManager
├── rules/TierBreakRules.java      拓阶挖掘等级规则（双 Mixin 共用）
├── fx/                            FxHelper（粒子/音效/节流）+ GemFx（宝石反馈）
├── advancement/Advancements.java  成就授予统一入口
├── entity/DecoyEntity.java        假象诱饵（复用原版盔甲架）
├── access/                        Mixin duck 接口（EquipmentReady / HomingPlumeAccess / StarfallAccess）
└── mixin/                         主 Mixin（21 个，extra-enchantry-short.mixins.json）

src/client/java/realmikoto/extraenchantryshort/client/
├── ExtraEnchantryShortClient.java 客户端入口（断线重置 AccessoryHudState）
├── AccessoryColumnRenderer / AccessoryHudState / SlotReposition   配饰栏自绘（列背景 / 幽灵图标 / 动画帧 + 展开状态 + 槽位坐标重写接口）
└── mixin/                         客户端 Mixin（6 个，extra-enchantry-short.client.mixins.json）

src/main/resources/data/            数据包（enchantment / tags / loot_table / recipe / advancement）
src/main/resources/assets/extra-enchantry-short/   资源（lang / items / models / textures / atlases/gui.json）
src/test/java/realmikoto/extraenchantryshort/      4 组单元测试（纯 Java，不依赖 MC 运行时）
```

### 代码注册约定

每个附魔在 `registry/ModEnchantments.java` 注册 ResourceKey（数据驱动附魔是动态注册表，无法静态引用）：

```
public static final ResourceKey<Enchantment> REACH =
        ResourceKey.create(Registries.ENCHANTMENT, id("reach"));
```

物品上的等级读取统一用 `ModEnchantments.level(stack, KEY)` / `getXxxLevel(stack)`（遍历 `stack.getEnchantments()` 匹配 `holder.is(KEY)` 后取等级）。跨 Mixin 读取私有状态用 duck 接口（`access/` 下 `EquipmentReady` / `HomingPlumeAccess` / `StarfallAccess`）。**新增附魔时**：注册 Key → 建 JSON → 按需加获取标签 → `ModCreativeTabs.ENCHANTMENTS` 列表追加。

### 创造模式物品栏

`registry/ModCreativeTabs.java` 将本 mod 全部附魔书单列一页（「额外附魔书」）：

- `displayItems` 回调在打开物品栏时执行，此时数据包已加载：经 `Registries.ENCHANTMENT` 的 Holder 查询取等级
- 附魔书构造：`Items.ENCHANTED_BOOK` + `DataComponents.STORED_ENCHANTMENTS`；同页提供 24 件饰品（16 配饰 + 8 宝石）
- **新增附魔时**：在 `ENCHANTMENTS` 列表追加 `(KEY, 最大等级)` 即自动列出全部等级的附魔书

### 视听反馈（FxHelper）

所有粒子 / 音效统一走 `fx/FxHelper`，不在业务代码里裸写 `sendParticles` / `playSound`：

- 封装：`burst`（实体中心爆发）/ `burstAt`（定点）/ `ring` / `ringAt`（环，每 4 粒一锚点包）/ `trail`（两点间排点）/ `play` / `playAt`（音效，`SoundEvent` 与 `Holder<SoundEvent>` 双重载 —— 26.2 的 `SoundEvents` 常量两种类型并存，调用前确认字段类型）/ `pitchForLevel`（等级变调 1.0 + 0.1/级）/ `throttle`（按 UUID + key 节流，gameTime 计量、惰性淘汰）/ `clearThrottle`
- **分级约定**：L1 触发确认（≤2 粒/秒，短促）/ L2 持续氛围（3~16 粒 + 1 音，**必须节流**）/ L3 高光时刻（20~40 粒，免死、处决等稀有事件才允许大场面）。常态生效的效果（如触及）只给粒子不配音效，防吵
- 宝石专用反馈在 `fx/GemFx`：`onEquip`（穿戴 L2 爆发，音高按材质分档）/ `ambient`（常驻 L1 环境粒子，逐宝石节流）/ `playChime`
- 客户端独占反馈（如空跃振翅音）放 client source set 的 Mixin，服务端不可见的实体状态别往服务端发

### 成就体系

两棵成就树（`data/extra-enchantry-short/advancement/`：usage 实战 / hidden_challenges 隐秘挑战）。**所有 criteria 均为 `minecraft:impossible`**（仅 usage/root 为 `minecraft:tick`）—— 全部由效果代码调用 `Advancements.award()` 授予（幂等）：

- **usage/**（实战树，14 个）：JSON 用 `minecraft:impossible` 触发器（任何游戏事件都无法自然完成），由效果代码在生效处调 `Advancements.award(ServerPlayer, KEY)`；criterion 名统一 `triggered`；ResourceKey 常量在 `advancement/Advancements.java` 集中声明。**新增附魔时**：有「首次成功使用」纪念价值的，加一个 usage 成就 + 一句 award 调用
- **hidden_challenges/**（隐秘挑战，7 个）：challenge 帧 + announce_to_chat，授予经 `Advancements.awardHidden(name)` 在各 Manager 内；「雷霆之礼」经 `LIMIT_BREAK_SOURCE` 数据组件区分破限书来源
- 代码授予的查找方式（26.2）：成就**不是**注册表，`server.getAdvancements().get(key.identifier())` 拿 `AdvancementHolder` 再 award；**成就 ID 含子目录路径**（`usage/foo.json` → `ns:usage/foo`），parent 引用同样必须带前缀，否则静默返回 null
- **实战成就**：环佩琳琅（首穿配饰）、劫后余辉、铜墙铁壁（壁垒挡 ≥4 点）、冲锋陷阵、金蝉脱壳、深海呼吸、拔刀斩、余烬不灭、处刑者、火树银花、丰收时刻、倦鸟归林、雷霆万钧、基石崩解（challenge 帧）
- **隐藏挑战**：深渊回响（水下佩渊息杀远古守卫者）、死而不僵（余辉锁血反杀）、丰收之神的赞许（3 秒连锁 64 株）、百步穿杨（归羽 40 格命中）、以彼之道（冲阵放倒劫掠兽）、极限之证（获得破限书）、雷霆之礼（闪电苦力怕代杀来源破限书）

### Mixin 清单

主 Mixin（`extra-enchantry-short.mixins.json`，21 个，compatibilityLevel JAVA_25）：

| Mixin                    | 目标                      | 作用 |
| ------------------------ | ----------------------- | --- |
| LivingEntityMixin        | `LivingEntity`          | tick 统一分发（活力 / 庇护 / 足下粒子 / 疾风 / 渊息 / 狼铠）；hurtServer 注入处理配饰伤免、雷鸣扣、假象、蚀命、冲阵 / 霆霓 / 藏锋、坚壁、断罪、汲取、破阵、触及剑气；凋零缩短；破限保护上限；无踪索敌；御风耐久；余烬免死；壁垒钳制；誓约经验球跳过 |
| AnvilMenuMixin           | `AnvilMenu`             | 破限：铁砧融合带破限输入时无视「过于昂贵」+ 费用固定 10 级 |
| PlayerMixin              | `Player`                | 锁血（余辉 / 余烬）取消 `actuallyHurt`；誓约掉落提取 / 回插；配饰死亡掉落；`addItem` 破限书检测 |
| ServerPlayerMixin        | `ServerPlayer`          | 誓约重生搬运（四件套连带经验分数）；配饰重生回插 |
| EntityMixin              | `Entity`                | 无踪 I 屏蔽脚步声 / 震动 + 落地震动；渊息放大氧气上限（EquipmentReady 防构造期 NPE） |
| ItemStackMixin           | `ItemStack`             | 拓阶挖掘等级 / 速度（含基岩判定） |
| BlockBehaviourMixin      | `BlockBehaviour`        | 拓阶基岩可破坏 |
| BlockMixin               | `Block`                 | 丰壤双倍掉落 / 3×3 范围收获 + 拓阶特效 + 授「基石崩解」 |
| EnchantmentHelperMixin   | `EnchantmentHelper`     | 破限附魔台互斥无视（ThreadLocal 传物品） |
| EnchantRandomlyFunctionMixin | `EnchantRandomlyFunction` | 随机附魔等级钳制（触及 / 壁垒 1 级、疾风 ≤2 级） |
| GrindstoneMenuMixin      | `GrindstoneMenu`        | 破限砂轮防移除 |
| BlocksAttacksMixin       | `BlocksAttacks`         | 不屈：II 免疫破盾、I 破盾时长减半 + 抗性、II 格挡耐久 ×2 |
| DeathProtectionMixin     | `DeathProtection`       | 劫后余辉：图腾生效时读图腾副本附魔触发 |
| InventoryMixin           | `Inventory`             | `/give` 路径破限书检测兜底 |
| InventoryMenuMixin       | `InventoryMenu`         | 追加 4 个配饰槽（46~49）+ quickMoveStack |
| MobMixin                 | `Mob`                   | `setTarget` 裁决：假象仇恨重定向 |
| ItemEntityMixin          | `ItemEntity`            | 火焰家族附魔 / 烬心石配饰掉落物免烧毁（烬火不侵） |
| FireworkRocketEntityMixin | `FireworkRocketEntity` | 御风烟花推进放大 + 坠星爆炸常量修改与星形粒子 |
| AbstractArrowMixin       | `AbstractArrow`         | 归羽命中标记与延迟返还 + 百步穿杨 |
| ProjectileWeaponItemMixin | `ProjectileWeaponItem` | 归羽等级快照写入箭实体 |
| CrossbowItemMixin        | `CrossbowItem`          | 弩射烟花打坠星标记 |

客户端 Mixin（`extra-enchantry-short.client.mixins.json`，6 个）：`HudMixin`（活力 HUD）、`LocalPlayerMixin`（空跃多段跳）、`InventoryScreenMixin`（生存配饰列）、`CreativeModeInventoryScreenMixin`（创造 2×2 集成）、`AbstractContainerScreenMixin`（按钮命中）、`SlotMixin`（坐标重写 duck）。

### 踩坑记录（26.2）

- `@Inject` 有返回值的方法必须用 `CallbackInfoReturnable`，否则类加载时崩溃（懒加载，主菜单不报错进世界才炸）
- `@Redirect` 处理器参数顺序：被重定向调用参数在前、外围方法参数在后；对**虚方法调用**的处理器必须显式带上接收者参数，静态调用不需要
- **Redirect 是独占注入**：fabric-api 自身的 Mixin 已占用的调用点不能再 Redirect（如 fabric-item-api 对 `AnvilMenu.createResult` 中 `canEnchant` 的 Redirect），冲突即 Critical injection failure —— 改用官方事件（`EnchantmentEvents.ALLOW_ENCHANTING`，破限跨部位摔落保护即此实现）
- `@Shadow` / `@Invoker` 只在目标类本类解析，**父类字段与方法 shadow 不到**：需要上下文时改走 Fabric 事件、Mixin 继承目标类父类（`InventoryMenuMixin extends AbstractContainerMenu` 访问 protected 的 `addSlot` / `moveItemStackTo`）或 `@Invoker` 透传（归羽的 `getPickupItem`）；跨 Mixin 读取状态用 duck 接口 + `instanceof` cast，不要直接调另一 Mixin 的 `@Unique` 方法
- 附魔 JSON 的 `supported_items` / `primary_items` **列表内不能写 `#tag` 引用**（数组元素按纯 Identifier 解析，`#` 开头报 `Not a valid resource location` 导致注册表加载崩溃）；聚合多个标签必须建自定义物品标签（`data/<ns>/tags/item/xxx.json` 的 `values` 支持 `#tag` 嵌套）再整体引用为单字符串 `"#ns:xxx"`
- `exclusive_set` 支持 `"#tag"`、`["id1","id2"]` 列表、单 `"id"` 三种 JSON 写法（`RegistryCodecs.homogeneousList`；本 mod 三种并存：凋零保护 / 炽焰行者用标签，破阵 / 假象等用列表，劫后余辉 / 御风用单 ID）
- 附魔台槽位准入检查是 `ItemStack#isEnchantable()`（要求物品带 `DataComponents.ENCHANTABLE` 组件），与附魔的 `supported_items` 无关；`AnvilMenu` 不做该检查（只看 `canEnchant` / `supported_items`）—— 不死图腾 / 马铠 / 狼铠的附魔只能走「附魔台出书 → 铁砧上书」
- **限级 cost 曲线不能只盯附魔台的 30**：加入 `non_treasure` 的附魔会经宝箱装备的 `enchant_with_levels`（最高 cost 50）泄级 —— 「最高只能随机到 N 级」的阈值必须按 50 算（疾风 III 级 min_cost 定在 55；触及 / 壁垒 `per_level_above_first: 100` 双保险）
- `Player` **重写了** `actuallyHurt` **且不调用 super**（自己完整实现护甲 / 魔抗 / 吸收结算与 `setHealth`）—— 针对玩家扣血链路的锁血注入必须挂 `Player`；壁垒则注入 `getDamageAfterMagicAbsorb` 方法本身（`Player#actuallyHurt` 对它是虚调用，单点注入同时覆盖玩家与非玩家，还不破坏破限对其内部 `CombatRules` 调用的 Redirect）
- `hurtServer` 的 `this` 永远是受害者：「攻击者侧加伤」必须从 `source.getEntity()` 解析攻击者并排除自伤（雷鸣扣）；「近战限定」用 `source.isDirect()`（冲阵曾因漏判让非近战触发撞击类效果）
- **玩家跳跃输入是客户端权威**：服务端看不到跳跃键状态（`jumping` 字段服务端对玩家不生效），多段跳类效果必须做在客户端（`LocalPlayer#aiStep` 读公开字段 `input.keyPresses.jump()` 做按键沿检测，比读 `jumping` 字段更可靠）
- 不死图腾的触发链路：`checkTotemDeathProtection` copy 图腾栈 → `shrink(1)` → `setHealth(1.0F)` → `DeathProtection#applyEffects(副本, this)`；要在触发时读附魔只能从 `applyEffects` 的栈副本参数拿，且注入点必须晚于死亡效果列表首项 `ClearAllStatusEffectsConsumeEffect`，否则施加的效果被立刻清空
- 自定义免死挂 `checkTotemDeathProtection` 的 **RETURN**（`hurtServer` 的判定是 `if (isDeadOrDying()) { if (!checkTotemDeathProtection(source)) die(source); }`）—— 图腾没救命才轮到自定义免死（余烬）；锁血则复用 `actuallyHurt` HEAD 取消（玩家挂 `Player`、非玩家挂 `LivingEntity`，两处都要）
- **26.2 氧气体系**：上限 = `Entity#getMaxAirSupply()` 硬编码 300（15 秒），`increaseAirSupply` 以它为钳制上限 —— HEAD 注入放大即全链路生效；**实体构造早期钩子陷阱**：`Entity#<init>` 的 defineSyncker 回调时装备字段尚未初始化，`getItemBySlot` 必 NPE（「Couldn't place player in world」级事故）—— 渊息经 `EquipmentReady` duck（`LivingEntity#<init>` TAIL 置位）防护
- 26.2 水下挖掘惩罚是属性不是分支：`Player#getDestroySpeed` 尾部乘 `Attributes.SUBMERGED_MINING_SPEED`（基础 0.2）—— 免减速写 +0.8 瞬态修改器即可（渊息 III）；`isInWaterRainOrBubble()` 已移除，水中 / 雨中需拆成 `isInWater()` + `level.isRaining()`（霆霓）
- **26.2 HUD 为渲染状态提取架构**：无旧版 `InGameHud.render`，改为 `Hud.extractPlayerHealth / extractArmor` 提取贴图 / 文本元素；绘制用 `blitSprite(RenderPipelines.GUI_TEXTURED, ...)`，文本颜色是 ARGB 且 **alpha == 0 直接不渲染**（必须 `0xFFFFFFFF` 带透明度）；活力行 y 要经 `extractArmor` HEAD 捕获实参锚定，不要写死偏移；隐藏额外生命须同时 Redirect `getAttributeValue(MAX_HEALTH)` 与 `getHealth()`，否则高血量把加成画成多行红心
- **26.2 创造页签槽位列表不可替换**：`selectTab` 把槽位列表整体替换为 `SlotWrapper` 包装、`slotClicked` 硬转 `SlotWrapper` —— 插入自定义槽位条目每次点击 ClassCastException；重定位只能**原地改坐标**（`SlotMixin` + `@Shadow @Final @Mutable x/y`）；`Slot#isActive` 是纯客户端概念，折叠 / 展开门控只影响渲染与点击，服务端无感知
- **Fabric Data Attachment 是快照语义**：`getAttached` 返回落盘快照，原地修改不自动保存 —— 所有写路径收敛到 `setAttached` 回写；且**注册在静态块 = 类加载时序陷阱**（方法引用不触发 `<clinit>`，玩家登录反序列化时未注册类型被静默丢弃）—— 必须在 `onInitialize` 最顶部显式 `register()`（配饰若踩坑是背包清空级事故）
- **成就 ID 含子目录路径**：成就 ID = JSON 相对 `data/<ns>/advancement/` 的完整路径（`usage/foo.json` → `ns:usage/foo`），代码查找与 JSON `parent` 引用都必须带前缀，否则静默返回 null；`requirements` 必须是「数组的数组」（组内 AND、组间 OR，单个也要 `[["triggered"]]`）—— `AdvancementSchemaTest` 对全部进度 JSON 强制校验
- **效果图标与语言键必须成对**：新增自定义状态效果时 `effect.<ns>.<id>` 语言键与 `textures/mob_effect/<id>.png` 必须同时存在，且效果图标走 GUI 图集（`atlases/gui.json` 的 directory source 注册 `mob_effect/` 前缀）—— `MobEffectIconAtlasTest` 三不变量强制
- **26.2 无统一 boss 判定**：`Entity` / `LivingEntity` / `Mob` 均无 `isBoss()`，也没有 boss 实体标签 —— 区分 boss 只能显式 `instanceof`（断罪的 `isBossLike`：EnderDragon / WitherBoss / Warden）
- **运行时状态生命周期**：所有按 UUID 键控的静态表必须登记 DISCONNECT / SERVER_STOPPED 双清理（本 mod 在主入口统一装配）；余烬的马铠冷却刻意不随断线清理，防「修补耐久 → 反复免死」
- dev 环境日志噪音识别：`Failed to retrieve profile key pair` + 401 是 Loom dev 离线会话的正常现象（聊天签名密钥请求），无功能影响，不要在 mod 里「修复」它

### 构建与测试

```
./gradlew build       # 构建（含 4 组门禁单元测试）
./gradlew runServer   # 开发服验证
./gradlew runClient   # 开发客户端
```

- 构建：Java 25（release 25），`splitEnvironmentSourceSets()` 分 main / client 源集，`withSourcesJar()`
- 4 组单元测试（`src/test/java`，纯 Java 不依赖 MC 运行时）：
  - `DatapackIntegrityTest`：数据包交叉引用（成就 parent / 配方物品 ID / 图标与模型引用）
  - `PngIntegrityTest`：全部纹理 PNG 的 STB 同等校验（签名 / chunk CRC / IHDR / IDAT 精确对账）
  - `MobEffectIconAtlasTest`：效果图标 ↔ 语言键 ↔ GUI 图集注册三不变量
  - `AdvancementSchemaTest`：进度 JSON criteria 键与 requirements 完全一致
- CI：`.github/workflows/build.yml` —— push / PR 触发，ubuntu-24.04 + JDK 25 + `./gradlew build` + 上传 `build/libs` 产物

## 记录规范

### 附魔记录规范

> **约定：每新增一个附魔，必须在本 README 追加对应章节（编号接续「附魔总览」表的行号）。**

新增附魔时按以下格式记录（保持与现有章节一致）：

```
### N. 附魔名 (English Name)

**功能**：<一句话描述效果与数值>

#### 实现方法

**数据定义** `<json 路径>`：

- <关键字段与取值理由>

**核心逻辑** `<代码路径>`：

- <Mixin 注入点与机制>

**获取途径**：<标签/掉落表/事件追加等>
```

涉及通用机制变化的（新 Mixin、新标签体系、原版行为覆写）需同步更新「通用技术模式」和「踩坑记录」章节。

新增附魔的配套登记清单（除章节外逐项过）：

1. `registry/ModEnchantments.java` 注册 ResourceKey + `getXxxLevel` 等级读取辅助
2. `registry/ModCreativeTabs.java` 的 `ENCHANTMENTS` 列表追加（自动列出全部等级附魔书）
3. **获取途径**：按需加入 `data/minecraft/tags/enchantment/` 的 `non_treasure` / `tradeable` / `on_random_loot` / `treasure`（26.2 三者为显式列表，互不引用）；火焰系附魔同步加入 `#extra-enchantry-short:family_fire`（掉落物防火共用）
4. **视听反馈**：按「视听反馈」分级约定经 `FxHelper` 补齐 L1（L2/L3 视稀有度），常态效果记得 `throttle`
5. **成就**（可选）：有「首次成功使用」纪念价值的加 `advancement/usage/` JSON + `Advancements.java` 常量 + 一句 award 调用
6. `zh_cn.json` / `en_us.json` 同步翻译（附魔名 + 成就 title / description）
7. **配方解锁**（如涉及新合成配方）：在 `advancement/recipes/` 补解锁成就（材料 `inventory_changed` + `recipe_unlocked` 双 criteria，`requirements` 单组 OR 且与 criteria 键完全一致）

### 排版规则

> 适用于本 README 全文。新增 / 改写章节前先过一遍，避免反复返工。

#### 标题层级

- **H1**：仅文档标题 `# Extra Enchantry Short`（一处）
- **H2**：文档大节 —— `功能介绍` / `环境` / `安装与使用` / `附魔总览` / `目录` / `附魔` / `版本主题` / `更新日志` / `通用技术模式` / `记录规范`
- **H3**：H2 的直接子节；安装与使用下用 `### 安装` / `### 使用` / `### 数据包自定义`；附魔下用 `### N. 附魔名 (English Name)`（编号与「附魔总览」表行号一致）；成组附魔用 `### 盾牌四附魔 (Shield Enchantments)（20~23）` 形式；版本主题下用 `### 1.0.1「独立移植」 (Independent Port)` 形式；通用技术模式与记录规范下的功能子节
- **H4**：H3 的子节；附魔实现细节用 `#### 实现方法`；成组附魔内单条用 `#### N. 名称（等级，槽位 / 稀有度）`；版本主题的功能专题（配饰栏 / 配饰与宝石 / 配方）

#### 列表项

- **项目符号**：功能介绍 / 安装与使用等顶层列表统一 `* `（半角星 + 半角空格）；附魔「实现方法」与通用技术模式内的明细用 `- `
- **列表项之间**：无空行
- **格式**：以 `**加粗标签**：` 起头，后接内容；纯文本段落不进列表
- **多行项**：回行后用 2 空格缩进续行（GitHub 列表续行约定）

#### 代码与转义

- **内联代码**：单反引号包裹
- **围栏代码块**：三反引号包裹，**不指定语言**；代码块内**不要**写转义字符，原样写 JSON / 配置
- **正文中**：`_` `(` `)` `[` `]` **不要**反斜杠转义 —— Markdown 不会把它们当格式字符
- **要写字面量 `*`**：用 `\*`（如 `data/\*.json` 之类）
- **中文标点**：用全角「」、，。；技术 ID / 类名 / 文件路径保留半角

#### 空行

- **标题前后**：空 1 行
- **段落之间**：空 1 行
- **列表项之间**：**不要**空行
- **代码块前后**：空 1 行

#### 目录（TOC）

- **位置**：放在「附魔总览」之后、「附魔」之前
- **缩进**：2 空格 = H3 子项；4 空格 = H4 子项
- **锚点格式**：`(#<github-anchor>)`，GitHub 算法：lowercase → 去 `[^\w\s-]` → 空格换 `-`
- **括号内容影响锚点**：`（20~23）` 去括号与波浪号后无空格，结果是 `...2023` 连写（如 `盾牌四附魔-shield-enchantments2023`），不是 `...-2023`

#### 表格

- **列宽**：GitHub 渲染时按需拉宽，肉眼编辑时无需严格对齐竖线
- **左对齐**：默认即可（无需 `:---`）
- **三列以上**：表头 `| col1 | col2 | col3 |`，分隔行 `| --- | --- | --- |`，数据行同上

#### 段落内容

- **不要为了对齐手动添加空格** —— Markdown 渲染时连续空格折叠为 1
- **加粗强调**：用 `**...**`，正文中只对**关键概念**用（首次出现的术语、重要的数值节点）
- **代码引用**：类名 / 文件路径 / 字段名 / 数值 / 标签全部用反引号包裹
- **不要裸链接**：用 `[text](url)` 或 `<url>`

#### 反例（常见错误）

| 错误                                | 正确                                                |
| --------------------------------- | ------------------------------------------------- |
| `### 1.0.1 独立移植`（无书名号 / 无英文）      | `### 1.0.1「独立移植」 (Independent Port)`（版本主题统一格式）    |
| `## 配饰与宝石` 独立 H2 放功能专题            | `#### 配饰与宝石` 在 `### 1.0.1「独立移植」` 下                |
| 列表项之间插空行                          | 列表项之间紧贴                                           |
| 正文里 `\[` / `\_` 转义                | 直接写 `[` / `_`                                     |
| `[锚点](#1-凋零保护-wither-protection)` 漏编号或漏横线 | 锚点与标题逐字对应（编号保留、空格换 `-`）                    |

#### 改动后必跑

- 改完任一章节后跑 `./gradlew build`（数据包交叉引用 / PNG 结构 / 成就 schema / 效果图集 4 组门禁测试同时把关文档引用的资源）
- 改标题层级后**同步更新目录**：所有 `[#anchor]` 必须能跳到对应标题
- 改章节名后**同步检查正文引用**：如「见配饰八附魔」「详见踩坑记录」
