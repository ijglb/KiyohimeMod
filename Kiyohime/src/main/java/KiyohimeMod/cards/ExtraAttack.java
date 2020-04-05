package KiyohimeMod.cards;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.patches.KiyohimeTags;
import KiyohimeMod.powers.ChaldeaPower;

public class ExtraAttack extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:ExtraAttack";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/ExtraAttack.png";

    public ExtraAttack(int damage,int hits) {
        super(ID, NAME, IMG_PATH, DESCRIPTION, hits, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ENEMY);
        this.setDisplayRarity(CardRarity.RARE);
        isSpecialTag = true;
        this.exhaust = true;
        this.isEthereal = true;
        this.baseDamage = this.damage = damage;
        this.baseMagicNumber = this.magicNumber = hits;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ExtraAttack(this.baseDamage,this.baseMagicNumber);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if (AbstractDungeon.player instanceof Kiyohime && AbstractDungeon.player.hasPower(ChaldeaPower.POWER_ID)) {
            ChaldeaPower power = (ChaldeaPower) AbstractDungeon.player.getPower(ChaldeaPower.POWER_ID);
            String firstCard = power.CardList.get(0);
            if (firstCard == ChaldeaPower.Buster) {
                card.tags.add(KiyohimeTags.ATTACK_Buster);
            } else if (firstCard == ChaldeaPower.Arts) {
                card.tags.add(KiyohimeTags.ATTACK_Arts);
            } else if (firstCard == ChaldeaPower.Quick) {
                card.tags.add(KiyohimeTags.ATTACK_Quick);
            }
        }
        return card;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractMonster tempM = m;
            int tempDmage = this.damage;
            if(tempM == null){
                ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                int r = 0;
                if(monsters.size() > 1){
                    r = AbstractDungeon.miscRng.random(0, monsters.size()-1);
                }
                tempM = monsters.get(r);
                tempDmage = this.multiDamage[r];
            }
            AbstractDungeon.actionManager
                    .addToBottom(new DamageAction(tempM, new DamageInfo(p, tempDmage, this.damageTypeForTurn),
                            AbstractGameAction.AttackEffect.FIRE));
        }
    }
}