package KiyohimeMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import KiyohimeMod.powers.AbstractStackablePower;

public class ApplyStackablePowerAction extends ApplyPowerAction {

    private int Value;
    private int Round;
    private AbstractStackablePower Power;

    public ApplyStackablePowerAction(AbstractCreature target, AbstractCreature source, AbstractStackablePower powerToApply,
            int value, int round, boolean isFast) {
        super(target, source, powerToApply, round, isFast, AbstractGameAction.AttackEffect.NONE);
        this.Value = value;
        this.Round = round;
        this.Power = powerToApply;
    }
        
    @Override
    public void update() {
        if (target.hasPower(Power.ID)) {
            AbstractStackablePower power = (AbstractStackablePower) target.getPower(Power.ID);
            power.stackaBuff(Value, Round);
        }
        super.update();
        this.isDone = true;
    }
}