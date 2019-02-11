package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Destruction extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:Destruction";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/Destruction.png";
    private static final int ATTACK_DMG = 12;
    private static final int UPGRADE_PLUS_DMG = 8;

    public Destruction() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, 1, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    protected float calculate(float base, AbstractMonster target) {
        float temp = base;
        if (target != null && target.currentBlock > 0) {
            temp = temp * 2;
        }

        return super.calculate(temp, target);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Destruction();
    }
}