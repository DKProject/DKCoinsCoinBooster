package net.pretronic.dkcoins.coinbooster.command;

import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.libraries.command.NotFindable;
import net.pretronic.libraries.command.command.MainCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

public class BoosterCommand extends MainCommand implements NotFindable {

    public BoosterCommand(ObjectOwner owner, CommandConfiguration configuration) {
        super(owner, configuration);

        registerCommand(new ActivateCommand(owner));
        registerCommand(new ListCommand(owner));
        registerCommand(new AddCommand(owner));
        registerCommand(new RemoveCommand(owner));
        registerCommand(new DisableAllCommand(owner));
    }

    @Override
    public void commandNotFound(CommandSender sender, String command, String[] args) {
        sender.sendMessage(Messages.COMMAND_BOOSTER_HELP);
    }
}
