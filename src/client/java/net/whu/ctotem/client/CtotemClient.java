package net.whu.ctotem.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CtotemClient implements ClientModInitializer {
    private static final Logger log = LoggerFactory.getLogger(CtotemClient.class);
    private boolean n = false;
    private boolean initialized = false;
    private boolean stateReset = true;
    private float lastHealth = 0;

    @Override
    public void onInitializeClient() {

        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess) -> {
            commandDispatcher.register(
                    ClientCommandManager.literal("ctotem")
                            .then(ClientCommandManager.literal("toggle")
                                    .executes(commandContext -> {
                                        boolean activate = CtotemConfig.HANDLER.instance().activate;
                                        CtotemConfig.HANDLER.instance().activate = !activate;

                                        MinecraftClient.getInstance().player.sendMessage(
                                                !activate ? Text.translatable("ctotem.command.toggle.enabled") : Text.translatable("ctotem.command.toggle.disabled"), false
                                        );
                                        CtotemConfig.HANDLER.save();
                                        return 1;
                                    }))
            );
        });

        CtotemConfig.HANDLER.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                stateReset = false;
                handleHealthCheck(client);
            } else if (!stateReset) {
                resetState();
            }
        });
    }

    private void handleHealthCheck(MinecraftClient client) {
        boolean activate = CtotemConfig.HANDLER.instance().activate;
        boolean exit = CtotemConfig.HANDLER.instance().exit;
        float lifeLine = (float) CtotemConfig.HANDLER.instance().lifeLine;
        String lifeCommand = CtotemConfig.HANDLER.instance().lifeCommand;

        if (activate) {
            PlayerEntity player = client.player;
            float health = player.getHealth();

            if (!initialized) {
                lastHealth = health;
                initialized = true;
                log.info("Initial health recorded: " + health);
                return;
            }

            if (health != lastHealth) {

                if (health >= lifeLine && !n) {
                    n = true;
                }

                if (n && health < lastHealth && health <= lifeLine) {
                    if (exit) {
                        client.player.networkHandler.sendChatCommand(lifeCommand);
                    } else {
                        client.world.disconnect();
                        client.disconnect(new DisconnectedScreen(new MultiplayerScreen(new TitleScreen()), Text.translatable("ctotem.disconnect.title"), Text.translatable("ctotem.disconnect")));
                    }
                    n = false;
                }

                lastHealth = health;
            }
        }
    }

    private void resetState() {
        log.info("Resetting state...");
        n = false;
        initialized = false;
        lastHealth = 0;
        stateReset = true;
    }
}