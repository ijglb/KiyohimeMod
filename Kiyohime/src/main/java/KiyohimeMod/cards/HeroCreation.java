package KiyohimeMod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.BusterUPPower;
import KiyohimeMod.powers.CritUPPower;
import basemod.abstracts.CustomCard;

public class HeroCreation extends CustomCard {

    public static final String ID = "KiyohimeMod:HeroCreation";
    public static final String IMG_PATH = "Kiyohime/images/cards/HeroCreation.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 6;
    private static final int UPGRADE_COST = 5;
    private static final int BASE_DAMAGE = 30;//power
    private static final int UPGRADE_PLUS_DAMAGE = 20;
    private static final int BASE_BLOCK = 10;//temp hp
    private static final int UPGRADE_PLUS_BLOCK = 5;
    private static final int BASE_TURN = 3;
    private static final int BASE_MAGIC = 50;//crit up
    private static final int UPGRADE_PLUS_MAGIC = 50;
    private int costChange = 0;

    public HeroCreation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.baseDamage = this.damage = BASE_DAMAGE;
        this.baseBlock = this.block = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        //buster up
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(p, p,
                new BusterUPPower(p, this.damage, BASE_TURN), this.damage, BASE_TURN, true));
        //add temp HP
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, this.block));
        //lost temp HP
        // AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
        //         new RoundTimerPower(p, BASE_TURN, EXTENDED_DESCRIPTION[0] + this.block + EXTENDED_DESCRIPTION[1],
        //                 PowerType.BUFF, new AddTemporaryHPAction(p, p, -this.block))));
        //crit up 1 turn
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(p, p,
                new CritUPPower(p, this.magicNumber, 1), this.magicNumber, 1, true));
    }

    @Override
    public void applyPowers() {
        this.damage = this.baseDamage;
        this.block = this.baseBlock;
        this.magicNumber = this.baseMagicNumber;
        this.costForTurn += costChange;
        costChange = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.hasTag(KiyohimeTags.ATTACK_Buster) && this.costForTurn > 0) {
                this.costForTurn--;
                costChange++;
            }
        }
        if (this.costForTurn != this.cost) {
            this.isCostModifiedForTurn = true;
        }
    }

    @Override
    public void atTurnStart() {
        costChange = 0;
        super.atTurnStart();
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        this.damage = this.baseDamage;
        this.block = this.baseBlock;
        this.magicNumber = this.baseMagicNumber;
    }

    public AbstractCard makeCopy() {
        return new HeroCreation();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}