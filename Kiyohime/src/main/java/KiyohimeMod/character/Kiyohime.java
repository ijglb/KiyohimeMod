package KiyohimeMod.character;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import KiyohimeMod.cards.Defend;
import KiyohimeMod.cards.FlameKiss;
import KiyohimeMod.cards.Shapeshift;
import KiyohimeMod.cards.Strike;
import KiyohimeMod.cards.Track;
import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.patches.KiyohimeEnum;
import KiyohimeMod.powers.ChaldeaPower;
import KiyohimeMod.powers.NPPower;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import KiyohimeMod.relics.Stone;

public class Kiyohime extends CustomPlayer {
    public static final String MY_CHARACTER_SHOULDER_2 = "Kiyohime/images/char/shoulder2.png";
    public static final String MY_CHARACTER_SHOULDER_1 = "Kiyohime/images/char/shoulder1.png";
    public static final String MY_CHARACTER_CORPSE = "Kiyohime/images/char/corpse.png";
    public static final int ENERGY_PER_TURN = 3;
    private static final String ID = "KiyohimeMod:Kiyohime";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final Color buster = CardHelper.getColor(254.0f, 32.0f, 22.0f);
    public static final String[] orbTextures = {
            "images/ui/topPanel/blue/1.png",
            "images/ui/topPanel/blue/2.png",
            "images/ui/topPanel/blue/3.png",
            "images/ui/topPanel/blue/4.png",
            "images/ui/topPanel/blue/5.png",
            "images/ui/topPanel/blue/border.png",
            "images/ui/topPanel/blue/1d.png",
            "images/ui/topPanel/blue/2d.png",
            "images/ui/topPanel/blue/3d.png",
            "images/ui/topPanel/blue/4d.png",
            "images/ui/topPanel/blue/5d.png"
    };

    public AbstractServant Servant;
    public StarCounter StarCounter;

    public Kiyohime(String name) {
        super(name, KiyohimeEnum.Kiyohime, orbTextures, "images/ui/topPanel/energyBlueVFX.png",
                new SpriterAnimation("Kiyohime/images/char/idle/Kiyohime.scml"));
        this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values
        this.Servant = new BerserkerKiyohime();
        this.StarCounter = new StarCounter();
        initializeClass(null, MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1, MY_CHARACTER_CORPSE, getLoadout(),
                20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    @Override
    public void preBattlePrep() {
        String sound = Servant.getBattleStartSound();
        if (sound != null) {
            CardCrawlGame.sound.play(sound);
        }
        this.StarCounter.resetCounter();
        super.preBattlePrep();
    }

    @Override
    public void applyStartOfCombatLogic() {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new NPPower(AbstractDungeon.player)));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ChaldeaPower(AbstractDungeon.player)));
        BasePlayerMinionHelper.changeMaxMinionAmount(AbstractDungeon.player, 2);
        
        super.applyStartOfCombatLogic();
    }

    @Override
    public void update() {
        super.update();
        this.StarCounter.update();
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);
        this.StarCounter.onUseCard(c);
    }

    public void changeAbstractServant(AbstractServant servant, boolean playSound) {
        if (playSound) {
            String sound = servant.getBattleStartSound();
            if (sound != null) {
                CardCrawlGame.sound.play(sound);
            }
        }
        this.Servant = servant;
        this.animation = this.Servant.animation;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.play("KiyohimeMod:SELECT");
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public CardColor getCardColor() {
        return AbstractCardEnum.Kiyohime_Color;
    }

    @Override
    public Color getCardRenderColor() {
        return buster;
    }

    @Override
    public Color getCardTrailColor() {
        return buster;
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "KiyohimeMod:SELECT";
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0], 60, 60, 0, 99, 5, //starting hp, max hp, max orbs, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    @Override
    public Color getSlashAttackColor() {
        return buster;
    }

    @Override
    public AttackEffect[] getSpireHeartSlashEffect() {
        return new AttackEffect[]{
            AttackEffect.SLASH_HEAVY,
            AttackEffect.FIRE,
            AttackEffect.SLASH_DIAGONAL,
            AttackEffect.SLASH_HEAVY,
            AttackEffect.FIRE,
            AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new FlameKiss();
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Track.ID);
        retVal.add(FlameKiss.ID);
        retVal.add(Shapeshift.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Stone.ID);
        UnlockTracker.markRelicAsSeen(Stone.ID);
        return retVal;
    }

    @Override
    public String getTitle(PlayerClass arg0) {
        return characterStrings.NAMES[0];
    }

    @Override
    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Kiyohime(name);
    }
}