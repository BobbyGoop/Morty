package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.GuildMusicManager;
import me.morty.bot.app.lavaplayer.PlayerManager;

import java.util.List;

public class ClearQueue implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx);
        if (musicManager.queue.size() == 0) {
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
