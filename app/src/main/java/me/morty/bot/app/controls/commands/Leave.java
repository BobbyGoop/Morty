package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;

import java.util.List;

public class Leave implements ICommand {
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
