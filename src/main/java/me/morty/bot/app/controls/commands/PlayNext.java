package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.PlayerManager;

import java.util.List;

public class PlayNext implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        String request = String.join(" ", ctx.getArgs());

        if (errorBotState(ctx)) return;

        PlayerManager.getMusicManager(ctx).loadTracks(ctx, request, true);
    }

    @Override
    public String getName() {
        return "playnext";
    }

    @Override
    public String getHelp() {
        return "Вне очереди проигрывает трек";
    }

    @Override
    public List<String> getAliases() {
        return List.of("pn");
    }
}

