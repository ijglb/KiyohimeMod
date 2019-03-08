package KiyohimeMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.character.Kiyohime;

public class GetCritStarPower extends AbstractPower {
    public static final String POWER_ID = "KiyohimeMod:GetCritStarPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public GetCritStarPower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = AbstractPower.PowerType.BUFF;
        updateDescription();
        this.img = new Texture("Kiyohime/images/powers/GetCritStarPower.png");
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        if (AbstractDungeon.player instanceof Kiyohime) {
            ((Kiyohime) AbstractDungeon.player).StarCounter.addStarCount(this.amount);
        }
        // AbstractDungeon.actionManager
        //         .addToBottom(new ApplyPowerAction(owner, owner, new CritStarPower(owner), this.amount, true));
    }
}