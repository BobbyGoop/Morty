package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

import java.util.List;

public class NowPlaying implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx);
        if (!ctx.getMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription(String.format("<@%s>, Вы должны находиться в голосовом канале", ctx.getAuthor().getId())));
            return;
        }
        if (musicManager.player.getPlayingTrack() == null){
            ctx.getChannel().sendMessage("В данный момент ничего не проигрывается").queue();
            return;
        }


        ctx.send(builder -> builder.setColor(0x815ab2)
                .addField("Сейчас играет: ",
                        String.format("[%s](%s)",
                                musicManager.player.getPlayingTrack().getInfo().title,
                                musicManager.player.getPlayingTrack().getInfo().uri), false)
        );
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public List<String> getAliases(){
        return List.of("np");
    }
}
