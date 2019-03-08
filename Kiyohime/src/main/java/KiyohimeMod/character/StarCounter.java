package KiyohimeMod.character;

import basemod.ClickableUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import KiyohimeMod.actions.CardApplyStarAction;
import KiyohimeMod.cards.AbstractAttackCard;
import KiyohimeMod.cards.ExtraAttack;
import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.AbstractStackablePower;
import KiyohimeMod.powers.ChaldeaPower;
import KiyohimeMod.powers.CritStarGenerationRatePower;
import KiyohimeMod.powers.CritUPPower;
import KiyohimeMod.powers.QuickFirstPower;

public class StarCounter extends ClickableUIElement {
    private static float x = 20f * Settings.scale;
    private static float y = 190f * Settings.scale;
    private static float textX = 80f * Settings.scale;
    private static float textY = 190f * Settings.scale;
    private static float hb_w = 128f;
    private static float hb_h = 128f;
    private static Texture normalImage = ImageMaster.loadImage("Kiyohime/images/ui/StarCounter.png");
    private static Texture hoverlImage = ImageMaster.loadImage("Kiyohime/images/ui/StarCounter_hover.png");
    private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("KiyohimeMod:StarCounter");
    public static String[] starCounterText = uiStrings.TEXT;
    public static final float[] Star_Buster = { 0.1f, 0.15f, 0.20f, 1f };
    public static final float[] Star_Quick = { 0.8f, 1.3f, 1.8f, 1f };
    private int starCount = 0;

    public StarCounter() {
        super(normalImage, x, y, hb_w, hb_h);
    }

    public void addStarCount(int count) {
        starCount += count;
        if (starCount < 0)
            starCount = 0;
    }

    public int getStarCount() {
        return starCount;
    }

    public void resetCounter() {
        starCount = 0;
    }

    public void onUseCard(AbstractCard usedCard) {
        if (usedCard.type == AbstractCard.CardType.ATTACK) {
            if (usedCard instanceof AbstractAttackCard) {
                float starRate = 0;
                if (AbstractDungeon.player instanceof Kiyohime) {
                    AbstractServant servant = ((Kiyohime) AbstractDungeon.player).Servant;
                    starRate = servant.starRate;
                }
                float first = 0f;//首位加成
                if (AbstractDungeon.player.hasPower(QuickFirstPower.POWER_ID))
                    first = 1f;
                int position = 0;
                if (AbstractDungeon.player.hasPower(ChaldeaPower.POWER_ID))
                    position = AbstractDungeon.player.getPower(ChaldeaPower.POWER_ID).amount;
                if (position > 2)
                    position = 2;
                float card = 0f;//指令卡掉星率
                float cardbuff = ((AbstractAttackCard) usedCard).getCardUpBuffRate();
                if (usedCard.hasTag(KiyohimeTags.ATTACK_Buster)) {
                    card = Star_Buster[position];
                } else if (usedCard.hasTag(KiyohimeTags.ATTACK_Quick)) {
                    card = Star_Quick[position];
                }
                if (usedCard instanceof ExtraAttack) {
                    card = 1f;
                }
                float starGenerationRate = 0f;
                if (AbstractDungeon.player.hasPower(CritStarGenerationRatePower.POWER_ID)) {
                    starGenerationRate = AbstractDungeon.player.getPower(CritStarGenerationRatePower.POWER_ID).amount
                            / 100.0f;
                }
                int hits = ((AbstractAttackCard) usedCard).Hits;
                float critical = ((AbstractAttackCard) usedCard).isCritical() ? 0.15f : 0f;//暴击补正
                //从者掉星率 + 指令卡掉星率 * (1 ± 指令卡性能BUFF ∓ 指令卡耐性) + 首位加成 + 敌补正 ± 掉星率BUFF ± 敌人掉星率BUFF + 暴击补正 + Overkill补正
                int getStar = MathUtils
                        .round(hits * (starRate + card * (1 + cardbuff) + first + starGenerationRate + critical));
                if (getStar > 0)
                    this.starCount += getStar;
            }
        }
    }
    
    @Override
    protected void onHover() {
        this.image = hoverlImage;
        float buff = 0f;
        float base = 1.5f;
        if (AbstractDungeon.player.hasPower(CritUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(CritUPPower.POWER_ID)).valueAmount;
        }
        float power = (base * (1f + (buff / 100f))) * 100;
        String desc = starCounterText[1] + power + "%";
        TipHelper.renderGenericTip(50.0f * Settings.scale, 380.0f * Settings.scale, starCounterText[0], desc);
    }

    @Override
    protected void onUnhover() {
        this.image = normalImage;
    }

    @Override
    protected void onClick() {
        if (!AbstractDungeon.actionManager.turnHasEnded && this.starCount > 0) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, starCounterText[2], 1.0f, 1.0f));
            AbstractDungeon.actionManager.addToBottom(new CardApplyStarAction(this.starCount));
            this.starCount = 0;
        }
    }

    @Override
    public void update() {
        if (CardCrawlGame.dungeon != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            super.update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (CardCrawlGame.dungeon != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            super.render(sb);
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontRed, Integer.toString(this.starCount), textX,
                    textY, Color.WHITE);
        }
    }
}