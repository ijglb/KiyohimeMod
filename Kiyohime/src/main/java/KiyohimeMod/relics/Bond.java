package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import KiyohimeMod.powers.NPPower;
import basemod.abstracts.CustomRelic;


public class Bond extends CustomRelic {
    public static final String ID = "KiyohimeMod:Bond";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/Bond.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/Bond_P.png");

    public Bond() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Bond();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 50 + DESCRIPTIONS[1];
    }
    
    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new NPPower(AbstractDungeon.player), 50, true));
    }
}