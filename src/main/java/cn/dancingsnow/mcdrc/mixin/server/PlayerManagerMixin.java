package cn.dancingsnow.mcdrc.mixin.server;

import cn.dancingsnow.mcdrc.networking.CommandNetwork;
import cn.dancingsnow.mcdrc.server.MCDRCommandServer;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    public void sendNodes(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        CommandNetwork.sendNodeDataToClient(player.networkHandler, MCDRCommandServer.getNodeData());
    }
}
