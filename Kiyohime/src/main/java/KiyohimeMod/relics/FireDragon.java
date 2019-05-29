package KiyohimeMod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;

public class FireDragon extends CustomRelic {
    public static final String ID = "KiyohimeMod:FireDragon";
    public static final Texture IMG = new Texture("Kiyohime/images/relics/FireDragon.png");
    public static final Texture OUTLINE = new Texture("Kiyohime/images/relics/FireDragon_P.png");

    public FireDragon() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FireDragon();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}