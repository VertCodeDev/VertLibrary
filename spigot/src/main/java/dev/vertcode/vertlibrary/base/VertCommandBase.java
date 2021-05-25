


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.base;

import dev.vertcode.vertlibrary.standalone.config.GenericLang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class VertCommandBase implements CommandExecutor, TabCompleter {

    private final String command, permission;
    private final boolean mustBePlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (permission != null && !sender.hasPermission(this.permission)) {
            sender.sendMessage(GenericLang.NO_PERMISSIONS.getString());
            return true;
        }

        if (this.mustBePlayer && !(sender instanceof Player)) {
            sender.sendMessage(GenericLang.MUST_BE_PLAYER.getString());
            return true;
        }

        try {
            this.execute(sender, command, args);
        } catch (Exception ex) {
            sender.sendMessage(GenericLang.COMMAND$SOMETHING_WENT_WRONG.getString());
            ex.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabComplete = this.onTabComplete(sender, command, args);
        return tabComplete == null ? new ArrayList<>() : tabComplete;
    }

    protected abstract void execute(CommandSender sender, Command command, String[] args);

    protected abstract List<String> onTabComplete(CommandSender sender, Command command, String[] args);
}
