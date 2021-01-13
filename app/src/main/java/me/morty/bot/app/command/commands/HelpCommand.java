package me.morty.bot.app.command.commands;

import me.morty.bot.app.CommandManager;
import me.morty.bot.app.Config;
import me.morty.bot.app.command.CommandContext;
import me.morty.bot.app.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    private final CommandManager manager;

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        //переделать
        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(" Список доступных команд: \n");
            manager.getAllCommands().stream().map(ICommand::getName).forEach(
                    (name) -> builder.append('`').append(Config.getPrefix()).append(name).append("`\n")
            );
            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            ctx.send(builder -> builder.setColor(0x7289da).setDescription("Не было найдено команды по запросу"));
        } else {
            channel.sendMessage('`' + Config.getPrefix() +
                    command.getName() + "`:" + command.getHelp()).queue();
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Отображает список команд, доступных боту";
    }

    @Override
    public List<String> getAliases() {
        return List.of("h", "hp", "hlp");
    }

}
