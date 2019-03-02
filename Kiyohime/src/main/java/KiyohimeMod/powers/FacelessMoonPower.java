package KiyohimeMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FacelessMoonPower extends AbstractPower {
    public static final String POWER_ID = "KiyohimeMod:FacelessMoonPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public String lockedCard;
    private boolean isFirstTurn;

    public FacelessMoonPower(AbstractCreature owner, String lockedCard, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.lockedCard = lockedCard;
        this.amount = amount;
        this.type = AbstractPower.PowerType.BUFF;
        updateDescription();
        this.img = new Texture("Kiyohime/images/powers/FacelessMoonPower.png");
        this.isFirstTurn = true;
    }

    @Override
    public void updateDescription() {
        String card = "";
        switch (lockedCard) {
        case "Buster":
            card = " #rBuster ";
            break;
        case "Quick":
            card = " #gQuick ";
            break;
        case "Arts":
            card = " #gArts ";
            break;
        default:
            break;
        }
        description = DESCRIPTIONS[0] + card;
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            if (isFirstTurn) {
                isFirstTurn = false;
            } else {
                this.amount--;
            }
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
    }
}