package github.metemoii.customitemdurability.model;

import com.cryptomorin.xseries.messages.ActionBar;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import github.metemoii.customitemdurability.bar.DurabilityActionBar;
import github.metemoii.customitemdurability.config.SettingsConfig;
import github.metemoii.customitemdurability.constant.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DurabilityItem implements IDurabilityItem {
    private final ItemStack item;
    public DurabilityItem(ItemStack item) {
        this.item = item;
    }
    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public boolean hasDurability() {
        return this.item.getType().getMaxDurability() != 0 && NBT.get(this.item, nbt -> nbt.hasTag(Constants.COMPOUND));
    }
    @Override
    public int getDurability() {
        return NBT.get(this.item, nbt -> nbt.getCompound(Constants.COMPOUND).getInteger(Constants.DURABILITY));
    }
    @Override
    public int getMaxDurability() {
        return NBT.get(this.item, nbt -> nbt.getCompound(Constants.COMPOUND).getInteger(Constants.MAX_DURABILITY));
    }
    @Override
    public void setDurability(int durability) {
        if(this.hasDurability()) {
            int max, cur;
            max = this.getMaxDurability();
            cur = Math.min(durability, max);
            //cur = durability > max ? max : durability;
            final int itemMaxDurability = this.item.getType().getMaxDurability();
            if(cur <= 0 && this.item.getType() == Material.TRIDENT && itemMaxDurability != 249) {
                NBT.modify(this.item, nbt -> {
                    nbt.getCompound(Constants.COMPOUND).setInteger(Constants.DURABILITY, 1);
                    nbt.setInteger(Constants.DAMAGE, itemMaxDurability -1);
                });
                return;
            }
            NBT.modify(this.item, nbt -> {
                nbt.getCompound(Constants.COMPOUND).setInteger(Constants.DURABILITY, cur);
                nbt.setInteger(Constants.DAMAGE, itemMaxDurability - (itemMaxDurability * cur / max));
            });
            if(cur <= 0) this.item.setAmount(0);
        }
    }
    @Override
    public void createDurability(int maxDurability, int durability) {
        try {
            if(!(this.item.getType().getMaxDurability() != 0)) {
                throw new IllegalAccessException("Durability cannot be applied to this item.");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        int max = Math.max(durability, maxDurability);
        int cur = Math.max(0, Math.min(durability, max));
        final int itemMaxDurability = this.item.getType().getMaxDurability();
        NBT.modify(this.item, nbt -> {
            ReadWriteNBT compound = nbt.getOrCreateCompound(Constants.COMPOUND);
            compound.setInteger(Constants.DURABILITY, cur);
            compound.setInteger(Constants.MAX_DURABILITY, max);
            nbt.setInteger(Constants.DAMAGE, itemMaxDurability - (itemMaxDurability * cur / max));
        });
    }
    @Override
    public void damageItem(int amount) {
        int damageAmount = this.getDurability();
        damageAmount -= amount;
        this.setDurability(damageAmount);
    }
    @Override
    public void repairItem(int amount) {
        int repairAmount = this.getDurability();
        repairAmount += amount;
        this.setDurability(repairAmount);
    }

    @Override
    public void printDebugInfo(Player player) {
        if(!item.getType().isAir()) {
            if(SettingsConfig.getInstance().isActionBarEnabled()) {
                new DurabilityActionBar(player).sendActionBar(this.getDurability(), this.getMaxDurability());
            }
        }

    }
}
