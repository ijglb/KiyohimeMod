package KiyohimeMod.servants;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.powers.CritStarGenerationRatePower;
import KiyohimeMod.powers.NPGenerationRatePower;
import KiyohimeMod.powers.NPPower;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class Osakabehime extends AbstractFriendlyServant {

    public static String ID = "KiyohimeMod:Osakabehime";
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    public static String[] AttackIntents = { 
        "Kiyohime/images/servants/Osakabehime/attack_intent_1.png", 
        "Kiyohime/images/servants/Osakabehime/attack_intent_2.png", 
        "Kiyohime/images/servants/Osakabehime/attack_intent_3.png", 
        "Kiyohime/images/servants/Osakabehime/attack_intent_4.png",
        "Kiyohime/images/servants/Osakabehime/attack_intent_5.png", 
        "Kiyohime/images/servants/Osakabehime/attack_intent_6.png", 
            "Kiyohime/images/servants/Osakabehime/attack_intent_7.png" };
    private static final String[] BattleStartSounds = { "KiyohimeMod:Servants_Osakabehime_BattleStart1",
            "KiyohimeMod:Servants_Osakabehime_BattleStart2" };
    public int baseDamageAmount = 6;

    public Osakabehime(float x) {
        super(NAME, ID, 22, "Kiyohime/images/servants/Osakabehime/Osakabehime.png", x, 10f, AttackIntents);
        addMoves();
        initMoves();
    }

    private void addMoves() {
        baseMoves.add(new ServantMove(DIALOG[0], this,
                new Texture("Kiyohime/images/servants/Osakabehime/Osakabehime_Head.png"), null, MOVES[0], null, false) {
            @Override
            protected void onClick() {

            }
        });
        baseMoves.add(new ServantMove(DIALOG[1], this, new Texture("Kiyohime/images/servants/DiWuShi.png"),
                new Texture("Kiyohime/images/servants/DiWuShi_cd.png"), MOVES[1] + baseDamageAmount + MOVES[2], () -> {
                    target = AbstractDungeon.getRandomMonster();
                    DamageInfo info = new DamageInfo(this, baseDamageAmount, DamageInfo.DamageType.NORMAL);
                    info.applyPowers(this, target);
                    AbstractDungeon.actionManager
                            .addToBottom(new DamageAction(target, info, AttackEffect.SLASH_DIAGONAL));
                }, false));
        baseMoves.add(new ServantMove(DIALOG[2], this, new Texture("Kiyohime/images/servants/JiaFang.png"),
                new Texture("Kiyohime/images/servants/JiaFang_cd.png"), MOVES[3], () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, this, 10));
                    BasePlayerMinionHelper.getMinions(AbstractDungeon.player).monsters.forEach(m -> {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, 10));
                    });
                    AbstractPlayer p = AbstractDungeon.player;
                    if (!p.powers.isEmpty()) {
                        ArrayList<AbstractPower> pows = new ArrayList<>();
                        for (AbstractPower pow : p.powers) {
                            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                                pows.add(pow);
                            }
                        }
                        if (!pows.isEmpty()) {
                            AbstractPower po = pows.get(AbstractDungeon.miscRng.random(0, pows.size() - 1));
                            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, this, po));
                        }
                    }
                }, true));
        baseMoves.add(new ServantMove(DIALOG[3], this, new Texture("Kiyohime/images/servants/ChongNeng.png"),
                new Texture("Kiyohime/images/servants/ChongNeng_cd.png"), MOVES[4], () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this,
                            new NPPower(AbstractDungeon.player), 20));
                    AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(AbstractDungeon.player,
                            this, new CritStarGenerationRatePower(AbstractDungeon.player, 50, 3), 50, 3, true));
                }, true));
        baseMoves.add(new ServantMove(DIALOG[4], this, new Texture("Kiyohime/images/servants/JiangFang.png"),
                new Texture("Kiyohime/images/servants/JiangFang_cd.png"), MOVES[5], () -> {
                    target = AbstractDungeon.getRandomMonster();
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(target, this, new VulnerablePower(target, 1, false), 1, true));
                    if (target.type != AbstractMonster.EnemyType.BOSS && !target.powers.isEmpty()) {
                        for (AbstractPower pow : target.powers) {
                            if (pow.type == AbstractPower.PowerType.BUFF) {
                                AbstractDungeon.actionManager
                                        .addToBottom(new RemoveSpecificPowerAction(target, this, pow));
                            }
                        }
                    }
                }, true));
    }

    @Override
    public void applyStartOfTurnPowers() {
        super.applyStartOfTurnPowers();
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(AbstractDungeon.player, this,
                new NPGenerationRatePower(AbstractDungeon.player, 15, 1), 15, 1, true));
    }

    @Override
    public String getSummonSound() {
        int r = AbstractDungeon.miscRng.random(0, BattleStartSounds.length - 1);
        return BattleStartSounds[r];
    }
}