/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.base;

import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.conf.GenericLang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class VertCommandBase implements SimpleCommand, CommandMeta {

    private final String command, permission;
    private final String[] aliases;
    private final boolean mustBePlayer;

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();
        if (permission != null && !sender.hasPermission(this.permission)) {
            sender.sendMessage(ChatUtils.getInstance().colorize(GenericLang.NO_PERMISSIONS.getString()));
            return;
        }

        if (this.mustBePlayer && !(sender instanceof Player)) {
            sender.sendMessage(ChatUtils.getInstance().colorize(GenericLang.MUST_BE_PLAYER.getString()));
            return;
        }

        try {
            this.perform(sender, args);
        } catch (Exception ex) {
            sender.sendMessage(ChatUtils.getInstance().colorize(GenericLang.COMMAND$SOMETHING_WENT_WRONG.getString()));
            ex.printStackTrace();
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        List<String> tabComplete = this.onTabComplete(invocation.source(), invocation.arguments());

        if (tabComplete == null || tabComplete.isEmpty()) return new ArrayList<>();

        String lastArg = invocation.arguments()[invocation.arguments().length - 1];

        return tabComplete.stream().filter(s -> s.startsWith(lastArg)).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAliases() {
        if (aliases == null || aliases.length < 1) return Collections.singletonList(command);

        List<String> aliases = Arrays.asList(this.aliases);
        aliases.add(command);

        return aliases;
    }

    @Override
    public Collection<CommandNode<CommandSource>> getHints() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true;
    }

    protected abstract void perform(CommandSource sender, String[] args);

    protected abstract List<String> onTabComplete(CommandSource sender, String[] args);
}
