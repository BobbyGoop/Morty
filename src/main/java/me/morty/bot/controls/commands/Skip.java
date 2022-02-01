package me.morty.bot.controls.commands;

import me.morty.bot.controls.ICommand;
import me.morty.bot.lavaplayer.GuildMusicManager;
import me.morty.bot.lavaplayer.PlayerManager;
import me.morty.bot.controls.CommandContext;

import java.util.List;

public class Skip implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx);

        if (errorBotState(ctx)) return;

        if (musicManager.player.getPlayingTrack() == null) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription("В данный момент бот ничего не проигрывает"));
        } else {
            musicManager.nextTrack();
        }
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Пропускает трек";
    }

    @Override
    public List<String> getAliases() {
        return List.of("sk");
    }

}
