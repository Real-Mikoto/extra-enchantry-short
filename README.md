# Extra Enchantry Short

Minecraft 26.2 (Fabric) 自定义附魔模组——**只新增附魔，并保留饰品与饰品栏**。

- 许可证：AGPL-3.0-or-later（GNU Affero 通用公共许可证 v3）

## 功能介绍

* **40 个附魔，全部可用且可自然获取**：全部由数据包定义（`data/extra-enchantry-short/enchantment/*.json`），可用数据包覆盖；全部带专属行为（破限/拓阶、断罪、蚀命、汲取、假象、归羽、空跃、劫后余辉、丰壤、冲阵、誓约、狼铠三附魔、配饰八附魔、盾牌四附魔等）
* **饰品与饰品栏**：耳环 / 项链 / 戒指 / 手镯四槽位（内嵌原版背包与创造界面，展开按钮 + 动画）+ 八家族宝石（合成时镶嵌入配饰，材质决定被动传导率）+ 16 配饰与 8 宝石共 40 个合成配方
* **附魔获取**：附魔台 / 铁砧打书 + 原版战利品池追加（远古城市：蚀命、无踪；末地城：御风、坠星；试炼密室：庇护、余烬；海洋宝箱与钓鱼：渊息；凋零：拓阶；监守者 / 闪电苦力怕：破限书）
* **附魔成就**：14 个实战成就（如「处刑者」「金蝉脱壳」「铜墙铁壁」）+ 7 个隐藏挑战（「深渊回响」「以彼之道」「丰收之神的赞许」等），代码授予
* **数据驱动**：附魔 / 标签 / 战利品表 / 成就 / 配方均为标准数据包内容，原版 `/reload` 生效；模组侧无额外规则文件

## 环境

- Minecraft 26.2 / Fabric Loader 0.19.5 / Fabric API 0.160.0+26.2 / Java 25 / Loom 1.17-SNAPSHOT
- 26.2 的关键 API 变化：`ResourceLocation` → `Identifier`；镐 / 斧等工具由**数据驱动**（Tool 组件规则）；附魔全部走 JSON 数据包定义；`tradeable` / `on_random_loot` 为**显式列表**（不引用 `#non_treasure`）

## 安装与使用

### 安装

* **前置**：Fabric Loader `0.19.5+`、Fabric API `0.160.0+26.2`、Java 25
* **客户端 / 服务端**：均需安装（附魔结算与成就授予在服务端；配饰栏渲染、活力 HUD、空跃多段跳在客户端）
* **步骤**：安装 Fabric Loader → 将 `fabric-api` 与本 mod 的 jar（`build/libs/extra-enchantry-short-1.0.0.jar`）放入 `.minecraft/mods/` → 启动

### 使用

