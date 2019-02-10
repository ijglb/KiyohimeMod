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
import KiyohimeMod.powers.ArtsUPPower;
import KiyohimeMod.powers.BusterUPPower;
import KiyohimeMod.powers.CritUPPower;
import KiyohimeMod.powers.GetCritStarPower;
import KiyohimeMod.powers.GetNPPower;
import KiyohimeMod.powers.QuickUPPower;
import basemod.abstracts.CustomCard;

public class AVerseOfBurningLoveStory extends CustomCard {

    public static final String ID = "KiyohimeMod:AVerseOfBurningLoveStory";
    public static final String IMG_PATH = "Kiyohime/images/cards/AVerseOfBurningLoveStory.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 10;//tag and crit power
    private static final int UPGRADE_PLUS_DAMAGE = 5;
    private static final int BASE_BLOCK = 2;//np
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int BASE_MAGIC = 1;//star
    private static final int UPGRADE_PLUS_MAGIC = 2;

    public AVerseOfBurningLoveStory() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.POWER, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.baseDamage = this.damage = BASE_DAMAGE;
        this.baseBlock = this.block = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyStackablePowerAction(p, p, new BusterUPPower(p, this.damage, -1), this.damage, -1, true));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyStackablePowerAction(p, p, new ArtsUPPower(p, this.damage, -1), this.damage, -1, true));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyStackablePowerAction(p, p, new QuickUPPower(p, this.damage, -1), this.damage, -1, true));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyStackablePowerAction(p, p, new CritUPPower(p, this.damage, -1), this.damage, -1, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GetNPPower(p,this.block), this.block));
        AbstractDungeon.actionManager
                .addToBottom(new ApplyPowerAction(p, p, new GetCritStarPower(p,this.magicNumber), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.damage = this.baseDamage;
        this.block = this.baseBlock;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        applyPowers();
    }

    public AbstractCard makeCopy() {
        return new AVerseOfBurningLoveStory();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            this.isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}