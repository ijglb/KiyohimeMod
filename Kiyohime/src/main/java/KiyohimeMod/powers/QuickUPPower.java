package KiyohimeMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class QuickUPPower extends AbstractStackablePower {
    public static final String POWER_ID = "KiyohimeMod:QuickUPPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public QuickUPPower(AbstractCreature owner,float value ,int round) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = round;
        this.type = AbstractPower.PowerType.BUFF;
        stackaBuff(value, round);
        this.img = new Texture("Kiyohime/images/powers/QuickUPPower.png");
    }

    @Override
    public void updateDescription() {
        String str = "";
        str += DESCRIPTIONS[0] + valueAmount + DESCRIPTIONS[1];
        for (Buff buff : BuffPool) {
            String round = buff.Round > 0 ? String.valueOf(buff.Round) : "-";
            str += DESCRIPTIONS[2] + buff.Value + DESCRIPTIONS[3] + round + DESCRIPTIONS[4];
        }
        description = str;
    }
}