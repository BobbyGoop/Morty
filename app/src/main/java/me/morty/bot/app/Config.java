package me.morty.bot.app;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Paths;

public class Config {

    public static final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .path(Paths.get("bot.conf")) // Set where we will load and save to
            .build();
    public static CommentedConfigurationNode root;

    static {
        try {
            root = loader.load();
        } catch (final ConfigurateException e) {
            Listener.LOGGER.info("Проблемы с загрузкой конфига (bot.conf)");
            System.exit(1);
        }
    }

    public static String getToken() {
        return root.node("token").getString();
    }

    public static String getPrefix() {
        return root.node("commands", "prefix").getString();
    }

    public static Integer getAdmin() {
        return root.node("commands", "administrator").getInt();
    }
}
