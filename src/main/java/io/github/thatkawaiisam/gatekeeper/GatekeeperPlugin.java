package io.github.thatkawaiisam.gatekeeper;

import io.github.thatkawaiisam.artus.bungee.BungeePlugin;
import io.github.thatkawaiisam.filare.BungeeConfiguration;
import io.github.thatkawaiisam.gatekeeper.modules.motd.MOTDModule;
import io.github.thatkawaiisam.gatekeeper.modules.reconnect.ReconnectModule;
import io.github.thatkawaiisam.gatekeeper.modules.whitelist.WhitelistModule;

import lombok.Getter;

@Getter
public class GatekeeperPlugin extends BungeePlugin {

    private final BungeeConfiguration config = new BungeeConfiguration(this, "config", this.getDataFolder().getAbsolutePath());

    @Override
    public void onEnable() {

        config.load();

        if(config.getImplementation().getBoolean("Modules.motd")){
            this.getModuleFactory().addModule(new MOTDModule(this));
        }

        if(config.getImplementation().getBoolean("Modules.reconnect")){
            this.getModuleFactory().addModule(new ReconnectModule(this));
        }

        if(config.getImplementation().getBoolean("Modules.whitelist")){
            this.getModuleFactory().addModule(new WhitelistModule(this));
        }

        this.getModuleFactory().enableModules();

    }

    @Override
    public void onDisable() {
        this.getModuleFactory().disableModules();
    }
}
