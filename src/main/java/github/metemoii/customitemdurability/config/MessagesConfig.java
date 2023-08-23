package github.metemoii.customitemdurability.config;


import github.metemoii.customitemdurability.util.HexUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MessagesConfig {
    private final static MessagesConfig instance = new MessagesConfig();
    private File file;
    private YamlConfiguration config;
    private MessagesConfig() {

    }

    private String prefix, NO_PERMISSION, NO_ITEM, NO_DAMAGE_ITEM, REPAIR_DURABILITY_ITEM, REPAIR_MAXIMUM_DURABILITY_ITEM, APPLIED_DURABILTIY;

    public void load(String path, JavaPlugin plugin) {
        path = path.endsWith(".yml") ? path : path + ".yml";
        file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            plugin.saveResource(path, false);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reload();
    }
    public void reload() {
        prefix = HexUtil.color(config.getString("prefix"));
        NO_PERMISSION = HexUtil.color(config.getString("NO_PERMISSION").replace("%prefix%", prefix));
        NO_ITEM = HexUtil.color(config.getString("NO_ITEM").replace("%prefix%", prefix));
        NO_DAMAGE_ITEM = HexUtil.color(config.getString("NO_DAMAGE_ITEM").replace("%prefix%", prefix));
        REPAIR_DURABILITY_ITEM = HexUtil.color(config.getString("REPAIR_DURABILITY_ITEM").replace("%prefix%", prefix));
        REPAIR_MAXIMUM_DURABILITY_ITEM = HexUtil.color(config.getString("REPAIR_MAXIMUM_DURABILITY_ITEM").replace("%prefix%", prefix));
        APPLIED_DURABILTIY = HexUtil.color(config.getString("APPLIED_DURABILTIY").replace("%prefix%", prefix));
    }
    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNO_PERMISSION() {
        return NO_PERMISSION;
    }

    public String getNO_ITEM() {
        return NO_ITEM;
    }

    public String getNO_DAMAGE_ITEM() {
        return NO_DAMAGE_ITEM;
    }

    public String getREPAIR_DURABILITY_ITEM() {
        return REPAIR_DURABILITY_ITEM;
    }

    public String getREPAIR_MAXIMUM_DURABILITY_ITEM() {
        return REPAIR_MAXIMUM_DURABILITY_ITEM;
    }

    public String getAPPLIED_DURABILTIY() {
        return APPLIED_DURABILTIY;
    }

    public static MessagesConfig getInstance() {
        return instance;
    }
}
