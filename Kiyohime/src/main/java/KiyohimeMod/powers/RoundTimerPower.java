package KiyohimeMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RoundTimerPower extends AbstractPower implements NonStackablePower {
    public static final String POWER_ID = "KiyohimeMod:RoundTimerPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public String actionDESC;
    public AbstractGameAction task;

    public RoundTimerPower(AbstractCreature owner, int round, String actionDESC, AbstractPower.PowerType powerType,
            AbstractGameAction action) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = round;
        this.type = powerType;
        this.actionDESC = actionDESC;
        this.task = action;
        updateDescription();
        this.img = new Texture("Kiyohime/images/powers/RoundTimerPower.png");
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + actionDESC;
    }

    @Override
    public void atEndOfRound() {
        this.amount--;
        if (this.amount > 0) {
            updateDescription();
        } else {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(task);
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}