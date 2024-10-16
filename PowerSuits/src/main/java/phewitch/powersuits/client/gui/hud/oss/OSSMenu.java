package phewitch.powersuits.client.gui.hud.oss;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import phewitch.powersuits.client.gui.GUIManager;
import phewitch.powersuits.common.capabilities.PlayerOSSProvider;
import phewitch.powersuits.common.entity.OSSManager;
import phewitch.powersuits.common.items.MenuManager;

import java.util.List;

public class OSSMenu extends AbstractContainerMenu {

    public OSSMenu(int pContainerId, Inventory inv) {
        this(pContainerId, inv, ContainerLevelAccess.NULL);
    }

    public OSSMenu(int pContainerId, Inventory inv, ContainerLevelAccess access) {
        super(GUIManager.OSS_MENU.get(), pContainerId);
        checkContainerSize(inv, 2);

        addPlayerInventory(inv);

        if(!inv.player.level().isClientSide) {
            inv.player.getCapability(PlayerOSSProvider.PLAYER_OSS).ifPresent(oss -> {
                OSSManager.getSuits();
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 108 + l * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 108 + k * 18, 142));
        }
    }
}