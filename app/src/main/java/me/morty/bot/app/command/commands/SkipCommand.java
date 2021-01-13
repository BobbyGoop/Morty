package me.morty.bot.app.command.commands;

import me.morty.bot.app.command.CommandContext;
import me.morty.bot.app.command.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

import java.util.List;

public class SkipCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (!ctx.getMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription(String.format("<@%s>, Вы должны находиться в голосовом канале", ctx.getAuthor().getId())));
            return;
        } else if (ctx.getSelfMember().getVoiceState().inVoiceChannel() && ctx.getMember().getVoiceState().equals(ctx.getSelfMember().getVoiceState())) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription(String.format("<@%s>, бот Morty и так трудится в голосовом канале", ctx.getAuthor().getId())));
            return;
        } else if (!ctx.getSelfMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription("Бот должен находиться в голосовом канале"));
            return;
        }

        if (musicManager.audioPlayer.getPlayingTrack() == null) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription("В данный момент бот ничего не проигрывает"));
            return;
        } else {
            musicManager.scheduler.nextTrack();
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
