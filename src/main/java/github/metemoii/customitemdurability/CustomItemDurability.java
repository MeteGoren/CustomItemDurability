package github.metemoii.customitemdurability;

import com.hakan.core.HCore;
import github.metemoii.customitemdurability.command.DurabilityCommand;
import github.metemoii.customitemdurability.config.MessagesConfig;
import github.metemoii.customitemdurability.config.SettingsConfig;
import github.metemoii.customitemdurability.listener.ItemDamageEvent;
import github.metemoii.customitemdurability.listener.ItemMendEvent;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItemDurability extends JavaPlugin {

    private static CustomItemDurability intance;

    public static CustomItemDurability getIntance() {
        return intance;
    }

    static FileConfiguration config;
    @Override
    public void onEnable() {
        new InventoryAPI(this).init();
        HCore.initialize(this);
        SettingsConfig.getInstance().load("settings.yml", this);
        MessagesConfig.getInstance().load("messages.yml", this);
        getServer().getPluginManager().registerEvents(new ItemDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemMendEvent(), this);
        getCommand("customitemdurability").setExecutor(new DurabilityCommand());
        getCommand("customitemdurability").setTabCompleter(new DurabilityCommand());
        getServer().getPluginCommand("customitemdurability").setPermissionMessage(MessagesConfig.getInstance().getNO_PERMISSION());
        intance = this;
    }
}
