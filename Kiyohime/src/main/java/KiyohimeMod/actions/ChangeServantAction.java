package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import KiyohimeMod.character.*;

public class ChangeServantAction extends AbstractGameAction {
    
    public ChangeServantAction() {
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player instanceof Kiyohime) {
            Kiyohime kiyohime = (Kiyohime) AbstractDungeon.player;
            if (kiyohime.Servant instanceof BerserkerKiyohime) {
                kiyohime.changeAbstractServant(new LancerKiyohime(), true);
            } else if (kiyohime.Servant instanceof LancerKiyohime) {
                kiyohime.changeAbstractServant(new BerserkerKiyohime(), true);
            }
        }
        isDone = true;
    }
}