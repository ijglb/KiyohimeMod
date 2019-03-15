package KiyohimeMod.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.RipAndTearEffect;

public class RandomAttackAction extends AbstractGameAction {
    private DamageInfo info;
    private int numTimes;

    public RandomAttackAction(AbstractCreature target, DamageInfo info, int numTimes) {
        this.info = info;
        this.target = target;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        if (Settings.FAST_MODE) {
            this.startDuration = 0.05F;
        } else {
            this.startDuration = 0.2F;
        }
        this.duration = this.startDuration;
        this.numTimes = numTimes;
    }

    public void update() {
        if (this.target == null) {
            this.isDone = true;
            return;
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration) {
            AbstractDungeon.effectsQueue
                    .add(new RipAndTearEffect(this.target.hb.cX, this.target.hb.cY, Color.RED, Color.GOLD));
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            if (this.target.currentHealth > 0) {
                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                //this.info.applyPowers(this.info.owner, this.target);
                //applypower start
                this.info.output = this.info.base;
                this.info.isModified = false;
                float tmp = this.info.output;
                for (AbstractPower p : target.powers) {
                    tmp = p.atDamageReceive(tmp, this.info.type);
                    if (this.info.base != (int) tmp) {
                        this.info.isModified = true;
                    }
                }
                for (AbstractPower p : target.powers) {
                    tmp = p.atDamageFinalReceive(tmp, this.info.type);
                    if (this.info.base != (int) tmp) {
                        this.info.isModified = true;
                    }
                }
                this.info.output = MathUtils.floor(tmp);
                if (this.info.output < 0) {
                    this.info.output = 0;
                }
                //applypower end
                this.target.damage(this.info);
                if ((this.numTimes > 1) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
                    this.numTimes -= 1;
                    AbstractDungeon.actionManager.addToTop(new RandomAttackAction(

                            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
                            this.info, this.numTimes));
                }
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                } else {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.2F));
                }
            }
            this.isDone = true;
        }
    }
}
