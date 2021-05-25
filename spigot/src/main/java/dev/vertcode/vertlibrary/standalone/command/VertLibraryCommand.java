


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.standalone.command;

import dev.vertcode.vertlibrary.VertLibrary;
import dev.vertcode.vertlibrary.base.VertCommandBase;
import dev.vertcode.vertlibrary.base.VertPluginBase;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.standalone.gui.MainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class VertLibraryCommand extends VertCommandBase {

    public VertLibraryCommand() {
        super("vertlibrary", null, false);
    }

    @Override
    protected void execute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("VertLibrary.Admin.GUI")) {
            sender.sendMessage(ChatUtils.getInstance().colorize((sender instanceof Player ? "" : "\n") +
                    "&8&m----------&r&8[ &c&lVertLibrary &8]&m----------\n" +
                    "&f* &cAuthors &8» &f" +
                    String.join(", ", VertLibrary.getInstance().getDescription().getAuthors()) + "\n" +
                    "&f* &cDiscord &8» &fvertcode.dev/discord\n" +
                    "&f* &cWebsite &8» &fComing Soon\n" +
                    "&f* &cLoaded Plugins &8» &f" + VertLibrary.getInstance().getLoadedPlugins().size() + "\n" +
                    "&f* &cLoaded Commands &8» &f" + VertLibrary.getInstance().getLoadedCommands().values().stream()
                    .map(List::size).flatMapToInt(IntStream::of).sum() + "\n" +
                    "&f* &cLoaded Listeners &8» &f" + VertLibrary.getInstance().getLoadedPlugins().values().stream()
                    .map(VertPluginBase::getTotalListeners).flatMapToInt(IntStream::of).sum() + "\n" +
                    "&8&m----------&r&8[ &c&lVertLibrary &8]&m----------"));
            return;
        }

        new MainGUI().open((Player) sender);
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, Command command, String[] args) {
        return Arrays.asList("Made", "By", "VertCode");
    }
}
