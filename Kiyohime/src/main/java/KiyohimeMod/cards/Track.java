package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.RoundTimerPower;
import basemod.abstracts.CustomCard;

public class Track extends CustomCard {

    public static final String ID = "KiyohimeMod:Track";
    public static final String IMG_PATH = "Kiyohime/images/cards/Track.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 0;
    private static final int BASE_MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = -2;

    public Track() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager
                .addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, 3, false), 3, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, 1), 1, true));
        AbstractDungeon.actionManager
                .addToBottom(
                        new ApplyPowerAction(
                                m, p, new RoundTimerPower(m, this.magicNumber, EXTENDED_DESCRIPTION[1],
                                        PowerType.DEBUFF, new ApplyPowerAction(m, p, new StrengthPower(m, -1))),
                                this.magicNumber, true));
    }

    public AbstractCard makeCopy() {
        return new Track();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.name = EXTENDED_DESCRIPTION[0];
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}