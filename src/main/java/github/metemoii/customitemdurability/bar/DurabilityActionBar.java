package github.metemoii.customitemdurability.bar;

import com.cryptomorin.xseries.messages.ActionBar;
import github.metemoii.customitemdurability.config.SettingsConfig;
import org.bukkit.entity.Player;

public class DurabilityActionBar {

    private final Player player;
    public DurabilityActionBar(Player player) {
        this.player = player;
    }

    public void sendActionBar(int durability, int maxDurability) {
        String text = SettingsConfig.getInstance().getActionBarText();
        int percentage = 100 * durability / maxDurability;
        text = text.replace("{current}", durability + "")
                .replace("{maximum}", maxDurability + "")
                .replace("{percentage}", percentage + "");
        ActionBar.sendActionBar(player, text);
    }
}
