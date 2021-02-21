package me.morty.bot.app.controls.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

public class Queue implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        if (musicManager.scheduler.queue.size() == 0) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setTitle("Очередь воспроизведения")
                    .setDescription("В данный момент список пуст"));
            return;
        }

        StringBuilder description = new StringBuilder();
        int number = 1;
        for (AudioTrack track : musicManager.scheduler.queue){
            description.append(number).append(") ").append(
                    String.format("[%s](%s)",track.getInfo().title,track.getInfo().uri))
                    .append("\n");
            number++;
        }
        ctx.send(builder -> builder.setColor(0x7289da)
                .addField("Сейчас играет: ",
                        String.format("[%s](%s)",
                                musicManager.audioPlayer.getPlayingTrack().getInfo().title,
                                musicManager.audioPlayer.getPlayingTrack().getInfo().uri), false)
                .addField("Очередь", String.valueOf(description), true)
        );
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "null";
    }
}
