package me.morty.bot.controls.commands;

import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;
import me.morty.bot.lavaplayer.GuildMusicManager;
import me.morty.bot.lavaplayer.PlayerManager;

import java.util.List;

public class ClearQueue implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx);
        if (musicManager.queue.isEmpty()) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription("В данный момент очередь воспроизведения пустая"));
            return;
        }
        musicManager.queue.clear();
        ctx.getChannel().sendMessage("Очередь воспроизведения очищена").queue();
    }

    @Override
    public String getName() {
        return "clearqueue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("cq");
    }
}
