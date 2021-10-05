package io.github.thatkawaiisam.gatekeeper.modules.motd;

import io.github.thatkawaiisam.gatekeeper.utils.CC;
import io.github.thatkawaiisam.artus.bungee.BungeeCommand;

import net.md_5.bungee.api.CommandSender;

import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.CommandAlias;

@CommandAlias("gatekeeper")
public class MOTDCommand extends BungeeCommand<MOTDModule> {

    /**
     * MOTD Commands.
     *
     * @param module instance.
     */
    public MOTDCommand(MOTDModule module) {
        super(module);
    }

    @Default
    public void helpCmd(CommandSender sender) {
        for(String lines : this.getModule().getPlugin().getConfig().getImplementation().getStringList("Help-Command")) {
            sender.sendMessage(CC.translate(lines));
	}
    }

    @Syntax("<message>")
    @Subcommand("motd line1")
    public void setLine1(CommandSender sender, String value) {
        this.getModule().setLine1(value);
        sender.sendMessage(CC.translate("&aYou have put line 1 of the MOTD."));
    }
    
    @Syntax("<message>")
    @Subcommand("motd line2")
    public void setLine2(CommandSender sender, String value) {
        this.getModule().setLine2(value);
        sender.sendMessage(CC.translate("&aYou have put line 2 of the MOTD."));
    }
}
