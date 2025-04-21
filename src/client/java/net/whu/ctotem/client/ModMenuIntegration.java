package net.whu.ctotem.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

import static net.whu.ctotem.client.CtotemClient.activate;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Ctotem Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Ctotem"))
                        .tooltip(Text.literal("Category tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Активация"))
                                .description(OptionDescription.of(Text.literal("Активация мода")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Включить"))
                                        .description(OptionDescription.of(Text.literal("Мод будет работать.")))
                                        .binding(
                                                true,
                                                () -> activate,
                                                newVal -> activate = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}