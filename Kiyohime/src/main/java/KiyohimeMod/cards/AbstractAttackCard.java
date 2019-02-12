package KiyohimeMod.cards;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.actions.LoadCardBGAction;
import KiyohimeMod.patches.AbstractCardEnum;
import basemod.abstracts.CustomCard;
import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.AbstractStackablePower;
import KiyohimeMod.powers.ArtsUPPower;
import KiyohimeMod.powers.BusterUPPower;
import KiyohimeMod.powers.ChaldeaPower;
import KiyohimeMod.powers.QuickUPPower;

public abstract class AbstractAttackCard extends CustomCard {

    public static String BG_Buster = "Kiyohime/images/512/bg_attack_buster.png";
    public static String BG_Arts = "Kiyohime/images/512/bg_attack_arts.png";
    public static String BG_Quick = "Kiyohime/images/512/bg_attack_quick.png";
    public static String BG_Buster_L = "Kiyohime/images/1024/bg_attack_buster.png";
    public static String BG_Arts_L = "Kiyohime/images/1024/bg_attack_arts.png";
    public static String BG_Quick_L = "Kiyohime/images/1024/bg_attack_quick.png";
    public static String BG_Defult = "Kiyohime/images/512/bg_attack.png";
    public static String BG_Defult_L = "Kiyohime/images/1024/bg_attack.png";

    private boolean isSetTag = false;
    private int ampDamage = -1;
    protected int[] multiAmpDamage;
    protected boolean isCalcBlock = false;
    protected boolean isSpecialTag = false;
    public int Hits;

