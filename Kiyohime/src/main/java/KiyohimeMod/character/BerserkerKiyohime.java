package KiyohimeMod.character;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import KiyohimeMod.cards.ExtraAttack;
import KiyohimeMod.cards.TenshinKashoZanmaii;
import basemod.animations.SpriterAnimation;

public class BerserkerKiyohime extends AbstractServant {

    public static final String NAME = "Kiyohime_Berserker";

    private static final String[] BattleStartSounds = { "KiyohimeMod:Berserker_BattleStart_1",
            "KiyohimeMod:Berserker_BattleStart_2", "KiyohimeMod:Berserker_BattleStart_3",
            "KiyohimeMod:Berserker_BattleStart_4" };

    public BerserkerKiyohime() {
        super(NAME, 2.03f, 3f, 0.75f, new TenshinKashoZanmaii(), new ExtraAttack(5, 3),
                new SpriterAnimation("Kiyohime/images/char/idle/Kiyohime.scml"));
    }

    @Override
    public String getBattleStartSound() {
        int r = AbstractDungeon.miscRng.random(0, BattleStartSounds.length - 1);
        return BattleStartSounds[r];
    }

    @Override
    public int getBusterHits() {
        return 1;
    }

    @Override
    public int getQuickHits() {
        return 2;
    }

    @Override
    public int getArtsHits() {
        return 1;
    }
    
}