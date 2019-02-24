package KiyohimeMod.servants;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import KiyohimeMod.actions.ApplyStackablePowerAction;
import KiyohimeMod.powers.ArtsUPPower;
import KiyohimeMod.powers.NPDamagePower;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class TamamoNoMae extends AbstractFriendlyServant {

    public static String ID = "KiyohimeMod:TamamoNoMae";
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    public static String[] AttackIntents = { 
        "Kiyohime/images/servants/TamamoNoMae/attack_intent_1.png", 
        "Kiyohime/images/servants/TamamoNoMae/attack_intent_2.png", 
        "Kiyohime/images/servants/TamamoNoMae/attack_intent_3.png", 
        "Kiyohime/images/servants/TamamoNoMae/attack_intent_4.png",
        "Kiyohime/images/servants/TamamoNoMae/attack_intent_5.png", 
        "Kiyohime/images/servants/TamamoNoMae/attack_intent_6.png", 
            "Kiyohime/images/servants/TamamoNoMae/attack_intent_7.png" };
    public int baseDamageAmount = 6;

    public TamamoNoMae(float x) {
        super(NAME, ID, 25, "Kiyohime/images/servants/TamamoNoMae/TamamoNoMae.png", x, 10f, AttackIntents);
        addMoves();
    }

    private void addMoves() {
        baseMoves.add(new ServantMove(DIALOG[0], this,
                new Texture("Kiyohime/images/servants/TamamoNoMae/TamamoNoMae_Head.png"), null, MOVES[0], null, false) {
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
        baseMoves.add(new ServantMove(DIALOG[2], this, new Texture("Kiyohime/images/servants/MieQi.png"),
                new Texture("Kiyohime/images/servants/MieQi_cd.png"), MOVES[3], () -> {
                    target = AbstractDungeon.getRandomMonster();
                    AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(AbstractDungeon.player,
                            this, new NPDamagePower(AbstractDungeon.player, 30, 3), 30, 3, true));
                    AbstractDungeon.actionManager
                            .addToBottom(new ApplyPowerAction(target, this, new StrengthPower(target, -1), -1));
                }, true));
        baseMoves.add(new ServantMove(DIALOG[3], this, new Texture("Kiyohime/images/servants/JiaFang.png"),
                new Texture("Kiyohime/images/servants/JiaFang_cd.png"), MOVES[4], () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, this, 10));
                    BasePlayerMinionHelper.getMinions(AbstractDungeon.player).monsters.forEach(m -> {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, 10));
                    });
                }, true));
        baseMoves.add(new ServantMove(DIALOG[4], this, new Texture("Kiyohime/images/servants/LanFang.png"),
                new Texture("Kiyohime/images/servants/LanFang_cd.png"), MOVES[5], () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(AbstractDungeon.player,
                            this, new ArtsUPPower(AbstractDungeon.player, 50, 3), 50, 3, true));
                    AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, this, 6));
                }, true));

        initMoves();
    }

    @Override
    public void applyStartOfTurnPowers() {
        super.applyStartOfTurnPowers();
        AbstractDungeon.actionManager.addToBottom(new ApplyStackablePowerAction(AbstractDungeon.player, this,
                new ArtsUPPower(AbstractDungeon.player, 15, 1), 15, 1, true));
    }

    @Override
    public String getSummonSound() {
        return "KiyohimeMod:Servants_TamamoNoMae_Summon";
    }
}