package KiyohimeMod.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.CritStarPower;
import basemod.abstracts.CustomCard;

public class CelestialInvertedMoon extends CustomCard {

    public static final String ID = "KiyohimeMod:CelestialInvertedMoon";
    public static final String IMG_PATH = "Kiyohime/images/cards/CelestialInvertedMoon.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    //private static final int UPGRADE_COST = 0;
    private static final int BLOCK = 0;
    private static final int UPGRADE_BLOCK = 5;
    private static final int BASE_MAGIC = 1;

    public CelestialInvertedMoon() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void applyPowers() {
        this.isBlockModified = false;
        float tmp = this.baseBlock;
        if (AbstractDungeon.player.hasPower(CritStarPower.POWER_ID)) {
            AbstractPower pow = AbstractDungeon.player.getPower(CritStarPower.POWER_ID);
            int star = pow.amount;
            tmp += (star * this.magicNumber);
            if (this.baseBlock != MathUtils.floor(tmp)) {
                this.isBlockModified = true;
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            tmp = p.modifyBlock(tmp);
            if (this.baseBlock != MathUtils.floor(tmp)) {
                this.isBlockModified = true;
            }
        }
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        this.block = MathUtils.floor(tmp);
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        applyPowers();
    }

    public AbstractCard makeCopy() {
        return new CelestialInvertedMoon();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            //upgradeBaseCost(UPGRADE_COST);
        }
    }
}