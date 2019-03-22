package KiyohimeMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
public class CardAddFieldsPatch {
    public static SpireField<Integer> starCount = new SpireField<>(() -> 0);
    public static SpireField<Boolean> critical = new SpireField<>(() -> false);
    public static SpireField<Boolean> isSetTag = new SpireField<>(() -> false);
    public static SpireField<Boolean> isSpecialTag = new SpireField<>(() -> false);
    public static SpireField<String> origDescription = new SpireField<>(() -> null);
}