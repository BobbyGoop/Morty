package me.morty.bot.app.controls;

import java.util.List;

// Интерфейс комманды
public interface ICommand {

    // Обрабатывает команду и Отправляет сообщение в ctx
    void handle(CommandContext ctx);

    // Возвращает название команды без префикса
    String getName();
    // Нужна для команды помощи
    String getHelp();

    // Псевдонимы команды
    default List<String> getAliases(){
        return List.of();
    }
}
