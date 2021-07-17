package net.pretronic.dkcoins.coinbooster;

public class PermissionBoost {

    private final String permission;
    private final String type;
    private final double percentage;

    public PermissionBoost(String permission, String type, double percentage) {
        this.permission = permission;
        this.type = type;
        this.percentage = percentage;
    }

    public String getPermission() {
        return permission;
    }

    public String getType() {
        return type;
    }

    public double getPercentage() {
        return percentage;
    }
}
