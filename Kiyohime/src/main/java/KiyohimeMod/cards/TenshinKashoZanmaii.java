package KiyohimeMod.cards;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.NPPower;
import KiyohimeMod.relics.TheBlackGrail;

public class TenshinKashoZanmaii extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:TenshinKashoZanmaii";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/TenshinKashoZanmaii.png";
    public static final String BannerSmallImg = "images/cardui/512/banner_rare.png";
    public static final String BannerLargeImg = "images/cardui/1024/banner_rare.png";
    private static final int ATTACK_DMG = 12;
    //private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BASE_MAGIC = 3;//hits

    public TenshinKashoZanmaii() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, BASE_MAGIC, AbstractCard.CardRarity.SPECIAL,
                AbstractCard.CardTarget.ALL_ENEMY);
        this.setBannerTexture(BannerSmallImg, BannerLargeImg);
        isSpecialTag = true;
        isMultiDamage = true;
        this.tags.add(KiyohimeTags.ATTACK_Buster);
        this.exhaust = true;
        this.isEthereal = true;
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
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (canUse) {
            if (p.hasPower(NPPower.POWER_ID)) {
                int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
                if (np < 100) {
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

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //use all np
        int np = AbstractDungeon.player.getPower(NPPower.POWER_ID).amount;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NPPower(p), -np, true));
        //attack
        for (int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                    this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        }
        //stun
        float stun = 50;
        if (np >= 100) {
            stun = stun * (np / 100);
        }
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (AbstractMonster monster : monsters) {
            int rd = AbstractDungeon.miscRng.random(0, 100);
            if (stun >= rd) {
                AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(monster, p));
            }
        }
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
        if (AbstractDungeon.player.hasRelic(TheBlackGrail.ID)) {
            temp = temp * 1.8f;//黑杯80%
        }

        return super.calculate(temp, target);
    }

    @Override
    public AbstractCard makeCopy() {
        return new TenshinKashoZanmaii();
    }
}