package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ProjectionAction extends AbstractGameAction {
    private boolean upgraded;
    private AbstractPlayer p;
    private int strength;

    public ProjectionAction(AbstractPlayer p, boolean upgraded,int strength) {
        this.p = p;
        this.upgraded = upgraded;
        this.strength = strength;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }

    public void update() {
        AbstractCard card = AbstractDungeon.srcColorlessCardPool.getRandomCard(CardType.ATTACK, true).makeCopy();
        if (upgraded) {
            card.setCostForTurn(0);
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
        if (card.exhaust) {
            AbstractDungeon.actionManager
                    .addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.strength), this.strength, true));
        }
        this.isDone = true;
    }
}
