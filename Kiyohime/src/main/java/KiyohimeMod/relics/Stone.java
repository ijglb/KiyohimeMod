package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import KiyohimeMod.powers.ChaldeaPower;
import KiyohimeMod.powers.CritStarPower;
import KiyohimeMod.powers.NPPower;
import basemod.abstracts.CustomRelic;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;;

public class Stone extends CustomRelic implements ModalChoice.Callback,ClickableRelic {
    public static final String ID = "KiyohimeMod:Stone";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/Stone.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/Stone_P.png");
    private ModalChoice modal;
    private boolean canShowModal = false;

    public Stone() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        counter = 3;
        modal = new ModalChoiceBuilder().setCallback(this).setTitle(DESCRIPTIONS[1]).setColor(CardColor.RED)
                .addOption(DESCRIPTIONS[2], DESCRIPTIONS[3], CardTarget.NONE).setColor(CardColor.BLUE)
                .addOption(DESCRIPTIONS[4], DESCRIPTIONS[5], CardTarget.NONE).setColor(CardColor.COLORLESS)
                .addOption(DESCRIPTIONS[6], DESCRIPTIONS[7], CardTarget.NONE).create();
    }
    
    @Override
    public void onRightClick() {
        if (counter > 0 && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && canShowModal
                && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            modal.open();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        canShowModal = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        canShowModal = true;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Stone();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new CritStarPower(AbstractDungeon.player)));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new NPPower(AbstractDungeon.player)));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ChaldeaPower(AbstractDungeon.player)));
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int i) {
        if (counter > 0) {
            switch (i) {
            case 0:
                counter--;
                flash();
                AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, 99999999));
                return;
            case 1:
                counter--;
                flash();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NPPower(p), 100));
                return;
            default:
                return;
            }
        }
    }
}