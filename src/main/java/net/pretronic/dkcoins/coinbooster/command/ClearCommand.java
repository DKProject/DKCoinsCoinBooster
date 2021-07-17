package net.pretronic.dkcoins.coinbooster.command;

/*

./booster list
./booster activate <name>

./booster add <player> <name>
./booster remove <player> <name>
./booster list <player>
./booster disableAll

 */

import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class ClearCommand extends BasicCommand {

    public ClearCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder()
                .name("clear").aliases("c")
                .permission(DKCoinsCoinBoosterConfig.PERMISSION_ADMIN).create());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(arguments.length < 1){
            sender.sendMessage(Messages.COMMAND_BOOSTER_HELP);
            return;
        }

        MinecraftPlayer player = CommandUtil.getPlayer(arguments[0]);
        if(player == null) return;

        String boost;
        if(arguments.length == 1){
            boost =  "all";
            player.setSetting("DKCoinsBooster","booster",Document.newDocument());
        }else{
            boost = arguments[1];
            Setting settings = player.getSetting("DKCoinsBooster","booster");
            Document document = settings != null ? settings.getDocumentValue() : Document.newDocument();
            document.set(boost,0);
            player.setSetting("DKCoinsBooster","booster",document);
        }

        sender.sendMessage(Messages.COMMAND_BOOSTER_CLEARED, VariableSet.create()
                .addDescribed("player",player)
                .add("boost",boost)
                .add("name",boost));
    }
}
