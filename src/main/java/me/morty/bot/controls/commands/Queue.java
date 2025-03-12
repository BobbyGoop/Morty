package me.morty.bot.controls.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;
import me.morty.bot.lavaplayer.GuildMusicManager;
import me.morty.bot.lavaplayer.PlayerManager;

public class Queue implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx);
        if (musicManager.queue.isEmpty()) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setTitle("Очередь воспроизведения")
                    .setDescription("В данный момент список пуст"));
            return;
        }

        // TODO: Pagination
        ctx.send(builder -> {
            builder.setColor(0x815ab2);

            String trackDescription = formatTrackString(musicManager.player.getPlayingTrack());
            builder.addField("Сейчас играет: ", trackDescription, false);

            int n = 1;
            for (AudioTrack track : musicManager.queue) {
                builder.addField("", String.format("%d) %s", n++, formatTrackString(track)), false);
            }
        });
    }

    @Override
    public String getName() {
        return "queue";
    }

    private String formatTrackString(AudioTrack track) {
        return String.format("[%s](%s)", track.getInfo().title, track.getInfo().uri);
    }
}
