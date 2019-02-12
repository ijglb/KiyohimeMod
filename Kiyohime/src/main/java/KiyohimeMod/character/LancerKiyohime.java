package KiyohimeMod.character;

import KiyohimeMod.cards.DoujyoujikaneHyakuhachishikikaryuunagi;
import KiyohimeMod.cards.ExtraAttack;
import basemod.animations.SpriterAnimation;

public class LancerKiyohime extends AbstractServant {

    public LancerKiyohime(){
        super(1.05f, 2f, 1.2f, new DoujyoujikaneHyakuhachishikikaryuunagi(), new ExtraAttack(3, 5), new SpriterAnimation("Kiyohime/images/char/idle/Kiyohime_Lancer.scml"));
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