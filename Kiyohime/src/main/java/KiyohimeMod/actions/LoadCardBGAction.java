package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import basemod.abstracts.CustomCard;

public class LoadCardBGAction extends AbstractGameAction {
    private CustomCard card;
    private String bgSmallImg;
    private String bgLargeImg;
    private boolean withFlash;
    
    public LoadCardBGAction(CustomCard card, String bgSmallImg,String bgLargeImg, boolean withFlash) {
        actionType = ActionType.SPECIAL;
        this.card = card;
        this.bgSmallImg = bgSmallImg;
        this.bgLargeImg = bgLargeImg;
        this.withFlash = withFlash;
    }

    @Override
    public void update() {
        card.setBackgroundTexture(bgSmallImg, bgLargeImg);
        if (withFlash) {
            card.flash();
        }
        isDone = true;
    }
}