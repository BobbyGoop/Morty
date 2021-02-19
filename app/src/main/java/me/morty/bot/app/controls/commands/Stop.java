package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;
import java.util.List;

public class Stop implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (errorBotState(ctx)) return;
        if (musicManager.audioPlayer.getPlayingTrack() == null) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription("В данный момент бот ничего не проигрывает"));
            return;
        }

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        ctx.send(builder -> builder.setColor(0x7289da).setDescription("Воспроизведение остановлено"));
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Останавливает воспроизведение";
    }

    @Override
    public List<String> getAliases() {
        return List.of("st");
    }
}
