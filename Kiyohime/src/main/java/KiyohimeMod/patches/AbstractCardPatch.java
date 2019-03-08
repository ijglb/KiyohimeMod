package KiyohimeMod.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;

import KiyohimeMod.cards.AbstractAttackCard;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class AbstractCardPatch {

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class InitializeDescriptionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "lastChar", "word" })
        public static void Insert(AbstractCard __instance, @ByRef StringBuilder[] lastChar, @ByRef String[] word) {
            if (__instance instanceof AbstractAttackCard) {
                if (word[0].equals("☆") || word[0].equals("★")) {
                    lastChar[0] = new StringBuilder(" ");
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
}