package net.whu.ctotem.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
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
                        .tooltip(Text.literal("Ctotem"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("ctotem.group.activate"))
                                .description(OptionDescription.of(Text.translatable("ctotem.group.activate.desc")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("ctotem.option.activate"))
                                        .description(OptionDescription.of(Text.translatable("ctotem.option.activate.desc")))
                                        .binding(
                                                true,
                                                () -> CtotemConfig.HANDLER.instance().activate,
                                                newVal -> CtotemConfig.HANDLER.instance().activate = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("ctotem.group.settings"))
                                .description(OptionDescription.of(Text.translatable("ctotem.group.settings.desc")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("ctotem.option.exit"))
                                        .description(OptionDescription.of(Text.translatable("ctotem.option.exit.desc")))
                                        .binding(
                                                true,
                                                () -> CtotemConfig.HANDLER.instance().exit,
                                                newVal -> CtotemConfig.HANDLER.instance().exit = newVal
                                        )
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .formatValue(val -> val ? Text.translatable("ctotem.controller.exit.true") : Text.translatable("ctotem.controller.exit.false"))
                                                .coloured(false))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                    .name(Text.translatable("ctotem.option.health"))
                                    .description(OptionDescription.of(Text.translatable("ctotem.option.health.desc")))
                                    .binding(
                                            2,
                                            () -> CtotemConfig.HANDLER.instance().lifeLine,
                                            newVal -> CtotemConfig.HANDLER.instance().lifeLine = newVal
                                    )
                                    .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                            .range(1, 20)
                                            .step(1))
                                    .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.translatable("ctotem.option.command"))
                                        .description(OptionDescription.of(Text.translatable("ctotem.option.command.desc")))
                                        .binding(
                                                "lobby",
                                                () -> CtotemConfig.HANDLER.instance().lifeCommand,
                                                newVal -> CtotemConfig.HANDLER.instance().lifeCommand = newVal
                                        )
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .save(CtotemConfig.HANDLER::save)
                .build()
                .generateScreen(parentScreen);
    }
}