* **获取附魔**：按[附魔总览](#附魔总览)表，经附魔台 / 铁砧打书 / 原版战利品池追加获得；创造模式物品栏「额外附魔书」页签提供全部 40 个附魔的整级附魔书与 24 件饰品
* **饰品栏**：背包界面护甲左侧一列为耳环 / 项链 / 戒指 / 手镯四槽位，点击护甲行上方的展开按钮滑出；右键手持配饰可快捷穿戴（占用则交换回主手）；配饰上可附魔专属附魔
* **宝石**：8 种宝石按槽位配对（耳环=魂珀/盾纹玉，项链=雷光石/萌芽晶，戒指=刃晶/风羽晶，手镯=烬心石/潮汐珠），**合成时镶嵌、不可更换**；配饰材质（铜/铁/金/钻）决定宝石被动传导率
* **成就**：实战成就随效果首次触发授予；隐藏挑战达成即弹出（challenge 帧）

### 数据包自定义

* `data/extra-enchantry-short/enchantment/`：40 个附魔定义（等级 / 费用曲线 / 可附物品 / 互斥）
* `data/minecraft/tags/enchantment/`：获取途径标签（`non_treasure` / `tradeable` / `on_random_loot` / `treasure` / `exclusive_set/*`）
* `data/minecraft/loot_table/`：4 处原版战利品表覆盖（远古城市 / 末地城 / 不祥试炼唯一箱 / 凋零）
* `data/extra-enchantry-short/recipe/`：8 宝石 + 32 配饰配方（每件配饰有 2 个宝石变体）
* 单文件解析失败仅该条目回退；`/reload` 生效（模组无额外监听逻辑）

## 开发

```bash
./gradlew build       # 构建（含数据包完整性 / 成就 schema / PNG 结构 / 效果图标图集 4 组单元测试）
./gradlew runServer   # 开发服验证
./gradlew runClient   # 开发客户端
```

- 构建：Java 25（release 25），`splitEnvironmentSourceSets()` 分 main / client 源集，`withSourcesJar()`
- CI：`.github/workflows/build.yml` —— push / PR 触发，ubuntu-24.04 + JDK 25 + `./gradlew build` + 上传 `build/libs` 产物

### 项目结构（独立移植版的分层组织）

```
src/main/java/realmikoto/extraenchantryshort/
├── ExtraEnchantryShort.java       主入口：MOD_ID / id() / 事件装配
├── registry/                      注册中心
│   ├── ModEnchantments.java       40 附魔 ResourceKey + 等级查询 + 数值规则表
│   ├── ModEffects.java            自定义状态效果（余辉 / 余烬）
│   ├── ModDataComponents.java     自定义数据组件（破限书来源标记）
│   └── ModCreativeTabs.java       创造物品栏页签
├── accessory/                     饰品与宝石体系
│   ├── Accessories.java           槽位 / 材质 / 宝石注册中心与查询 API
│   ├── AccessoryItem / GemItem    配饰与宝石物品
│   ├── AccessorySlot / AccessoryContainer / AccessoryAttachments   槽位 / 容器 / 持久化
│   └── AccessoryManager.java      配饰结算中心（属性 / 伤免 / 击杀 / 死亡链路）
├── combat/                        机制管理器（每附魔一个）
│   ├── AfterglowManager           BulwarkManager      CleaveManager
│   ├── DecoyManager               EmberfallManager    HomingPlumeManager
│   ├── JudgementManager           LimitBreakManager   LoamManager
│   ├── OathboundManager           SanctuaryManager    SheathedEdgeManager
│   ├── ShieldChargeManager        StormsurgeManager   WolfArmorManager
├── rules/TierBreakRules.java      拓阶挖掘等级规则（双 Mixin 共用）
├── fx/                            FxHelper（粒子/音效/节流）+ GemFx（宝石铭印）
├── advancement/Advancements.java  成就授予统一入口
├── entity/DecoyEntity.java        假象诱饵（复用原版盔甲架）
├── access/                        Mixin duck 接口（EquipmentReady / HomingPlumeAccess / StarfallAccess）
└── mixin/                         主 Mixin（21 个）

src/client/java/realmikoto/extraenchantryshort/client/
├── ExtraEnchantryShortClient.java 客户端入口
├── AccessoryColumnRenderer / AccessoryHudState / SlotReposition
└── mixin/                         客户端 Mixin（6 个）

src/test/java/realmikoto/extraenchantryshort/   4 组单元测试（纯 Java，不依赖 MC 运行时）
```

## 附魔总览

| #  | 附魔                     | 等级  | 可附魔物品                              | 获取方式                                              |
| -- | ---------------------- | --- | ---------------------------------- | ------------------------------------------------- |
| 1  | 凋零保护 Wither Protection | IV  | 四件护甲                               | 非宝藏：附魔台 / 铁砧打书（与保护系互斥）                            |
| 2  | 炽焰行者 Blazing Walker    | II  | 靴子                                 | 宝藏：图书管理员 / 宝箱 / 钓鱼（无附魔台）                          |
| 3  | 破限 Limit Break         | I   | 武器 / 工具 / 护甲 / 弓弩 / 三叉戟 / 钓鱼竿 / 马铠 / 配饰 | 监守者死亡 0.05%；被闪电苦力怕击杀 0.5%                    |
| 4  | 拓阶 Tier Break          | III | 镐 / 斧 / 锹 / 锄                      | 击杀凋零 20%（1-3 级随机）                                 |
| 5  | 触及 Reach               | X   | 近战武器 + 工具                          | 非宝藏：附魔台（高费用出高级）/ 铁砧融合升级                           |
| 6  | 假象 Decoy               | III | 头盔                                 | 非宝藏：与荆棘同稀有度（互斥）                                   |
| 7  | 汲取 Siphon              | III | 武器 / 工具 / 弓弩 / 三叉戟                 | 非宝藏：附魔台 / 铁砧打书                                    |
| 8  | 蚀命 Life Erosion        | III | 武器 / 工具 / 弓弩 / 三叉戟                 | 仅远古城市宝箱（附魔书 / 带附魔的武器工具）                           |
| 9  | 活力 Vitality            | V   | 四件盔甲 / 马铠 / 狼铠 / 配饰                | 非宝藏：附魔台 / 铁砧打书                                    |
| 10 | 壁垒 Bulwark             | X   | 胸甲 / 马铠 / 狼铠 / 配饰                  | 非宝藏：附魔台 / 铁砧打书                                    |
| 11 | 劫后余辉 Afterglow         | I   | 不死图腾                               | 非宝藏：附魔台出书 → 铁砧上书（图腾无附魔台途径）                        |
| 12 | 誓约 Oathbound           | I   | 全装备                                | 非宝藏：附魔台 / 铁砧打书                                    |
| 13 | 空跃 Skyward             | II  | 靴子                                 | 非宝藏：附魔台 / 铁砧打书（与冰霜行者 / 深海探索者 / 炽焰行者互斥）            |
| 14 | 破阵 Cleave              | III | 近战武器 / 工具                          | 非宝藏：附魔台 / 铁砧打书（与横扫之刃互斥）                           |
| 15 | 御风 Windrider           | III | 鞘翅                                 | 仅末地城宝箱（附魔书 / 带附魔的鞘翅）                              |
| 16 | 无踪 Unseen              | II  | 靴子                                 | 仅远古城市宝箱（与靴子系附魔互斥）                                 |
| 17 | 断罪 Judgement           | II  | 近战武器 / 工具                          | 非宝藏：very_rare，附魔台 / 铁砧打书                          |
| 18 | 疾风 Gale                | III | 护腿                                 | 非宝藏：附魔台 / 铁砧（两本 II 铁砧合 III）                       |
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
| 30 | 魂铃 Soul Chime          | II  | 耳环                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 31 | 盾坠 Shield Pendant      | II  | 耳环                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 32 | 雷鸣扣 Thunder Clasp      | II  | 项链                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 33 | 翠滴 Verdant Drop        | II  | 项链                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 34 | 刃戒 Blade Ring          | II  | 戒指                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 35 | 羽环 Plume Ring          | II  | 戒指                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 36 | 烬镯 Ember Bracelet      | II  | 手镯                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 37 | 潮镯 Tide Bracelet       | II  | 手镯                                 | 非宝藏：附魔台（对配饰或书）/ 铁砧上书                              |
| 38 | 锐牙 Sharp Fang          | III | 狼铠                                 | 非宝藏：附魔台（对狼铠或书）/ 铁砧上书                              |
| 39 | 哨戒 Vigil               | II  | 狼铠                                 | 非宝藏：附魔台（对狼铠或书）/ 铁砧上书                              |
| 40 | 回春 Renewal             | II  | 狼铠                                 | 非宝藏：附魔台（对狼铠或书）/ 铁砧上书                              |

## 附魔要点速查

> 每个附魔的机制要点如下（完整行为以代码与数据包为准）。数据定义均位于
> `data/extra-enchantry-short/enchantment/<id>.json`，行为 Mixin 位于
> `mixin/`（主）与 `client/mixin/`（客户端）。

### 基础战斗 / 防御（1~19）

1. **凋零保护**：按装备总等级缩短凋零时长（每级 -15%，上限 90%）。`LivingEntityMixin` 拦截 `addEffect`。
2. **炽焰行者**：纯数据驱动（`damage_immunity` + `location_changed`→`replace_disk` 岩浆凝固），与冰霜行者同构；足下火迹粒子为纯视觉分支。
3. **破限**（单级）：① 附魔台互斥无视（`EnchantmentHelperMixin`）② 保护减免上限 80%→100%（EPF 20→25，Redirect `CombatRules`）③ 破限腿甲可铁砧附原版摔落保护（`EnchantmentEvents.ALLOW_ENCHANTING`）④ 砂轮无法去除（`GrindstoneMenuMixin`）⑤ 带破限护甲的活力上限失效 ⑥ 铁砧融合带破限输入时无视「过于昂贵」且费用固定 10 级（`AnvilMenuMixin`）。获取：监守者 0.05% / 闪电苦力怕代杀 0.5% 掉书（来源组件区分隐藏挑战）。
4. **拓阶**：挖掘等级 +1/级；下界合金镐 + III 级可挖基岩（掉落 + 速度）。规则在 `rules/TierBreakRules`，`ItemStackMixin`（掉落 / 速度）+ `BlockBehaviourMixin`（基岩可破坏）。
5. **触及**：实体 / 方块交互范围各 +0.5 格/级（纯数据驱动 attributes 效果）；3 格外命中粒子反馈。
6. **假象**：被玩家攻击时概率生成替身盔甲架（复刻皮肤装备、游走、可被摧毁），仇恨立即转移至诱饵。I/II/III → 25%/40%/50% 触发、1/1/2 个诱饵；含阿西莫夫（0.5%）与嵌套诱饵（5%）彩蛋。
7. **汲取**：成功造成伤害回复 1 心/级，远程减半。
8. **蚀命**：命中附加目标最大生命 14%/15%/17% 伤害，远程 ×0.75；远古城市获取。
9. **活力**：护甲总附魔等级 ×4 生命上限，上限 +50（破限失效）；马铠 / 狼铠照常生效。
10. **壁垒**：胸甲壁垒将**防御减免后**的单次承伤钳制到 11→2 HP（I–X 级）；排除即死 / 虚空 / 饥饿。
11. **劫后余辉**：不死图腾触发后回满血 + 10 秒锁血 + 5 颗吸收黄心；效果图标即锁血计时。
12. **誓约**：带誓约物品死亡不掉落不消失（消失诅咒无效），重生回原槽位；四件套全带则经验与分数也保留。
13. **空跃**：空中再跳 I→1 次 / II→2 次；按键沿检测，触地 / 入水 / 攀爬重置；客户端权威（`LocalPlayerMixin`）。
14. **破阵**：近战溅射 45%/55%/65% 伤害至 1/2/3 个额外目标（外扩 2 格按距离取近）。
15. **御风**：滑翔耐久消耗 50%/75% 概率跳过 + 烟花推进增量 ×1.5/×1.75。
16. **无踪**：I 级屏蔽脚步声与步进 / 落地震动；II 级索敌可见度 ×0.7。
17. **断罪**：目标生命 ≤10%/20% 直接斩杀（伤害放大至必死量，仍受壁垒钳制）；boss 不斩杀改 ×1.75；5 秒冷却。
18. **疾风**：护腿移速 +5%/级（潜行失效）；两本 II 铁砧合 III。
19. **余烬**：金胸甲免死（保留 1 心 + 5 秒锁血 + 60 秒虚弱，耗 50% 耐久，60 秒冷却）；金马铠马与骑手共享、触发即消失。

### 盾牌四附魔（20~23）

26.2 盾牌为 `BlocksAttacks` 数据组件，注入组件方法即覆盖所有盾牌。

20. **冲阵**：疾跑持盾近战命中 +4/6/8 伤害与强力击退（0.9/1.1/1.3），1 秒冷却。
21. **不屈**：I 级破盾时长减半 + 等长抗性提升；II 级免疫破盾但格挡耐久 ×2。
22. **庇护**：格挡中每 2 秒治疗 8 格内玩家 1/2/4 HP，每次耗 1 点盾耐久；有视线检测。
23. **坚壁**：格挡中受 `bypasses_shield` 伤害减免 30/45/60%。

### 远程 / 采集（24~29）

24. **归羽**：落空箭矢 1 秒后飞回（I 50% / II 100%）；命中过实体 / 无限弓不返还；含「百步穿杨」40 格测距挑战。
25. **坠星**：弩烟花爆炸基础伤害 5→9、半径 5→6（阈值 25→36 同步缩放）+ 星形散射粒子。
26. **霆霓**：雨天 / 水中投掷三叉戟命中 +2/+4 伤害，连锁 2 格内最近 1 个目标（减半）。
27. **藏锋**：脱战 5 秒后首次近战命中 +2/+4/+6 伤害；未就绪时 10 秒节流提示。
28. **渊息**：氧气上限 300→600/900/1200 tick（30/45/60 秒）；III 级水下挖掘不减速（`SUBMERGED_MINING_SPEED` +0.8）。
29. **丰壤**：成熟作物 20%/35%/50% 双倍掉落；III 级 3×3 范围收获（仅同种成熟邻格，每格 1 耐久）。

### 配饰八附魔（30~37）

全部 II 级上限、仅对玩家生效；行为集中在 `accessory/AccessoryManager`。

| #  | 附魔                | 槽位 | 效果（每级）                          |
| -- | ----------------- | -- | ------------------------------- |
| 30 | 魂铃 Soul Chime     | 耳环 | 击杀敌对生物回复饥饿 2 点 + 饱和 level 点      |
| 31 | 盾坠 Shield Pendant | 耳环 | 受到的全部伤害 -3%                     |
| 32 | 雷鸣扣 Thunder Clasp | 项链 | 雷雨天气造成的伤害 +4%（自伤不加成）            |
| 33 | 翠滴 Verdant Drop   | 项链 | 自然恢复间隔 80 tick → 最低 40 tick     |
| 34 | 刃戒 Blade Ring     | 戒指 | 攻击速度 +5%                        |
| 35 | 羽环 Plume Ring     | 戒指 | 受到的弹射物伤害 -6%                    |
| 36 | 烬镯 Ember Bracelet | 手镯 | 受到的火焰伤害 -10%；物品掉落物免烧毁           |
| 37 | 潮镯 Tide Bracelet  | 手镯 | 游泳效率 +8%                        |

- 烬镯属火焰家族（`#extra-enchantry-short:family_fire`）：带烬镯附魔的物品与镶嵌烬心石的配饰作为掉落物免疫火焰 / 岩浆烧毁（「烬火不侵」）

### 狼铠三附魔（38~40）

`slots: [body]`，行为集中在 `combat/WolfArmorManager`（瞬态属性修改器，卸甲即清理）。

| #  | 附魔             | 效果（每级）                    |
| -- | -------------- | ------------------------- |
| 38 | 锐牙 Sharp Fang  | 狼攻击伤害 +10%                |
| 39 | 哨戒 Vigil       | 狼索敌 / 跟随范围 +25%           |
| 40 | 回春 Renewal     | 狼每 4 秒回复 1 HP（按全局节拍，满血跳过） |

## 饰品与饰品栏

### 槽位与界面

- **数据层**：`AccessoryAttachments`——Fabric Data Attachment（`extra-enchantry-short:accessories`，persistent，**刻意非 copyOnDeath**）：4 槽 ItemStack 快照，死亡掉落 / 誓约保留由 `AccessoryManager` 显式处理
- **菜单层**：`InventoryMenuMixin` 在 `InventoryMenu` 构造尾部追加 4 个 `AccessorySlot`（下标 46~49），内容由 `AccessoryContainer` 承接，走 containerId 0 原生同步——**生存背包与创造玩家页签直接可用，无需独立 GUI**
- **生存界面**：`InventoryScreenMixin` 在护甲左侧绘制配饰列，护甲行上方展开按钮点击滑出 / 收起；动画由 `AccessoryHudState` 状态机驱动（约 170ms ease-out），断线重置防跨世界残留
- **创造界面**：`CreativeModeInventoryScreenMixin` + `SlotMixin`（向 `Slot` 基类织入坐标重写接口，规避 `SlotWrapper` 强转）重排副手槽与配饰 2×2 槽位
- **活力 HUD**：`HudMixin`——高血量时血条保持单行，上方紧凑显示「❤×n/N」

### 配饰与宝石

**16 件配饰** = 4 材质 × 4 槽位（`{copper,iron,gold,diamond}_{earring,necklace,ring,bracelet}`）。**材质只影响宝石被动传导率**：铜 ×1.0 / 铁 ×1.25 / 金 ×1.5 / 钻 ×2.0。右键快捷穿戴（占用则交换回主手）+ 材质对应装备音效 + 首穿成就「环佩琳琅」。

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

- 镶宝石配饰触发 `GemFx`：穿戴 L2 爆发 + 常驻 L1 环境粒子（逐宝石节流）
- 烬心石兼任「火焰家族凭证」：镶嵌它的配饰免火烧毁

### 配方

`data/extra-enchantry-short/recipe/` 共 **40** 个：8 个宝石配方（无序合成，基础材料 + 锻造模板）+ 32 个配饰配方（每件配饰 2 个宝石变体，产物携带对应 `socketed_gem`）。

## 附魔成就

两棵成就树（`data/extra-enchantry-short/advancement/`：usage 实战 / hidden_challenges 隐秘挑战）。**所有 criteria 均为 `minecraft:impossible`**（仅 root 为 `minecraft:tick`）——全部由效果代码调用 `Advancements.award()` 授予（幂等）。

**实战成就**：环佩琳琅（首穿配饰）、劫后余辉、铜墙铁壁（壁垒挡 ≥4 点）、冲锋陷阵、金蝉脱壳、深海呼吸、拔刀斩、余烬不灭、处刑者、火树银花、丰收时刻、倦鸟归林、雷霆万钧、基石崩解（challenge 帧）。

**隐藏挑战**：深渊回响（水下佩渊息杀远古守卫者）、死而不僵（余辉锁血反杀）、丰收之神的赞许（3 秒连锁 64 株）、百步穿杨（归羽 40 格命中）、以彼之道（冲阵放倒劫掠兽）、极限之证（获得破限书）、雷霆之礼（闪电苦力怕代杀来源破限书）。

## 更新日志

### 1.0.0（当前）

- **独立移植版首发**：modid 由 `extra-enchantry` 迁移为 `extra-enchantry-short`，Java 包名迁移为 `realmikoto.extraenchantryshort`，Mixin 注入前缀迁移为 `extraenchantryshort$`
- 代码重写为分层结构：`registry`（注册中心）/ `accessory`（饰品体系）/ `combat`（机制管理器）/ `rules` / `fx` / `advancement` / `access`（duck 接口）/ `entity` / `mixin`
- 适配目标环境：Fabric Loader 0.19.5、Fabric API 0.160.0+26.2
- 功能与原版 `1.0.1-short` 完全一致：40 附魔 + 配饰与宝石体系 + 成就体系；数据包 / 资源文件按新命名空间迁移
- 许可证：AGPL-3.0-or-later

## 通用技术模式

### 代码注册约定

每个附魔在 `registry/ModEnchantments.java` 注册 ResourceKey（数据驱动附魔是动态注册表，无法静态引用）。物品上的等级读取统一用 `ModEnchantments.level(stack, KEY)` / `getXxxLevel(stack)`（遍历 `stack.getEnchantments()` 匹配 `holder.is(KEY)` 后取等级）。**新增附魔时**：注册 Key → 建 JSON → 按需加获取标签 → `ModCreativeTabs.ENCHANTMENTS` 列表追加。

### 视听反馈（FxHelper）

所有粒子 / 音效统一走 `fx/FxHelper`（`burst` / `burstAt` / `ring` / `trail` / `play` 双重载 / `pitchForLevel` / `throttle` 节流），分级约定 L1 触发确认 / L2 持续氛围（必须节流）/ L3 高光时刻；宝石专用反馈在 `GemFx`。客户端独占反馈放 client source set 的 Mixin。

### 成就体系

- **usage/**（实战树）：JSON 用 `minecraft:impossible` 触发器，代码在效果生效处调 `Advancements.award(ServerPlayer, KEY)`；criterion 名统一 `triggered`
- **hidden_challenges/**：challenge 帧 + announce_to_chat，授予逻辑在各 Manager
- 代码授予（26.2）：成就不是注册表，`server.getAdvancements().get(key.identifier())` 拿 `AdvancementHolder` 再 award；**成就 ID 含子目录路径**（`usage/foo.json` → `ns:usage/foo`）
- 隐藏挑战「雷霆之礼」经 `LIMIT_BREAK_SOURCE` 数据组件区分破限书来源

### Mixin 清单

主 Mixin（`extra-enchantry-short.mixins.json`，21 个，compatibilityLevel JAVA_25）：

| Mixin                    | 目标                      | 作用 |
| ------------------------ | ----------------------- | --- |
| LivingEntityMixin        | `LivingEntity`          | tick 统一分发（活力 / 庇护 / 足下粒子 / 疾风 / 渊息 / 狼铠）；hurtServer 注入处理配饰伤免、雷鸣扣、假象、蚀命、冲阵 / 霆霓 / 藏锋、坚壁、断罪；凋零缩短；破限保护上限；无踪索敌；御风耐久；余烬免死；壁垒钳制；破阵溅射；汲取回血；触及剑气；誓约经验球跳过 |
| AnvilMenuMixin           | `AnvilMenu`             | 破限：铁砧融合带破限输入时无视「过于昂贵」+ 费用固定 10 级 |
| PlayerMixin              | `Player`                | 锁血（余辉 / 余烬）取消 `actuallyHurt`；誓约掉落提取 / 回插；配饰死亡掉落；`addItem` 破限书检测 |
| ServerPlayerMixin        | `ServerPlayer`          | 誓约重生搬运（四件套连带经验分数）；配饰重生回插 |
| EntityMixin              | `Entity`                | 无踪 I 屏蔽脚步声 / 震动 + 落地震动；渊息放大氧气上限 |
| ItemStackMixin           | `ItemStack`             | 拓阶挖掘等级 / 速度（含基岩判定） |
| BlockBehaviourMixin      | `BlockBehaviour`        | 拓阶基岩可破坏 |
| BlockMixin               | `Block`                 | 丰壤双倍掉落 / 3×3 范围收获 + 拓阶特效 + 授「基石崩解」 |
| EnchantmentHelperMixin   | `EnchantmentHelper`     | 破限附魔台互斥无视（ThreadLocal 传物品） |
| EnchantRandomlyFunctionMixin | `EnchantRandomlyFunction` | 随机附魔等级钳制（触及 / 壁垒 1 级、疾风 ≤2 级） |
| GrindstoneMenuMixin      | `GrindstoneMenu`        | 破限砂轮防移除 |
| BlocksAttacksMixin       | `BlocksAttacks`         | 不屈：II 免疫破盾、I 破盾时长减半 + 抗性、II 格挡耐久 ×2 |
| DeathProtectionMixin     | `DeathProtection`       | 劫后余辉：图腾生效时读图腾副本附魔触发 |
| InventoryMixin           | `Inventory`             | `/give` 路径破限书检测兜底 |
| InventoryMenuMixin       | `InventoryMenu`         | 追加 4 个配饰槽（46–49）+ quickMoveStack |
| MobMixin                 | `Mob`                   | `setTarget` 裁决：假象仇恨重定向 |
| ItemEntityMixin          | `ItemEntity`            | 火焰家族附魔 / 烬心石配饰掉落物免烧毁（烬火不侵） |
| FireworkRocketEntityMixin | `FireworkRocketEntity` | 御风烟花推进放大 + 坠星爆炸常量修改与星形粒子 |
| AbstractArrowMixin       | `AbstractArrow`         | 归羽命中标记与延迟返还 + 百步穿杨 |
| ProjectileWeaponItemMixin | `ProjectileWeaponItem` | 归羽等级快照写入箭实体 |
| CrossbowItemMixin        | `CrossbowItem`          | 弩射烟花打坠星标记 |

客户端 Mixin（`extra-enchantry-short.client.mixins.json`，6 个）：`HudMixin`（活力 HUD）、`LocalPlayerMixin`（空跃多段跳）、`InventoryScreenMixin`（生存配饰列）、`CreativeModeInventoryScreenMixin`（创造 2×2 集成）、`AbstractContainerScreenMixin`（按钮命中）、`SlotMixin`（坐标重写 duck）。

## License

AGPL-3.0-or-later，详见 [LICENSE](LICENSE)。
