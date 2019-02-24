package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import KiyohimeMod.actions.SummonServantAction;
import KiyohimeMod.patches.AbstractCardEnum;
import KiyohimeMod.powers.NPPower;
import KiyohimeMod.servants.AbstractFriendlyServant;
import KiyohimeMod.servants.TamamoNoMae;
import basemod.abstracts.CustomCard;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class Servant_TamamoNoMae extends CustomCard {

    public static final String ID = "KiyohimeMod:Servant_TamamoNoMae";
    public static final String IMG_PATH = "Kiyohime/images/cards/Servant_TamamoNoMae.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int BASE_MAGIC = 10;
    private static final int UPGRADE_PLUS_MAGIC = 10;

    public Servant_TamamoNoMae() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.Kiyohime_Color,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = BASE_MAGIC;
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean can = super.canUse(p, m);
        if (can) {
            MonsterGroup playerServents = BasePlayerMinionHelper.getMinions(p);
            int serventCount = playerServents.monsters.size();
            int max = BasePlayerMinionHelper.getMaxMinions(p);
            if (playerServents.getMonsterNames().contains(TamamoNoMae.ID)) {
                serventCount--;
            }
            if (serventCount >= max) {
                can = false;
                cantUseMessage = EXTENDED_DESCRIPTION[0];
            }
        }
        return can;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        MonsterGroup playerServents = BasePlayerMinionHelper.getMinions(p);
        if (playerServents.getMonsterNames().contains(TamamoNoMae.ID)) {
            AbstractFriendlyServant servent = (AbstractFriendlyServant) playerServents.getMonster(TamamoNoMae.ID);
            servent.increaseMaxHp(5, true);
            servent.resetSkill();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NPPower(p), this.magicNumber));
        } else if (playerServents.monsters.size() < BasePlayerMinionHelper.getMaxMinions(p)) {
            int size = playerServents.monsters.size();
            int x = -675;
            if (size == 1) {
                x = -1075;
            }
            AbstractDungeon.actionManager.addToBottom(new SummonServantAction(new TamamoNoMae(x)));
        }
    }

    public AbstractCard makeCopy() {
        return new Servant_TamamoNoMae();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}