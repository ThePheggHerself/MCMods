package phewitch.powersuits.common.networking.packets.client2server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import phewitch.powersuits.client.gui.hud.oss.OSSMenu;
import phewitch.powersuits.common.capabilities.PlayerOSSProvider;
import phewitch.powersuits.common.networking.ModMessages;

import java.util.function.Supplier;

public class C2SGetOSSSuits {
    public C2SGetOSSSuits(){ }
    public C2SGetOSSSuits(FriendlyByteBuf buf){ }

    public void toBytes(FriendlyByteBuf buf){ }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
//            var plr = context.getSender();
//
//            plr.getCapability(PlayerOSSProvider.PLAYER_OSS).ifPresent(playerOSS -> {
//                plr.openMenu(new SimpleMenuProvider(((pContainerId, pPlayerInv, pPlayer1) -> {
//                    return new OSSMenu(pContainerId, pPlayerInv, playerOSS.getSuits());
//                }), Component.literal("Orbital Suit Storage")));
//            });
        });

        return true;
    }
}