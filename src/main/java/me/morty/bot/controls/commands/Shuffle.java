package me.morty.bot.controls.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;
import me.morty.bot.lavaplayer.PlayerManager;

import java.util.Collections;
import java.util.LinkedList;

public class Shuffle implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final LinkedList<AudioTrack> queue = PlayerManager.getMusicManager(ctx).queue;
        if (queue.isEmpty()) {
            ctx.getChannel().sendMessage("В данный момент очердь произведения пустая").queue();
            return;
        }

        Collections.shuffle(queue);
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