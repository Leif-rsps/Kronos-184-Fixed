package io.ruin.services.discord.impl;/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 3/24/2020
 */

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PrivateMessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getAuthor().isBot())
            return;

        e.getChannel().sendMessage("Please use all commands in a public channel!").queue();

    }
}
