package KiyohimeMod;

import static KiyohimeMod.patches.AbstractCardEnum.Kiyohime_Color;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;

import KiyohimeMod.character.BerserkerKiyohime;
import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.character.LancerKiyohime;
import KiyohimeMod.character.StarCounter;
import KiyohimeMod.patches.KiyohimeEnum;
import KiyohimeMod.powers.ChaldeaPower;
import KiyohimeMod.relics.*;
import KiyohimeMod.cards.*;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.RenderSubscriber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class KiyohimeMod implements EditCharactersSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,
        EditRelicsSubscriber, EditCardsSubscriber, PostInitializeSubscriber, RenderSubscriber {

    public static final Logger Logger = LogManager.getLogger(KiyohimeMod.class.getName());
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
        addSaveField();
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
        String characterStrings = Gdx.files.internal(languageString + "/character.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String relicStrings = Gdx.files.internal(languageString + "/relics.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String powerStrings = Gdx.files.internal(languageString + "/powers.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String cardStrings = Gdx.files.internal(languageString + "/cards.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String monsterStrings = Gdx.files.internal(languageString + "/monsters.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String uiStrings = Gdx.files.internal(languageString + "/ui.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
    }

    private String getLanguageString(Settings.GameLanguage language) {
        switch (language) {
        case ZHS:
        case ZHT:
            return "zhs";
        default:
            return "eng";
        }
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String languageString = "Kiyohime/strings/" + getLanguageString(Settings.language);
        String keywordStrings = Gdx.files.internal(languageString + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {
        }.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k, v) -> {
            BaseMod.addKeyword(v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditRelics() {
        // start
        BaseMod.addRelicToCustomPool(new Stone(), Kiyohime_Color);

        // UNCOMMON
        BaseMod.addRelicToCustomPool(new TerritoryCreation(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new ElixirOfRejuvenation(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new HolyShroudOfMagdalene(), Kiyohime_Color);
        // RARE
        BaseMod.addRelicToCustomPool(new Divinity(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new UnlimitedPranaSupply(), Kiyohime_Color);
        // boss
        BaseMod.addRelicToCustomPool(new TheBlackGrail(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new Bond(), Kiyohime_Color);
        BaseMod.addRelicToCustomPool(new Necromancy(), Kiyohime_Color);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AbstractAttackCard.StarCountNumber());
        // Basic ASSP
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new Track());
        BaseMod.addCard(new FlameKiss());
        BaseMod.addCard(new Shapeshift());

        // Attack
        // COMMON
        BaseMod.addCard(new Preemptive());
        BaseMod.addCard(new RedBlackKey());
        BaseMod.addCard(new BlueBlackKey());
        BaseMod.addCard(new GreenBlackKey());
        // UNCOMMON
        BaseMod.addCard(new Fragarach());
        BaseMod.addCard(new BeastOfBillows());
        BaseMod.addCard(new HydraDagger());
        BaseMod.addCard(new RealityMarble());
        BaseMod.addCard(new CommandCard_Buster());
        BaseMod.addCard(new CommandCard_Quick());
        BaseMod.addCard(new CommandCard_Arts());
        // RARE
        BaseMod.addCard(new Destruction());
        BaseMod.addCard(new JeweledSword());
        // SPECIAL
        BaseMod.addCard(new TenshinKashoZanmaii());
        BaseMod.addCard(new ExtraAttack(0, 0));
        BaseMod.addCard(new DoujyoujikaneHyakuhachishikikaryuunagi());

        // Skill
        // COMMON
        BaseMod.addCard(new WaterTurn());
        BaseMod.addCard(new EatSoul());
        BaseMod.addCard(new SteelTraining());
        BaseMod.addCard(new AndSoTheShipConquers());
        // UNCOMMON
        BaseMod.addCard(new DragonVein());
        BaseMod.addCard(new AgainstMagic());
        BaseMod.addCard(new ExSpicyMapoTofu());
        BaseMod.addCard(new Grimoire());
        BaseMod.addCard(new DivineConstruct());
        BaseMod.addCard(new Projection());
        BaseMod.addCard(new CelestialInvertedMoon());
        // RARE
        BaseMod.addCard(new ImaginaryNumberMagecraft());
        BaseMod.addCard(new HeroCreation());
        BaseMod.addCard(new Barrier());
        BaseMod.addCard(new GoldenCarpFigure());
        BaseMod.addCard(new FacelessMoon());
        // Servant
        BaseMod.addCard(new Servant_TamamoNoMae());
        BaseMod.addCard(new Servant_Osakabehime());

        // Power
        // COMMON
        BaseMod.addCard(new Berserker());
        // UNCOMMON
        BaseMod.addCard(new Tenacious());
        BaseMod.addCard(new LimitedZeroOver());
        BaseMod.addCard(new ImaginaryAround());
        BaseMod.addCard(new FormalCraft());
        BaseMod.addCard(new HotSpringOfTheMoon());
        // RARE
        BaseMod.addCard(new PrismaCosmos());
        BaseMod.addCard(new FragmentsOf2030());
        BaseMod.addCard(new AVerseOfBurningLoveStory());
        BaseMod.addCard(new CastleOfTheSun());
    }

    @Override
    public void receivePostInitialize() {
        // add sounds
        HashMap<String, Sfx> reflectedMap = getSoundsMap();
        reflectedMap.put("KiyohimeMod:SELECT", new Sfx("Kiyohime/sounds/Kiyohime_SELECT.ogg"));

        reflectedMap.put("KiyohimeMod:Lancer_BattleStart_1",
                new Sfx("Kiyohime/sounds/Kiyohime_Lancer_BattleStart_1.ogg"));
        reflectedMap.put("KiyohimeMod:Lancer_BattleStart_2",
                new Sfx("Kiyohime/sounds/Kiyohime_Lancer_BattleStart_2.ogg"));

        reflectedMap.put("KiyohimeMod:Berserker_BattleStart_1",
                new Sfx("Kiyohime/sounds/Kiyohime_Berserker_BattleStart_1.ogg"));
        reflectedMap.put("KiyohimeMod:Berserker_BattleStart_2",
                new Sfx("Kiyohime/sounds/Kiyohime_Berserker_BattleStart_2.ogg"));
        reflectedMap.put("KiyohimeMod:Berserker_BattleStart_3",
                new Sfx("Kiyohime/sounds/Kiyohime_Berserker_BattleStart_3.ogg"));
        reflectedMap.put("KiyohimeMod:Berserker_BattleStart_4",
                new Sfx("Kiyohime/sounds/Kiyohime_Berserker_BattleStart_4.ogg"));

        reflectedMap.put("KiyohimeMod:Servants_TamamoNoMae_BattleStart1",
                new Sfx("Kiyohime/sounds/servants/TamamoNoMae_BattleStart1.ogg"));
        reflectedMap.put("KiyohimeMod:Servants_TamamoNoMae_BattleStart2",
                new Sfx("Kiyohime/sounds/servants/TamamoNoMae_BattleStart2.ogg"));
        reflectedMap.put("KiyohimeMod:Servants_TamamoNoMae_BattleStart3",
                new Sfx("Kiyohime/sounds/servants/TamamoNoMae_BattleStart3.ogg"));

        reflectedMap.put("KiyohimeMod:Servants_Osakabehime_BattleStart1",
                new Sfx("Kiyohime/sounds/servants/Osakabehime_BattleStart1.ogg"));
        reflectedMap.put("KiyohimeMod:Servants_Osakabehime_BattleStart2",
                new Sfx("Kiyohime/sounds/servants/Osakabehime_BattleStart2.ogg"));
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Sfx> getSoundsMap() {
        return (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
    }

    private void addSaveField() {
        BaseMod.addSaveField("KiyohimeMod:Kiyohime:Servant", new CustomSavable<String>() {
            public Class<String> savedType() {
                return String.class;
            }

            public String onSave() {
                String servant = null;
                if (AbstractDungeon.player instanceof Kiyohime) {
                    servant = ((Kiyohime) AbstractDungeon.player).Servant.name;
                }
                return servant;
            }

            public void onLoad(String x) {
                if (AbstractDungeon.player instanceof Kiyohime && x != null) {
                    if (x.equals(BerserkerKiyohime.NAME)) {
                        ((Kiyohime) AbstractDungeon.player).changeAbstractServant(new BerserkerKiyohime(), false);
                    } else if (x.equals(LancerKiyohime.NAME)) {
                        ((Kiyohime) AbstractDungeon.player).changeAbstractServant(new LancerKiyohime(), false);
                    }
                }
            }
        });
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (AbstractDungeon.player != null && CardCrawlGame.dungeon != null
                && AbstractDungeon.player.hasPower(ChaldeaPower.POWER_ID)) {
            if (!AbstractDungeon.isScreenUp) {
                StarCounter counter = ((Kiyohime) AbstractDungeon.player).StarCounter;
                counter.render(sb);
                counter.update();
            }
        }
    }
}