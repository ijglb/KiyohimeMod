package KiyohimeMod.patches.othermods.friendlyminions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import KiyohimeMod.character.Kiyohime;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.monsters.MinionMove;
import kobting.friendlyminions.monsters.MinionMoveGroup;
import kobting.friendlyminions.patches.PlayerAddFieldsPatch;
import kobting.friendlyminions.patches.PlayerMethodPatches;

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

    @SpirePatch(clz = PlayerMethodPatches.RenderPatch.class, method = "Prefix")
    public static class RenderPatch {

        public static SpireReturn<Object> Prefix(AbstractPlayer _instance, SpriteBatch sb) {

            if (!(_instance instanceof AbstractPlayerWithMinions)) {
                MonsterGroup minions;
                minions = PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);

                if (AbstractDungeon.getCurrRoom() != null) {
                    switch (AbstractDungeon.getCurrRoom().phase) {
                    case COMBAT:
                        if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player))
                            minions.render(sb);
                        break;
                    default:
                        break;
                    }
                }
            }
            return SpireReturn.Return(null);
        }

    }

    @SpirePatch(clz = PlayerMethodPatches.UpdatePatch.class, method = "Postfix")
    public static class UpdatePatch {

        public static SpireReturn<Object> Prefix(AbstractPlayer _instance) {

            if (!(_instance instanceof AbstractPlayerWithMinions)) {
                MonsterGroup minions;
                minions = PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);

                if (AbstractDungeon.getCurrRoom() != null) {
                    switch (AbstractDungeon.getCurrRoom().phase) {
                    case COMBAT:
                        if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player))
                            minions.update();
                        break;
                    default:
                        break;
                    }
                }
            }
            return SpireReturn.Return(null);
        }
    }
}