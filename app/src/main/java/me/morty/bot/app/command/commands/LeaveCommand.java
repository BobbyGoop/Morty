package me.morty.bot.app.command.commands;

import me.morty.bot.app.command.CommandContext;
import me.morty.bot.app.command.ICommand;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getSelfMember().getVoiceState() == null || !ctx.getSelfMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x7289da).setDescription("Бот должен находиться в голосовом чате"));
            return;
        }
        ctx.getGuild().getAudioManager().closeAudioConnection();
        ctx.send(builder -> builder.setColor(0x7289da).setDescription("Бот покидает голосовой канал"));
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
