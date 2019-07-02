package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;


public class Divinity extends CustomRelic {
    public static final String ID = "KiyohimeMod:Divinity";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/Divinity.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/Divinity_P.png");

    public Divinity() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Divinity();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1];
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if ((info.owner != null) && (info.type != DamageInfo.DamageType.HP_LOSS)
                && (info.type != DamageInfo.DamageType.THORNS) && damageAmount > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            return damageAmount + 3;
        }
        return damageAmount;
    }
}