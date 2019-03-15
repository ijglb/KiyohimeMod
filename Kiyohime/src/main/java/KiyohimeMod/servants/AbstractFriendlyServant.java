package KiyohimeMod.servants;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;

public abstract class AbstractFriendlyServant extends AbstractFriendlyMonster {
    protected AbstractMonster target;
    protected ArrayList<ServantMove> baseMoves;

    public AbstractFriendlyServant(String name, String id, int maxHealth, String imgUrl, float x,float y,
            String[] attackIntents) {
        super(name, id, maxHealth, -8.0F, 10.0F, 230.0F, 240.0F, imgUrl, x, y);
        if (attackIntents != null) {
            ArrayList<Texture> tempAttackIntents = new ArrayList<Texture>();
            for (String str : attackIntents) {
                tempAttackIntents.add(new Texture(str));
            }
            this.attackIntents = new Texture[tempAttackIntents.size()];
            tempAttackIntents.toArray(this.attackIntents);
        }
        this.baseMoves = new ArrayList<ServantMove>();
        this.moves.setxStart(200 * Settings.scale);
        //930 845
        float yStart = 930;
        MonsterGroup monsterGroup = BasePlayerMinionHelper.getMinions(AbstractDungeon.player);
        int size = monsterGroup.monsters.size();
        if (size == 1) {
            AbstractFriendlyServant other = (AbstractFriendlyServant) monsterGroup.monsters.get(0);
            if (other.getMoves().getyStart() == yStart * Settings.scale) {
                yStart = 845;
            }
        }
        yStart = yStart * Settings.scale;
        this.moves.setyStart(yStart);
    }

    public abstract String getSummonSound();

    public void resetSkill() {
        baseMoves.forEach(m -> {
            if (m.isSkill && m.isUsed) {
                m.setUsed(false);
            }
        });
    }
    
    protected void resetMoves() {
        baseMoves.forEach(m -> {
            if (!m.isSkill) {
                m.setUsed(false);
            }
        });
    }

    protected void initMoves() {
        baseMoves.forEach(m -> {
            this.moves.addMove(m);
        });
    }

    @Override
    public void applyStartOfTurnPowers() {
        super.applyStartOfTurnPowers();
        this.resetMoves();
    }
}