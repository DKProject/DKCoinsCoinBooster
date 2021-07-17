package net.pretronic.dkcoins.coinbooster.command;

import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.GeneralUtil;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class RemoveCommand extends BasicCommand {

    public RemoveCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder()
                .name("remove").aliases("r")
                .permission(DKCoinsCoinBoosterConfig.PERMISSION_ADMIN).create());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(arguments.length < 2){
            sender.sendMessage(Messages.COMMAND_BOOSTER_HELP);
            return;
        }

        MinecraftPlayer player = CommandUtil.getPlayer(arguments[0]);
        if(player == null) return;

        String boost = arguments[1];
        int amount = 1;
        if(arguments.length > 2 && GeneralUtil.isNaturalNumber(arguments[2])){
            amount = Integer.parseInt(arguments[2]);
        }

        if(!DKCoinsCoinBoosterConfig.existsBoost(boost)){
            sender.sendMessage(Messages.COMMAND_BOOSTER_NOT_FOUND, VariableSet.create()
                    .add("name",boost));
            return;
        }

        Setting settings = player.getSetting("DKCoinsBooster","booster");
        Document document = settings != null ? settings.getDocumentValue() : Document.newDocument();

        int newAmount = document.getInt(boost)-amount;
        if(newAmount < 0) newAmount = 0;
        document.set(boost,newAmount);
        player.setSetting("DKCoinsBooster","booster",document);

        sender.sendMessage(Messages.COMMAND_BOOSTER_REMOVED, VariableSet.create()
                .addDescribed("player",player)
                .add("amount",amount)
                .add("name",boost));
    }
}
