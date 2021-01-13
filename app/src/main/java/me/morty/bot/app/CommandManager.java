package me.morty.bot.app;

import me.morty.bot.app.command.CommandContext;
import me.morty.bot.app.command.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    // CONSTRUCTOR
    public CommandManager() {
        findCommands();
    }

    // PRIVATE FIELDS
    // Logger, needed to log information
    private static final Logger log = LoggerFactory.getLogger(CommandManager.class);

    // Hash-set that contains commands
    private final Set<ICommand> commands = new HashSet<>();

    // Maps command name and all its aliases to specific command implementation.
    // Keys are lower-case to simplify user inout matching
    private final Map<String, ICommand> aliases = new HashMap<>();

    // PUBLIC METHODS
    /**
     * Get command
     *
     * @param search command name or alias
     * @return command instance if exists, null otherwise
     */
    public ICommand getCommand(String search) {
        return aliases.get(search.toLowerCase());
    }

    /**
     * Handle user event
     *
     * @param event incoming event
     */
    void handle(GuildMessageReceivedEvent event) {
        // Received message from user
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.getPrefix()), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);
        if (cmd != null) {
            //event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);
            cmd.handle(ctx);
        }
    }

    // PRIVATE METHODS

    /**
     * Scan all annotated classes and save its instances
     *
     */
    private void findCommands() {
        Reflections reflections = new Reflections("me.morty.bot.app.command.commands");

        Set<Class<? extends ICommand>> allClasses = reflections.getSubTypesOf(ICommand.class);
        for (Class<? extends ICommand> cmdClazz : allClasses) {
            try {
                ICommand cmd = getCommandInstance(cmdClazz);
                addCommand(cmd);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get instance of ICommand from Class reference
     *
     * @param cmdClazz ICommand Class
     * @return ICommand instance
     */
    private ICommand getCommandInstance(
            Class<? extends ICommand> cmdClazz
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        try {
            return cmdClazz.getConstructor(CommandManager.class).newInstance(this);
        } catch (NoSuchMethodException ignored) {
            return cmdClazz.getConstructor().newInstance();
        }
    }

    /**
     * Add Command if it not already exists
     *
     * @param cmd command to add
     */
    private void addCommand(ICommand cmd) {
        // Holds command name and all its aliases in lower case
        Set<String> allNames = new HashSet<>();

        // Put command name in set
        allNames.add(cmd.getName().toLowerCase());

        // Put all command aliases in set
        /*
         * Function forEach in the usual view
         * for (String alias : cmd.getAliases()){
         *             allNames.add(alias.toLowerCase());
         *         }
         */
        cmd.getAliases().forEach(alias -> allNames.add(alias.toLowerCase()));

        // Check that any of that names has not already registered
        if (allNames.stream().anyMatch(aliases::containsKey)) {
            throw new IllegalArgumentException("Такая команда уже существует");
        }

        // Adding to commands hash list
        commands.add(cmd);

        // Save command under all of its names
        allNames.forEach(trigger -> aliases.put(trigger, cmd));

        log.info("Saved command '{}' with aliases: [{}]", cmd.getName(), String.join(",", cmd.getAliases()));
    }

    public Collection<ICommand> getAllCommands() {
        return commands;
    }
}
