package github.metemoii.customitemdurability.gui;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.type.SignType;
import github.metemoii.customitemdurability.config.MessagesConfig;
import github.metemoii.customitemdurability.model.DurabilityItem;
import github.metemoii.customitemdurability.runnable.DurabilityTask;
import github.metemoii.customitemdurability.util.HexUtil;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DurabilityCreate extends Gui {
    private ItemStack item;
    AdvancedSlotManager advancedSlotManager = new AdvancedSlotManager(this);
    AdvancedSlot slot;

    public DurabilityCreate(Player player, ItemStack item) {
        super(player, "durability-create", "Durability Edit", 4);
        this.item = item;
    }
    @Override
    public void onOpen(InventoryOpenEvent e) {
        fillGui(new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()));
        this.slot = advancedSlotManager.addAdvancedIcon(10, new Icon(Material.BARRIER));
        this.slot.onPrePutClick((event, item) -> {
            if (item.getType().getMaxDurability() == 0) {
                player.sendMessage(MessagesConfig.getInstance().getNO_DAMAGE_ITEM());
                return true;
            }
            return false;
        }).onPut((event, item) -> this.updateInventory());
        this.slot.onPrePickupClick((event, item) -> false).onPickup((event, item) -> this.updateInventory());

        updateInventory();
    }

    private void updateInventory() {
        final Inventory inventory = getInventory();
        addItem(12, new Icon(Material.BLACK_STAINED_GLASS_PANE));
        if(!this.item.getType().isAir() && this.item.getType().getMaxDurability() != 0) {
            slot.reset();
            advancedSlotManager.putIconToAdvancedSlot(slot, item);
        } else {
            this.item = inventory.getItem(10);
        }
        DurabilityTask.task(() -> {
            if(this.item != null && Objects.requireNonNull(getInventory().getItem(10)).getType() == this.item.getType() && this.item.getType().getMaxDurability() != 0) {
                addItem(12, new Icon(Material.ANVIL).setName(HexUtil.color("&aEdit Durability")).appendLore(HexUtil.color("&7Click to set the item's durability.")).onClick(event -> {
                    slot.reset();
                    openSign(player, this.item);
                }));
            }
            player.updateInventory();
        }, 3);
        player.updateInventory();
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        slot.reset();
    }

    public void openSign(Player player, ItemStack item) {
        SignGui sign = HCore.signBuilder(player).type(SignType.NORMAL).lines("", "^^^^^^^^", "Put your", "your durability").build();
        sign.open();
        sign.whenInputReceived(lines -> {
            String input = lines[0];
            int durability = 0;
            try {
                durability = Integer.parseInt(input);
                if(durability < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                DurabilityTask.task(sign::open, 2);
            }
            openGuiWithItem(player, item, durability);
        });
    }

    private void openGuiWithItem(Player player, ItemStack item, int durability) {
        DurabilityTask.task(() -> {
            DurabilityItem modifier = new DurabilityItem(item);
            modifier.createDurability(durability, durability);
            DurabilityCreate newGui = new DurabilityCreate(player,  modifier.getItem());
            newGui.open();
            slot.reset();
            advancedSlotManager.putIconToAdvancedSlot(slot, modifier.getItem());
        });
    }
}
