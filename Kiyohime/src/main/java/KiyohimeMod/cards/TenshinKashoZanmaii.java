package KiyohimeMod.cards;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;

import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.NPPower;

public class TenshinKashoZanmaii extends AbstractNPCard {

    public static final String ID = "KiyohimeMod:TenshinKashoZanmaii";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/TenshinKashoZanmaii.png";
    private static final int ATTACK_DMG = 12;
    //private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BASE_MAGIC = 3;//hits

    public TenshinKashoZanmaii() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, BASE_MAGIC, AbstractCard.CardRarity.SPECIAL,
                AbstractCard.CardTarget.ALL_ENEMY);
        isMultiDamage = true;
        this.tags.add(KiyohimeTags.ATTACK_Buster);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = BASE_MAGIC;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (!this.upgraded) {
            upgradeName();
            //upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        //attack
        for (int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                    this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        }
        //stun
        int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;

        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (AbstractMonster monster : monsters) {
            float stun = 50;
            if (monster.type == EnemyType.BOSS) {
                stun = 35;
            }
            if (np >= 100) {
                stun = stun * (np / 100);
            }
            int rd = AbstractDungeon.miscRng.random(0, 100);
            if (stun >= rd) {
                AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(monster, p));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TenshinKashoZanmaii();
    }
}