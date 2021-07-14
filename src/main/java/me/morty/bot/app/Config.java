package me.morty.bot.app;


public class Config {
    public static String getToken() {
        return System.getenv("BOT_TOKEN");
    }

    public static String getPrefix() {
        return System.getenv("BOT_PREFIX");
    }

    public static String getAdmin() { return System.getenv("BOT_ADMIN"); }
}
