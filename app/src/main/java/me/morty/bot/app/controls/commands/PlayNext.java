package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayNext implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        String request = String.join(" ", ctx.getArgs());

        if (errorBotState(ctx)) return;
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

        PlayerManager.getInstance().loadTracks(ctx, request, true);
    }

    @Override
    public String getName() {
        return "playnext";
    }

    @Override
    public String getHelp() {
        return "Вне очереди проигрывает трек";
    }

    @Override
    public List<String> getAliases() {
        return List.of("pn");
    }
}

