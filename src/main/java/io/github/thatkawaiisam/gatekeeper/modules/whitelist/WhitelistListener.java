package io.github.thatkawaiisam.gatekeeper.modules.whitelist;

import io.github.thatkawaiisam.artus.bungee.BungeeListener;
import io.github.thatkawaiisam.gatekeeper.utils.CC;

import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.util.UUID;

public class WhitelistListener extends BungeeListener<WhitelistModule> {

    /**
     * Whitelist Listener.
     *
     * @param module instance.
     */
    public WhitelistListener(WhitelistModule module) {
        super(module);
    }

    @EventHandler
    public void onProxyJoin(LoginEvent event) {
        
        UUID uuid = event.getConnection().getUniqueId();
        
        if (this.getModule().getMode() == WhitelistMode.OFF || this.getModule().getWhitelisted().contains(uuid)) {
            return;
        }

        event.setCancelled(true);
        event.setCancelReason(CC.translate(this.getModule().getKickMessage()));
    }
    
    @EventHandler
    public void onPing(ProxyPingEvent event) {
        
        event.getResponse().getVersion().setProtocol(2);
        event.getResponse().getVersion().setName(CC.translate(this.getModule().getConfiguration().getImplementation().getString("Sever-List")));
    }
}
