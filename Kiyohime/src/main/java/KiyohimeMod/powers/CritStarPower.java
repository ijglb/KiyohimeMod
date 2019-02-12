package KiyohimeMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.cards.AbstractAttackCard;
import KiyohimeMod.character.AbstractServant;
import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.patches.KiyohimeTags;

public class CritStarPower extends AbstractPower {
    public static final String POWER_ID = "KiyohimeMod:CritStarPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    //public static final float Star_Rate = 0.49f;
    public static final float[] Star_Buster = { 0.1f, 0.15f, 0.20f };
    public static final float[] Star_Quick = { 0.8f, 1.3f, 1.8f };

    public CritStarPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.type = AbstractPower.PowerType.BUFF;
        updateDescription();
        this.img = new Texture("Kiyohime/images/powers/CritStarPower.png");
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (this.amount < 0)
            this.amount = 0;
        super.stackPower(stackAmount);
    }

    @Override
    public void onAfterUseCard(AbstractCard usedCard, UseCardAction action) {
        if (usedCard.type == AbstractCard.CardType.ATTACK) {
            if (this.amount >= 50)
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, this, -50, true));
            if (usedCard instanceof AbstractAttackCard) {
                //从者掉星率 + 指令卡掉星率 * (1 ± 指令卡性能BUFF ∓ 指令卡耐性) + 首位加成 + 敌补正 ± 掉星率BUFF ± 敌人掉星率BUFF + 暴击补正 + Overkill补正
                float starRate = 0;
                if(AbstractDungeon.player instanceof Kiyohime){
                    AbstractServant servant = ((Kiyohime)AbstractDungeon.player).Servant;
                    starRate = servant.starRate;
                }
                float first = 0f;//首位加成
                if (owner.hasPower(QuickFirstPower.POWER_ID))
                    first = 1f;
                int position = owner.getPower(ChaldeaPower.POWER_ID).amount;
                if (position > 2)
                    position = 2;
                float card = 0f;//指令卡掉星率
                float cardbuff = ((AbstractAttackCard) usedCard).getCardUpBuffRate();
                if (usedCard.hasTag(KiyohimeTags.ATTACK_Buster)) {
                    card = Star_Buster[position];
                } else if (usedCard.hasTag(KiyohimeTags.ATTACK_Quick)) {
                    card = Star_Quick[position];
                }
                int hits = ((AbstractAttackCard) usedCard).Hits;
                int getStar = MathUtils.round(hits * (starRate + card * (1 + cardbuff) + first));
                if (getStar > 0)
                    AbstractDungeon.actionManager
                            .addToBottom(new ApplyPowerAction(owner, owner, this, getStar, true));
            }
        }
    }

    @Override
    public float atDamageFinalGive(float damage, DamageType type) {
        if (this.amount >= 50 && type == DamageType.NORMAL) {
            float buff = 0f;
            if (owner.hasPower(CritUPPower.POWER_ID)) {
                buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(CritUPPower.POWER_ID)).valueAmount;
            }
            return damage * (2 * (1 + (buff / 100)));
        }
        return damage;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (usedCard.type == CardType.ATTACK && this.amount >= 50) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, this, -50, true));
        }
    }
}