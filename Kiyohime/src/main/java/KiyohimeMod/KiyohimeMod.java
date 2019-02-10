package KiyohimeMod;

import static KiyohimeMod.patches.AbstractCardEnum.Kiyohime_Color;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;

import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.patches.KiyohimeEnum;
import KiyohimeMod.relics.*;
import KiyohimeMod.cards.*;
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;

@SpireInitializer
public class KiyohimeMod implements EditCharactersSubscriber,EditStringsSubscriber,EditKeywordsSubscriber,EditRelicsSubscriber,EditCardsSubscriber {

    private static final Color buster = CardHelper.getColor(254.0f, 32.0f, 22.0f);
    private static final String attackCard = "Kiyohime/images/512/bg_attack.png";
    private static final String skillCard = "Kiyohime/images/512/bg_skill.png";
    private static final String powerCard = "Kiyohime/images/512/bg_power.png";
    private static final String energyOrb = "Kiyohime/images/512/card_orb.png";
    private static final String attackCardPortrait = "Kiyohime/images/1024/bg_attack.png";
    private static final String skillCardPortrait = "Kiyohime/images/1024/bg_skill.png";
    private static final String powerCardPortrait = "Kiyohime/images/1024/bg_power.png";
    private static final String energyOrbPortrait = "Kiyohime/images/1024/card_orb.png";
    private static final String charButton = "Kiyohime/images/charSelect/button.png";
    private static final String charPortrait = "Kiyohime/images/charSelect/portrait.png";

    public KiyohimeMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(Kiyohime_Color, buster, buster, buster, buster, buster, buster, buster, attackCard, skillCard,
                powerCard, energyOrb, attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait);
    }

    public static void initialize() {
        new KiyohimeMod();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Kiyohime(CardCrawlGame.playerName), charButton, charPortrait, KiyohimeEnum.Kiyohime);
    }

    @Override
    public void receiveEditStrings() {
        String languageString = "Kiyohime/strings/" + getLanguageString(Settings.language);
        String characterStrings = Gdx.files.internal(languageString + "/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String relicStrings = Gdx.files.internal(languageString + "/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String powerStrings = Gdx.files.internal(languageString + "/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String cardStrings = Gdx.files.internal(languageString + "/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
    }

    private String getLanguageString(Settings.GameLanguage language) {
        switch (language) {
            default:
                return "zhs";
        }
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String languageString = "Kiyohime/strings/" + getLanguageString(Settings.language);
        String keywordStrings = Gdx.files.internal(languageString + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            BaseMod.addKeyword(v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditRelics() {
        //start
        BaseMod.addRelicToCustomPool(new Stone(), Kiyohime_Color);

        //RARE
        BaseMod.addRelicToCustomPool(new UnlimitedPranaSupply(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new TerritoryCreation(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new Divinity(), Kiyohime_Color);
        //boss
        BaseMod.addRelicToCustomPool(new TheBlackGrail(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new Bond(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new Necromancy(), Kiyohime_Color);
    }

    @Override
    public void receiveEditCards() {
        //Basic ASSP
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new Track());
        BaseMod.addCard(new FlameKiss());

        //Attack
        //COMMON
        BaseMod.addCard(new Preemptive());
        BaseMod.addCard(new RedBlackKey());
        BaseMod.addCard(new BlueBlackKey());
        BaseMod.addCard(new GreenBlackKey());
        //UNCOMMON
        BaseMod.addCard(new Fragarach());
        BaseMod.addCard(new BeastOfBillows());
        BaseMod.addCard(new HydraDagger());
        BaseMod.addCard(new RealityMarble());
        //RARE
        BaseMod.addCard(new Destruction());
        BaseMod.addCard(new JeweledSword());
        //SPECIAL
        BaseMod.addCard(new TenshinKashoZanmaii());

        //Skill
        //COMMON
        BaseMod.addCard(new WaterTurn());
        BaseMod.addCard(new EatSoul());
        BaseMod.addCard(new SteelTraining());
        //UNCOMMON
        BaseMod.addCard(new DragonVein());
        BaseMod.addCard(new AgainstMagic());
        BaseMod.addCard(new ExSpicyMapoTofu());
        BaseMod.addCard(new Grimoire());
        BaseMod.addCard(new DivineConstruct());
        BaseMod.addCard(new Barrier());
        BaseMod.addCard(new Projection());
        //RARE
        BaseMod.addCard(new ImaginaryNumberMagecraft());
        BaseMod.addCard(new HeroCreation());
        BaseMod.addCard(new CelestialInvertedMoon());

        //Power
        //COMMON
        BaseMod.addCard(new Berserker());
        //UNCOMMON
        BaseMod.addCard(new Tenacious());
        BaseMod.addCard(new LimitedZeroOver());
        BaseMod.addCard(new ImaginaryAround());
        BaseMod.addCard(new FormalCraft());
        BaseMod.addCard(new HotSpringOfTheMoon());
        //RARE
        BaseMod.addCard(new PrismaCosmos());
        BaseMod.addCard(new FragmentsOf2030());
        BaseMod.addCard(new AVerseOfBurningLoveStory());
    }

}