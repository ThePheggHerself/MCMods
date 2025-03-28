package phewitch.powersuits.common.item.suits.armor;

import net.minecraft.world.effect.MobEffects;
import phewitch.powersuits.common.item.suits.armorbase.enums.ArmorMaterials;
import phewitch.powersuits.common.item.suits.armorbase.SuitAbility;
import phewitch.powersuits.common.item.suits.armorbase.SuitFeatures;
import phewitch.powersuits.common.item.suits.armorbase.enums.ChargeType;
import phewitch.powersuits.common.item.suits.armorbase.enums.PassiveAbilities;
import phewitch.powersuits.common.item.suits.armorbase.enums.Weakness;

import java.util.ArrayList;
import java.util.List;

public class Mark6Armor extends Mark5Armor {
    public Mark6Armor(Type type, Properties properties) {
        super(ArmorMaterials.MARK6, type, properties, new SuitFeatures(
                180,
                2,
                0.0f,
                9f,
                0.25f,
                0.0f,
                new ArrayList<>(List.of(SuitAbility.SHOOT_FIRE_ARROWS_ABILITY, SuitAbility.SHOOT_FLAMETHROWER_ABILITY, SuitAbility.SENTRY_MODE_ABILITY)),
                new ArrayList<>(List.of(PassiveAbilities.LIMITED_FLIGHT, PassiveAbilities.FULL_FLIGHT)),
                ChargeType.IN_FIRE_OR_LAVA,
                new ArrayList<>(List.of(Weakness.WATER)),
                new ArrayList<>(List.of(MobEffects.NIGHT_VISION, MobEffects.MOVEMENT_SPEED, MobEffects.FIRE_RESISTANCE)),
                "Mark 6"
        ));
    }
}
