package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.powers.AbstractStackablePower;
import KiyohimeMod.powers.NPDamagePower;
import KiyohimeMod.powers.NPPower;

public abstract class AbstractNPCard extends AbstractAttackCard {

    public static final CardStrings npCardStrings = CardCrawlGame.languagePack.getCardStrings("KiyohimeMod:NPCard");

    public AbstractNPCard(String id, String name, String img, String rawDescription,int hits, AbstractCard.CardRarity rarity,
    AbstractCard.CardTarget target) {
        super(id, name, img, rawDescription, hits, rarity, target);
        this.setDisplayRarity(CardRarity.RARE);
        isSpecialTag = true;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (canUse) {
            if (p.hasPower(NPPower.POWER_ID)) {
                int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
                if (np < 100) {
                    canUse = false;
                    this.cantUseMessage = npCardStrings.EXTENDED_DESCRIPTION[0];
                }
            }
            else{
                canUse = false;
                this.cantUseMessage = npCardStrings.EXTENDED_DESCRIPTION[0];
            }
        }
        return canUse;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //use all np
        int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NPPower(p), -np, true));
    }

    @Override
    protected float calculate(float base, AbstractMonster target) {
        float temp = base;
        if (AbstractDungeon.player.hasPower(NPPower.POWER_ID)) {
            int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
            if (np >= 100) {
                temp = temp * ((float) np / 100);
            }
        }
        if (AbstractDungeon.player.hasPower(NPDamagePower.POWER_ID)) {
            float up = (((AbstractStackablePower) AbstractDungeon.player.getPower(NPDamagePower.POWER_ID)).valueAmount)
                    / 100.0f;
            temp = temp * (1 + up);
        }

        return super.calculate(temp, target);
    }
}