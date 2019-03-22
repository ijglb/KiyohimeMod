package KiyohimeMod.actions;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import KiyohimeMod.helpers.CardHelper;

public class CardApplyStarAction extends AbstractGameAction {
    private int count;
    
    public CardApplyStarAction(int count) {
        actionType = ActionType.SPECIAL;
        this.count = count;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && count > 0) {
            CardGroup attackCards = AbstractDungeon.player.hand.getAttacks();
            ArrayList<AbstractCard> group = new ArrayList<AbstractCard>();
            for (AbstractCard card : attackCards.group) {
                // if (card instanceof AbstractAttackCard) {
                //     group.add((AbstractAttackCard) card);
                // }
                group.add(card);
            }
            if (group.size() > 0) {
                int max = group.size() - 1;
                int i = AbstractDungeon.miscRng.random(0, max);
                while (this.count > 0) {
                    AbstractCard attackCard = group.get(i);
                    int room = CardHelper.getStarRoom(attackCard);
                    if (room > 0) {
                        if (room > this.count)
                            room = this.count;
                        int r = AbstractDungeon.miscRng.random(0, room);
                        if (r > 0) {
                            CardHelper.addStarCount(attackCard, r);
                            attackCard.flash(Color.YELLOW);
                            this.count -= r;
                        }
                    }
                    if (group.stream().allMatch(m -> CardHelper.getStarRoom(m) <= 0)) {
                        break;
                    }
                    i++;
                    if (i > max)
                        i = 0;
                }
            }
        }
        isDone = true;
    }
}