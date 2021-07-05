package me.morty.bot.app.controls.commands;

import me.morty.bot.app.CommandManager;
import me.morty.bot.app.Config;
import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Help implements ICommand {

    public Help(CommandManager manager) {
        this.manager = manager;
    }

    private final CommandManager manager;

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        List<String> commandNames = new ArrayList<>();
        String prefix = Config.getPrefix();
        if (args.isEmpty()) {


            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (name) -> commandNames.add("`" +Config.getPrefix() + name + "`"));

            ctx.send(builder -> builder.setColor(0x815ab2)
                        .setTitle("Команды")
                        .setDescription("Префикс для комманд: `" + prefix + "`")
                        .addField("Полный список", manager.getCommands().stream()
                                        .map(cmd -> "`" + prefix + cmd.getName() + "`")
                                        .collect(Collectors.joining(" ")),true)
            );
            return;
        }

        ICommand command = manager.getCommand(args.get(0));

        if (command == null) {
            ctx.send(builder -> builder.setColor(0x815ab2).setDescription("Не было найдено команды по запросу"));
        } else {
            ctx.send(builder -> builder.setColor(0x815ab2)
                    .setTitle(Config.getPrefix() + command.getName())
                    .addField("Описание: ", command.getHelp(), true)
                    .addField("Сокращения: ",command.getAliases().stream()
                            .map(cmd -> "`" + prefix + cmd + "`")
                            .collect(Collectors.joining(" ")), true)
            );
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
