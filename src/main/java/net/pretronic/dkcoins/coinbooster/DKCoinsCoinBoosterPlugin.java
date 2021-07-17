package net.pretronic.dkcoins.coinbooster;

import net.pretronic.dkcoins.coinbooster.command.BoosterCommand;
import net.pretronic.dkcoins.coinbooster.config.DKCoinsCoinBoosterConfig;
import net.pretronic.dkcoins.coinbooster.listener.BoosterListener;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import org.mcnative.licensing.context.platform.McNativeLicenseIntegration;
import org.mcnative.licensing.exceptions.CloudNotCheckoutLicenseException;
import org.mcnative.licensing.exceptions.LicenseNotValidException;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

public class DKCoinsCoinBoosterPlugin extends MinecraftPlugin {

    public static final String RESOURCE_ID = "76e9923a-e655-11eb-8ba0-0242ac180002";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgizJMXHTG5mXj0k15+MCkscxr4I/ONitiKv6JBDsPCMriAHu/V0+6DRFn8r/3vw4Wclja806STP4sb/gU6ECVEPja9YtVm5aYvsJko0naQ9CVGhoToODieSIuveRMnroyE12VfBZJd35grbfQhkX4LWyrrOID3lbe1pluWvWh9x441NEACHDX26aNDx/zjY/8bHMFURyAkeggjBYq+L7rHhAH5Cd33LtdLsXSTG5H03wT8aHubFvTyDHuMMCmJMLW+SkpMs55kCMab8KUth7VllvFhNBZcRFrNEG00H+qntQ+hbgxp3Rqd+ToCtpTNPsVLfJ59SshSC5oAEsJUAmdQIDAQAB";

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKCoinsCoinBooster is starting, please wait..");

        if(getRuntime().getPlatform().isProxy()){
            getLogger().warn("On proxy are only permission boost available (For named boosts, you have to install the plugin on your sub server)");
            getLogger().warn("Boosts are also only available one one server and not synchronized across the network");
        }

        try{
            McNativeLicenseIntegration.newContext(this,RESOURCE_ID,PUBLIC_KEY).verifyOrCheckout();
        }catch (LicenseNotValidException | CloudNotCheckoutLicenseException e){
            getLogger().error("--------------------------------");
            getLogger().error("-> Invalid license");
            getLogger().error("-> Error: "+e.getMessage());
            getLogger().error("--------------------------------");
            getLogger().info("DKCoinsCoinBooster is shutting down");
            getLoader().shutdown();
            return;
        }
        getConfiguration().load(DKCoinsCoinBoosterConfig.class);

        getRuntime().getLocal().getCommandManager().registerCommand(new BoosterCommand(this,DKCoinsCoinBoosterConfig.COMMAND_BOOSTER));
        getRuntime().getLocal().getEventBus().subscribe(this, new BoosterListener());

        getLogger().info("DKCoinsCoinBooster started successfully");
    }
}
