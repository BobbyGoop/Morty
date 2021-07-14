package me.morty.bot.app.lavaplayer;

import me.morty.bot.app.controls.CommandContext;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    private PlayerManager() {}

    public static GuildMusicManager getMusicManager(CommandContext ctx) {
        return musicManagers.computeIfAbsent(ctx.getGuild().getIdLong(), (guildID) -> new GuildMusicManager(ctx));
    }
}
