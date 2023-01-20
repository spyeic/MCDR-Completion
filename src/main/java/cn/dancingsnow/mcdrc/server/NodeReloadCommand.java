package cn.dancingsnow.mcdrc.server;

import cn.dancingsnow.mcdrc.networking.CommandNetwork;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;

public class NodeReloadCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(
                literal("mcdrc")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(literal("reload").executes(context -> {
                            context.getSource().sendFeedback(new LiteralText("Reloading nodes..."), true);
                            MCDRCommandServer.loadNodeData();
                            context.getSource().getMinecraftServer().getPlayerManager().getPlayerList().forEach(player -> {
                                CommandNetwork.sendNodeDataToClient(player.networkHandler, MCDRCommandServer.getNodeData());
                            });
                            return 1;
                        }))
        );
    }
}
