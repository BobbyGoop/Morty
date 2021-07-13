package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

public class Continue implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (errorBotState(ctx)) return;
        if (musicManager.audioPlayer.getPlayingTrack() == null) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription("В данный момент бот ничего не проигрывает"));
            return;
        }

        musicManager.scheduler.player.setPaused(false);
        // musicManager.scheduler.queue.clear();
        ctx.getChannel().sendMessage("Продолжаю проигрывать музыку").queue();
    }

    @Override
    public String getName() {
        return "continue";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
