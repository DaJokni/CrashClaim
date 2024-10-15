package net.crashcraft.crashclaim.payment.providers;


import net.crashcraft.crashclaim.payment.PaymentProvider;
import net.crashcraft.crashclaim.payment.ProviderInitializationException;
import net.crashcraft.crashclaim.payment.TransactionRecipe;
import net.crashcraft.crashclaim.payment.TransactionType;
import net.gahvila.gahvilacore.GahvilaCore;
import net.gahvila.gahvilacore.Profiles.Playtime.PlaytimeManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GahvilaCorePaymentProvider implements PaymentProvider {
    private PlaytimeManager playtimeManager;

    public GahvilaCorePaymentProvider() {
        this.playtimeManager = new PlaytimeManager();
    }

    @Override
    public String getProviderIdentifier() {
        return "PlaytimePaymentProvider";
    }

    @Override
    public boolean checkRequirements() {
        return Bukkit.getServer().getPluginManager().getPlugin("GahvilaCore") != null;
    }

    @Override
    public void setup() throws ProviderInitializationException {
        playtimeManager = GahvilaCore.instance.getPlaytimeManager();
    }

    @Override
    public void makeTransaction(UUID user, TransactionType type, String comment, double amount, Consumer<TransactionRecipe> callback) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(user);
        long amountLong = (long) amount;

        getClaimedPlaytime(player).thenAccept(claimedPlaytime -> {
            switch (type) {
                case DEPOSIT:
                    break;
                case WITHDRAW:
                    if (claimedPlaytime >= amountLong) {
                        callback.accept(new TransactionRecipe(user, amount, comment));
                    } else {
                        callback.accept(new TransactionRecipe(user, amount, comment, "Insufficient claimed playtime"));
                    }
                    break;
            }
        }).exceptionally(ex -> {
            callback.accept(new TransactionRecipe(user, amount, comment, ex.getMessage()));
            return null;
        });
    }


    @Override
    public void getBalance(UUID user, Consumer<Double> callback) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(user);

        getClaimedPlaytime(player).thenAccept(claimedPlaytime -> {
            double balance = 3600 + (double) claimedPlaytime;

            callback.accept(balance);
        }).exceptionally(ex -> {
            callback.accept(3600.0);
            return null;
        });
    }

    private CompletableFuture<Long> getClaimedPlaytime(OfflinePlayer player) {
        return playtimeManager.getPlaytime(player).thenApplyAsync(playtime -> playtime);
    }
}