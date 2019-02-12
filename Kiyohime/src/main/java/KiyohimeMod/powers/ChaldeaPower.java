package KiyohimeMod.powers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import KiyohimeMod.actions.ExtraAttackAction;
import KiyohimeMod.cards.AbstractAttackCard;
import KiyohimeMod.patches.KiyohimeTags;

public class ChaldeaPower extends AbstractPower {
    public static final String POWER_ID = "KiyohimeMod:ChaldeaPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    public static final String Buster = " #rB ";
    public static final String Arts = " #bA ";
    public static final String Quick = " #gQ ";

    public ArrayList<String> CardList = new ArrayList<String>();
    private boolean isChain = false;

    public ChaldeaPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.type = AbstractPower.PowerType.BUFF;
        updateDescription();
        this.img = new Texture("Kiyohime/images/powers/ChaldeaPower.png");
    }

    @Override
    public void updateDescription() {
        String str = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        if (!CardList.isEmpty()) {
            str += DESCRIPTIONS[3];
            for (String var : CardList) {
                str += var;
            }
        }
        description = str;
    }

    @Override
    public void atStartOfTurn() {
        this.amount = 0;
        this.isChain = false;
        this.CardList.clear();
        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card instanceof AbstractAttackCard) {
            this.amount++;
            if (card.hasTag(KiyohimeTags.ATTACK_Buster))
                this.CardList.add(Buster);
            if (card.hasTag(KiyohimeTags.ATTACK_Arts))
                this.CardList.add(Arts);
            if (card.hasTag(KiyohimeTags.ATTACK_Quick))
                this.CardList.add(Quick);

            int size = this.CardList.size();
            if (size == 1) {
                String c = this.CardList.get(size - 1);
                if (c == Buster) {
                    AbstractDungeon.actionManager
                            .addToTop(new ApplyPowerAction(owner, owner, new BusterFirstPower(owner)));
                } else if (c == Arts) {
                    AbstractDungeon.actionManager
                            .addToTop(new ApplyPowerAction(owner, owner, new ArtsFirstPower(owner)));
                } else if (c == Quick) {
                    AbstractDungeon.actionManager
                            .addToTop(new ApplyPowerAction(owner, owner, new QuickFirstPower(owner)));
                }
            }
            if (size >= 3 && !isChain) {
                String last1Card = this.CardList.get(size - 1);
                String last2Card = this.CardList.get(size - 2);
                String last3Card = this.CardList.get(size - 3);
                if (last1Card == last2Card && last2Card == last3Card) {
                    flash();
                    if (last1Card == Buster) {
                        AbstractDungeon.actionManager.addToBottom(new ExtraAttackAction(m));
                    } else if (last1Card == Arts) {
                        AbstractDungeon.actionManager
                                .addToBottom(new ApplyPowerAction(owner, owner, new NPPower(owner), 20));
                    } else if (last1Card == Quick) {
                        AbstractDungeon.actionManager
                                .addToBottom(new ApplyPowerAction(owner, owner, new CritStarPower(owner), 25));
                    }
                    isChain = true;
                }
            }
            updateDescription();
        }
    }
}