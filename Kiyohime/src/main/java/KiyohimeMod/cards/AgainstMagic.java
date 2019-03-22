package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import KiyohimeMod.patches.AbstractCardEnum;
import basemod.abstracts.CustomCard;

public class AgainstMagic extends CustomCard {

    public static final String ID = "KiyohimeMod:AgainstMagic";
    public static final String IMG_PATH = "Kiyohime/images/cards/AgainstMagic.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int BASE_MAGIC = 1;
    private static final int UPGRADE_COST = 0;

    public AgainstMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager
                .addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
    }

    public AbstractCard makeCopy() {
        return new AgainstMagic();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}