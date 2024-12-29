package net.whu.ctotem.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);

    private static final File CONFIG_FILE = new File("config/Ctotem.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(CtotemConfig.class, writer);
            log.info("Конфиг записан");
        } catch (IOException e) {
            log.error("Ошибка при сохранении конфига: " + e.getMessage());
        }
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                CtotemConfig config = GSON.fromJson(reader, CtotemConfig.class);
                if (config != null) {
                    CtotemConfig.command = config.command;
                    CtotemConfig.healthLevel = config.healthLevel;
                    log.info("Конфигурация успешно загружена");
                }
            } catch (IOException e) {
                log.error("Ошибка при загрузке конфигурации: " + e.getMessage());
            }
        } else {
            CtotemConfig.loadDefault();
            save();
        }
    }
}
