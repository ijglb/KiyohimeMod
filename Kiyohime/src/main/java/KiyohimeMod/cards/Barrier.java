package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.InvinciblePower;
import KiyohimeMod.powers.NPPower;
import basemod.abstracts.CustomCard;

public class Barrier extends CustomCard {

    public static final String ID = "KiyohimeMod:Barrier";
    public static final String IMG_PATH = "Kiyohime/images/cards/Barrier.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int BASE_MAGIC = 1;
    private static final int BASE_DMG = 20;

    public Barrier() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BASE_MAGIC;
        this.baseDamage = this.damage = BASE_DMG;
        this.exhaust = true;
    }

    @Override
    public void applyPowers() {
        this.damage = this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster arg0) {
        applyPowers();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (canUse) {
            if (p.hasPower(NPPower.POWER_ID)) {
                int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
                if (np < 20) {
                    canUse = false;
                    this.cantUseMessage = EXTENDED_DESCRIPTION[0];
                }
            }
            else{
                canUse = false;
                this.cantUseMessage = EXTENDED_DESCRIPTION[0];
            }
        }
        return canUse;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        //use 20 np
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NPPower(p), -this.damage, true));
        AbstractDungeon.actionManager
                .addToBottom(new ApplyPowerAction(p, p, new InvinciblePower(p, this.magicNumber), this.magicNumber));
    }

    public AbstractCard makeCopy() {
        return new Barrier();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}