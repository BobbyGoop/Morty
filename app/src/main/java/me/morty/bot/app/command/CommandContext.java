package me.morty.bot.app.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.List;
import java.util.function.Consumer;

public class CommandContext implements ICommandContext {

    private final GuildMessageReceivedEvent event;
    private final List<String> args;




    public CommandContext(GuildMessageReceivedEvent event, List<String> args){
        this.event = event;
        this.args = args;
    }
    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent(){
        return this.event;
    }

    public List<String> getArgs() {
        return args;
    }

    public void send(Consumer<EmbedBuilder> setup) {
        final TextChannel channel = this.getChannel();
        final EmbedBuilder builder = new EmbedBuilder();

        setup.accept(builder);
        channel.sendMessage(builder.build()).queue();
    }
}
