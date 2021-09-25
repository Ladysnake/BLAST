package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.BombEntity;

public class SlimeBombardItem extends BombardItem {
    public SlimeBombardItem(Settings settings) {
        super(settings);
    }

    @Override
    public BombEntity.BombardModifier getBombardModifier() {
        return BombEntity.BombardModifier.SLIME;
    }
}
