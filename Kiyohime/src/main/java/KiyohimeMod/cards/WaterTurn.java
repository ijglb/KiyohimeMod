package KiyohimeMod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.BusterUPPower;
import basemod.abstracts.CustomCard;

public class WaterTurn extends CustomCard {

    public static final String ID = "KiyohimeMod:WaterTurn";
    public static final String IMG_PATH = "Kiyohime/images/cards/WaterTurn.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 2;
    private static final int BASE_Damage = 20;
    private static final int UPGRADE_PLUS_Damage = 10;
    private static final int BASE_MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public WaterTurn() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
        this.baseDamage = this.damage = BASE_Damage;
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(p, p,
                new BusterUPPower(p, this.damage, this.magicNumber), this.damage, this.magicNumber, true));
    }

    @Override
    public void applyPowers() {
        this.damage = this.baseDamage;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        applyPowers();
    }

    public AbstractCard makeCopy() {
        return new WaterTurn();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            upgradeDamage(UPGRADE_PLUS_Damage);
        }
    }
}