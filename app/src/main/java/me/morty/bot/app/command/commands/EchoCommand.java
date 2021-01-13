package me.morty.bot.app.command.commands;

import me.morty.bot.app.command.CommandContext;
import me.morty.bot.app.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.internal.StringUtil;

import java.util.List;

public class EchoCommand implements ICommand {


    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("Мне нечего ответить.").queue();
            return;
        }

        channel.sendMessageFormat(StringUtil.join(args, " ")).queue();

    }


    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getHelp(){
       return " Отвечает тем же сообщением";
    }
}

