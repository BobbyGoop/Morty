package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import me.morty.bot.app.lavaplayer.PlayerManager;

public class Play implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        String request = String.join(" ", ctx.getArgs());

        if (!ctx.getMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setDescription(String.format("<@%s>, Вы должны находиться в голосовом канале", ctx.getAuthor().getId())));
            return;
        }
        
        PlayerManager.getMusicManager(ctx).loadTracks(ctx, request, false);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Проигрывает определенную песню по запросу либо по ссылке либо добавляет плейлист\n" +
                "Использование: |префикс|play <ссылка либо или название>";
    }
}
