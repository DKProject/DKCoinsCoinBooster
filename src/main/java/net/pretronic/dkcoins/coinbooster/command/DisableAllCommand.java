package net.pretronic.dkcoins.coinbooster.command;

import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.dkcoins.coinbooster.listener.BoosterListener;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

public class DisableAllCommand extends BasicCommand {

    public DisableAllCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder()
                .name("disableAll")
                .permission(DKCoinsCoinBoosterConfig.PERMISSION_ADMIN).create());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        BoosterListener.ACTIVE_BOOSTS.clear();
        sender.sendMessage(Messages.COMMAND_BOOSTER_DISABLED);
    }
}
