package KiyohimeMod.servants;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class ServantMove extends MinionMove {

    public boolean isSkill;
    public boolean isUsed = false;
    protected AbstractFriendlyMonster owner;
    private Texture moveImage;
    private Texture cdImage;

    public ServantMove(String ID, AbstractFriendlyMonster owner, Texture moveImage,Texture cdImage, String moveDescription,
            Runnable moveActions, boolean isSkill) {
        super(ID, owner, moveImage, moveDescription, moveActions);
        this.moveImage = moveImage;
        this.cdImage = cdImage;
        this.owner = owner;
        this.isSkill = isSkill;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
        if (this.isUsed) {
            this.setMoveImage(cdImage);
        } else {
            this.setMoveImage(moveImage);
        }
    }

    @Override
    protected void onClick() {
        if (!AbstractDungeon.actionManager.turnHasEnded && !this.owner.hasTakenTurn() && !this.isUsed) {
            super.onClick();
            this.owner.setTakenTurn(false);
            setUsed(true);
        }
    }
}