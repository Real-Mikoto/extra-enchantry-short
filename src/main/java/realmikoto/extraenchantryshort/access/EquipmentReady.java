package realmikoto.extraenchantryshort.access;

/**
 * EquipmentReady duck 接口（独立类，避开 mixin 包）。
 *
 * <p>踩坑记录：此接口若定义在 mixin 类内部，跨 Mixin 读取会触发
 * "IllegalClassLoadError: is in a defined mixin package"——duck 接口必须放在正常包。</p>
 *
 * <p>语义：LivingEntity 的 equipment 在其自身构造体内赋值（早于 {@code <init>} TAIL），
 * LivingEntityMixin 在构造 TAIL 置位标记；查询方通过本接口读取。</p>
 */
public interface EquipmentReady {

	boolean extraenchantryshort$isEquipmentReady();
}
