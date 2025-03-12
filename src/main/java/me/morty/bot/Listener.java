package me.morty.bot;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger((Listener.class));
    public final String prefix = Config.getPrefix();

    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready for work", event.getJDA().getSelfUser().getAsTag());
        LOGGER.info("Current Prefix: {}", prefix);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        if (message.startsWith(prefix)) {
            manager.handle(event);
        }
    }
}
