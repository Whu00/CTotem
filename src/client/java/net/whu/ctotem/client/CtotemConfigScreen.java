package net.whu.ctotem.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CtotemConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Настройки MyFabricMod"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        builder.getOrCreateCategory(Text.of("Общее"))
                .addEntry(entryBuilder.startStrField(Text.of("Команда"), CtotemConfig.command)
                        .setDefaultValue("lobby")
                        .setSaveConsumer(newValue -> CtotemConfig.command = newValue)
                        .build())
                .addEntry(entryBuilder.startFloatField(Text.of("Жизни"), CtotemConfig.healthLevel)
                        .setDefaultValue(2.0f)
                        .setSaveConsumer(newValue -> CtotemConfig.healthLevel = newValue)
                        .build());
        builder.setSavingRunnable(() -> {
            saveConfig();
        });
        return builder.build();
    }
    public static void saveConfig() {

    }
}
