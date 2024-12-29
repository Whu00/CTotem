package net.whu.ctotem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CtotemClient implements ClientModInitializer {

    private static final Logger log = LoggerFactory.getLogger(CtotemClient.class);
    private boolean n = false;
    private boolean initialized = false;
    private boolean stateReset = true;
    private float lastHealth = 0;

    @Override
    public void onInitializeClient() {
        log.info("onInitializeClient() works");

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
        PlayerEntity player = client.player;
        float health = player.getHealth();

        if (!initialized) {
            lastHealth = health;
            initialized = true;
            log.info("Initial health recorded: " + health);
            return;
        }

        if (health != lastHealth) {
            log.info("Health changed: " + health);

            if (health >= 2.0f && !n) {
                n = true;
                log.info("Количество жизней игрока было больше 2");
            }

            if (n && health < lastHealth && health <= 2.0f) {
                client.player.networkHandler.sendChatCommand("lobby");
                log.info("Отправлена команда LOBBY");
                n = false;
            }

            lastHealth = health;
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
