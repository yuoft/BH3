package com.yuo.bh3;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class Config {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ServerConfig SERVER;
    public static ClientConfig CLIENT;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair1 = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair1.getLeft();
            CLIENT_CONFIG = specPair1.getRight();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER_CONFIG = specPair.getRight();
            SERVER = specPair.getLeft();
        }
    }

    public static class ServerConfig{

        public final ForgeConfigSpec.BooleanValue isRenderGui;
        public final ForgeConfigSpec.BooleanValue isRenderFixed;
        public final ForgeConfigSpec.BooleanValue isRenderGround;

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Item is render Config").push("general");
            this.isRenderGui = buildBoolean(builder, "isRenderGui", true, "obj is render?");
            this.isRenderFixed = buildBoolean(builder, "isRenderFixed", false, "obj is render?");
            this.isRenderGround = buildBoolean(builder, "isRenderGround", true, "obj is render?");
            builder.pop();

        }
    }

    public static class ClientConfig{
        public ClientConfig(ForgeConfigSpec.Builder builder){

        }
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> buildConfig(ForgeConfigSpec.Builder builder, String name, String comment){
        return builder.comment(comment).translation(name).defineList(name, Collections.emptyList(), s -> s instanceof String && ResourceLocation.tryParse((String) s) != null);
    }

}
