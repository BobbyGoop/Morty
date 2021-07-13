package me.morty.bot.app.controls.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class Shuffle implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        if (musicManager.scheduler.queue.size() == 0) {
            ctx.getChannel().sendMessage("В данный момент очердь произведения пустая").queue();
            return;
        }
        List<AudioTrack> shuffledQueue = new ArrayList<>(musicManager.scheduler.queue);
        Collections.shuffle(shuffledQueue);
        musicManager.scheduler.queue = new LinkedBlockingDeque<>(shuffledQueue);
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getHelp() {
        return "Shuffles the queue";
    }
}