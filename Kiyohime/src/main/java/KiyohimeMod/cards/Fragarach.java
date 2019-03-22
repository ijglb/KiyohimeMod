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

public class Fragarach extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:Fragarach";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/Fragarach.png";
    private static final int ATTACK_DMG = 5;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BASE_MAGIC = 1;//Hits

    public Fragarach() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, BASE_MAGIC, AbstractCard.CardRarity.UNCOMMON,
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
          }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.Hits = this.magicNumber;
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager
                    .addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        upgradeMagicNumber(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fragarach();
    }
}