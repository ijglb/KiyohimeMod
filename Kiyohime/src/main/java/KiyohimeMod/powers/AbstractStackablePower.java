package KiyohimeMod.powers;

import java.util.HashSet;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AbstractStackablePower extends TwoAmountPower {

    public float valueAmount = 0;

    protected class Buff {
        public float Value;
        public int Round;
    }

    protected HashSet<Buff> BuffPool = new HashSet<Buff>();

    public void stackaBuff(float value, int round) {
        Buff buff = new Buff();
        buff.Value = value;
        buff.Round = round;
        BuffPool.add(buff);
        updateAmount();
    }

    @Override
    public void stackPower(int stackAmount) {
        
    }

    public void updateAmount() {
        int round = 0;
        float value = 0;
        for (Buff buff : BuffPool) {
            if (buff.Round > round)
                round = buff.Round;
            value += buff.Value;
        }
        this.amount = round;
        this.valueAmount = value;
        this.amount2 = (int) this.valueAmount;
        updateDescription();
        //AbstractDungeon.onModifyPower();
    }

    @Override
    public void atEndOfRound() {
        for (Buff buff : BuffPool) {
            buff.Round--;
        }
        BuffPool.removeIf(m -> m.Round == 0);
        if (BuffPool.isEmpty()) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateAmount();
        }
    }
}