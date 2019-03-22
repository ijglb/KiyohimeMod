package KiyohimeMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.cards.ExtraAttack;
import KiyohimeMod.character.AbstractServant;
import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.helpers.CardHelper;
import KiyohimeMod.patches.KiyohimeTags;

public class NPPower extends AbstractPower {
    public static final String POWER_ID = "KiyohimeMod:NPPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    //public static final float NP_Suffer = 3f;
    //public static final float NP_Rate = 2.03f;
    public static final float[] NP_Arts = { 3f, 4.5f, 6f };
    public static final float[] NP_Quick = { 1f, 1.5f, 2f };

    public NPPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.type = AbstractPower.PowerType.BUFF;
        updateDescription();
        this.img = new Texture("Kiyohime/images/powers/NPPower.png");
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
        if (this.amount > 500) {
            this.amount = 500;
        }
        if (this.amount >= 100) {
            drawExCard();
        }
    }

    private void drawExCard() {
        if(AbstractDungeon.player instanceof Kiyohime){
            AbstractServant servant = ((Kiyohime)AbstractDungeon.player).Servant;
            AbstractCard ex = servant.npCard;
            if (!AbstractDungeon.player.hand.getCardNames().contains(ex.cardID)) {
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(ex, 1, false));
            }
        }
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount >= 100) {
            drawExCard();
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard usedCard, UseCardAction action) {
        if (usedCard.type == AbstractCard.CardType.ATTACK) {
            float npRate = 0;
            if(AbstractDungeon.player instanceof Kiyohime){
                AbstractServant servant = ((Kiyohime)AbstractDungeon.player).Servant;
                npRate = servant.npRate;
            }
            float first = 0f;//首位加成
            if (owner.hasPower(ArtsFirstPower.POWER_ID))
                first = 1f;
            int position = 0;
            if(owner.hasPower(ChaldeaPower.POWER_ID))
                position = owner.getPower(ChaldeaPower.POWER_ID).amount;
            if (position > 2)
                position = 2;
            float card = 0f;//指令卡补正
            float cardbuff = CardHelper.getCardUpBuffRate(usedCard);
            if (usedCard.hasTag(KiyohimeTags.ATTACK_Arts)) {
                card = NP_Arts[position];
            } else if (usedCard.hasTag(KiyohimeTags.ATTACK_Quick)) {
                card = NP_Quick[position];
            }
            if (usedCard instanceof ExtraAttack) {
                card = 1f;
            }
            float npGenerationRate = 0f;
            if (owner.hasPower(NPGenerationRatePower.POWER_ID)) {
                npGenerationRate = owner.getPower(NPGenerationRatePower.POWER_ID).amount / 100.0f;
            }
            int hits = CardHelper.getHits(usedCard);
            float critical = CardHelper.isCritical(usedCard) ? 1.5f : 1f;//暴击补正
            //NP率 * (指令卡补正 * (1 ± 指令卡性能BUFF ∓ 指令卡耐性) + 首位加成) * 敌补正 * (1 ± NP获得量BUFF) * 暴击补正 * Overkill补正
            float getNP = hits * ((npRate * (card * (1 + cardbuff) + first)) * (1 + npGenerationRate) * critical);
            if((int) getNP > 0)
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, this, (int) getNP, true));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        float npSuffer = 0;
        if (AbstractDungeon.player instanceof Kiyohime) {
            AbstractServant servant = ((Kiyohime) AbstractDungeon.player).Servant;
            npSuffer = servant.npSuffer;
        }
        float npGenerationRate = 0f;
        if (owner.hasPower(NPGenerationRatePower.POWER_ID)) {
            npGenerationRate = owner.getPower(NPGenerationRatePower.POWER_ID).amount / 100.0f;
        }
        //受击NP率 * 敌补正 * (1 ± NP获得量BUFF ± 受击NP获得量BUFF) * Overkill补正
        float getNP = npSuffer * (1 + npGenerationRate);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, this, (int) getNP, true));
        return damageAmount;
    }
}