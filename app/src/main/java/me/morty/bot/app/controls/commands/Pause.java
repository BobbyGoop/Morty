package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;
import java.util.List;

public class Pause implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (errorBotState(ctx)) return;
        if (musicManager.audioPlayer.getPlayingTrack() == null) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription("В данный момент бот ничего не проигрывает"));
            return;
        }

        musicManager.scheduler.player.setPaused(true);
       // musicManager.scheduler.queue.clear();
        ctx.getChannel().sendMessage("Воспроизведение остановлено").queue();
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp() {
        return "Останавливает воспроизведение";
    }

}
