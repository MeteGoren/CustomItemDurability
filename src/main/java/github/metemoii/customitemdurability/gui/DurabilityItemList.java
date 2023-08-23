package github.metemoii.customitemdurability.gui;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.type.SignType;
import github.metemoii.customitemdurability.config.MessagesConfig;
import github.metemoii.customitemdurability.config.SettingsConfig;
import github.metemoii.customitemdurability.model.DurabilityItem;
import github.metemoii.customitemdurability.runnable.DurabilityTask;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DurabilityItemList extends Gui {
    private PaginationManager pagination = new PaginationManager(this);

    public DurabilityItemList(Player player) {
        this(player, SettingsConfig.getInstance().getGuiTitle(), SettingsConfig.getInstance().getGuiRows(), SettingsConfig.getInstance().getItemListSlots());
    }

    public DurabilityItemList(Player player, String title, int rows, List<Integer> itemPerPages) {
        super(player, "item-list", title, rows);
        pagination.registerPageSlots(itemPerPages);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        update();
    }

    private void update() {
        if (SettingsConfig.getInstance().isEnabledfillGui()) {
            fillGui(SettingsConfig.getInstance().getFillGuiItem());
        }
        pagination.getItems().clear();
        getDurabilityItems();
        pagination.update();
        if (!pagination.isFirstPage()) {
            Icon prevButton = SettingsConfig.getInstance().getPrev_page_button();
            addItem(getSize() - 9, prevButton.onClick(e -> {
                pagination.goPreviousPage();
                update();
            }));
        }
        if (!pagination.isLastPage()) {
            Icon nextButton = SettingsConfig.getInstance().getNext_page_button();
            addItem(getSize() - 1, nextButton.onClick(e -> {
                pagination.goNextPage();
                update();
            }));
        }
    }
    private void getDurabilityItems() {
        for (Player plr : Bukkit.getOnlinePlayers()) {
            for (ItemStack item : plr.getInventory().getContents()) {
                if (item != null && !item.getType().isAir()) {
                    DurabilityItem modifier = new DurabilityItem(item);
                    if (modifier.hasDurability()) {
                        Icon icon = new Icon(modifier.getItem().clone());
                        boolean repairItemEnabled = true, durabilityItemEnabled = true;
                        for (String lore : SettingsConfig.getInstance().getItemDescription()) {
                            lore = lore.replace("{player}", plr.getName()).replace("{current}", modifier.getDurability() + "").replace("{maximum}", modifier.getMaxDurability() + "").replace("{percentage}", 100 * modifier.getDurability() / modifier.getMaxDurability() + "");
                            icon.appendLore(lore);
                        }
                        if (SettingsConfig.getInstance().isItemRepairEnabled()) {
                            for (String repairLore : SettingsConfig.getInstance().getItemRepairLore()) {
                                icon.appendLore(repairLore);
                            }
                        }
                        if (SettingsConfig.getInstance().isIsitemDurabilityEnabled()) {
                            for (String durabilityLore : SettingsConfig.getInstance().getItemDurabilityLore()) {
                                icon.appendLore(durabilityLore);
                            }
                        }
                        icon.onClick(e -> {
                            if (e.isLeftClick()) {
                                if (repairItemEnabled) {
                                    if (e.isShiftClick()) {
                                        Enchantment enchantment = Enchantment.MENDING;
                                        if (item.containsEnchantment(enchantment)) {
                                            item.removeEnchantment(enchantment);
                                        } else {
                                            item.addUnsafeEnchantment(enchantment, enchantment.getStartLevel());
                                        }
                                    } else {
                                        if (modifier.getDurability() == modifier.getMaxDurability()) {
                                            player.sendMessage(MessagesConfig.getInstance().getREPAIR_MAXIMUM_DURABILITY_ITEM());
                                        } else {
                                            modifier.createDurability(modifier.getMaxDurability(), modifier.getMaxDurability());
                                            player.sendMessage(MessagesConfig.getInstance().getREPAIR_DURABILITY_ITEM());
                                        }
                                    }
                                }
                            } else if(e.isRightClick()) {
                                openSign(player, item);
                            }
                            update();
                        });
                        this.pagination.addItem(icon);
                    }
                }
            }
        }
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
            editItem(item, durability);
        });
    }

    private void editItem(ItemStack item, int durability) {
        DurabilityTask.task(() -> {
            new DurabilityItem(item).createDurability(durability, durability);
            new DurabilityItemList(player).open();
        }, 2);
    }
}