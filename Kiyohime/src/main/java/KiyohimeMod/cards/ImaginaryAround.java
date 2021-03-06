package KiyohimeMod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.QuickUPPower;
import basemod.abstracts.CustomCard;

public class ImaginaryAround extends CustomCard {

    public static final String ID = "KiyohimeMod:ImaginaryAround";
    public static final String IMG_PATH = "Kiyohime/images/cards/ImaginaryAround.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 2;
    private static final int BASE_MAGIC = 25;
    private static final int UPGRADE_PLUS_MAGIC = 15;

    public ImaginaryAround() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.POWER, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(p, p,
                new QuickUPPower(p, this.magicNumber, -1), this.magicNumber, -1, true));
    }

    public AbstractCard makeCopy() {
        return new ImaginaryAround();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}