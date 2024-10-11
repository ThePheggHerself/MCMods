package phewitch.powersuits.Common.Items.Armor.ArmorBase;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import phewitch.powersuits.Client.GUI.GUIManager;
import phewitch.powersuits.Client.GUI.IHUDItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SuitArmourBase extends ArmorItem implements GeoItem, IHUDItem {

    public ArrayList<MobEffectInstance> fullArmourEffects = new ArrayList<>();
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public String name;
    //The fall damage multiplier. 0 = no damage, 1 = 100% damage, 2 = 200% damage
    public float fallDamageMultiplier = 0.1f;

    public boolean fullFlightOnFullArmour = true;
    public boolean lfOnBootsOrChestplate = true;
    public double lfVelocity = 0.1d;
    public double lfFuelRecharge = 2f;
    public float lfMaxfuel = -1;
    public float lfCurrentFuel = 0;

    public boolean hasFlightFuel() {
        return lfMaxfuel == -1 || lfCurrentFuel > 0;
    }

    public boolean shootsArrows = false;
    public boolean shootsLasers = true;
    public int projectileDamage = 7;


    public final Minecraft minecraft = Minecraft.getInstance();
    public SuitArmourBase(ArmorMaterial materialIn, Type type, Properties properties, String name ) {
        super(materialIn, type, properties);
        this.name = name;

        GUIManager.registerHUDItem(name + "_armor", this);
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<SuitArmourBase>(this, "controller",
                20, this::predicate));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @SuppressWarnings("removal")
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(stack.getItem() instanceof ArmorItem ai) {
            if (hasFullSet(player) && ai.getMaterial() == material) {
                for (MobEffectInstance effect : fullArmourEffects) {
                    player.addEffect(effect);
                }
            }

            if(ai.getEquipmentSlot() == EquipmentSlot.HEAD) {
                if (fullFlightOnFullArmour && hasFullSet(player))
                    player.getAbilities().mayfly = true;
            }

            if(!hasFullSet(player)){
                player.getAbilities().mayfly = false;
            }

            if(ai.getEquipmentSlot() == EquipmentSlot.FEET || ai.getEquipmentSlot() == getEquipmentSlot().CHEST){
                if (lfOnBootsOrChestplate && hasBootsOrChestplate(player)){
                    if(Minecraft.getInstance().options.keyJump.isDown() && hasFlightFuel()){
                        var motion = player.getDeltaMovement();
                        var upwardsVelocity = motion.get(Direction.Axis.Y);
                        upwardsVelocity += lfVelocity;

                        if (upwardsVelocity > 1)
                            upwardsVelocity = 1;

                        player.setDeltaMovement(motion.get(Direction.Axis.X), upwardsVelocity, motion.get(Direction.Axis.Z));

                        if(lfMaxfuel >= 0){
                            lfCurrentFuel -= 1;

                            if(lfCurrentFuel < 0)
                                lfCurrentFuel = 0;
                        }
                    } else if(player.onGround() && lfMaxfuel >=0 && lfCurrentFuel < lfMaxfuel)
                    {
                        lfCurrentFuel += lfFuelRecharge;

                        if(lfCurrentFuel > lfMaxfuel)
                            lfCurrentFuel = lfMaxfuel;
                    }
                }
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public Boolean hasBoots(Player player){
        if(player == null)
            return false;

        return (player.getInventory().getArmor(0).getItem() instanceof ArmorItem ai) && ai.getMaterial() == material;
    }

    public Boolean hasLegs(Player player){
        if(player == null)
            return false;

        return (player.getInventory().getArmor(1).getItem() instanceof ArmorItem ai) && ai.getMaterial() == material;
    }

    public Boolean hasChestplate(Player player){
        if(player == null)
            return false;

        return (player.getInventory().getArmor(2).getItem() instanceof ArmorItem ai) && ai.getMaterial() == material;
    }

    public Boolean hasHelmet(Player player){
        if(player == null)
            return false;

        return (player.getInventory().getArmor(3).getItem() instanceof ArmorItem ai) && ai.getMaterial() == material;
    }

    public Boolean hasFullSet(Player player){
        if(player == null)
            return false;

        return hasBoots(player) && hasLegs(player) && hasChestplate(player) && hasHelmet(player);
    }

    public boolean hasBootsOrChestplate(Player player){
        return hasBoots(player) || hasChestplate(player);
    }

    @Override
    public void renderGUI(RenderGuiEvent event, PoseStack matrix) {
        var instance = Minecraft.getInstance();
        var player = instance.player;


        if (instance.isPaused() || player == null || player.level() == null || instance.options.hideGui)
            return;

        if(lfOnBootsOrChestplate && lfMaxfuel > 0){
            int x = event.getGuiGraphics().guiWidth();
            int y = event.getGuiGraphics().guiHeight();
            int color = Integer.parseInt("33AA66", 16);

            event.getGuiGraphics().drawString(instance.font, "Fuel: " + lfCurrentFuel, x - 75, y-25, color);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack item, Level level, List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(Component.translatable("tooltip.powersuits." + name + ".identifier"));
        components.add(Component.translatable("tooltip.powersuits." + name + ".extra"));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SuitArmourRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if(this.renderer == null){
                    this.renderer = new SuitArmourRenderer(name);
                }

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }
}
