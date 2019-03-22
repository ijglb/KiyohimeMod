package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.CustomCard;
import KiyohimeMod.patches.KiyohimeTags;

public class LoadCardBGAction extends AbstractGameAction {
    public static String BG_Buster = "Kiyohime/images/512/bg_attack_buster.png";
    public static String BG_Arts = "Kiyohime/images/512/bg_attack_arts.png";
    public static String BG_Quick = "Kiyohime/images/512/bg_attack_quick.png";
    public static String BG_Buster_L = "Kiyohime/images/1024/bg_attack_buster.png";
    public static String BG_Arts_L = "Kiyohime/images/1024/bg_attack_arts.png";
    public static String BG_Quick_L = "Kiyohime/images/1024/bg_attack_quick.png";
    public static String BG_Defult = "Kiyohime/images/512/bg_attack.png";
    public static String BG_Defult_L = "Kiyohime/images/1024/bg_attack.png";
    private CustomCard card;
    private AbstractCard abstractCard;
    private String bgSmallImg;
    private String bgLargeImg;
    private boolean withFlash;
    
    public LoadCardBGAction(CustomCard card, String bgSmallImg, String bgLargeImg, boolean withFlash) {
        actionType = ActionType.SPECIAL;
        this.card = card;
        this.abstractCard = card;
        this.bgSmallImg = bgSmallImg;
        this.bgLargeImg = bgLargeImg;
        this.withFlash = withFlash;
    }
    
    public LoadCardBGAction(AbstractCard card, boolean withFlash) {
        actionType = ActionType.SPECIAL;
        this.abstractCard = card;
        if (card instanceof CustomCard) {
            this.card = (CustomCard) card;
            if (card.hasTag(KiyohimeTags.ATTACK_Buster)) {
                this.bgSmallImg = BG_Buster;
                this.bgLargeImg = BG_Buster_L;
            } else if (card.hasTag(KiyohimeTags.ATTACK_Arts)) {
                this.bgSmallImg = BG_Arts;
                this.bgLargeImg = BG_Arts_L;
            } else if (card.hasTag(KiyohimeTags.ATTACK_Quick)) {
                this.bgSmallImg = BG_Quick;
                this.bgLargeImg = BG_Quick_L;
            } else {
                this.bgSmallImg = BG_Defult;
                this.bgLargeImg = BG_Defult_L;
            }
        }
        this.withFlash = withFlash;
    }

    @Override
    public void update() {
        if (this.card != null) {
            card.setBackgroundTexture(bgSmallImg, bgLargeImg);
        }
        if (withFlash) {
            abstractCard.flash();
        }
        isDone = true;
    }
}