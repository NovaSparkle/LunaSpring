package org.novasparkle.lunaspring.API.util.utilities;

public enum MaterialAttribute {
    NETHERITE_AXE(10, 1),
    DIAMOND_AXE(9, 1),
    IRON_AXE(9, 0.9),
    GOLDEN_AXE(7, 1),
    STONE_AXE(9, 0.8),
    WOODEN_AXE(8, 0.8),

    NETHERITE_SWORD(8, 1.6),
    DIAMOND_SWORD(7, 1.6),
    IRON_SWORD(6, 1.6),
    GOLDEN_SWORD(4, 1.6),
    STONE_SWORD(5, 1.6),
    WOODEN_SWORD(4, 1.6),

    NETHERITE_HELMET(3, 3, 1),
    DIAMOND_HELMET(3, 2, 0),
    IRON_HELMET(2, 0, 0),
    GOLDEN_HELMET(2, 0, 0),
    LEATHER_HELMET(1, 0, 0),
    CHAINMAIL_HELMET(2, 0, 0),

    NETHERITE_CHESTPLATE(8, 3, 1),
    DIAMOND_CHESTPLATE(8, 2, 0),
    IRON_CHESTPLATE(6, 0, 0),
    GOLDEN_CHESTPLATE(5, 0, 0),
    LEATHER_CHESTPLATE(3, 0, 0),
    CHAINMAIL_CHESTPLATE(5, 0, 0),

    NETHERITE_LEGGINGS(6, 3, 1),
    DIAMOND_LEGGINGS(6, 2, 0),
    IRON_LEGGINGS(5, 0, 0),
    GOLDEN_LEGGINGS(3, 0, 0),
    LEATHER_LEGGINGS(2, 0, 0),
    CHAINMAIL_LEGGINGS(4, 0, 0),

    NETHERITE_BOOTS(3, 3, 1),
    DIAMOND_BOOTS(3, 2, 0),
    IRON_BOOTS(2, 0, 0),
    GOLDEN_BOOTS(1, 0, 0),
    LEATHER_BOOTS(1, 0, 0),
    CHAINMAIL_BOOTS(1, 0, 0);

    private double damage = 0;
    private double speed = 0;
    MaterialAttribute(double damage, double speed) {
        this.damage = damage;
        this.speed = speed;
    }

    private double armor_protection = 0;
    private double armor_weight = 0;
    private double armor_akb = 0;
    MaterialAttribute(double armor_protection, double armor_weight, double armor_akb) {
        this.armor_akb = armor_akb;
        this.armor_protection = armor_protection;
        this.armor_weight = armor_weight;
    }
}
