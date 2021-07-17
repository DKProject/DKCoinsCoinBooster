package net.pretronic.dkcoins.coinbooster;

import java.util.UUID;

public class ActiveNamedBoost {

    private final NamedBoost boost;
    private final long timeout;
    private final UUID booster;

    public ActiveNamedBoost(NamedBoost boost, long timeout, UUID booster) {
        this.boost = boost;
        this.timeout = timeout;
        this.booster = booster;
    }

    public NamedBoost getBoost() {
        return boost;
    }

    public long getTimeout() {
        return timeout;
    }

    public UUID getBooster() {
        return booster;
    }
}
