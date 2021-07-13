package me.morty.bot.app.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.morty.bot.app.controls.CommandContext;
import net.dv8tion.jda.api.entities.Guild;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadTracks (CommandContext ctx, String trackUrl, Boolean playnext) {
        final GuildMusicManager musicManager = this.getMusicManager(ctx.getGuild());
        final String authorID = ctx.getAuthor().getId();
        String header = "";
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (playnext) {
                    musicManager.scheduler.playNext(track);
                    ctx.send(builder -> builder
                            .setColor(0x815ab2)
                            .setTitle("Воспроизведение")
                            .setDescription("**В начало очереди** добавлен трек:  ")
                            .appendDescription(String.format("[%s](%s)", track.getInfo().title, trackUrl))
                            .appendDescription(String.format(", запрошенный <@%s>", authorID)));

                }
                else {
                    musicManager.scheduler.queue(track);
                    ctx.send(builder -> builder
                            .setColor(0x815ab2)
                            .setTitle("Воспроизведение")
                            .setDescription("Добавлен трек:  ")
                            .appendDescription(String.format("[%s](%s)", track.getInfo().title, trackUrl))
                            .appendDescription(String.format(", запрошенный <@%s>", authorID)));
                    }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();
                if (playlist.isSearchResult()) {
                    AudioTrack firstFound = tracks.get(0);
                    if (playnext) {
                        musicManager.scheduler.playNext(firstFound);
                        ctx.send(builder -> builder
                                .setColor(0x815ab2)
                                .setTitle("Воспроизведение")
                                .setDescription("**В начало очереди** добавлен трек:  ")
                                .appendDescription(String.format("[%s](%s)", firstFound.getInfo().title, firstFound.getInfo().uri))
                                .appendDescription(String.format(", запросил <@%s>", authorID)));
                    }
                    else {
                        musicManager.scheduler.queue(firstFound);
                        ctx.send(builder -> builder
                                .setColor(0x815ab2)
                                .setTitle("Воспроизведение")
                                .setDescription("Добавлен трек:  ")
                                .appendDescription(String.format("[%s](%s)", firstFound.getInfo().title, firstFound.getInfo().uri))
                                .appendDescription(String.format(", запросил <@%s>", authorID)));
                    }
                } else {
                    for (AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }
                    ctx.send(builder -> builder
                            .setColor(0x815ab2)
                            .setTitle("Воспроизведение")
                            .setDescription("В очередь добавлен плейлист: ")
                            .appendDescription(String.format("`%s` треков из плейлиста [%s](%s)", tracks.size(), playlist.getName(), trackUrl))
                            .appendDescription(String.format(" (запрошены <@%s>)", authorID)));
                }
            }

            @Override
            public void noMatches() {
                if (ctx.getChannel() != null) ctx.getChannel().sendMessage("Увы, совпадений не найдено").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if (ctx.getChannel() != null) ctx.getChannel().sendMessage("Произошла непредвиденная ошибка загрузки трека").queue();
            }
        });
    }


    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
