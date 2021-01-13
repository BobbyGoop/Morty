package me.morty.bot.app.command.commands;

import me.morty.bot.app.command.CommandContext;
import me.morty.bot.app.command.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;


public class StopCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        if (ctx.getSelfMember().getVoiceState() == null || !ctx.getSelfMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription("Бот должен находиться в голосовом чате")
                    .setTitle("Ошибка"));
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
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
}
