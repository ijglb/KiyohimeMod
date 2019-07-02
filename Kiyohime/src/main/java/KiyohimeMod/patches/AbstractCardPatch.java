package KiyohimeMod.patches;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.cards.AbstractAttackCard;
import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.helpers.CardHelper;
import KiyohimeMod.patches.CardAddFieldsPatch;
import KiyohimeMod.powers.CritUPPower;
import basemod.helpers.SuperclassFinder;
import KiyohimeMod.powers.AbstractStackablePower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class AbstractCardPatch {

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class InitializeDescriptionPatch {
        public static void Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player instanceof Kiyohime) {
                if (CardAddFieldsPatch.origDescription.get(__instance) == null
                        || (__instance.rawDescription.indexOf(CardHelper.star_Crit) == -1
                                && __instance.rawDescription.indexOf(CardHelper.star_Defult) == -1)) {
                    CardAddFieldsPatch.origDescription.set(__instance, __instance.rawDescription);
                }
            }
        }
        @SpireInsertPatch(locator = Locator.class, localvars = { "word" })
        public static void Insert(AbstractCard __instance, @ByRef String[] word) {
            if (__instance instanceof AbstractAttackCard) {
                if (word[0].equals("☆") || word[0].equals("★")) {
                    //lastChar[0] = new StringBuilder(" ");
                    word[0] = word[0] + " ";
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(String.class, "substring");
                ArrayList<Matcher> prevMatchers = new ArrayList<Matcher>();
                prevMatchers.add(new Matcher.MethodCallMatcher(Character.class, "isLetterOrDigit"));

                return LineFinder.findInOrder(ctMethodToPatch, prevMatchers, finalMatcher);
            }
        }
    }
    
    @SpirePatch(clz = AbstractCard.class, method = "teleportToDiscardPile")
    public static class TeleportToDiscardPilePatch {
        public static void Postfix(AbstractCard __instance) {
            if (AbstractDungeon.player instanceof Kiyohime && __instance.type == CardType.ATTACK
                    && !(__instance instanceof AbstractAttackCard)
                    && __instance.color == AbstractCard.CardColor.COLORLESS) {
                CardHelper.setDefultTag(__instance);
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class MoveToExhaustPilePatch {
        public static void Prefix(CardGroup __instance, AbstractCard c) {
            if (AbstractDungeon.player instanceof Kiyohime && c.type == CardType.ATTACK
                    && !(c instanceof AbstractAttackCard) && c.color == AbstractCard.CardColor.COLORLESS) {
                CardHelper.setDefultTag(c);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardDamagePatch {
        public static void Postfix(AbstractCard __instance, AbstractMonster mo) {
            if (AbstractDungeon.player instanceof Kiyohime) {
                __instance.damage = CalcDamage(__instance, __instance.damage);
                if (__instance.multiDamage != null) {
                    for (int i = 0; i < __instance.multiDamage.length; i++) {
                        __instance.multiDamage[i] = CalcDamage(__instance, __instance.multiDamage[i]);
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersPatch {
        public static void Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player instanceof Kiyohime && __instance.type == CardType.ATTACK
                    && !(__instance instanceof AbstractAttackCard)
                    && __instance.color == AbstractCard.CardColor.COLORLESS) {
                CardHelper.setRandomTag(__instance);
            }
        }

        public static void Postfix(AbstractCard __instance) {
            if (AbstractDungeon.player instanceof Kiyohime) {
                __instance.damage = CalcDamage(__instance, __instance.damage);
                if (__instance.multiDamage != null) {
                    for (int i = 0; i < __instance.multiDamage.length; i++) {
                        __instance.multiDamage[i] = CalcDamage(__instance, __instance.multiDamage[i]);
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderAttackBg")
    public static class RenderAttackBgPatch {
        public static HashMap<String, Texture> imgMap;
        static {
            imgMap = new HashMap<>();
            imgMap.put("BG_Buster", ImageMaster.loadImage("Kiyohime/images/512/colorless_attack_buster.png"));
            imgMap.put("BG_Arts", ImageMaster.loadImage("Kiyohime/images/512/colorless_attack_arts.png"));
            imgMap.put("BG_Quick", ImageMaster.loadImage("Kiyohime/images/512/colorless_attack_quick.png"));
            //imgMap.put("BG_Defult", ImageMaster.CARD_ATTACK_BG_GRAY);
        }

        public static SpireReturn<?> Prefix(AbstractCard __instance, SpriteBatch sb, float x, float y) {
            if (AbstractDungeon.player instanceof Kiyohime && __instance.type == CardType.ATTACK
                    && !(__instance instanceof AbstractAttackCard)
                    && __instance.color == AbstractCard.CardColor.COLORLESS) {
                Texture texture = null;
                TextureAtlas.AtlasRegion region = null;
                if (__instance.hasTag(KiyohimeTags.ATTACK_Buster))
                    texture = imgMap.get("BG_Buster");
                else if (__instance.hasTag(KiyohimeTags.ATTACK_Arts))
                    texture = imgMap.get("BG_Arts");
                else if (__instance.hasTag(KiyohimeTags.ATTACK_Quick))
                    texture = imgMap.get("BG_Quick");
                else
                    region = ImageMaster.CARD_ATTACK_BG_GRAY;

                if (texture != null) {
                    region = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
                }

                renderHelper(__instance, sb, Color.WHITE, region, x, y);
                SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    public static int CalcDamage(AbstractCard c, float damage) {
        float buff = 0f;
        if (AbstractDungeon.player.hasPower(CritUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(CritUPPower.POWER_ID)).valueAmount;
        }

        float temp = damage;
        temp = temp * (1 + CardHelper.getCardUpBuffRate(c));
        if (CardHelper.isCritical(c)) {
            temp = temp * 1.5f * (1f + (buff / 100f));
        }
        return MathUtils.floor(temp);
    }

    private static void renderHelper(AbstractCard card, SpriteBatch sb, Color color, TextureAtlas.AtlasRegion region, float xPos, float yPos)
	{
		try {
			// use reflection hacks to invoke renderHelper (without float scale)
			Method renderHelperMethod;
			Field renderColorField; 
			
			
			renderHelperMethod = SuperclassFinder.getSuperClassMethod(card.getClass(), "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
			renderHelperMethod.setAccessible(true);
			renderColorField = SuperclassFinder.getSuperclassField(card.getClass(), "renderColor");
			renderColorField.setAccessible(true);
			
				
			Color renderColor = (Color) renderColorField.get(card);
			renderHelperMethod.invoke(card, sb, renderColor, region, xPos, yPos);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}
	}
}