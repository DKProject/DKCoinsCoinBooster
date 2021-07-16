package net.pretronic.dkcoins.coinbooster.listener;

import net.pretronic.dkcoins.api.DKCoins;
import net.pretronic.dkcoins.api.account.BankAccount;
import net.pretronic.dkcoins.api.account.member.AccountMember;
import net.pretronic.dkcoins.api.account.transaction.AccountTransaction;
import net.pretronic.dkcoins.api.events.account.DKCoinsAccountTransactEvent;
import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.config.Messages;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Iterators;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class DKCoinsListener {

    @Listener
    public void onDKCoinsAccountTransact(DKCoinsAccountTransactEvent event) {
        if(event.getTransaction().getReceiver().getAccount().getType().getName().equalsIgnoreCase("User")
                && DKCoinsCoinBoosterConfig.isAllowedTransactionCause(event.getTransaction().getCause())) {
            OnlineMinecraftPlayer player = detectReceiverPlayer(event.getTransaction());
            double boostPercent = getBoostPercent(player);
            if(boostPercent <= 0) return;
            double boostMultiplier = getBoostMultiplier(boostPercent);
            double boostAmount = event.getTransaction().getAmount()*boostMultiplier;

            event.getTransaction().getReceiver().addAmount(boostAmount);
            player.sendMessage(Messages.BOOST, VariableSet.create().add("boostPercent", boostPercent)
                    .add("boostMultiplier", boostMultiplier)
                    .add("boostAmount", boostAmount)
                    .add("formattedBoostAmount", DKCoins.getInstance().getFormatter().formatCurrencyAmount(boostAmount)));
        }
    }

    private double getBoostMultiplier(double boostPercent) {
        return boostPercent/100;
    }

    private double getBoostPercent(OnlineMinecraftPlayer player) {

    }

    private OnlineMinecraftPlayer detectReceiverPlayer(AccountTransaction transaction) {//@Todo maybe change detection
        BankAccount account = transaction.getReceiver().getAccount();
        AccountMember owner = Iterators.findOne(account.getMembers(), member -> member.getRole().getName().equalsIgnoreCase("OWNER"));
        return McNative.getInstance().getLocal().getOnlinePlayer(owner.getUser().getUniqueId());
    }
}
