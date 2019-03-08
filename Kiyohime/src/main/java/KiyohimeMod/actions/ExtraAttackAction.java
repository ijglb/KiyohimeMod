package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import KiyohimeMod.character.AbstractServant;
import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.powers.NPPower;

public class ExtraAttackAction extends AbstractGameAction {
    private AbstractMonster monster;
    
    public ExtraAttackAction(AbstractMonster m) {
        actionType = ActionType.DAMAGE;
        this.monster = m;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player instanceof Kiyohime) {
            AbstractServant servant = ((Kiyohime) AbstractDungeon.player).Servant;
            AbstractCard ex = servant.extraAttack.makeStatEquivalentCopy();
            ex.use(player, monster);
            UnlockTracker.markCardAsSeen(ex.cardID);
            if (player.hasPower(NPPower.POWER_ID)) {
                player.getPower(NPPower.POWER_ID).onAfterUseCard(ex, null);
            }
            ((Kiyohime) player).StarCounter.onUseCard(ex);
        }
        isDone = true;
    }
}