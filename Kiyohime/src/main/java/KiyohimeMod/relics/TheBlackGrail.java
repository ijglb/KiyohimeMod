package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;


public class TheBlackGrail extends CustomRelic {
    public static final String ID = "KiyohimeMod:TheBlackGrail";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/TheBlackGrail.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/TheBlackGrail_P.png");

    public TheBlackGrail() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheBlackGrail();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player,
                new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE));
    }
}