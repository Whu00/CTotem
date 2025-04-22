package net.whu.ctotem.client;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class CtotemConfig {
    public static final ConfigClassHandler<CtotemConfig> HANDLER = ConfigClassHandler.createBuilder(CtotemConfig.class)
            .id(Identifier.tryParse("ctotemconfig", "ctotem_config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("ctotem.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean activate = true;

    @SerialEntry
    public boolean exit = true;

    @SerialEntry
    public int lifeLine = 2;

    @SerialEntry
    public String lifeCommand = "lobby";
}
