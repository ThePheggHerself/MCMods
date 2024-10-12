package phewitch.powersuits.common.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import phewitch.powersuits.common.networking.packets.*;
import phewitch.powersuits.PowerSuits;

public class ModMessages {
    private static SimpleChannel Instance;
    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }
    
    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(
                new ResourceLocation(PowerSuits.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        Instance = net;

        net.messageBuilder(C2SSuitShootArrow.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSuitShootArrow::new)
                .encoder(C2SSuitShootArrow::toBytes)
                .consumerMainThread(C2SSuitShootArrow::handle)
                .add();

        net.messageBuilder(C2SSuitShootLaser.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSuitShootLaser::new)
                .encoder(C2SSuitShootLaser::toBytes)
                .consumerMainThread(C2SSuitShootLaser::handle)
                .add();

        net.messageBuilder(C2SSuitShootChestLaser.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSuitShootChestLaser::new)
                .encoder(C2SSuitShootChestLaser::toBytes)
                .consumerMainThread(C2SSuitShootChestLaser::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        Instance.sendToServer(message);
    }
    public static <MSG> void sendToClient(MSG message, ServerPlayer player){
        Instance.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}