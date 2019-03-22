package KiyohimeMod.helpers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import KiyohimeMod.actions.LoadCardBGAction;
import KiyohimeMod.cards.AbstractAttackCard;
import KiyohimeMod.character.StarCounter;
import KiyohimeMod.patches.CardAddFieldsPatch;
import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.ArtsUPPower;
import KiyohimeMod.powers.BusterUPPower;
import KiyohimeMod.powers.ChaldeaPower;
import KiyohimeMod.powers.FacelessMoonPower;
import KiyohimeMod.powers.QuickUPPower;
import KiyohimeMod.powers.AbstractStackablePower;

public class CardHelper {
    public static String star_Defult = StarCounter.starCounterText[3];
    public static String star_Crit = StarCounter.starCounterText[4];
    public static int maxStarRoom = 15;

    public static int getHits(AbstractCard c) {
        if (c instanceof AbstractAttackCard) {
            return ((AbstractAttackCard) c).Hits;
        }
        return 1;
    }

    public static int getStarCount(AbstractCard c) {
        if (c instanceof AbstractAttackCard) {
            return ((AbstractAttackCard) c).getStarCount();
        }
        return CardAddFieldsPatch.starCount.get(c);
    }

    public static int getStarRoom(AbstractCard c) {
        if (c instanceof AbstractAttackCard) {
            return ((AbstractAttackCard) c).getStarRoom();
        }
        return maxStarRoom - getStarCount(c);
    }

    public static boolean isCritical(AbstractCard c) {
        if (c instanceof AbstractAttackCard) {
            return ((AbstractAttackCard) c).isCritical();
        }
        return CardAddFieldsPatch.critical.get(c);
    }

    public static void addStarCount(AbstractCard c, int count) {
        if (c instanceof AbstractAttackCard) {
            ((AbstractAttackCard) c).addStarCount(count);
        } else {
            int starCount = getStarCount(c) + count;
            CardAddFieldsPatch.starCount.set(c, starCount);
            if (starCount >= maxStarRoom) {
                CardAddFieldsPatch.starCount.set(c, maxStarRoom);
                CardAddFieldsPatch.critical.set(c, true);
            } else {
                int i = AbstractDungeon.miscRng.random(1, maxStarRoom);
                CardAddFieldsPatch.critical.set(c, starCount >= i);
            }
            if (isCritical(c))
                c.rawDescription = star_Crit + CardAddFieldsPatch.origDescription.get(c);
            else
                c.rawDescription = star_Defult + CardAddFieldsPatch.origDescription.get(c);
            c.initializeDescription();
            c.calculateDamageDisplay(null);
        }
    }

    public static void setDefultTag(AbstractCard c) {
        if (!CardAddFieldsPatch.isSpecialTag.get(c) && CardAddFieldsPatch.isSetTag.get(c)) {
            if (c.hasTag(KiyohimeTags.ATTACK_Buster))
                c.tags.remove(KiyohimeTags.ATTACK_Buster);
            if (c.hasTag(KiyohimeTags.ATTACK_Arts))
                c.tags.remove(KiyohimeTags.ATTACK_Arts);
            if (c.hasTag(KiyohimeTags.ATTACK_Quick))
                c.tags.remove(KiyohimeTags.ATTACK_Quick);
            CardAddFieldsPatch.isSetTag.set(c, false);
            AbstractDungeon.actionManager.addToBottom(new LoadCardBGAction(c, false));
        }
        c.rawDescription = CardAddFieldsPatch.origDescription.get(c);
        c.initializeDescription();
        CardAddFieldsPatch.starCount.set(c, 0);
        CardAddFieldsPatch.critical.set(c, false);
    }

    public static void setRandomTag(AbstractCard c) {
        if (!CardAddFieldsPatch.isSetTag.get(c)) {
            if (!CardAddFieldsPatch.isSpecialTag.get(c) && AbstractDungeon.player.hasPower(ChaldeaPower.POWER_ID)) {
                if (AbstractDungeon.player.hasPower(FacelessMoonPower.POWER_ID)) {
                    // 锁定卡色
                    String lockedCard = ((FacelessMoonPower) AbstractDungeon.player
                            .getPower(FacelessMoonPower.POWER_ID)).lockedCard;
                    switch (lockedCard) {
                    case "Arts":
                        c.tags.add(KiyohimeTags.ATTACK_Arts);
                        break;
                    case "Quick":
                        c.tags.add(KiyohimeTags.ATTACK_Quick);
                        break;
                    case "Buster":
                        c.tags.add(KiyohimeTags.ATTACK_Buster);
                    default:
                        break;
                    }
                } else {
                    // 随机分配
                    int i = AbstractDungeon.miscRng.random(1, 3);
                    switch (i) {
                    case 1:
                        c.tags.add(KiyohimeTags.ATTACK_Arts);
                        break;
                    case 2:
                        c.tags.add(KiyohimeTags.ATTACK_Quick);
                        break;
                    case 3:
                        c.tags.add(KiyohimeTags.ATTACK_Buster);
                    default:
                        break;
                    }
                }
                CardAddFieldsPatch.isSetTag.set(c, true);
            }
            AbstractDungeon.actionManager.addToBottom(new LoadCardBGAction(c, true));
        }
    }

    public static float getCardUpBuffRate(AbstractCard c) {
        float buff = 0;
        if (c.hasTag(KiyohimeTags.ATTACK_Buster) && AbstractDungeon.player.hasPower(BusterUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(BusterUPPower.POWER_ID)).valueAmount;
        }
        if (c.hasTag(KiyohimeTags.ATTACK_Arts) && AbstractDungeon.player.hasPower(ArtsUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(ArtsUPPower.POWER_ID)).valueAmount;
        }
        if (c.hasTag(KiyohimeTags.ATTACK_Quick) && AbstractDungeon.player.hasPower(QuickUPPower.POWER_ID)) {
            buff = ((AbstractStackablePower) AbstractDungeon.player.getPower(QuickUPPower.POWER_ID)).valueAmount;
        }
        return buff / 100;
    }
}