package io.github.thatkawaiisam.gatekeeper.modules.reconnect;

import io.github.thatkawaiisam.artus.bungee.BungeeModule;
import io.github.thatkawaiisam.gatekeeper.GatekeeperPlugin;

public class ReconnectModule extends BungeeModule<GatekeeperPlugin> {
    
    /**
     * Reconnect Module
     *
     * @param plugin instance.
     */
    
    public ReconnectModule(GatekeeperPlugin plugin) {
        super(plugin, "reconnect");
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        this.addListener(new ReconnectListener(this));
    }

    @Override
    public void onDisable() {}

}
