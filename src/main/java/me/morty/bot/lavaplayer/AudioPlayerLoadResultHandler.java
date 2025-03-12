package me.morty.bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.morty.bot.controls.CommandContext;

import java.util.List;

public class AudioPlayerLoadResultHandler implements AudioLoadResultHandler {

    private final CommandContext ctx;
    private final GuildMusicManager musicManager;

    private final boolean playNext;
    private final String authorId;
    private final String request;

    public AudioPlayerLoadResultHandler(CommandContext ctx, boolean playNext, String authorId, String request) {
        this.ctx = ctx;
        this.musicManager = PlayerManager.getMusicManager(ctx);
        this.playNext = playNext;
        this.authorId = authorId;
        this.request = request;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        scheduleTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        List<AudioTrack> tracks = playlist.getTracks();
        if (playlist.isSearchResult()) {
            scheduleTrack(tracks.getFirst());
        } else {
            for (AudioTrack track : tracks) {
                musicManager.queue(track);
            }
            sendMessage(String.format("В очередь добавлен плейлист: `%s` треков из плейлиста [%s](%s)",
                    tracks.size(), playlist.getName(), request));
        }
    }

    @Override
    public void noMatches() {
        if (ctx.getChannel() != null) ctx.getChannel().sendMessage("Увы, совпадений не найдено").queue();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        if (ctx.getChannel() != null)
            ctx.getChannel().sendMessage("Произошла непредвиденная ошибка загрузки трека").queue();
    }

    private void scheduleTrack(AudioTrack track) {
        String prefix;
        if (playNext) {
            musicManager.playNext(track);
            prefix = "**В начало очереди** добавлен трек:  ";
        } else {
            musicManager.queue(track);
            prefix = "Добавлен трек:  ";
        }

        sendMessage(prefix + String.format("[%s](%s)", track.getInfo().title, track.getInfo().uri));
    }

    private void sendMessage(String description) {
        ctx.send(builder -> builder
                .setColor(0x815ab2)
                .setTitle("Воспроизведение")
                .setDescription(description)
                .appendDescription(String.format(" (запросил <@%s>)", authorId))
        );
    }
}
