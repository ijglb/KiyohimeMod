package KiyohimeMod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import KiyohimeMod.character.AbstractServant;
import KiyohimeMod.character.Kiyohime;
import KiyohimeMod.patches.KiyohimeTags;

public class CommandCard_Quick extends AbstractAttackCard {

    public static final String ID = "KiyohimeMod:CommandCard_Quick";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "Kiyohime/images/cards/CommandCard_Quick.png";
    private static final int ATTACK_DMG = 0;
    //private static final int UPGRADE_PLUS_DMG = 3;
    private static final int BASE_BLOCK = 12;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int BASE_MAGIC = 0;//hits

    public CommandCard_Quick() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, BASE_MAGIC, AbstractCard.CardRarity.UNCOMMON,
                AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = BASE_MAGIC;
        isSpecialTag = true;
        this.tags.add(KiyohimeTags.ATTACK_Quick);
    }

    @Override
    public void applyPowers() {
        if(AbstractDungeon.player instanceof Kiyohime){
            AbstractServant servant = ((Kiyohime)AbstractDungeon.player).Servant;
            int hits = servant.getQuickHits();
            this.baseDamage = this.baseBlock / hits;
            this.Hits = hits;
            this.magicNumber = this.baseMagicNumber = hits;
        }
        super.applyPowers();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
          }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager
                    .addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CommandCard_Quick();
    }
}