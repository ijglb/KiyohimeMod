package KiyohimeMod.patches.othermods.friendlyminions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import KiyohimeMod.character.Kiyohime;
import kobting.friendlyminions.monsters.MinionMove;
import kobting.friendlyminions.monsters.MinionMoveGroup;

public  class MinionMoveGroupPatch {
    @SpirePatch(clz = MinionMoveGroup.class, method = "drawMoveImage")
    public static class DrawMoveImagePatch {
        public static SpireReturn<Object> Prefix(MinionMoveGroup __instance, MinionMove move, SpriteBatch sb,
                Texture moveImage, int currentIndex) {
            if (AbstractDungeon.player instanceof Kiyohime) {
                sb.draw(moveImage, move.getHitbox().x, move.getHitbox().y, 48.0f, 48.0f, 48.0f, 48.0f,
                        Settings.scale * 1.5f, Settings.scale * 1.5f, 0.0f, 0, 0, moveImage.getWidth(),
                        moveImage.getHeight(), false, false);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}