package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.powers.ArtsUPPower;
import basemod.abstracts.CustomRelic;


public class TerritoryCreation extends CustomRelic {
    public static final String ID = "KiyohimeMod:TerritoryCreation";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/TerritoryCreation.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/TerritoryCreation_P.png");

    public TerritoryCreation() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TerritoryCreation();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 15 + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(AbstractDungeon.player,
                AbstractDungeon.player, new ArtsUPPower(AbstractDungeon.player, 15, -1), 15, -1, true));
    }
}