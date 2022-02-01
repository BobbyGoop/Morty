package me.morty.bot.lavaplayer;

import me.morty.bot.controls.CommandContext;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    private PlayerManager() {}

    public static GuildMusicManager getMusicManager(CommandContext ctx) {
        return musicManagers.computeIfAbsent(ctx.getGuild().getIdLong(), (guildID) -> new GuildMusicManager(ctx));
    }
}
