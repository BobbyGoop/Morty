package me.morty.bot;

import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    // CONSTRUCTOR
    public CommandManager() {
        parseCommands();
    }

    // PRIVATE FIELDS
    // Logger, needed to log information
    private static final Logger log = LoggerFactory.getLogger(CommandManager.class);

    // Hash-set that contains commands
    private final Set<ICommand> commands = new HashSet<>();

    // Maps controls name and all its aliases to specific controls implementation.
    // Keys are lower-case to simplify user inout matching
    private final Map<String, ICommand> aliases = new HashMap<>();

    // PUBLIC METHODS
    /**
     * Get controls
     *
     * @param search controls name or alias
     * @return controls instance if exists, null otherwise
     */
    public ICommand getCommand(String search) {
        return aliases.get(search.toLowerCase());
    }

    /**
     * Handle user event
     *
     * @param event incoming event
     */
    void handle(MessageReceivedEvent event) {
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
    private void parseCommands() {
        Reflections reflections = new Reflections("me.morty.bot.controls.commands");

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
     * @param cmd controls to add
     */
    private void addCommand(ICommand cmd) {
        // Holds controls name and all its aliases in lower case
        Set<String> allNames = new HashSet<>();

        // Put controls name in set
        allNames.add(cmd.getName().toLowerCase());

        // Put all controls aliases in set
        cmd.getAliases().forEach(alias -> allNames.add(alias.toLowerCase()));

        // Check that any of that names has not already registered
        if (allNames.stream().anyMatch(aliases::containsKey)) {
            throw new IllegalArgumentException("Такая команда уже существует");
        }

        // Adding to commands hash list
        commands.add(cmd);

        // Save controls under all of its names
        allNames.forEach(trigger -> aliases.put(trigger, cmd));

        log.info("Saved controls '{}' with aliases: [{}]", cmd.getName(), String.join(",", cmd.getAliases()));
    }

    public Collection<ICommand> getCommands() {
        return commands;
    }
}
