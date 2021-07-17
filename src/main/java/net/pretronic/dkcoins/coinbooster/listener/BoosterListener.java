package net.pretronic.dkcoins.coinbooster.listener;

import net.pretronic.dkcoins.api.DKCoins;
import net.pretronic.dkcoins.api.account.BankAccount;
import net.pretronic.dkcoins.api.account.member.AccountMember;
import net.pretronic.dkcoins.api.account.transaction.AccountTransaction;
import net.pretronic.dkcoins.api.events.account.DKCoinsAccountTransactEvent;
import net.pretronic.dkcoins.coinbooster.ActiveNamedBoost;
import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.dkcoins.coinbooster.PermissionBoost;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Iterators;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.event.player.MinecraftPlayerLogoutEvent;
import org.mcnative.runtime.api.event.player.login.MinecraftPlayerLoginEvent;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;
import org.mcnative.runtime.api.text.components.MessageComponent;

import java.util.*;

public class BoosterListener {

    public static final List<ActiveNamedBoost> ACTIVE_BOOSTS = new ArrayList<>();
    private final Map<UUID,Double> serverPermissionBoosts = new HashMap<>();

    @Listener
    public void onPlayerLogin(MinecraftPlayerLoginEvent event) {
        double percentage = getServerPermissionBoost(event.getOnlinePlayer());
        if(percentage > 0) serverPermissionBoosts.put(event.getOnlinePlayer().getUniqueId(),percentage);
    }

    @Listener
    public void onPlayerLogin(MinecraftPlayerLogoutEvent event) {
        serverPermissionBoosts.remove(event.getOnlinePlayer().getUniqueId());
    }

    @Listener
    public void onDKCoinsAccountTransact(DKCoinsAccountTransactEvent event) {
        if(event.getTransaction().getReceiver().getAccount().getType().getName().equalsIgnoreCase("User")
                && !event.getTransaction().getCause().equalsIgnoreCase(DKCoinsCoinBoosterConfig.TRANSACTION_CAUSE)
                && DKCoinsCoinBoosterConfig.isAllowedTransactionCause(event.getTransaction().getCause())
                && DKCoinsCoinBoosterConfig.isAllowedCurrency(event.getTransaction().getCurrency().getName())) {

            OnlineMinecraftPlayer player = detectReceiverPlayer(event.getTransaction());

            MessageComponent<?> component = Messages.BOOST_PERSONAL;
            double boost = getPersonalPermissionBoost(player);
            UUID booster = null;

            for (Map.Entry<UUID, Double> entry : this.serverPermissionBoosts.entrySet()) {
                if(entry.getValue() > boost){
                    component = Messages.BOOST_SERVER;
                    boost = entry.getValue();
                    booster = entry.getKey();
                }
            }

            for (ActiveNamedBoost activeBoost : ACTIVE_BOOSTS) {
                if(activeBoost.getTimeout() > System.currentTimeMillis()
                        && activeBoost.getBoost().getPercentage() > boost
                        && (activeBoost.getBoost().getType().equalsIgnoreCase("server") || activeBoost.getBooster().equals(player.getUniqueId()))){
                    component = activeBoost.getBoost().getType().equalsIgnoreCase("server") ? Messages.BOOST_SERVER : Messages.BOOST_PERSONAL;
                    boost = activeBoost.getBoost().getPercentage();
                    booster = activeBoost.getBooster();
                }
            }

            if(boost <= 0) return;

            double multiplier = boost/100.0;
            double amount = event.getTransaction().getAmount()*multiplier;

            event.getTransaction().getReceiver().addAmount(null
                    ,amount
                    ,DKCoinsCoinBoosterConfig.TRANSACTION_REASON.replace("{percentage}",boost+"")
                    ,DKCoinsCoinBoosterConfig.TRANSACTION_CAUSE
                    ,new ArrayList<>());

            player.sendMessage(component, VariableSet.create().add("percentage", boost)
                    .add("amount", amount)
                    .add("booster",booster != null ? McNative.getInstance().getPlayerManager().getPlayer(booster) : null)
                    .add("formattedAmount", DKCoins.getInstance().getFormatter().formatCurrencyAmount(amount)));
        }
    }


    private double getPersonalPermissionBoost(OnlineMinecraftPlayer player) {
        double percentage = 0;
        for (PermissionBoost permissionBoost : DKCoinsCoinBoosterConfig.BOOSTS_PERMISSIONS) {
            if(permissionBoost.getType().equals("personal")
                    && player.hasPermission(permissionBoost.getPermission())
                    && permissionBoost.getPercentage() > percentage){
                percentage = permissionBoost.getPercentage();
            }
        }
        return percentage;
    }

    private double getServerPermissionBoost(OnlineMinecraftPlayer player) {
        double percentage = 0;
        for (PermissionBoost permissionBoost : DKCoinsCoinBoosterConfig.BOOSTS_PERMISSIONS) {
            if(permissionBoost.getType().equals("server")
                    && player.hasPermission(permissionBoost.getPermission())
                    && permissionBoost.getPercentage() > percentage){
                percentage = permissionBoost.getPercentage();
            }
        }
        return percentage;
    }

    private OnlineMinecraftPlayer detectReceiverPlayer(AccountTransaction transaction) {//@Todo maybe change detection
        BankAccount account = transaction.getReceiver().getAccount();
        AccountMember owner = Iterators.findOne(account.getMembers(), member -> member.getRole().getName().equalsIgnoreCase("OWNER"));
        return McNative.getInstance().getLocal().getOnlinePlayer(owner.getUser().getUniqueId());
    }
}
