package io.github.thatkawaiisam.gatekeeper.modules.whitelist;

import io.github.thatkawaiisam.gatekeeper.utils.CC;
import io.github.thatkawaiisam.artus.bungee.BungeeCommand;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.CommandAlias;

import java.net.URL;
import java.net.HttpURLConnection;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;

@CommandAlias("gatekeeper")
public class WhitelistCommand extends BungeeCommand<WhitelistModule> {

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static JsonParser parser = new JsonParser();
    Configuration config = getModule().getPlugin().getConfig().getImplementation();
    Configuration moduleConfig = getModule().getConfiguration().getImplementation();

    /**
     * Whitelist Commands.
     *
     * @param module instance.
     */
    public WhitelistCommand(WhitelistModule module) {
        super(module);
    }

    @Syntax("<state>")
    @Subcommand("whitelist on")
    public void whitelistOn(CommandSender sender) {

        if(!sender.hasPermission(this.config.getString("Permissions.Whitelist.modes")) ||
                !sender.hasPermission(this.config.getString("Permissions.Admin"))) {

            sender.sendMessage(CC.translate(this.config.getString("No-Permissions")));
            return;
        }

        this.getModule().setMode(WhitelistMode.ON);

        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-Whitelist")));
            return;
        }

        for (ProxiedPlayer player : this.getModule().getPlugin().getProxy().getPlayers()) {
            if (player.hasPermission(this.config.getString("Permissions.Whitelist.modes")) ||
                    player.hasPermission(this.config.getString("Permissions.Admin"))) {

                player.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-Whitelist")));
            }
        }
    }

    @Syntax("<state>")
    @Subcommand("whitelist off")
    public void whitelistOff(CommandSender sender) {

        if(!sender.hasPermission(this.config.getString("Permissions.Whitelist.modes")) ||
                !sender.hasPermission(this.config.getString("Permissions.Admin"))) {

            sender.sendMessage(CC.translate(this.config.getString("No-Permissions")));
            return;
        }

        this.getModule().setMode(WhitelistMode.OFF);

        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-UnWhitelist")));
            return;
        }

        for (ProxiedPlayer player : this.getModule().getPlugin().getProxy().getPlayers()) {
            if (player.hasPermission(this.config.getString("Permissions.Whitelist.modes")) ||
                    player.hasPermission(this.config.getString("Permissions.Admin"))) {

                player.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-UnWhitelist")));
            }
        }
    }

    @Syntax("<player>")
    @Subcommand("whitelist add")
    public void whitelistAdd(CommandSender sender, String target) {
        getUUID(target).whenComplete(((uuid, throwable) -> {
            if (uuid == null) {
                sender.sendMessage(CC.translate("&cUnable to find a valid player."));
                return;
            }

            this.getModule().getWhitelisted().add(uuid);

            if(!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-AddPlayer")));
                return;
            }

            for (ProxiedPlayer player : this.getModule().getPlugin().getProxy().getPlayers()) {
                if (player.hasPermission(this.config.getString("Permissions.Whitelist.list")) || 
                        player.hasPermission(this.config.getString("Permissions.Admin"))) {

                    player.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-AddPlayer")));
                }
            }
        }));
    }

    @Syntax("<player>")
    @Subcommand("whitelist remove")
    public void whitelistRemove(CommandSender sender, String target) {
        getUUID(target).whenComplete(((uuid, throwable) -> {
            if (uuid == null) {
                sender.sendMessage(CC.translate("&cUnable to find a valid player."));
                return;
            }
            if (!this.getModule().getWhitelisted().contains(uuid)) {
                sender.sendMessage(CC.translate("&cPlayer is not currently whitelisted."));
                return;
            }

            this.getModule().getWhitelisted().remove(uuid);

            if(!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-RemovePlayer")));
                return;
            }

            for (ProxiedPlayer player : this.getModule().getPlugin().getProxy().getPlayers()) {
                if (player.hasPermission(this.config.getString("Permissions.Whitelist.list")) || 
			  player.hasPermission(this.config.getString("Permissions.Admin"))) {

                    player.sendMessage(CC.translate(this.moduleConfig.getString("Messages.Notifications.On-AddPlayer")));
                }
            }
        }));
    }

    /**
     * Get UUID of player based on name.
     *
     * @param player to fetch UUID of.
     * @return UUID if player is valid.
     */

    // TODO: Put a this back into my utils.
    private static CompletableFuture<UUID> getUUID(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "https://api.ashcon.app/mojang/v2/user/" + player;
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("User-Agent",  "Mozilla/5.0");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JsonElement object = parser.parse(response.toString());
                JsonObject parsedObject = object.getAsJsonObject();
                if (!parsedObject.has("uuid")) {
                    return null;
                }
                return UUID.fromString(parsedObject.get("uuid").getAsString());
            } catch (JsonSyntaxException | IOException e) {
                return null;
            }
        }, executorService);
    }
}
