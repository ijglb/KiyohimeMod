package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

import KiyohimeMod.powers.RoundTimerPower;

public class Fragarach extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:Fragarach";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/Fragarach.png";
    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int BASE_MAGIC = 2;//Hits
    private static final int BASE_Block = 10;
    private static final int UPGRADE_PLUS_Block = 3;

    public Fragarach() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, BASE_MAGIC, AbstractCard.CardRarity.UNCOMMON,
                AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = BASE_MAGIC;
        block = baseBlock = BASE_Block;
        isCalcBlock = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_Block);
          }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager
                .addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p,
                new RoundTimerPower(m, 1, EXTENDED_DESCRIPTION[0] + this.block + EXTENDED_DESCRIPTION[1],
                        PowerType.BUFF, new DamageAction(m, new DamageInfo(p, this.block, this.damageTypeForTurn),
                                AbstractGameAction.AttackEffect.SLASH_HEAVY))));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fragarach();
    }
}