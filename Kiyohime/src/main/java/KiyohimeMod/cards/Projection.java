package KiyohimeMod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.actions.ProjectionAction;
import KiyohimeMod.patches.AbstractCardEnum;
import basemod.abstracts.CustomCard;

public class Projection extends CustomCard {

    public static final String ID = "KiyohimeMod:Projection";
    public static final String IMG_PATH = "Kiyohime/images/cards/Projection.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int BASE_MAGIC = 2;

    public Projection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ProjectionAction(p, this.upgraded, this.magicNumber));
    }

    public AbstractCard makeCopy() {
        return new Projection();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}