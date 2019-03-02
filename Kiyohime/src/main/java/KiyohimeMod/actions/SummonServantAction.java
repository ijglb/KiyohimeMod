package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import KiyohimeMod.character.*;
import KiyohimeMod.servants.AbstractFriendlyServant;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class SummonServantAction extends AbstractGameAction {
    private AbstractFriendlyServant servant;

    public SummonServantAction(AbstractFriendlyServant servant) {
        actionType = ActionType.SPECIAL;
        this.servant = servant;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player instanceof Kiyohime) {
            BasePlayerMinionHelper.addMinion(AbstractDungeon.player, servant);
            String sound = servant.getSummonSound();
            if (sound != null) {
                CardCrawlGame.sound.play(sound);
            }
        }
        isDone = true;
    }
}