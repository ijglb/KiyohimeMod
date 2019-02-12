package KiyohimeMod.character;

import KiyohimeMod.cards.AbstractAttackCard;
import KiyohimeMod.cards.ExtraAttack;
import basemod.animations.AbstractAnimation;

public abstract class AbstractServant {

    public float npRate;
    public float npSuffer;
    public float starRate;
    public AbstractAttackCard npCard;
    public ExtraAttack extraAttack;
    public AbstractAnimation animation;

    public AbstractServant(float npRate,float npSuffer,float starRate, AbstractAttackCard npCard,ExtraAttack extraAttack,AbstractAnimation animation){
        this.npRate = npRate;
        this.npSuffer = npSuffer;
        this.starRate = starRate;
        this.npCard = npCard;
        this.extraAttack = extraAttack;
        this.animation = animation;
    }

    public abstract int getBusterHits();

    public abstract int getQuickHits();

    public abstract int getArtsHits();
}