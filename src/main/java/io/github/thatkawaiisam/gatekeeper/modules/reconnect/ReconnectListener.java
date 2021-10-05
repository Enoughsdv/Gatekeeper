package io.github.thatkawaiisam.gatekeeper.modules.reconnect;

import io.github.thatkawaiisam.artus.bungee.BungeeListener;

import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;

public class ReconnectListener extends BungeeListener<ReconnectModule> {

    /**
     * Reconnect Listener.
     *
     * @param module instance.
     */
    public ReconnectListener(ReconnectModule module) {
        super(module);
    }
    
    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        // TODO: Better handling of closing and banning.
        if (!event.getKickReason().contains("Server closed")) {
            return;
        }
            
        for(String hubServersList : this.getModule().getConfiguration().getImplementation().getStringList("Hubs")) {
            // TODO: Send them to a pool of lobby servers - connect to redstone.
            ServerInfo hubServer = getModule().getPlugin().getProxy().getServerInfo(hubServersList);

            if (event.getKickedFrom() == hubServer) {
                return;
            }

            event.setCancelled(true);
            event.setCancelServer(hubServer);

        }
    }
}
