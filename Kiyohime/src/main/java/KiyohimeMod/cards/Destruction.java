package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
    private static final int ATTACK_DMG = 18;
    private static final int UPGRADE_PLUS_DMG = 7;

    public Destruction() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, 1, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL_ENEMY);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void upgrade() {
        //super.upgrade();
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        int drawCard = 0;
        int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        for (int i = 0; i < temp; i++) {
            AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
            if (!mo.isDead && mo.currentHealth > 0 && mo.currentBlock > 0) {
                drawCard++;
            }
        }
        if (drawCard > 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, drawCard));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Destruction();
    }
}