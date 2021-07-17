package net.pretronic.dkcoins.coinbooster.command;

import net.pretronic.dkcoins.coinbooster.NamedBoost;
import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.MainCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListCommand extends BasicCommand {

    public ListCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.name("list","l"));
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        MinecraftPlayer player;
        if(arguments.length > 0 && sender.hasPermission(DKCoinsCoinBoosterConfig.PERMISSION_ADMIN)){
            player = McNative.getInstance().getPlayerManager().getPlayer(arguments[0]);
            if(player == null){
                sender.sendMessage(Messages.COMMAND_BOOSTER_PLAYER_NOT_FOUND,VariableSet.create()
                        .add("player",arguments[0]));
                return;
            }
        }else{
            player = (MinecraftPlayer) sender;
        }

        Setting settings = player.getSetting("DKCoinsBooster","booster");
        Document document = settings != null ? settings.getDocumentValue() : Document.newDocument();

        List<BoostListEntry> booster = Iterators.map(DKCoinsCoinBoosterConfig.BOOSTS_NAMED
                , namedBoost -> new BoostListEntry(namedBoost,document.getInt(namedBoost.getName())));

        sender.sendMessage(Messages.COMMAND_BOOSTER_LIST, VariableSet.create()
                .add("boosters",booster)
                .addDescribed("player",player));
    }

    public static class BoostListEntry {

        private final NamedBoost boost;
        private final int amount;

        public BoostListEntry(NamedBoost boost, int amount) {
            this.boost = boost;
            this.amount = amount;
        }

        public NamedBoost getBoost() {
            return boost;
        }

        public int getAmount() {
            return amount;
        }
    }
}
