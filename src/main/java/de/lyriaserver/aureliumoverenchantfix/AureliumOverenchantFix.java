package de.lyriaserver.aureliumoverenchantfix;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public final class AureliumOverenchantFix extends JavaPlugin implements Listener {
    private static final List<Enchantment> BANNED_ENCHANTMENTS =
            List.of(
                    Enchantment.MENDING,
                    Enchantment.ARROW_INFINITE,
                    Enchantment.LOYALTY,
                    Enchantment.RIPTIDE,
                    Enchantment.FROST_WALKER,
                    Enchantment.SOUL_SPEED,
                    Enchantment.THORNS);

    private static final Map<Enchantment, Integer> CAPPED_ENCHANTMENTS =
            Map.ofEntries(
                    Map.entry(Enchantment.DURABILITY, 1),
                    Map.entry(Enchantment.DAMAGE_ALL, 3),
                    Map.entry(Enchantment.DAMAGE_ARTHROPODS, 3),
                    Map.entry(Enchantment.DAMAGE_UNDEAD, 3),
                    Map.entry(Enchantment.FIRE_ASPECT, 1),
                    Map.entry(Enchantment.ARROW_DAMAGE, 3),
                    Map.entry(Enchantment.SWEEPING_EDGE, 2),
                    Map.entry(Enchantment.ARROW_KNOCKBACK, 1),
                    Map.entry(Enchantment.PROTECTION_ENVIRONMENTAL, 3),
                    Map.entry(Enchantment.PROTECTION_EXPLOSIONS, 3),
                    Map.entry(Enchantment.PROTECTION_FIRE, 3),
                    Map.entry(Enchantment.PROTECTION_PROJECTILE, 2),
                    Map.entry(Enchantment.PROTECTION_FALL, 2),
                    Map.entry(Enchantment.DIG_SPEED, 3),
                    Map.entry(Enchantment.LOOT_BONUS_BLOCKS, 1),
                    Map.entry(Enchantment.LURE, 2));

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getItem() == null
                || event.getItem().getType() != Material.ENCHANTED_BOOK) return;
        ItemStack item = event.getItem();
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        boolean changed = false;
        for (Enchantment e : BANNED_ENCHANTMENTS) {
            if (meta.removeStoredEnchant(e)) changed = true;
        }
        for (Map.Entry<Enchantment, Integer> entry : CAPPED_ENCHANTMENTS.entrySet()) {
            if (meta.getStoredEnchantLevel(entry.getKey()) > entry.getValue()) {
                meta.removeStoredEnchant(entry.getKey());
                meta.addStoredEnchant(entry.getKey(), entry.getValue(), false);
                changed = true;
            }
        }
        if (changed && !meta.getStoredEnchants().isEmpty()) item.setItemMeta(meta);
    }
}
