package io.github.thatkawaiisam.gatekeeper.modules.motd;

import io.github.thatkawaiisam.artus.bungee.BungeeModule;
import io.github.thatkawaiisam.gatekeeper.GatekeeperPlugin;

import net.md_5.bungee.config.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MOTDModule extends BungeeModule<GatekeeperPlugin> {

    private String line1, line2;

    /**
     * MOTD Module.
     *
     * @param plugin instance.
     */
    public MOTDModule(GatekeeperPlugin plugin) {
        super(plugin, "motd");
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {

        // Load Values
        Configuration configuration = this.getConfiguration().getImplementation();

        this.line1 = configuration.getString("Lines.1");
        this.line2 = configuration.getString("Lines.2");

        // Register Commands
        this.addCommand(new MOTDCommand(this));

        // Register Listeners
        this.addListener(new MOTDListener(this));
    }

    @Override
    public void onDisable() {
        Configuration configuration = this.getConfiguration().getImplementation();
        configuration.set("Lines.1", line1);
        configuration.set("Lines.2", line2);
        getConfiguration().save();
    }
}
