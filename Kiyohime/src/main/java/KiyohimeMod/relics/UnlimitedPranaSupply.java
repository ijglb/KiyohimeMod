package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import KiyohimeMod.powers.NPPower;
import basemod.abstracts.CustomRelic;


public class UnlimitedPranaSupply extends CustomRelic {
    public static final String ID = "KiyohimeMod:UnlimitedPranaSupply";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/UnlimitedPranaSupply.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/UnlimitedPranaSupply_P.png");

    public UnlimitedPranaSupply() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UnlimitedPranaSupply();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 4 + DESCRIPTIONS[1];
    }
    
    @Override
    public void atTurnStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new NPPower(AbstractDungeon.player), 4, true));
    }
}