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

import KiyohimeMod.powers.NPPower;

public class JeweledSword extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:JeweledSword";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/JeweledSword.png";
    private static final int ATTACK_DMG = 10;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int BASE_MAGIC = 1;

    public JeweledSword() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, 1, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = BASE_MAGIC;
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
                new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    protected float calculate(float base, AbstractMonster target) {
        float temp = base;
        if (AbstractDungeon.player.hasPower(NPPower.POWER_ID)) {
            int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
            int add = (np / 10) * this.magicNumber;
            temp = temp + add;
        }

        return super.calculate(temp, target);
    }

    @Override
    public AbstractCard makeCopy() {
        return new JeweledSword();
    }
}