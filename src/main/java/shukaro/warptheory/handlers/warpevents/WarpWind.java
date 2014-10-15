package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpWind implements IWarpEvent
{
    public WarpWind() { FMLCommonHandler.instance().bus().register(this); }

    @Override
    public String getName()
    {
        return "wind";
    }

    @Override
    public int getCost()
    {
        return 2;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.windstart"));
        MiscHelper.modTag(player, "wind", 5 + world.rand.nextInt(10));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getTag(player, "wind") > 0 && e.world.rand.nextBoolean() && e.world.getTotalWorldTime() % 20 == 0)
            {
                int wind = MiscHelper.getTag(player, "wind");
                PacketDispatcher.sendWindEvent(player, e.world.rand.nextDouble() - e.world.rand.nextDouble(), e.world.rand.nextDouble(), e.world.rand.nextDouble() - e.world.rand.nextDouble());
                MiscHelper.setTag(player, "wind", --wind);
                if (wind <= 0)
                {
                    MiscHelper.removeTag(player, "wind");
                    ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.windend"));
                }
            }
        }
    }
}