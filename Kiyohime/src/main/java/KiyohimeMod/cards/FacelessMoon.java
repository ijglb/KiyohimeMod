package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.FacelessMoonPower;
import basemod.abstracts.CustomCard;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;

public class FacelessMoon extends CustomCard implements ModalChoice.Callback {

    public static final String ID = "KiyohimeMod:FacelessMoon";
    public static final String IMG_PATH = "Kiyohime/images/cards/FacelessMoon.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int BASE_MAGIC = 3;
    private ModalChoice modal;

    public FacelessMoon() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
        modal = new ModalChoiceBuilder().setCallback(this).setTitle(EXTENDED_DESCRIPTION[1]).setColor(CardColor.RED)
                .addOption(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[2], CardTarget.NONE).setColor(CardColor.BLUE)
                .addOption(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[3], CardTarget.NONE).setColor(CardColor.GREEN)
                .addOption(EXTENDED_DESCRIPTION[4], EXTENDED_DESCRIPTION[4], CardTarget.NONE).create();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean can = super.canUse(p, m);
        if (can) {
            if (AbstractDungeon.player.hasPower(FacelessMoonPower.POWER_ID)) {
                can = false;
                this.cantUseMessage = EXTENDED_DESCRIPTION[0];
            }
        }
        return can;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        modal.open();
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int i) {
        String lockedCard = null;
        switch (i) {
        case 0:
            lockedCard = "Buster";
            break;
        case 1:
            lockedCard = "Arts";
            break;
        case 2:
            lockedCard = "Quick";
            break;
        }
        if (lockedCard != null) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,
                    AbstractDungeon.player, new FacelessMoonPower(AbstractDungeon.player, lockedCard, this.magicNumber),
                    this.magicNumber));
        }
    }

    public AbstractCard makeCopy() {
        return new FacelessMoon();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}