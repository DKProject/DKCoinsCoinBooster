package net.pretronic.dkcoins.coinbooster.command;

import net.pretronic.dkcoins.coinbooster.ActiveNamedBoost;
import net.pretronic.dkcoins.coinbooster.NamedBoost;
import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.dkcoins.coinbooster.listener.BoosterListener;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.duration.DurationProcessor;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.ConnectedMinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;
import org.mcnative.runtime.api.text.components.MessageComponent;

public class ActivateCommand extends BasicCommand {

    public ActivateCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder()
                .name("activate").aliases("enable")
                .permission(DKCoinsCoinBoosterConfig.PERMISSION_ADMIN).create());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(arguments.length < 1){
            sender.sendMessage(Messages.COMMAND_BOOSTER_HELP);
            return;
        }
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) sender;

        NamedBoost boost = DKCoinsCoinBoosterConfig.getBoost(arguments[0]);
        if(boost == null){
            sender.sendMessage(Messages.COMMAND_BOOSTER_NOT_FOUND, VariableSet.create()
                    .add("name",arguments[0]));
            return;
        }

        long timeout = DurationProcessor.getStandard().parse(boost.getDuration()).toMillis()+System.currentTimeMillis();

        MessageComponent<?> message;
        if(boost.getType().equals("personal")) message = Messages.COMMAND_BOOSTER_ACTIVATED_PERSONAL;
        else message = Messages.COMMAND_BOOSTER_ACTIVATED_SERVER;

        if(sender.hasPermission(DKCoinsCoinBoosterConfig.PERMISSION_ADMIN)){
            BoosterListener.ACTIVE_BOOSTS.add(new ActiveNamedBoost(boost,timeout,player.getUniqueId()));
            McNative.getInstance().getLocal().broadcast(message,VariableSet.create()
                    .addDescribed("player",sender)
                    .add("percentage",boost.getPercentage())
                    .add("duration",boost.getDuration())
                    .add("boost",boost));
        }else{
            Setting settings = player.getSetting("DKCoinsBooster","booster");
            Document document = settings != null ? settings.getDocumentValue() : Document.newDocument();
            int amount = document.getInt(boost.getName());

            if(amount > 0){
                document.set(boost.getName(),amount>1?amount-1:0);
                player.setSetting("DKCoinsBooster","booster",document);

                BoosterListener.ACTIVE_BOOSTS.add(new ActiveNamedBoost(boost,timeout,((ConnectedMinecraftPlayer)sender).getUniqueId()));

                McNative.getInstance().getLocal().broadcast(message,VariableSet.create()
                        .addDescribed("player",sender)
                        .add("percentage",boost.getPercentage())
                        .add("duration",boost.getDuration())
                        .add("boost",boost));
            }else{
                sender.sendMessage(Messages.COMMAND_BOOSTER_NOT_ENOUGH,VariableSet.create()
                        .add("boost",boost));
            }
        }
    }
}
