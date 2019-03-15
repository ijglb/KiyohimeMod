package KiyohimeMod.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.actions.RandomAttackAction;
import KiyohimeMod.powers.AbstractStackablePower;
import KiyohimeMod.powers.CritUPPower;

public class BeastOfBillows extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:BeastOfBillows";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/BeastOfBillows.png";
    private static final int ATTACK_DMG = 5;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int BASE_MAGIC = 3;//hits

    public BeastOfBillows() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, BASE_MAGIC, AbstractCard.CardRarity.UNCOMMON,
                AbstractCard.CardTarget.ALL_ENEMY);
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
        AbstractDungeon.actionManager.addToBottom(new RandomAttackAction(
                AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
                new DamageInfo(p, this.damage), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BeastOfBillows();
    }

    @Override
    protected float calculate(float base, AbstractMonster target) {
        float temp = base;
        AbstractPlayer player = AbstractDungeon.player;
        for (AbstractPower p : player.powers) {
            temp = p.atDamageGive(temp, this.damageTypeForTurn);
        }
        // if (target != null) {
        //     for (AbstractPower p : target.powers) {
        //         temp = p.atDamageReceive(temp, this.damageTypeForTurn);
        //     }
        // }
        //卡性能 Final之前
        temp = MathUtils.round(temp * (1 + getCardUpBuffRate()));
        for (AbstractPower p : player.powers) {
            temp = p.atDamageFinalGive(temp, this.damageTypeForTurn);
        }
        // if (target != null) {
        //     for (AbstractPower p : target.powers) {
        //         temp = p.atDamageFinalReceive(temp, this.damageTypeForTurn);
        //     }
        // }
        if (critical) {
            float buff = 0f;
            if (player.hasPower(CritUPPower.POWER_ID)) {
                buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(CritUPPower.POWER_ID)).valueAmount;
            }
            temp = temp * 1.5f * (1f + (buff / 100f));
        }
        if (temp < 0) {
            temp = 0;
        }
        return temp;
    }
}