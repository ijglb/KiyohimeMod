package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.abstracts.CustomRelic;

public class Necromancy extends CustomRelic implements BetterOnLoseHpRelic {
    public static final String ID = "KiyohimeMod:Necromancy";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/Necromancy.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/Necromancy_P.png");
    public static final Logger logger = LogManager.getLogger(Necromancy.class.getName());

    public Necromancy() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Necromancy();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int betterOnLoseHp(DamageInfo info, int damageAmount) {
        int currHp = AbstractDungeon.player.currentHealth;
        if (damageAmount >= currHp) {
            int i = AbstractDungeon.miscRng.random(0, 1);
            if (i == 1) {
                int dmg = damageAmount - (damageAmount - currHp + 1);
                flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.player.heal(4, true);
                return dmg;
            }
        }
        return damageAmount;
    }
}