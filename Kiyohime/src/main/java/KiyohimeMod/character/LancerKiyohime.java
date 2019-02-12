package KiyohimeMod.character;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import KiyohimeMod.cards.DoujyoujikaneHyakuhachishikikaryuunagi;
import KiyohimeMod.cards.ExtraAttack;
import basemod.animations.SpriterAnimation;

public class LancerKiyohime extends AbstractServant {

    private static final String[] BattleStartSounds = { "KiyohimeMod:Lancer_BattleStart_1",
            "KiyohimeMod:Lancer_BattleStart_2" };


    public LancerKiyohime() {
        super(1.05f, 2f, 1.8f, new DoujyoujikaneHyakuhachishikikaryuunagi(), new ExtraAttack(3, 5),
                new SpriterAnimation("Kiyohime/images/char/idle/Kiyohime_Lancer.scml"));
    }

    @Override
    public String getBattleStartSound() {
        int r = AbstractDungeon.miscRng.random(0, BattleStartSounds.length - 1);
        return BattleStartSounds[r];
    }

    @Override
    public int getBusterHits() {
        return 6;
    }

    @Override
    public int getQuickHits() {
        return 2;
    }

    @Override
    public int getArtsHits() {
        return 2;
    }
    
}