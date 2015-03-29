package com.michael.e.liquislots.common;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.*;
import com.michael.e.liquislots.common.container.*;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return new ContainerPlayerTanks(player);
            case 1:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackIO)
                return new ContainerLiquipackIO((TileEntityLiquipackIO) world.getTileEntity(x, y, z));
            case 2:
                return new ContainerLiquipackBucketOptions(player.getHeldItem());
            case 3:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackWorkbench)
                    return new ContainerLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(x, y, z), player);
            case 4:
                return new ContainerLiquidXPConfig(player, x);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return new GuiPlayerTanks(player);
            case 1:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackIO) {
                    return new GuiLiquipackIO(player, (TileEntityLiquipackIO) world.getTileEntity(x, y, z));
                }
            case 2:
                return new GuiHandPump(player, player.getHeldItem());
            case 3:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackWorkbench)
                    return new GuiLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(x, y, z), player);
            case 4:
                return new GuiLiquidXpUpgrade(player, x, player.inventory.armorItemInSlot(2));
            default:
                return null;
        }
    }

    /*public static class GuiModeIO extends GuiTankOptions.GuiMode{

        private TileEntityLiquipackIO te;
        private GuiTank tank;

        public GuiModeIO(TileEntityLiquipackIO te) {
            super("Drain Liquipack", "Fill Liquipack");
            this.te = te;
            this.tank = new GuiTank(126, 21, 16, 58);
        }

        @Override
        public void actionPerformed() {
            Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(te.getTank(), te.isDrainingMode()));
        }

        @Override
        public int getTank() {
            return te.getTank();
        }

        @Override
        public void setTank(int tank) {
            te.setTank(tank);
        }

        @Override
        public int getMode() {
            return te.isDrainingMode() ? 0 : 1;
        }

        @Override
        public void setMode(int mode) {
            te.setDrainingMode(mode == 0);
        }

        @Override
        public void drawBackground(int guiLeft, int guiTop, GuiTankOptions guiTankOptions) {
            Minecraft.getMinecraft().renderEngine.bindTexture(GuiTankOptions.texture);
            guiTankOptions.drawTexturedModalRect(guiLeft + 125, guiTop + 20, 176, 0, 18, 60);
            Minecraft.getMinecraft().fontRenderer.drawString("Buffer:", guiLeft + 125, guiTop + 10, 4210752);
            float level = te.buffer.getFluidAmount() / ((float) te.buffer.getCapacity()) * 58;
            tank.render(te.buffer.getFluid(), (int) level, guiLeft, guiTop);
        }

        @Override
        public void drawForeground(int x, int y, int guiLeft, int guiTop, GuiTankOptions guiTankOptions) {
            if(tank.isMouseInBounds(x-guiLeft,y-guiTop)){
                FluidStack contents = te.buffer.getFluid();
                List<String> text = new ArrayList<String>();
                text.add(contents == null || contents.getFluid() == null ? "Empty" : (contents.amount + "mb X " + contents.getFluid().getLocalizedName(contents)));
                guiTankOptions.drawTooltip(text, x - guiLeft, y - guiTop);
            }
        }
    }

    public static class GuiModeLiquipackBucket extends GuiTankOptions.GuiMode{

        private ItemStack stack;

        public GuiModeLiquipackBucket(ItemStack stack) {
            super(StatCollector.translateToLocal("liquipackbucket.mode.0"), StatCollector.translateToLocal("liquipackbucket.mode.1"), StatCollector.translateToLocal("liquipackbucket.mode.2"));
            this.stack = stack;
        }

        @Override
        public void actionPerformed() {
            Liquislots.INSTANCE.netHandler.sendToServer(new ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage(ItemLiquipackBucket.getSelectedTank(stack), ItemLiquipackBucket.getMode(stack)));
        }

        @Override
        public int getTank() {
            return ItemLiquipackBucket.getSelectedTank(stack);
        }

        @Override
        public void setTank(int tank) {
            ItemLiquipackBucket.setSelectedTank(stack, tank);
        }

        @Override
        public int getMode() {
            return ItemLiquipackBucket.getMode(stack);
        }

        @Override
        public void setMode(int mode) {
            ItemLiquipackBucket.setMode(stack, mode);
        }

    }

    public static class GuiModeLiquidXp extends GuiTankOptions.GuiMode{
        private LiquipackStack stack;
        private LiquidXPUpgrade upgrade;
        private int upgradeIndex;

        public GuiModeLiquidXp(ItemStack stack) {
            super(StatCollector.translateToLocal("liquidxp.mode.0"), StatCollector.translateToLocal("liquidxp.mode.1"), StatCollector.translateToLocal("liquidxp.mode.2"));
            this.stack = new LiquipackStack(stack);
            int i = 0;
            for(LiquipackUpgrade upgrade : this.stack.getUpgrades()){
                if(LiquidXPUpgrade.isLiquidXPUpgrade(upgrade)){
                    this.upgrade = LiquidXPUpgrade.fromLiquipackUpgrade(upgrade);
                    upgradeIndex = i;
                    break;
                }
                i++;
            }
        }

        @Override
        public void actionPerformed() {
            Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquidXPOptionsMessageHandler.ChangeLiquidXPOptionsMessage(upgrade.getTank(), upgrade.getMode()));
        }

        @Override
        public int getTank() {
            return upgrade.getTank();
        }

        @Override
        public void setTank(int tank) {
            upgrade.setTank(tank);
            stack.setUpgrade(upgrade, upgradeIndex);
        }

        @Override
        public int getMode() {
            return upgrade.getMode();
        }

        @Override
        public void setMode(int mode) {
            upgrade.setMode(mode);
            stack.setUpgrade(upgrade, upgradeIndex);
        }

        @Override
        public void onGuiClosed() {
        }
    }*/
}
