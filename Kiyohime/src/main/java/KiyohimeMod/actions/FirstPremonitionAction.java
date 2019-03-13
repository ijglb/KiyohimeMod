package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class FirstPremonitionAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer p;
    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    
    public FirstPremonitionAction(AbstractCreature target, AbstractCreature source, int amount) {
        setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
        this.p = ((AbstractPlayer) target);
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() == 1) {
                AbstractCard tmpCard = this.p.hand.getBottomCard();
                this.p.hand.moveToDiscardPile(tmpCard);
                tmpCard.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                if (tmpCard.type == CardType.ATTACK) {
                    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.amount));
                }
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            AbstractCard tmpCard = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
            this.p.hand.moveToDiscardPile(tmpCard);
            tmpCard.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
            if (tmpCard.type == CardType.ATTACK) {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.amount));
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}