    public AbstractAttackCard(String id, String name, String img, String rawDescription,int hits, AbstractCard.CardRarity rarity,
            AbstractCard.CardTarget target) {

        super(id, name, img, 0, rawDescription, CardType.ATTACK, AbstractCardEnum.Kiyohime_Color, rarity, target);
        this.Hits = hits;
        this.cost = GetBaseCost();
        this.costForTurn = this.cost;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            int base = GetBaseCost();
            if (base > 0) {
                upgradeBaseCost(base - 1);
            }
        }
    }
    
    private int GetBaseCost() {
        switch (rarity) {
        case BASIC:
        case COMMON:
        case SPECIAL:
            return 0;
        case UNCOMMON:
            return 1;
        case RARE:
            return 2;
        default:
            return 0;
        }
    }

    @Override
    public void onMoveToDiscard() {
        setDefultTag();
    }
    
    @Override
    public void triggerOnExhaust() {
        setDefultTag();
    }

    private void setDefultTag() {
        if (!isSpecialTag && isSetTag) {
            if (this.hasTag(KiyohimeTags.ATTACK_Buster))
                this.tags.remove(KiyohimeTags.ATTACK_Buster);
            if (this.hasTag(KiyohimeTags.ATTACK_Arts))
                this.tags.remove(KiyohimeTags.ATTACK_Arts);
            if (this.hasTag(KiyohimeTags.ATTACK_Quick))
                this.tags.remove(KiyohimeTags.ATTACK_Quick);
            isSetTag = false;
            AbstractDungeon.actionManager.addToBottom(new LoadCardBGAction(this, BG_Defult, BG_Defult_L, false));
        }
    }

    private void setRandomTag() {
        if (!isSetTag) {
            if (!isSpecialTag && AbstractDungeon.player.hasPower(ChaldeaPower.POWER_ID)) {
                //随机分配
                int i = AbstractDungeon.miscRng.random(1, 3);
                switch (i) {
                case 1:
                    this.tags.add(KiyohimeTags.ATTACK_Arts);
                    break;
                case 2:
                    this.tags.add(KiyohimeTags.ATTACK_Quick);
                    break;
                case 3:
                    this.tags.add(KiyohimeTags.ATTACK_Buster);
                default:
                    break;
                }
                isSetTag = true;
            }
            if (this.hasTag(KiyohimeTags.ATTACK_Buster))
                AbstractDungeon.actionManager.addToBottom(new LoadCardBGAction(this, BG_Buster, BG_Buster_L, true));
            if (this.hasTag(KiyohimeTags.ATTACK_Arts))
                AbstractDungeon.actionManager.addToBottom(new LoadCardBGAction(this, BG_Arts, BG_Arts_L, true));
            if (this.hasTag(KiyohimeTags.ATTACK_Quick))
                AbstractDungeon.actionManager.addToBottom(new LoadCardBGAction(this, BG_Quick, BG_Quick_L, true));
        }
    }

    public float getCardUpBuffRate() {
        float buff = 0;
        if (this.hasTag(KiyohimeTags.ATTACK_Buster) && AbstractDungeon.player.hasPower(BusterUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(BusterUPPower.POWER_ID)).valueAmount;
        }
        if (this.hasTag(KiyohimeTags.ATTACK_Arts) && AbstractDungeon.player.hasPower(ArtsUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(ArtsUPPower.POWER_ID)).valueAmount;
        }
        if (this.hasTag(KiyohimeTags.ATTACK_Quick) && AbstractDungeon.player.hasPower(QuickUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(QuickUPPower.POWER_ID)).valueAmount;
        }
        return buff / 100;
    }

    //https://github.com/lf201014/STS_ThMod_MRS
    protected float calculate(float base, AbstractMonster target) {
        float temp = base;
        AbstractPlayer player = AbstractDungeon.player;
        for (AbstractPower p : player.powers) {
            temp = p.atDamageGive(temp, this.damageTypeForTurn);
        }
        if (target != null) {
            for (AbstractPower p : target.powers) {
                temp = p.atDamageReceive(temp, this.damageTypeForTurn);
            }
        }
        //卡性能 Final之前
        temp = MathUtils.round(temp * (1 + getCardUpBuffRate()));
        for (AbstractPower p : player.powers) {
            temp = p.atDamageFinalGive(temp, this.damageTypeForTurn);
        }
        if (target != null) {
            for (AbstractPower p : target.powers) {
                temp = p.atDamageFinalReceive(temp, this.damageTypeForTurn);
            }
        }
        if (temp < 0) {
            temp = 0;
        }
        return temp;
    }

    @Override
    public void triggerWhenDrawn() {
        setRandomTag();
    }

    @Override
    public void applyPowers() {
        setRandomTag();
        this.damage = this.baseDamage;
        this.block = this.baseBlock;

        this.isDamageModified = false;
        this.isBlockModified = false;
        float tmp = this.damage;
        float amp = this.block;
        tmp = calculate(tmp, null);
        if (isCalcBlock)
            amp = calculate(amp, null);
        if (this.baseDamage != (int) tmp) {
            this.isDamageModified = true;
        }
        if (isCalcBlock && this.ampDamage != (int) amp) {
            this.isBlockModified = true;
        }
        this.damage = MathUtils.floor(tmp);
        if (isCalcBlock)
            this.block = MathUtils.floor(amp);
        if (this.isMultiDamage) {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            this.multiDamage = new int[m.size()];
            for (int i = 0; i < m.size(); i++) {
                this.multiDamage[i] = MathUtils.floor(tmp);
            }
            if (isCalcBlock) {
                this.multiAmpDamage = new int[m.size()];
                for (int i = 0; i < m.size(); i++) {
                    this.multiAmpDamage[i] = MathUtils.floor(amp);
                }
            }
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.damage = this.baseDamage;
        this.block = this.baseBlock;
        
        this.isDamageModified = false;
        this.isBlockModified = false;
        if ((!this.isMultiDamage) && (mo != null)) {
            float tmp = this.damage;
            float amp = this.block;
            tmp = calculate(tmp, mo);
            if (isCalcBlock)
                amp = calculate(amp, mo);
            if (this.baseDamage != (int) tmp) {
                this.isDamageModified = true;
            }
            if (isCalcBlock && this.ampDamage != (int) amp) {
                this.isBlockModified = true;
            }
            this.damage = MathUtils.floor(tmp);
            if (isCalcBlock)
                this.block = MathUtils.floor(amp);
        } else {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[m.size()];
            float[] amp = new float[m.size()];
            for (int i = 0; i < m.size(); i++) {
                tmp[i] = calculate(damage, m.get(i));
                if (isCalcBlock)
                    amp[i] = calculate(block, m.get(i));
                if (this.baseDamage != (int) tmp[i]) {
                    this.isDamageModified = true;
                }
                if (isCalcBlock && this.ampDamage != (int) amp[i]) {
                    this.isBlockModified = true;
                }
            }
            this.multiDamage = new int[m.size()];
            for (int i = 0; i < tmp.length; i++) {
                this.multiDamage[i] = MathUtils.floor(tmp[i]);
            }
            if (isCalcBlock) {
                this.multiAmpDamage = new int[m.size()];
                for (int i = 0; i < amp.length; i++) {
                    this.multiAmpDamage[i] = MathUtils.floor(amp[i]);
                }
                this.block = this.multiAmpDamage[0];
            }

            this.damage = this.multiDamage[0];
        }
    }

    @Override
    public void calculateDamageDisplay(AbstractMonster mo) {
        calculateCardDamage(mo);
    }
}