package net.pretronic.dkcoins.coinbooster;

public class NamedBoost {

    private final String name;
    private final String type;
    private final double percentage;
    private final String duration;

    public NamedBoost(String name, String type, double percentage, String duration) {
        this.name = name;
        this.type = type;
        this.percentage = percentage;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }

    public double getPercentage() {
        return percentage;
    }


}
