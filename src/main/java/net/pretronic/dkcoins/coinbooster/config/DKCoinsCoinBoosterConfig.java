package net.pretronic.dkcoins.coinbooster.config;

import net.pretronic.dkcoins.coinbooster.NamedBoost;
import net.pretronic.dkcoins.coinbooster.PermissionBoost;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.utility.Iterators;

import java.util.ArrayList;
import java.util.List;

public class DKCoinsCoinBoosterConfig {

    public static String TRANSACTION_CAUSE = "Boost";
    public static String TRANSACTION_REASON = "Coin boost ({percentage}%)";

    public static String[] ALLOWED_TRANSACTION_CAUSES = new String[]{"API"};
    public static String[] ALLOWED_TRANSACTION_CURRENCIES = new String[]{"Coins"};

    public static List<PermissionBoost> BOOSTS_PERMISSIONS = new ArrayList<>();
    public static List<NamedBoost> BOOSTS_NAMED = new ArrayList<>();

    public static CommandConfiguration COMMAND_BOOSTER = CommandConfiguration.newBuilder()
            .name("booster")
            .permission("dkcoins.booster").create();

    public static String PERMISSION_ADMIN = "dkcoins.booster.admin";

    public static boolean isAllowedTransactionCause(String cause) {
        for (String allowedTransactionCause : ALLOWED_TRANSACTION_CAUSES) {
            if(cause.equalsIgnoreCase(allowedTransactionCause)) return true;
        }
        return false;
    }

    public static boolean isAllowedCurrency(String currency) {
        for (String allowedCurrency : ALLOWED_TRANSACTION_CURRENCIES) {
            if(allowedCurrency.equalsIgnoreCase(currency)) return true;
        }
        return false;
    }

    public static NamedBoost getBoost(String name) {
        return Iterators.findOne(BOOSTS_NAMED, boost -> boost.getName().equalsIgnoreCase(name));
    }

    public static boolean existsBoost(String name) {
        return getBoost(name) != null;
    }

    static {
        BOOSTS_PERMISSIONS.add(new PermissionBoost("dkcoins.boost.personal.15","personal",15));
        BOOSTS_PERMISSIONS.add(new PermissionBoost("dkcoins.boost.personal.50","personal",50));
        BOOSTS_PERMISSIONS.add(new PermissionBoost("dkcoins.boost.server.25","server",25));

        BOOSTS_NAMED.add(new NamedBoost("personal-25","personal",25,"2h"));
        BOOSTS_NAMED.add(new NamedBoost("server-25","server",25,"2h"));
    }

}
