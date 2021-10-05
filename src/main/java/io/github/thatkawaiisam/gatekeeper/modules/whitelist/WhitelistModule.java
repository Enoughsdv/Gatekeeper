package io.github.thatkawaiisam.gatekeeper.modules.whitelist;

import io.github.thatkawaiisam.artus.bungee.BungeeModule;
import io.github.thatkawaiisam.gatekeeper.GatekeeperPlugin;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter
public class WhitelistModule extends BungeeModule<GatekeeperPlugin> {

    private Set<UUID> whitelisted;
    private WhitelistMode mode;
    private String kickMessage, bypassPermission;

    /**
     * Whitelist Module.
     *
     * @param plugin instance.
     */
    public WhitelistModule(GatekeeperPlugin plugin) {
        super(plugin, "whitelist");
        this.getOptions().setGenerateConfiguration(true);
        whitelisted = new HashSet<>();
        mode = WhitelistMode.OFF;
    }

    @Override
    public void onEnable() {

        // Load values from configuration
        Configuration configuration = this.getConfiguration().getImplementation();
        
        if (configuration.contains("Whitelisted")) {
            for (String entry : configuration.getStringList("Whitelisted")) {
                this.whitelisted.add(UUID.fromString(entry));
            }
        }
        
        if (configuration.contains("Status")) {
            this.mode = WhitelistMode.valueOf(configuration.getString("Status"));
        }
        
        this.kickMessage = configuration.getString("Kick-Message");
        this.bypassPermission = configuration.getString("Bypass-Permission");
        
        // Register Commands
        this.addCommand(new WhitelistCommand(this));

        // Register Listeners
        this.addListener(new WhitelistListener(this));
    }

    @Override
    public void onDisable() {
        Configuration configuration = getConfiguration().getImplementation();

        // Save data values to file.
        if (whitelisted.size() > 1) {
            List<String> entries = new ArrayList<>();
            
            for (UUID uuid : whitelisted) {
                entries.add(uuid.toString());
            }
            
            configuration.set("Whitelisted", entries);
            
            ProxyServer.getInstance().getLogger().info("Saved whitelisted players to configuration file.");
        }
        
        configuration.set("Status", this.mode.name());
        getConfiguration().save();
        
        // Clear list in the event of a reload.
        this.whitelisted.clear();
    }
}
