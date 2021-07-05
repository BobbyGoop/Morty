package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import java.net.MalformedURLException;
import java.net.URL;

public class Play implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        String request = String.join(" ", ctx.getArgs());

        if (!ctx.getMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription(String.format("<@%s>, Вы должны находиться в голосовом канале", ctx.getAuthor().getId())));
            return;
        }
        if (!ctx.getSelfMember().getVoiceState().inVoiceChannel()) {
            audioManager.openAudioConnection(ctx.getMember().getVoiceState().getChannel());
            channel.sendMessageFormat(String.format("Присоединяюсь к `\uD83d\uDD0A %s`",
                    ctx.getMember().getVoiceState().getChannel().getName())).queue();

        }

        try {
            new URL(request);
        } catch (MalformedURLException e) {
            request = "ytsearch:" + request;
        }

        PlayerManager.getInstance().loadTracks(ctx, request, false);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Проигрывает определенную песню по запросу либо по ссылке либо добавляет плейлист\n" +
                "Использование: |префикс|play <ссылка либо или название>";
    }
}
