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

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.powers.QuickUPPower;

public class GreenBlackKey extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:GreenBlackKey";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/GreenBlackKey.png";
    private static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int BASE_MAGIC = 15;
    private static final int UPGRADE_PLUS_MAGIC = 5;

    public GreenBlackKey() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, 1, AbstractCard.CardRarity.COMMON,
                AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = BASE_MAGIC;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(p, p,
                new QuickUPPower(p, this.magicNumber, 1), this.magicNumber, 1, true));
        AbstractDungeon.actionManager
                .addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreenBlackKey();
    }
}