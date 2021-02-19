package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

import java.util.List;

public class Skip implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (errorBotState(ctx)) return;
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
