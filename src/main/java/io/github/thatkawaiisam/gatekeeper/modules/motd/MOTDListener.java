package io.github.thatkawaiisam.gatekeeper.modules.motd;

import io.github.thatkawaiisam.gatekeeper.utils.CC;
import io.github.thatkawaiisam.artus.bungee.BungeeListener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;

public class MOTDListener extends BungeeListener<MOTDModule> {

    /**
     * MOTD Listener.
     *
     * @param module instance.
     */
    public MOTDListener(MOTDModule module) {
        super(module);
    }

    @EventHandler
    public void onMOTD(ProxyPingEvent event) {
        ServerPing pingInfo = event.getResponse();

        pingInfo.setDescriptionComponent(new TextComponent(CC.translate(
                this.getModule().getLine1() + "\n" + this.getModule().getLine2())));

        event.setResponse(pingInfo);
    }

}
