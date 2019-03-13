package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.character.StarCounter;
import KiyohimeMod.patches.AbstractCardEnum;
import basemod.abstracts.CustomCard;

public class BeElegant extends CustomCard {

    public static final String ID = "KiyohimeMod:BeElegant";
    public static final String IMG_PATH = "Kiyohime/images/cards/BeElegant.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 0;
    private static final int BASE_MAGIC = 20;
    private static final int UPGRADE_PLUS_MAGIC = -5;

    public BeElegant() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player instanceof Kiyohime) {
            StarCounter starCounter = ((Kiyohime) AbstractDungeon.player).StarCounter;
            int star = starCounter.getStarCount();
            if (star >= this.magicNumber) {
                starCounter.addStarCount(-this.magicNumber);
                int e = 1;
                if (this.upgraded)
                    e = 2;
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(e));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (canUse) {
            if (AbstractDungeon.player instanceof Kiyohime) {
                int star = ((Kiyohime) AbstractDungeon.player).StarCounter.getStarCount();
                if (star < this.magicNumber) {
                    canUse = false;
                    this.cantUseMessage = EXTENDED_DESCRIPTION[0];
                }
            } 
            else{
                canUse = false;
                this.cantUseMessage = EXTENDED_DESCRIPTION[0];
            }
        }
        return canUse;
    }

    public AbstractCard makeCopy() {
        return new BeElegant();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}