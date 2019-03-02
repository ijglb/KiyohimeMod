package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.CritStarGenerationRatePower;
import KiyohimeMod.powers.CritUPPower;
import KiyohimeMod.powers.NPPower;
import basemod.abstracts.CustomCard;

public class CastleOfTheSun extends CustomCard {

    public static final String ID = "KiyohimeMod:CastleOfTheSun";
    public static final String IMG_PATH = "Kiyohime/images/cards/CastleOfTheSun.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 2;
    private static final int BASE_DAMAGE = 15;
    private static final int UPGRADE_PLUS_DAMAGE = 5;
    private static final int BASE_BLOCK = 60;
    private static final int UPGRADE_PLUS_BLOCK = 20;
    private static final int BASE_MAGIC = 25;
    private static final int UPGRADE_PLUS_MAGIC = 15;

    public CastleOfTheSun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.POWER, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.baseDamage = this.damage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
        this.baseBlock = this.block = BASE_BLOCK;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyStackablePowerAction(p, p, new CritUPPower(p, this.damage, -1), this.damage, -1, true));
        AbstractDungeon.actionManager
                .addToBottom(new ApplyStackablePowerAction(p, p, new CritStarGenerationRatePower(p, this.block, -1),
                        this.block, -1,true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NPPower(p), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.damage = this.baseDamage;
        this.magicNumber = this.baseMagicNumber;
        this.block = this.baseBlock;
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        applyPowers();
    }

    public AbstractCard makeCopy() {
        return new CastleOfTheSun();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}