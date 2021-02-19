package me.morty.bot.app.controls;

import java.util.List;

// Интерфейс комманды
public interface ICommand {
    @SuppressWarnings("ConstantConditions")
    // Обрабатывает команду и Отправляет сообщение в ctx
    void handle(CommandContext ctx);

    // Возвращает название команды без префикса
    String getName();
    // Нужна для команды помощи
    String getHelp();

    default List<String> getAliases(){
        return List.of();
    }

    default boolean errorBotState(CommandContext ctx) {
        if (!ctx.getMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription(String.format("<@%s>, Вы должны находиться в голосовом канале", ctx.getAuthor().getId())));
            return true;
        }
        if (ctx.getSelfMember().getVoiceState().inVoiceChannel() && ctx.getMember().getVoiceState().equals(ctx.getSelfMember().getVoiceState())) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription(String.format("<@%s>, бот Morty и так трудится в голосовом канале", ctx.getAuthor().getId())));
            return true;
        }
        if (!ctx.getSelfMember().getVoiceState().inVoiceChannel()) {
            ctx.send(builder -> builder.setColor(0x7289da)
                    .setDescription("Бот должен находиться в голосовом канале"));
            return true;
        }
        return false;
    }
}
