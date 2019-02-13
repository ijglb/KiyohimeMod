package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;


public class ElixirOfRejuvenation extends CustomRelic {
    public static final String ID = "KiyohimeMod:ElixirOfRejuvenation";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/ElixirOfRejuvenation.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/ElixirOfRejuvenation_P.png");
    private static final int HEAL = 10;

    public ElixirOfRejuvenation() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ElixirOfRejuvenation();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HEAL + DESCRIPTIONS[1];
    }
    
    @Override
    public void atBattleStart() {
        AbstractPlayer p = AbstractDungeon.player;
        if ((p.currentHealth <= p.maxHealth / 2.0F) && (p.currentHealth > 0)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, HEAL));
        }
    }
}