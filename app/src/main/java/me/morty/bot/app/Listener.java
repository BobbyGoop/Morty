package me.morty.bot.app;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger((Listener.class));

    private final CommandManager manager = new CommandManager();
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready for work", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage()){
            return;
        }

        String prefix = Config.getPrefix();
        String message = event.getMessage().getContentRaw();
        if (message.equalsIgnoreCase(prefix) || event.getAuthor().getIdLong() == Config.getAdmin()){
            LOGGER.info("Bot is shut down");
            event.getJDA().shutdown();
            //BotCommons.shutdown(event.getJDA());
            return;
        }

        if (message.startsWith(prefix)){
            manager.handle(event);
        }
    }
}
