package de.dytanic.cloudnet.ext.bridge.bungee.command;

import de.dytanic.cloudnet.ext.bridge.BridgeConfigurationProvider;
import de.dytanic.cloudnet.ext.bridge.bungee.BungeeCloudNetHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public final class CommandHub extends Command {

    public CommandHub() {
        super("hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

        if (BungeeCloudNetHelper.isOnAFallbackInstance(proxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', BridgeConfigurationProvider.load().getMessages().get("command-hub-already-in-hub"))));
            return;
        }

        BungeeCloudNetHelper.connectToFallback(proxiedPlayer, proxiedPlayer.getServer() != null ? proxiedPlayer.getServer().getInfo().getName() : null)
                .thenAccept(connectedFallback -> {
                    if (connectedFallback != null) {
                        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                                BridgeConfigurationProvider.load().getMessages().get("command-hub-success-connect"))
                                .replace("%server%", connectedFallback.getName())
                        ));
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', BridgeConfigurationProvider.load().getMessages().get("command-hub-no-server-found"))));
                    }
                });
    }

    @Override
    public String[] getAliases() {
        return new String[]{"leave"};
    }
}