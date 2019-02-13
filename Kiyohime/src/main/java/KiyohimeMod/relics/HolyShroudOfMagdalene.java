package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;


public class HolyShroudOfMagdalene extends CustomRelic {
    public static final String ID = "KiyohimeMod:HolyShroudOfMagdalene";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/HolyShroudOfMagdalene.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/HolyShroudOfMagdalene_P.png");
    private static final int Defend = 3;

    public HolyShroudOfMagdalene() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HolyShroudOfMagdalene();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + Defend + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner instanceof AbstractMonster
                && ((AbstractMonster) info.owner).type == EnemyType.ELITE) {
            damageAmount = damageAmount - Defend;
            if (damageAmount < 0)
                damageAmount = 0;
            this.flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
        return damageAmount;
    }
}