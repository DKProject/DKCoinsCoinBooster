package net.pretronic.dkcoins.coinbooster.command;

import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class CommandUtil {

    public static MinecraftPlayer getPlayer(String target){
        MinecraftPlayer player = McNative.getInstance().getPlayerManager().getPlayer(target);
        if(player == null){
            //@Todo message
            return null;
        }
        return player;
    }

}
