package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.defect.DiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.patches.AbstractCardEnum;
import basemod.abstracts.CustomCard;

public class GoldenCarpFigure extends CustomCard {

    public static final String ID = "KiyohimeMod:GoldenCarpFigure";
    public static final String IMG_PATH = "Kiyohime/images/cards/GoldenCarpFigure.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int BASE_BLOCK = 10;
    private static final int BASE_MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public GoldenCarpFigure() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.baseBlock = this.block = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
        this.exhaust = true;
    }

    @Override
    public void applyPowers() {
        this.block = this.baseBlock;
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        applyPowers();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player instanceof Kiyohime) {
            ((Kiyohime) AbstractDungeon.player).StarCounter.addStarCount(this.block);
        }
        // AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new CritStarPower(p), this.block));
        if (AbstractDungeon.player.discardPile.size() > 0) {
            AbstractDungeon.actionManager.addToBottom(new DiscardPileToHandAction(this.magicNumber));
        }
    }

    public AbstractCard makeCopy() {
        return new GoldenCarpFigure();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}