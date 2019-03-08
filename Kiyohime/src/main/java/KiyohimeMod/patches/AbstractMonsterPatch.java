package KiyohimeMod.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.relics.Stone;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class AbstractMonsterPatch {

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class DamagePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "r" })
        public static void Insert(AbstractMonster __instance, DamageInfo info,
                @ByRef(type = "com.megacrit.cardcrawl.relics.AbstractRelic") AbstractRelic[] r) {
            //Monster attack Monster = Monster attack Servant
            if (info.owner instanceof AbstractMonster) {
                if (AbstractDungeon.player instanceof Kiyohime && AbstractDungeon.player.hasRelic(Stone.ID)) {
                    r[0] = AbstractDungeon.player.getRelic(Stone.ID);//use Stone replace it
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            // This is the abstract method from SpireInsertLocator that will be used to find the line
            // numbers you want this patch inserted at
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                // finalMatcher is the line that we want to insert our patch before -
                // in this example we are using a `MethodCallMatcher` which is a type
                // of matcher that matches a method call based on the type of the calling
                // object and the name of the method being called. Here you can see that
                // we're expecting the `end` method to be called on a `SpireBatch`
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.relics.AbstractRelic",
                        "onAttackedMonster");

                // the `new ArrayList<Matcher>()` specifies the prerequisites before the line can be matched -
                // the LineFinder will search for all the prerequisites in order before it will match the finalMatcher -
                // since we didn't specify any prerequisites here, the LineFinder will simply find the first expression
                // that matches the finalMatcher.
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }
}