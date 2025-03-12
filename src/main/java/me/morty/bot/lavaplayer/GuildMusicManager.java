package me.morty.bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import me.morty.bot.controls.CommandContext;
import me.morty.bot.util.Task;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class GuildMusicManager extends AudioEventAdapter {

    private static final DefaultAudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public final AudioPlayer player;
    public LinkedList<AudioTrack> queue = new LinkedList<>();

    private final CommandContext ctx;
    // TODO: Move delay value to the configuration file
    private final Task leaveTask = new Task(300L, TimeUnit.SECONDS, this::leaveVoiceChannel);

    public GuildMusicManager(CommandContext ctx) {
        this.ctx = ctx;
        this.player = audioPlayerManager.createPlayer();
        this.player.addListener(this);

        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager();
        audioPlayerManager.registerSourceManager(ytSourceManager);

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

        ctx.getGuild().getAudioManager().setSendingHandler(new AudioPlayerSendHandler(this.player));

        joinVoiceChannel();
    }

    public void loadTracks(CommandContext ctx, String request, Boolean playNext) {
        final String authorId = ctx.getAuthor().getId();

        try {
            new URL(request);
        } catch (MalformedURLException e) {
            request = "ytsearch:" + request;
        }

        final AudioPlayerLoadResultHandler handler = new AudioPlayerLoadResultHandler(ctx, playNext, authorId, request);
        audioPlayerManager.loadItemOrdered(this, request, handler);
    }

    public void queue(AudioTrack track) {
        cancelLeave();
        joinVoiceChannel();

        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public void playNext(AudioTrack track) {
        cancelLeave();
        joinVoiceChannel();

        this.queue.addFirst(track);
    }

    public boolean nextTrack() {
        cancelLeave();
        joinVoiceChannel();

        return this.player.startTrack(this.queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (nextTrack()) {
                return;
            }
        }

        queueLeave();
    }

    private void queueLeave() {
        leaveTask.start();
        player.setPaused(true);
    }

    private void cancelLeave() {
        leaveTask.stop();
        player.setPaused(false);
    }

    @SuppressWarnings("ConstantConditions")
    private void joinVoiceChannel() {
        if (!ctx.getSelfMember().getVoiceState().inAudioChannel()) {
            final GuildVoiceState memberVoiceChannel = ctx.getMember().getVoiceState();
            if (memberVoiceChannel != null) {
                ctx.getGuild().getAudioManager().openAudioConnection(memberVoiceChannel.getChannel());
                ctx.getChannel().sendMessageFormat(
                        String.format("Присоединяюсь к `\uD83d\uDD0A %s`", memberVoiceChannel.getChannel().getName())
                ).queue();
            }
            else {
                ctx.getChannel().sendMessage("Произошли неполадки").queue();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void leaveVoiceChannel() {
        if (ctx.getSelfMember().getVoiceState().inAudioChannel()) {
            final VoiceChannel memberVoiceChannel = ctx.getMember().getVoiceState().getChannel().asVoiceChannel();

            ctx.getGuild().getAudioManager().closeAudioConnection();
            // TODO: English, please
            ctx.getChannel().sendMessageFormat(
                    String.format("Ну и хуй с вами, `\uD83d\uDD0A %s`", memberVoiceChannel.getName())
            ).queue();
        }
    }

    // TODO: use this function as a reaction on starting new track
    private void sendMessage(String description) {
        ctx.send(builder -> builder
                .setColor(0x815ab2)
                .setTitle("Воспроизведение")
                .setDescription(description)
        );
    }
}
