package KiyohimeMod.character;

import KiyohimeMod.cards.ExtraAttack;
import KiyohimeMod.cards.TenshinKashoZanmaii;
import basemod.animations.SpriterAnimation;

public class BerserkerKiyohime extends AbstractServant {

    public BerserkerKiyohime(){
        super(2.03f, 3f, 0.49f, new TenshinKashoZanmaii(), new ExtraAttack(5, 3), new SpriterAnimation("Kiyohime/images/char/idle/Kiyohime.scml"));
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