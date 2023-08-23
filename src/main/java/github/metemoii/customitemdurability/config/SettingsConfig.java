package github.metemoii.customitemdurability.config;

import com.cryptomorin.xseries.XMaterial;
import github.metemoii.customitemdurability.util.HexUtil;
import mc.obliviate.inventory.Icon;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;


public class SettingsConfig {
    private final static SettingsConfig instance = new SettingsConfig();
    private File file;
    private YamlConfiguration config;
    private SettingsConfig() {

    }
    private String ActionBarText;
    private boolean isActionBarEnabled;
    private String guiTitle;
    private int guiRows;
    private List<String> itemDescription, itemRepairLore, itemDurabilityLore;
    private boolean isItemRepairEnabled, isitemDurabilityEnabled;
    private List<Integer> itemListSlots;
    private Icon next_page_button, prev_page_button, fillGuiItem;
    private boolean enabledfillGui;
    public void load(String path, JavaPlugin plugin) {
        path = path.endsWith(".yml") ? path : path + ".yml";
        file = new File(plugin.getDataFolder(), path);
        if(!file.exists()) {
            plugin.saveResource(path, false);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isActionBarEnabled = config.getBoolean("actionbar.enabled");
        ActionBarText = HexUtil.color(config.getString("actionbar.text"));


        guiTitle = HexUtil.color(config.getString("item_list_gui.title"));
        guiRows = config.getInt("item_list_gui.rows", 4);

        itemDescription = HexUtil.color(config.getStringList("item_list_gui.item.description"));
        itemRepairLore = HexUtil.color(config.getStringList("item_list_gui.item.repair.description"));
        itemDurabilityLore = HexUtil.color(config.getStringList("item_list_gui.item.durability.description"));

        isItemRepairEnabled = config.getBoolean("item_list_gui.item.repair.enabled", true);
        isitemDurabilityEnabled = config.getBoolean("item_list_gui.item.durability.enabled", true);

        itemListSlots = config.getIntegerList("item_list_gui.slots");
        fillGuiItem = new Icon(XMaterial.valueOf(config.getString("item_list_gui.fillGui.material")).parseItem());
        enabledfillGui = config.getBoolean("item_list_gui.fillGui.enabled", true);
        next_page_button = new Icon(XMaterial.valueOf(config.getString("item_list_gui.next_page_button.material")).parseItem()).setName(HexUtil.color(config.getString("item_list_gui.next_page_button.name")));
        prev_page_button = new Icon(XMaterial.valueOf(config.getString("item_list_gui.prev_page_button.material")).parseItem()).setName(HexUtil.color(config.getString("item_list_gui.prev_page_button.name")));

    }

    public String getActionBarText() {
        return ActionBarText;
    }

    public boolean isActionBarEnabled() {
        return isActionBarEnabled;
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public int getGuiRows() {
        return guiRows;
    }

    public List<String> getItemDescription() {
        return itemDescription;
    }

    public List<String> getItemRepairLore() {
        return itemRepairLore;
    }

    public List<String> getItemDurabilityLore() {
        return itemDurabilityLore;
    }

    public boolean isItemRepairEnabled() {
        return isItemRepairEnabled;
    }

    public boolean isIsitemDurabilityEnabled() {
        return isitemDurabilityEnabled;
    }

    public List<Integer> getItemListSlots() {
        return itemListSlots;
    }

    public Icon getNext_page_button() {
        return next_page_button;
    }

    public Icon getPrev_page_button() {
        return prev_page_button;
    }

    public Icon getFillGuiItem() {
        return fillGuiItem;
    }

    public boolean isEnabledfillGui() {
        return enabledfillGui;
    }

    public static SettingsConfig getInstance() {
        return instance;
    }
}
