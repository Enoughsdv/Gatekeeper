package io.github.thatkawaiisam.gatekeeper.modules.whitelist;

import io.github.thatkawaiisam.artus.bungee.BungeeListener;
import io.github.thatkawaiisam.gatekeeper.utils.CC;

import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

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
    public void onProxyJoin(ServerConnectEvent event) {

        ProxiedPlayer player = event.getPlayer();

        if (this.getModule().getMode() == WhitelistMode.OFF
                || this.getModule().getWhitelisted().contains(player.getUUID())
                || player.hasPermission(this.getModule().getBypassPermission())) {
            return;
        }

        player.disconnect(CC.translate(this.getModule().getKickMessage()));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        if (this.getModule().getMode() == WhitelistMode.OFF) {
            return;
        }

        event.getResponse().getVersion().setProtocol(2);
        event.getResponse().getVersion().setName(CC.translate(this.getModule().getServerList()));
    }
}
