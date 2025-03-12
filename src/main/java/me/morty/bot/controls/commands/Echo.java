package me.morty.bot.controls.commands;

import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.List;

public class Echo implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("Мне нечего ответить.").queue();
            return;
        }

        channel.sendMessageFormat(String.join(" ", args)).queue();
    }

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getHelp() {
        return " Отвечает тем же сообщением";
    }
}

