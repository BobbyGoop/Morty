package me.morty.bot.controls.commands;

import me.morty.bot.controls.ICommand;
import me.morty.bot.lavaplayer.GuildMusicManager;
import me.morty.bot.lavaplayer.PlayerManager;
import me.morty.bot.controls.CommandContext;

public class Continue implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx);

        if (errorBotState(ctx)) return;
        if (musicManager.player.getPlayingTrack() == null) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription("В данный момент бот ничего не проигрывает"));
            return;
        }

        musicManager.player.setPaused(false);
        // musicManager.scheduler.queue.clear();
        ctx.getChannel().sendMessage("Продолжаю проигрывать музыку").queue();
    }

    @Override
    public String getName() {
        return "continue";
    }

}
