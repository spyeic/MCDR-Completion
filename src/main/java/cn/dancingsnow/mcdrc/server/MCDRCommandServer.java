package cn.dancingsnow.mcdrc.server;

import cn.dancingsnow.mcdrc.command.NodeData;
import cn.dancingsnow.mcdrc.config.ModConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraft.server.command.CommandManager.literal;

public class MCDRCommandServer implements DedicatedServerModInitializer {
    public static final String MOD_ID = "mcdrc";
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    public static Logger LOGGER = LogManager.getLogger();
    public static ModConfig modConfig = new ModConfig(
            // create config dir if not exist
            FabricLoader.getInstance().getConfigDir().resolve("%s.json".formatted(MOD_ID))
    );
    private static NodeData nodeData = null;

    @Override
    public void onInitializeServer() {
        if (!modConfig.load()) {
            LOGGER.error("MCDR-Completion load config fail.");
            throw new IllegalStateException("MCDR-Completion init server fail");
        }

        modConfig.save();

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> dispatcher.register(
                literal("mcdrc")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(literal("reload").executes(context -> {
                            context.getSource().sendFeedback(new LiteralText("Reloading nodes..."), true);
                            loadNodeData();
                            return 1;
                        })))));

        // TODO: remove this
        NodeChangeWatcher.init();
        loadNodeData();
    }

    public static void loadNodeData() {
        try {
            NodeData data = GSON.fromJson(Files.newBufferedReader(Path.of(modConfig.getNodePath())), NodeData.class);
            if (data != null) nodeData = data;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
    }

    public static NodeData getNodeData() {
        return nodeData;
    }

}
