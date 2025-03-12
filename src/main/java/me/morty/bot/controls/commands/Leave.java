package me.morty.bot.controls.commands;

import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;

import java.util.List;

public class Leave implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (errorBotState(ctx)) return;
        ctx.getGuild().getAudioManager().closeAudioConnection();
        ctx.send(builder -> builder.setColor(0x815ab2).setDescription("Бот покидает голосовой канал"));
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Удаляет бота из войсчата";
    }

    @Override
    public List<String> getAliases() {
        return List.of("l", "lv");
    }

}
