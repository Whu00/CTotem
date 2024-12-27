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

        // Регистрируем обработчик на каждый тик клиента
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                // Если игрок существует, сбрасываем флаг сброса
                stateReset = false;
                handleHealthCheck(client);
            } else if (!stateReset) {
                // Выполняем сброс состояния только один раз
                resetState();
            }
        });
    }

    private void handleHealthCheck(MinecraftClient client) {
        PlayerEntity player = client.player;
        float health = player.getHealth();

        // Пропускаем первую проверку после входа
        if (!initialized) {
            lastHealth = health;
            initialized = true;
            log.info("Initial health recorded: " + health);
            return;
        }

        // Проверяем изменения здоровья
        if (health != lastHealth) {
            log.info("Health changed: " + health);

            // Если здоровье увеличилось или уменьшилось, проверяем условия
            if (health >= 2.0f && !n) {
                n = true;
                log.info("Количество жизней игрока было больше 2");
            }

            if (n && health < lastHealth && health <= 2.0f) {
                client.player.networkHandler.sendChatCommand("lobby");
                log.info("Отправлена команда LOBBY");
                n = false; // Сбрасываем флаг после выполнения команды
            }

            lastHealth = health; // Обновляем последнее значение здоровья
        }
    }

    private void resetState() {
        log.info("Resetting state...");
        n = false;
        initialized = false;
        lastHealth = 0;
        stateReset = true; // Помечаем, что сброс выполнен
    }
}
