package KiyohimeMod.patches.cards;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import KiyohimeMod.relics.FireDragon;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class BurnPatch {

    @SpirePatch(clz = Burn.class, method = "use")
    public static class UsePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            AbstractRelic fireDragon = AbstractDungeon.player.getRelic(FireDragon.ID);
            if (fireDragon != null) {
                //fireDragon.flash();
                AbstractDungeon.actionManager
                        .addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, fireDragon));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player,
                        new DamageInfo(AbstractDungeon.player, 0, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.FIRE));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                ArrayList<Matcher> prevMatchers = new ArrayList<Matcher>();

                return LineFinder.findInOrder(ctMethodToPatch, prevMatchers, finalMatcher);
            }
        }
    }
}