package net.whu.ctotem.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.minecraft.text.Text;

import static net.whu.ctotem.client.CtotemClient.*;

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
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Настройки"))
                                .description(OptionDescription.of(Text.literal("Настройки мода")))
                                .option(Option.<Integer>createBuilder()
                                    .name(Text.literal("Количество жизней"))
                                    .description(OptionDescription.of(Text.literal("Количество жизней при котором будет отправляться команда")))
                                    .binding(
                                            2,
                                            () -> lifeLine,
                                            newVal -> lifeLine = newVal
                                    )
                                    .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                            .range(0, 20)
                                            .step(1))
                                    .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Команда"))
                                        .description(OptionDescription.of(Text.literal("Какую команду отправлять в чат при маленьком количестве жизней")))
                                        .binding(
                                                "lobby",
                                                () -> lifeCommand,
                                                newVal -> lifeCommand = newVal
                                        )
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}