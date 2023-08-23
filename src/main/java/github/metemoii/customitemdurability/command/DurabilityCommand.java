package github.metemoii.customitemdurability.command;

import com.google.common.collect.ImmutableList;
import github.metemoii.customitemdurability.config.MessagesConfig;
import github.metemoii.customitemdurability.gui.DurabilityCreate;
import github.metemoii.customitemdurability.gui.DurabilityItemList;
import github.metemoii.customitemdurability.model.DurabilityItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DurabilityCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command ,String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(!player.hasPermission(command.getPermission())) return true;
        if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
            new DurabilityItemList(player).open();
        } else if(args.length == 1 && args[0].equalsIgnoreCase("create")) {
            new DurabilityCreate(player, new ItemStack(Material.AIR)).open();
            //new DurabilityItem(item).createDurability(10, 10);
            //player.sendMessage(MessagesConfig.getInstance().getAPPLIED_DURABILTIY());
            //player.sendMessage(MessagesConfig.getInstance().getNO_ITEM());
        } else {

        }
        return false;
    }
    @Override
    public ImmutableList<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 1 && sender.hasPermission(command.getPermission())) {
            return ImmutableList.of("list", "create");
        }
        return ImmutableList.of("");
    }
}
