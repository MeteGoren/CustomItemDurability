package github.metemoii.customitemdurability.runnable;

import github.metemoii.customitemdurability.CustomItemDurability;
import org.bukkit.scheduler.BukkitScheduler;

public class DurabilityTask {
    public static void task(Runnable runnable) {
        task(runnable, 0);
    }
    public static void task(Runnable runnable, long delay) {
        BukkitScheduler scheduler = CustomItemDurability.getIntance().getServer().getScheduler();
        scheduler.runTaskLater(CustomItemDurability.getIntance(), runnable, delay);
    }
}
