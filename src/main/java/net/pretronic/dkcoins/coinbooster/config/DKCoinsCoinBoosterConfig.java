package net.pretronic.dkcoins.coinbooster.config;

public class DKCoinsCoinBoosterConfig {

    public static String TRANSACTION_BOOSTER_CAUSE = "CoinBoost";

    public static String[] ALLOWED_TRANSACTION_CAUSES = new String[]{"API"};

    public static boolean isAllowedTransactionCause(String cause) {
        for (String allowedTransactionCause : ALLOWED_TRANSACTION_CAUSES) {
            if(cause.equalsIgnoreCase(allowedTransactionCause)) return true;
        }
        return false;
    }
}
